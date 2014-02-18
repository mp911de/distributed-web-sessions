Example for Distributed Web Sessions backed by MongoDB
======================================================

This is a small example for distributing WebSessions among multiple tomcat servers. You don't need to tweak the Tomcat/JBossAS installation (beside the regular clustering config).

I created it in contrast to regular distributed web sessions. Usually the full session content is replicated after the web request tom all servers.

My approach is slightly different: I've a local and a global session timestamp. The global timestamp is replicated among the whole cluster. As soon as a server
receives a request for a specific session, the server compares his local timestamp against the global timestamp. In case they differ,
the server fetches on demand the session from MongoDB and has from that time on the full session.

This has the advantage, that only servers involved into requests have the full user session.

My usage scenario would incorporate a semi-sticky session but without using jvmroute since this would cause a creation of a new session on the target server.
Semi-sticky means, that in case a server goes offline the session is operated by a different server. So the load balancer in front of the tomcats needs to stick the sessions.

Although my example has a response time of 4-5ms (including MongoDB reads on a local MongoDB) I would not vote for round-robin or another balancing algorithm.

How to build
------------

This project is sbt-based using Scala, Java and Akka.

Just

    sbt clean package

to build the project.