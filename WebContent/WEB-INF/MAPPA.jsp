<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
	
	<title>Mappa</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/cssmappa.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<link rel="shortcut icon" type="image/x-icon" href="docs/images/favicon.ico" />

    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.5.1/dist/leaflet.css" integrity="sha512-xwE/Az9zrjBIphAcBb3F6JVqxf46+CDLwfLMHloNu6KEQCAWi6HcDUbeOfBIptF7tcCzusKFjFw2yuvEpDL9wQ==" crossorigin=""/>
    <script src="https://unpkg.com/leaflet@1.5.1/dist/leaflet.js" integrity="sha512-GffPMF3RvMeYyc1LWMHtK8EbPv0iNZ8/oTtHPx9/cc2ILxQ+u905qIwdpULaqDkyBKgOaB57QTMg7ztg8Jm2Og==" crossorigin=""></script>
    
    
<c:choose>
<c:when test="${user.tipo==\"manager\"}">
<script src="${pageContext.request.contextPath}/JAVASCRIPT/mappamanager.js" defer></script>
</c:when>
<c:otherwise>
<script src="${pageContext.request.contextPath}/JAVASCRIPT/mappalavoratore.js" defer></script>
</c:otherwise>
</c:choose>

	
</head>
<body>
<div class="header">
		<h1>Mappa della campagna</h1>
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


<div id="mapid" style="width: 600px; height: 400px;"></div>


</div>
</div>
			<div class="rightcolumn">
			<div id="cardDati" class="nascosto" >
<div class="card" >
<div id="localita"><div id="contenutolocalita"></div></div>

<div id="formAnnotazione"></div>
</div></div></div>
</div>
</body>
</html>