name := "distributed-web-sessions"

version := "1.0"

resolvers += "Local Maven Repository" at "file://" + Path.userHome.absolutePath + "/.m2/repository"

seq(webSettings: _*)

externalResolvers <<= resolvers map {
  rs =>
    Resolver.withDefaultResolvers(rs, mavenCentral = true)
}

libraryDependencies += "javax.servlet" % "javax.servlet-api" % "3.0.1" % "provided"

libraryDependencies += "org.mongodb" % "casbah_2.10" % "2.6.5" exclude("org.slf4j", "slf4j-api")

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.2.3"

libraryDependencies += "com.thoughtworks.xstream" % "xstream" % "1.4.7"

libraryDependencies += "org.mortbay.jetty" % "jetty" % "6.1.22" % "container"

libraryDependencies += "com.typesafe" %% "scalalogging-slf4j" % "1.0.1"
