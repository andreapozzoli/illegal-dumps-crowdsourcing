<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/css3.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home Page Lavoratore</title>
</head>
<body>
<div class="header">
		<h1>Home Page</h1>
	</div>
	<div class="topnav">
	<c:choose>
			<c:when test="${currentUser.tipo.equals('manager') }">
				<c:url value="/ApriHomeManager" var="home">
				</c:url>
			</c:when>
			<c:otherwise>
				<c:url value="/ApriHomeLavoratore" var="home">
				</c:url>
			</c:otherwise>
		</c:choose>
		<a href="${home }">Home</a>
			<c:url value="/ApriModificaDati" var="modificaDati">
					</c:url>
					<a href="${modificaDati }">ModificaDati</a>
					<!--  <form method="post" action="${modificaDati}">
						<input type="submit" value="Modifica Dati">
					</form>-->
					<c:url value="/Logout" var="logout">
					</c:url>
					<a href="${logout }">Logout</a>
					<!-- <form method="get" action="${logout}">
						<input type="submit" value="logout">
					</form> -->
</div>
	
	<div class="row">
			<div class="leftcolumn">
			<div class="card">
	<c:choose>
		<c:when test="${campagneNonIscritto.size()>0}">
			<table class="campagne">
				<tr>
					<th class="campagne">Campagne attive non ancora optate</th>
				</tr>
				<tbody>
					<c:forEach var="campagna" items="${campagneNonIscritto}"
						varStatus="row">
						<c:choose>
							<c:when test="${row.count % 2 == 0}">
								<tr class="even">
							</c:when>
							<c:otherwise>
								<tr>
							</c:otherwise>
						</c:choose>
						<c:url value="/ApriMappa" var="mapURL">
							<c:param name="idcampagna" value="${campagna.id}" />
						</c:url>
						<td class="campagne"><a class="nomeCampagna" href="${mapURL}"> <c:out value="${campagna.nome}" />
						</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>Non è presente nessuna campagna attiva non optata.
		</c:otherwise>
	</c:choose>
			</div>
						<div class="card">
			
	<c:choose>
		<c:when test="${campagneIscritto.size()>0}">
			<table class="campagne">
				<tr>
					<th class="campagne">Campagne attive già optate</th>
				</tr>
				<tbody>
					<c:forEach var="campagna" items="${campagneIscritto}"
						varStatus="row">
						<c:choose>
							<c:when test="${row.count % 2 == 0}">
								<tr class="even">
							</c:when>
							<c:otherwise>
								<tr>
							</c:otherwise>
						</c:choose>
						<c:url value="/ApriMappa" var="mapURL">
							<c:param name="idcampagna" value="${campagna.id}" />
						</c:url>
						<td class="campagne"><a class="nomeCampagna" href="${mapURL}"> <c:out value="${campagna.nome}" />
						</a></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</c:when>
		<c:otherwise>Non è presente nessuna campagna attiva già optata.
		</c:otherwise>
	</c:choose>
	</div>
</div>
	</div>
</body>
</html>