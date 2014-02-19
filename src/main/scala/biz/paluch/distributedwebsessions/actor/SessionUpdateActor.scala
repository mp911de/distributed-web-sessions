package biz.paluch.distributedwebsessions.actor

import akka.actor.{Actor, ActorLogging}
import java.util.Date
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.Imports._
import biz.paluch.distributedwebsessions.model.PersistentSession

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 18.02.14 12:37
 */
class SessionUpdateActor(mongodb: MongoClient) extends Actor with ActorLogging {

  def updateSession(sessionId: String, timestamp: Date, document: String) {

    val collection = mongodb("sessions_demo")("sessions")
    val query = MongoDBObject("sessionId" -> sessionId)
    val update = $set("document" -> document, "timestamp" -> timestamp)
    val result = collection.update(query, update, upsert = true)
    log.info(s"[$sessionId] Updated $result.getN documents")
  }

  def receive = {
    case session: PersistentSession => updateSession(session.sessionId, session.timestamp, session.document)
  }
}
