<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/CSS/css3.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Dettaglio campagna</title>
<script
	src="${pageContext.request.contextPath}/JAVASCRIPT/dettagliocampagna.js"
	defer></script>
</head>
<body>
	<div class="header">
		<h1>Dettaglio campagna</h1>
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
				<h3>Nome:</h3>
				<c:out value="${campagna.nome}" />
				<br>
				<h3>Committente:</h3>
				<c:out value="${campagna.committente}" />
				<br>
				<h3>Stato:</h3>
				<c:out value="${campagna.stato}" />
                 <input id="statocampagna" value="${campagna.stato}" hidden>

			</div>
			<div id="corpotabella" class="card">
			<h2>Elenco delle localita' con immagini</h2>
				<c:choose>
					<c:when test="${campagna.localita.size()>0}">
						<table class="campagne">
							<tr>
								<th class="campagne">Nome</th>
								<th class="campagne">Latitudine</th>
								<th class="campagne">Longitudine</th>
								<th class="campagne">Comune</th>
								<th class="campagne">Regione</th>
								
							</tr>
							<tbody >
								<c:forEach var="localita" items="${campagna.localita}"
									varStatus="row">
									<c:choose>
										<c:when test="${row.count % 2 == 0}">
											<tr class="even">
										</c:when>
										<c:otherwise>
											<tr>
										</c:otherwise>
									</c:choose>

									<td class="campagne"><c:out value="${localita.nome}" /></td>
									<td class="campagne"><c:out value="${localita.latitudine}" /></td>
									<td class="campagne"><c:out
											value="${localita.longitudine}" /></td>
									<td class="campagne"><c:out value="${localita.comune}" /></td>
									<td class="campagne"><c:out value="${localita.regione}" /></td>
									<c:choose>
										<c:when test="${localita.immagini.size()>0}">
										
											<c:set var="pres" value="true"></c:set>
											<c:forEach var="immagine" items="${localita.immagini}">
												<td class="campagne"><img width=50 height=50
													src="data:image/png;base64,${immagine.foto}" /></td>
											</c:forEach>
										
										</c:when>
									</c:choose>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						
					</c:when>
					<c:otherwise>Non è presente nessuna localita per questa campagna.
		</c:otherwise>
				</c:choose>				
			</div>
			
			
			<c:choose>
				<c:when test="${campagna.stato==\"creato\"}">
					<div class="card">
						<c:url value="/AggiornaCampagnaWizard" var="wizard">

						</c:url>

						<form id="regForm" action="${wizard }">

							<div class="tab">

								<input id="scelta" type="radio" name="scelta" value="scelta" />Scegli tra le località esistenti <br /> 
								<input id="crea" type="radio" name="scelta" value="crea" /> Crea una nuova località
								
								<div id="selezione">
								
								
									<p>Seleziona una località:</p>
									<label for="selezioneLocalita"><b>Località</b></label> 
									<select	id="selezioneLocalita">
									
									<option value="nonselezionabile" disabled selected>Seleziona una località</option>
													
										<c:forEach var="localita" items="${campagna.localita}" >
											<option id="${localita.id }"><c:out
													value="${localita.nome}" />
											</option>
							
								
										</c:forEach>
									</select>
									
									
									
									
								</div>
								<div id="creazione" class="nascosto">
								
									<p>
										<input id="opzione" type="text" name="opzione" value="scegli" hidden>
										<input id="idlocalita" type="text" name="idlocalita" hidden>
									</p>
									<p>
										<input id="latitudine" type="text" name="latitudine" placeholder="latitudine">
									</p>
									<p>
										<input id="longitudine" type="text" name="longitudine"
											placeholder="longitudine">
									</p>
									<p>
										<input id="nome" type="text" name="nome" placeholder="nome">
									</p>
									<p>
										<input id="comune" type="text" name="comune" placeholder="comune">
									</p>
									<p>
										<input id="regione" type="text" name="regione" placeholder="regione">
									</p>
								</div>
							</div>


							<div class="tab">

								<p>Inserisci immagine:</p>
								<input id="foto" type="file" name="foto" >


							</div>

							<div class="tab">
								<p>Aggiungi dati immagine:</p>


								<p>
									Provenienza: <input id="provenienza" type="text" name="provenienza" placeholder="provenienza">
								</p>
								<p>
									Data: <input id="data" type="date" name="data" placeholder="1999-12-31"><div id="datacon"></div>
								</p>
								<p>
									Risoluzione: <select id="ris" name="risoluzione">
										<option id="a" value="alta" selected>Alta</option>
										<option id="m" value="media">Media</option>
										<option id="b" value="bassa">Bassa</option>

									</select> 

								</p>
								<input id="idcampagna" name="idcampagna" type="text" value="${campagna.id }"
									hidden>


							</div>




							<div style="overflow: auto;">
								<div style="float: right;">
									<button type="button" id="prevBtn">Precedente</button>
									<button type="button" id="nextBtn">Prossimo</button>
									<button type="button" id="cancelBtn">Cancella</button>
								</div>
							</div>

							<div style="text-align: center; margin-top: 40px;">
								<span class="step"></span> <span class="step"></span> <span
									class="step"></span>
							</div>

						</form>


					</div>
				</c:when>
			</c:choose>
		</div>
		<div class="rightcolumn">
			<div class="card">
				<c:choose>
					<c:when test="${campagna.stato==\"creato\"}">
						<c:choose>
							<c:when test="${pres}">
								<c:url value="/AggiornaStatoCampagna" var="statocamp">
								</c:url>
								<form method="post" action="${statocamp}">
									<input type="text" name="stato" value="avviato" hidden>
									<input type="text" name="id" value="${campagna.id}" hidden>
									<input type="submit" value="Avvia campagna">
								</form>
							</c:when>
						</c:choose>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when test="${campagna.stato==\"avviato\"}">
						<c:url value="/AggiornaStatoCampagna" var="statcamp">
						</c:url>
						<form method="post" action="${statcamp}">
							<input type="text" name="stato" value="chiuso" hidden> <input
								type="text" name="id" value="${campagna.id}" hidden> <input
								type="submit" value="Chiudi campagna">
						</form>
					</c:when>
				</c:choose>
				<c:choose>
					<c:when
						test="${campagna.stato==\"avviato\" || campagna.stato==\"chiuso\"}">
						<c:url value="/ApriStatistica" var="statistica">
						</c:url>
						<form method="get" action="${statistica}">
							<input type="text" name="idcampagna" value="${campagna.id}"
								hidden> <input type="submit"
								value="Visualizza le statistiche">
						</form>
					</c:when>
				</c:choose>

				<c:url value="/ApriMappa" var="mappa">
				</c:url>
				<form method="get" action="${mappa}">
					<input type="text" name="idcampagna" value="${campagna.id }" hidden>
					<input type="submit" value="Mappa">
				</form>
			</div>

		</div>
	</div>


</body>
</html>