package biz.paluch.distributedwebsessions

import scala.concurrent.duration._
import javax.servlet._
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import akka.actor.{PoisonPill, ActorRef, Props, ActorSystem}
import javax.servlet.http.HttpServletRequest
import akka.pattern.Patterns
import scala.concurrent.{Await, Future}
import java.util.Date


/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 18.02.14 12:17
 */
class SessionUpdateFilter extends Filter {
  var uri: MongoClientURI = null;
  var mongoClient: MongoClient = null;
  val system = ActorSystem("mongo-replicator")
  var persister: ActorRef = null;
  var retriever: ActorRef = null;

  override def init(filterConfig: FilterConfig): Unit = {

    val mongoClientUri = filterConfig getInitParameter ("mongoClientUri")
    this.uri = MongoClientURI(mongoClientUri)
    this.mongoClient = MongoClient(this.uri)

    persister = system.actorOf(Props(classOf[SessionUpdateActor], mongoClient), name = "SessionUpdateActor")
    retriever = system.actorOf(Props(classOf[SessionRetrievalActor], mongoClient), name = "SessionRetrievalActorUpdateActor")

  }

  override def doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain): Unit = {

    val req = request.asInstanceOf[HttpServletRequest]
    val session = req.getSession()
    var mySession: MySession = null
    try {

      mySession = session.getAttribute("MySession").asInstanceOf[MySession];
      if (mySession == null) {
        mySession = new MySession
        session.setAttribute("MySession", mySession);
      }


      // Check whether local session is different from remote session
      // and load it if needed
      if (mySession.globalSessionTimestamp != mySession.localSessionTimestamp) {
        System.out.println(session.getId() + " Session retrieval needed, diff " + (mySession.globalSessionTimestamp - mySession.localSessionTimestamp))
        val future: Future[Option[PersistentSession]] = Patterns.ask(retriever, RetrieveSessionMessage(session.getId), 20 seconds).mapTo[Option[PersistentSession]];
        val result = Await.result(future, 20 seconds)

        if (result.nonEmpty) {
          val persistentSession = result.get;
          mySession.localSessionTimestamp = persistentSession.timestamp.getTime
          mySession.model = SXStream.fromXML(persistentSession.document);

          System.out.println(session.getId() + " Session retrieved")
        }
      }


      if (mySession.localCheckModel == null) {
        mySession.localCheckModel = new SomeModel
      }

      chain.doFilter(request, response)

    } finally {
      // trigger session write
      session.setAttribute("MySession", mySession);
      val timestamp = System.currentTimeMillis()
      mySession.globalSessionTimestamp = timestamp
      mySession.localSessionTimestamp = timestamp
      persister ! new PersistentSession(session.getId, new Date(timestamp), SXStream.toXML(mySession.model));
    }


  }

  override def destroy(): Unit = {
    persister ! PoisonPill
    system.shutdown()
    system.awaitTermination(5 seconds)
  }
}
