<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/css1.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login page</title>
<script src="${pageContext.request.contextPath}/JAVASCRIPT/login.js" defer></script>
</head>
<body>
	<div class="pre_contenitore">
		<h2>Login</h2>
	</div>
	<div class="contenitore">
	
				<p class="err">Le credenziali inserite non sono valide</p>
				<p class="err1">Completare tutti i campi</p>
				<br>
			
		<c:url  value="/ApriHomeManager" var="managerUrl" />
		<c:url  value="/ApriHomeLavoratore" var="lavoratoreUrl" />
		
		<input type="text" id="urlmanager" value="${managerUrl}" hidden>
		<input type="text" id="urllavoratore" value="${lavoratoreUrl}" hidden>
		
		
		<form id="formLogin" method="post" >
			<table>
				<tr>
					<td><label for="username"><b>Username</b></label></td>
					<td><input id="username" type="text" placeholder="Enter Username"
						name="username" required /></td>
				</tr>
				<tr>
					<td><label for="password"><b>Password</b></label></td>
					<td><input id="password" type="password" placeholder="Enter Password"
						name="password" required /></td>
				</tr>

			</table>
			<input id="login" type="submit" value="Login">
		</form>
		<table>
			<tr>
				<td><p>Se non hai un account effettua la registrazione:</p></td>
			</tr>
			<tr>
				<td><c:url value="/ApriRegistrazione" var="regUrlo">
				</c:url>
					</td>
			</tr>
		</table>
		<form method="get" action="${regUrlo}">
	<input type="submit" value="Registrati">
	</form>
	</div>
</body>
</html>