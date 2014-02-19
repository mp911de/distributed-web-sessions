package biz.paluch.distributedwebsessions.model

import java.util.Date

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 18.02.14 12:38
 */
class PersistentSession(val sessionId: String,
                        val timestamp: Date,
                        val document: String) {


}
