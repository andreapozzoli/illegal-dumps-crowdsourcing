<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/css4.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Home Page Manager</title>

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
			<c:when test="${campagne.size()>0}">
				<table class="campagne">
					<tr>
						<h2 class="campagne">Campagne realizzate</h2>
					</tr>
					<tbody>
						<c:forEach var="campagna" items="${campagne}" varStatus="row">
							<c:choose>
								<c:when test="${row.count % 2 == 0}">
									<tr class="even">
								</c:when>
								<c:otherwise>
									<tr>
								</c:otherwise>
							</c:choose>
							<c:url value="/ApriDettaglioCampagna" var="regURL">
								<c:param name="idcampagna" value="${campagna.id}" />
								<c:param name="nomecampagna" value="${campagna.nome}" />
							</c:url>
							<td class="campagne"><a class="nomeCampagna"
								href="${regURL}"> <c:out value="${campagna.nome}" />
							</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:when>
			<c:otherwise>Non è ancora stata creata nessuna campagna.
		</c:otherwise>
		</c:choose>
		</div>
		</div>
		
		<div class="rightcolumn">
		 <div class="card">
		<c:url value="/CreaCampagna" var="postUrl">
		</c:url>
		<table>
			<tr>
				<h2>Crea una nuova campagna</h2>
				<form method="post" action="${postUrl}">
					<br> Nome: <input name="nome" type="text"
						placeholder="Inserisci nome" required> <br>
					Committente: <input name="committente" type="text"
						placeholder="Inserisci committente" required> <br> <input
						type="submit" value="Crea">
				</form>
			</tr>
			</table>
	
	</div>
	</div>
	</div>


</body>
</html>