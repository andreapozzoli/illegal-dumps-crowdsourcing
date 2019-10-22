<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<script
	src="${pageContext.request.contextPath}/JAVASCRIPT/statistica.js" defer></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/cssstatistica.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Statistica campagna</title>
</head>
<body>
	<div class="header">
		<h1>Statistica campagna</h1>
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

		<c:url value="/Logout" var="logout">
		</c:url>
		<a href="${logout }">Logout</a>

	</div>

	<div class="row">
		<div class="leftcolumn">
			<div class="card">
				<strong>Numero totale di localita:</strong>
				<c:out value="${statistiche[0]}" />
				<br>
				<strong> Numero totale di immagini:</strong>
				<c:out value="${statistiche[1]}" />
				<br>
				<strong> Numero totale di annotazioni:</strong>
				<c:out value="${statistiche[2]}" />
				<br>
				<c:choose>
					<c:when test="${statistiche[0]>0}">
						<strong>Numero medio di immagini per localita:</strong>
						<c:out value="${statistiche[1]/statistiche[0]}" />
						<br>
					</c:when>
					<c:otherwise>
						<strong>Numero medio di immagini per localita:</strong> 0 <br>
					</c:otherwise>
				</c:choose>
				<c:choose>
					<c:when test="${statistiche[1]>0}">
						<strong>Numero medio di annotazioni per immagine:</strong>
						<c:out value="${statistiche[2]/statistiche[1]}" />
						<br>
					</c:when>
					<c:otherwise>
						<strong>Numero medio di annotazioni per immagine:</strong> 0 <br>
					</c:otherwise>
				</c:choose>
				<strong>Numero totale di conflitti:</strong>
				<c:out value="${statistiche[3]}" />
				<br>
			</div>
		</div>
		<div class="rightcolumn">
			<div class="card">


				<c:choose>
					<c:when test="${conflitti.size()>0}">
						<table class="campagne">
							<tr>
								<th class="campagne">Elenco dei conflitti</th>
							</tr>
							<tbody>
								<c:forEach var="immagine" items="${conflitti}" varStatus="row">
									<c:choose>
										<c:when test="${row.count % 2 == 0}">
											<tr id="div${immagine.id }" class="even">
										</c:when>
										<c:otherwise>
											<tr id="div${immagine.id }">
										</c:otherwise>
									</c:choose>

									<td class="campagne"><img id="${immagine.id }" width=100
										height=100 src="data:image/png;base64,${immagine.foto}" /> <br>
									<br></td>

									</tr>
								</c:forEach>
								<tr>
									<td><div id="ann"></div></td>
								</tr>
							</tbody>
						</table>
					</c:when>
					<c:otherwise>Non ci sono conflitti.
		</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</body>
</html>