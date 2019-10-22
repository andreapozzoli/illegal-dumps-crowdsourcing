<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<script src="${pageContext.request.contextPath}/JAVASCRIPT/modificadati.js"
	defer></script>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/modificadati.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Modifica Dati Personali</title>
</head>
<body>

<div class="header">
		<h1>Aggiorna i tuoi dati</h1>
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
			
			<p class="err">Nome utente o indirizzo mail gia' in uso</p>
		    <p class="err1">Completare tutti i campi</p>
		    <p class="err2">Indirizzo email non valido</p>
				<br>
			
			<input id="utentetipo" value="${currentUser.tipo }" hidden>
			<c:url  value="/ApriHomeManager" var="managerUrl" />
		<c:url  value="/ApriHomeLavoratore" var="lavoratoreUrl" />
		
		
<c:choose>
<c:when test="${currentUser.tipo.equals('manager') }">
				<form id="formManager" method="post" action="${managerUrl}">
					<br> <strong>Nome:</strong> <input name="nome" type="text" value="${utenteCompleto.nomeUtente }" placeholder="Inserisci nuovo nome utente" required>
					<br> <strong>Password:</strong> <input name="password" type="text" value="${utenteCompleto.password }" placeholder="Inserisci nuova password"  required>
					<br><strong> Email:</strong> <input name="email" type="text" value="${utenteCompleto.email }" placeholder="${utenteCompleto.email }" required>
					<input name="aggiorna" type="submit" value="Aggiorna">
				</form>
</c:when>
<c:otherwise>
			<input id="utenteesp" value="${utenteCompleto.livelloEsperienza }" hidden>

<form id="formLavoratore" method="post" action="${lavoratoreUrl}">
					<br> <strong>Nome:</strong> <input name="nome" type="text" value="${utenteCompleto.nomeUtente }" placeholder="${utenteCompleto.nomeUtente }" required>
					<br><strong> Password: </strong><input name="password" type="text" value="${utenteCompleto.password }" placeholder="Inserisci nuova password" required >
					<br><strong> Email:</strong> <input name="email" type="text" value="${utenteCompleto.email }" placeholder="${utenteCompleto.email }" required>
					<br><strong> Esperienza:</strong> <select id="livelloEsperienza" name="livelloEsperienza">
										<option id="a" value="alta" selected >Alta</option>
										<option id="m" value="media">Media</option>
										<option id="b" value="bassa">Bassa</option></select>
					<br> <strong>Foto:</strong><br><c:choose> <c:when test="${utenteCompleto.foto!=null}">
					<img id="immagine" src="data:image/png;base64,${utenteCompleto.foto}" height=300 width=300>
					</c:when> 
					<c:otherwise><br>Non hai ancora caricato una foto.<br></c:otherwise>
					</c:choose><br><input id="foto" type="file" name="foto">
					<input name="aggiorna"  type="submit" value="Aggiorna">
				</form>
</c:otherwise>
</c:choose>
</div>
</div></div>
</body>
</html>