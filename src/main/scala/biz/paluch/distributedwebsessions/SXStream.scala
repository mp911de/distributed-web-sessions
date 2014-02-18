package biz.paluch.distributedwebsessions

import com.thoughtworks.xstream.XStream

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 18.02.14 13:37
 */
object SXStream {
  private val xstream = new XStream

  def fromXML[T](xml: String): T = {
    xstream.fromXML(xml).asInstanceOf[T]
  }

  def toXML[T](obj: T): String = {
    xstream.toXML(obj)
  }

}
