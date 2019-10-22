<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Errore</title>
</head>
<body>
<h1>Ops! Si è verificato un errore.</h1>
<c:choose>
<c:when test="${user==\"manager\" }"><c:url value="/ApriHomeManager" var="homeUrl">
				</c:url></c:when>
				<c:otherwise><c:url value="/ApriHomeLavoratore" var="homeUrl">
				</c:url></c:otherwise>
</c:choose>
<h2>Torna alla home cliccando <a href="${homeUrl }">qui</a>.</h2>
</body>
</html>