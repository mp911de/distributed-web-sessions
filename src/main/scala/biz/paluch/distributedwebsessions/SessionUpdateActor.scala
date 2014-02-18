package biz.paluch.distributedwebsessions

import akka.actor.{Actor, ActorLogging}
import java.util.Date
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 18.02.14 12:37
 */
class SessionUpdateActor(mongodb: MongoClient) extends Actor with ActorLogging {

  def updateSession(sessionId: String, timeStamp: Date, document: String) {

    val collection = mongodb("sessions_demo")("sessions")
    val query = MongoDBObject("sessionId" -> sessionId)
    val update = $set("document" -> document, "timestamp" -> timeStamp)
    val result = collection.update(query, update, upsert = true)
    System.out.println(sessionId + " Updated " + result.getN + " Documents")

  }

  def receive = {
    case PersistentSession(sessionId, timeStamp, document) => updateSession(sessionId, timeStamp, document)
  }
}
