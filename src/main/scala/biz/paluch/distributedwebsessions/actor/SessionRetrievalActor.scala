package biz.paluch.distributedwebsessions.actor

import akka.actor.{Actor, ActorLogging}
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.MongoDBObject
import java.util.Date
import biz.paluch.distributedwebsessions.model.{RetrieveSessionMessage, PersistentSession}

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 18.02.14 12:37
 */
class SessionRetrievalActor(mongodb: MongoClient) extends Actor with ActorLogging {


  def retrieveSessionXml(sessionId: String): Option[PersistentSession] = {


    val collection = mongodb("sessions_demo")("sessions")
    val cursor = collection.findOne(MongoDBObject("sessionId" -> sessionId));

    if (!cursor.isEmpty) {

      val sessionObject = cursor.get
      val persistentSession = new PersistentSession(sessionObject.get("sessionId").asInstanceOf[String], sessionObject.get("timestamp").asInstanceOf[Date], sessionObject.get("document").asInstanceOf[String])
      log.info(s"[$sessionId] retrieved from mongodb")
      return Option(persistentSession)
    }

    return Option.empty;

  }


  def receive = {
    case request: RetrieveSessionMessage => sender ! retrieveSessionXml(request.id)
  }
}
