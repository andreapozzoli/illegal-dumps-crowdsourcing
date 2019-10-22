<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/css2.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Registration page</title>
<script
	src="${pageContext.request.contextPath}/JAVASCRIPT/registrazione.js"
	defer></script>
</head>
<body>
	<div class="pre_contenitore">
		<h2>Registrazione</h2>
	</div>
	<div class="contenitore">

				<p class="err"> Il nome utente inserito e' gia' esistente</p>
				

				<p class="err1"> L'indirizzo email inserito e' gia' esistente</p>
				

				<p class="err2">Il nome utente e l'indirizzo email inseriti sono gia' esistenti</p>
				

				<p class="err3">L'indirizzo email inserito non e' valido</p>
				
				
				<p class="err4">Completare tutti i campi</p>
				<br>

		
		<c:url  value="/ApriHomeManager" var="managerUrl" />
		<c:url  value="/ApriHomeLavoratore" var="lavoratoreUrl" />
		
		<input type="text" id="urlmanager" value="${managerUrl}" hidden>
		<input type="text" id="urllavoratore" value="${lavoratoreUrl}" hidden>
		
		<form id="formRegistrazione" method="post" name="formR">
			<table>


				<tr>
					<td><label for="selezioneRuolo"><b>Ruolo</b></label></td>
					<td><select id="selezioneRuolo">
							<option id="m" value="manager">Manager</option>
							<option id="l" value="lavoratore">Lavoratore</option>
					</select></td>
				</tr>


				<tr>
					<td><label for="username"><b>Username</b></label></td>
					<td><input id="username" type="text"
						placeholder="Enter Username" name="username" required /></td>
				</tr>
				<tr>
					<td><label for="password"><b>Password</b></label></td>
					<td><input id="password" type="password" placeholder="Enter Password"
						name="password" required /></td>
				</tr>
				<tr>
					<td><label for="email"><b>Email</b></label></td>
					<td><input id="email" type="text" placeholder="Enter email"
						name="email" required /> <br>
					<span id="correttezza"></span></br></td>
				<tr>
					<td><label class="nascosto" id="labelEsp"
						for="livelloEsperienza"><b>Esperienza</b></label></td>
					<td><select class="nascosto" id="esperienza"
						name="livelloEsperienza">
							<option value="alta">Alta</option>
							<option value="media">Media</option>
							<option value="bassa">Bassa</option>
					</select></td>
				</tr>
				<tr>
					<td><label class="nascosto" id="labelFoto" for="foto"><b>Foto</b></label></td>
					<td><input id="foto" class="nascosto" type="file" name="foto" /></td>
				</tr>
			</table>
			<input hidden="true" id="tipo" type="text" name="tipo"
				value="manager" required /> 
				<input id="registrati" type="submit" value="Registrati">
		</form>
	</div>

</body>

</html>