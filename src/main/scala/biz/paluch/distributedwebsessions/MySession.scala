package biz.paluch.distributedwebsessions

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 * @since 18.02.14 12:52
 */
class MySession extends Serializable {


  // In order to prevent tomcat distribution of the model
  @transient
  var model: SomeModel = new SomeModel;

  @transient
  var localSessionTimestamp: Long = -1;

  var globalSessionTimestamp: Long = -1;

  // this one is a local instance in order to show, that these can have local and distributed parts.
  // ideally, globalModel counter is 2 and two local instances have 1 each.
  @transient
  var localCheckModel: SomeModel = new SomeModel;
}
