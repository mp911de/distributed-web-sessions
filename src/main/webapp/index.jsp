<%@ page import="biz.paluch.distributedwebsessions.MySession" %>
<html>
<head><title>Demo</title></head>
<body>
<%

    MySession mySession = (MySession) session.getAttribute("MySession");
%>
<h2>System <%= System.getProperty("system")%>
</h2>

<h2>Local Value (pre-increment)</h2>

<p><%= mySession.localCheckModel().getCounter() %>
</p>

<h2>Global Value (pre-increment)</h2>

<p><%= mySession.model().getCounter() %>
</p>

<a href="<%= request.getRequestURI() %>"><h3>Reload</h3></a>
<%

    mySession.localCheckModel().setCounter(mySession.localCheckModel().getCounter() + 1);
    mySession.model().setCounter(mySession.model().getCounter() + 1);
%>
</body>
</html>
