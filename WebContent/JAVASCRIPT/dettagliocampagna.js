var currentTab = 0; // Current tab is set to be the first tab (0)
var next=document.getElementById("nextBtn");
var prev=document.getElementById("prevBtn");
var canc=document.getElementById("cancelBtn");
var scelta=document.getElementsByName("scelta");
var ris=document.getElementById("ris");
var idlocalita=document.createElement("input");
idlocalita.name="idlocalita";


var stato=document.getElementById("statocampagna");
if(stato.value==="creato"){
	var selezionaLocalita = document.getElementById("selezioneLocalita");
	selezionaLocalita.addEventListener("change",function(){
		var element = document.getElementById("idlocalita");
		if(element){
			element.parentNode.removeChild(element);
		}
		idlocalita.value=this.options[ this.selectedIndex ].id;
		idlocalita.hidden=true;
		document.getElementById("selezione").appendChild(idlocalita);
	});


	next.addEventListener("click", nextPrev);
	prev.addEventListener("click", nextPrev);
	canc.addEventListener("click", nextPrev);

	window.addEventListener("load", function (){
		document.getElementById("scelta").checked=true;
	});




	for(var x=0;x<scelta.length;x++){
		scelta[x].addEventListener("click",function(){
			if(this.value==="scelta"){
				document.getElementById("creazione").className="nascosto";
				document.getElementById("selezione").className="visibile";
				document.getElementById("opzione").value="scegli";	    
			}
			else{
				var element = document.getElementById("idlocalita");
				if(element){
					element.parentNode.removeChild(element);
				}
				document.getElementById("selezione").className="nascosto";
				document.getElementById("creazione").className="visibile";
				document.getElementById("opzione").value="crea";
			}
		});
	}
	showTab(currentTab); // Display the current tab



	function showTab(n) {
		// This function will display the specified tab of the form ...
		var x = document.getElementsByClassName("tab");
		x[n].style.display = "block";
		// ... and fix the Previous/Next buttons:
		if (n == 0) {
			document.getElementById("prevBtn").style.display = "none";
		} else {
			document.getElementById("prevBtn").style.display = "inline";
		}
		if (n == (x.length - 1)) {
			document.getElementById("nextBtn").innerHTML = "Fine";
		} else {
			document.getElementById("nextBtn").innerHTML = "Prossimo";
		}
		// ... and run a function that displays the correct step indicator:
		fixStepIndicator(n)
	}

	function nextPrev() {
		if(this.id!=="cancelBtn"){
			var n;

			if(this.id==="nextBtn")
				n=1;
			else 
				n=-1;
			// This function will figure out which tab to display
			var x = document.getElementsByClassName("tab");
			// Exit the function if any field in the current tab is invalid:
			if (n == 1 && !validateForm()) return false;
			// Hide the current tab:
			x[currentTab].style.display = "none";
			// Increase or decrease the current tab by 1:
			currentTab = currentTab + n;
			// if you have reached the end of the form... :
			if (currentTab >= x.length) {
				//...the form gets submitted:
				aggiornaTabella(next);
				return false;
			}
			// Otherwise, display the correct tab:
			showTab(currentTab);
		}
		else{
			var x, y,z;
			x = document.getElementsByClassName("tab");
			for (var i=0;i<3;i++){
				y = x[i].getElementsByTagName("input");
				z = x[i].getElementsByTagName("select");
				for(let k=0;k<y.length;k++){
					if(y[k].name!=="idcampagna" && y[k].name!=="idlocalita" && y[k].name!=="scelta" && y[k].name!=="opzione"){
						y[k].value="";
					}
				}

				for(let k=0;k<z.length;k++){
					z[k].selectedIndex=0;
				}

			}
			var e = document.createEvent("HTMLEvents");
			e.initEvent("click", false, true); 
			scelta[0].dispatchEvent(e); 
			if(currentTab<3)
				x[currentTab].style.display = "none";
			for(let h=0;h<3;h++)
				document.getElementsByClassName("step")[h].className = "step";
			currentTab=0;
			showTab(currentTab);
		}
	}

	function validateForm() {
		// This function deals with validation of the form fields
		var x, y, i, valid = true;
		x = document.getElementsByClassName("tab");
		y = x[currentTab].getElementsByTagName("input");

		if(currentTab===0){
			if(document.getElementById("scelta").checked){
				if(selezionaLocalita.options[ selezionaLocalita.selectedIndex ].value==="nonselezionabile"){
					selezionaLocalita.className = "invalid";
					valid=false;
				}
				else{
					selezionaLocalita.className = "";
				}
			}
			else{
				
				for (i = 0; i < y.length; i++) {
					// If a field is empty...
					if (y[i].value == "") {
						// add an "invalid" class to the field:
						y[i].className += " invalid";
						// and set the current valid status to false:
						valid = false;
					}
					else{
						y[i].className = "";
					}
					var latit = document.getElementById("latitudine");
					
					if (isNaN(latit.value) || latit.value<-90 || latit.value>90){
						latit.className += " invalid";						
						valid = false;
					}
					var longi = document.getElementById("longitudine");
					if (isNaN(longi.value) || longi.value<-180 || longi.value>180){
						longi.className += " invalid";						
						valid = false;
					}
				}
			}
		}

		else if(currentTab===1){
			if(!document.getElementById("foto").value){
				document.getElementById("foto").className="invalid";
				valid=false;
			}
			else{
				document.getElementById("foto").className="";
			}
		}

		else{
			// A loop that checks every input field in the current tab:
			for (i = 0; i < y.length; i++) {
				// If a field is empty...
				if (y[i].value == "") {
					// add an "invalid" class to the field:
					y[i].className += " invalid";
					// and set the current valid status to false:
					valid = false;
				}
				else{
					y[i].className = "";
				}
			}
			
			var dat = document.getElementById("data");
			var espressione = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
			if (!espressione.test(dat.value)){
				dat.className += " invalid";
				valid = false;}
			
			
			
		}
		// If the valid status is true, mark the step as finished and valid:
		if (valid) {
			document.getElementsByClassName("step")[currentTab].className += " finish";
		}
		else{
			document.getElementsByClassName("step")[currentTab].className = "step active";
		}
		return valid; // return the valid status
	}

	function fixStepIndicator(n) {
		// This function removes the "active" class of all steps...
		var i, x = document.getElementsByClassName("step");
		for (i = 0; i < x.length; i++) {
			x[i].className = x[i].className.replace(" active", "");
		}
		//... and adds the "active" class to the current step:
		x[n].className += " active";
	}

}


var immagini= document.getElementsByTagName("img");
var testo;

for(var g=0;g<immagini.length;g++){
	immagini[g].addEventListener("click",function(){
		var w = 700;
		var h = 600;
		var l = Math.floor((screen.width-w)/2);
		var t = Math.floor((screen.height-h)/2);
		var stili = "width=" + w + ",height=" + h + ",top=" + t + ",left=" + l+", status=no, menubar=no, toolbar=no scrollbars=no";
		testo = window.open("", "", stili);
		testo.document.write("<html>");
		testo.document.write(" <head>");
		testo.document.write(" <title>Immagine</title>");
		testo.document.write(" <basefont size=2 face=Tahoma>");
		testo.document.write(" </head>");
		testo.document.write("<body topmargin=50>");
		testo.document.write("<img width=500 height=500 src="+this.src+"/>");
		testo.document.write("</body>");
		testo.document.write("</html>");
	});

}










function aggiornaTabella(e){
	var idcampagna = document.getElementById("idcampagna").value;
	var scelta = document.getElementById("opzione").value;
	var foto = document.getElementById("foto").value;
	var provenienza = document.getElementById("provenienza").value;
	var data = document.getElementById("data").value;
	var ris = document.getElementById("ris");
	var risoluzione = ris.options[ris.selectedIndex].value;
	var x = new XMLHttpRequest();
	x.onreadystatechange = function aggiornaWizard(e){
		if (x.readyState == 4 && x.status == 200){

			
			var localitaaggiornate = JSON.parse(x.responseText);
			if (localitaaggiornate.ris.name==="vabene"){
				var l = localitaaggiornate.jaB.length;
				var elem;
				var elem2;
				var k;
				var divtabella = document.getElementById("corpotabella");
				divtabella.innerHTML="";
				var h2 = document.createElement("h2");
				h2.appendChild(document.createTextNode("Elenco delle localita' con immagini"));
				divtabella.appendChild(h2);
				
				var tabella =  document.createElement("table");
				var corpotabella = document.createElement("tbody"); 
				var tr = document.createElement("tr");
				var th = document.createElement("th");
				th.appendChild(document.createTextNode("Nome"));
				tr.appendChild(th);
				th = document.createElement("th");
				th.appendChild(document.createTextNode("Latitudine"));
				tr.appendChild(th);
				th = document.createElement("th");
				th.appendChild(document.createTextNode("Longitudine"));
				tr.appendChild(th);
				th = document.createElement("th");
				th.appendChild(document.createTextNode("Comune"));
				tr.appendChild(th);
				th = document.createElement("th");
				th.appendChild(document.createTextNode("Regione"));
				tr.appendChild(th);
				tabella.appendChild(tr);

				var selezioneLoc = document.getElementById("selezioneLocalita");
				selezioneLoc.innerHTML="";
				var noSel = document.createElement("option");
				noSel.value = "nonselezionabile";
				noSel.disabled = true;
				noSel.selected = true;
				noSel.appendChild(document.createTextNode("Seleziona una localita'"));
				selezioneLoc.appendChild(noSel);
				for (k=0; k<l; k++){
					var locagg = localitaaggiornate.jaB[k];
					var optionLoc = document.createElement("option");
					optionLoc.id = locagg.id;
					optionLoc.appendChild(document.createTextNode(locagg.nome));
					selezioneLoc.appendChild(optionLoc);
					elem = document.createElement("tr");
					if (k%2==1){
						elem.className = "even";
					}
					elem2 = document.createElement("td");
					elem2.appendChild(document.createTextNode(locagg.nome));
					elem.appendChild(elem2);
					elem2 = document.createElement("td");
					elem2.appendChild(document.createTextNode(locagg.latitudine));
					elem.appendChild(elem2);
					elem2 = document.createElement("td");
					elem2.appendChild(document.createTextNode(locagg.longitudine));
					elem.appendChild(elem2);
					elem2 = document.createElement("td");
					elem2.appendChild(document.createTextNode(locagg.comune));
					elem.appendChild(elem2);
					elem2 = document.createElement("td");
					elem2.appendChild(document.createTextNode(locagg.regione));
					elem.appendChild(elem2);
					var l2 = locagg.immagini.length;
					var g;
					for (g=0; g<l2; g++){
						var elem3 = document.createElement("img");
						elem3.height = 50;
						elem3.width = 50;
						elem3.src = "data:image/png;base64,"+locagg.immagini[g].foto;
						elem2 = document.createElement("td");
						elem2.appendChild(elem3);
						elem.appendChild(elem2);
					}
					corpotabella.appendChild(elem);
					tabella.appendChild(corpotabella);
					divtabella.appendChild(tabella);

				}
				var evento = document.createEvent("HTMLEvents");
				evento.initEvent("click",false,true);
				canc.dispatchEvent(evento);

				var immaginidopo= document.getElementsByTagName("img");
				var testodopo;

				for(var g=0;g<immaginidopo.length;g++){
					immaginidopo[g].addEventListener("click",function(){
						var w = 700;
						var h = 600;
						var l = Math.floor((screen.width-w)/2);
						var t = Math.floor((screen.height-h)/2);
						var stili = "width=" + w + ",height=" + h + ",top=" + t + ",left=" + l+", status=no, menubar=no, toolbar=no scrollbars=no";
						testodopo = window.open("", "", stili);
						testodopo.document.write("<html>");
						testodopo.document.write(" <head>");
						testodopo.document.write(" <title>Immagine</title>");
						testodopo.document.write(" <basefont size=2 face=Tahoma>");
						testodopo.document.write(" </head>");
						testodopo.document.write("<body topmargin=50>");
						testodopo.document.write("<img width=500 height=500 src="+this.src+"/>");
						testodopo.document.write("</body>");
						testodopo.document.write("</html>");
					});

				}


			}
			else if (localitaaggiornate.ris.name==="nonvabene"){
				alert("hai inserito la data in un formato non valido");
				window.location.reload();
			}
			else if (localitaaggiornate.ris.name==="nonvabenenumero"){
				alert("hai inserito latitudine o longitudine in un formato non valido");
				window.location.reload();
			}
			else if (localitaaggiornate.ris.name==="errpage"){
				window.location.href = "/ConvalidaImmagini/ApriPaginaErrore";
			}
			else if (localitaaggiornate.ris.name==="nullita"){
				alert("tutti i campi devono essere compilati");
				window.location.reload();
			}
		}

	};
	x.open("POST", "/ConvalidaImmagini/AggiornaCampagnaWizard");
	x.setRequestHeader("Content-type","application/x-www-form-urlencoded");
	if (scelta==="scegli"){
		x.send("opzione="+scelta+"&idlocalita="+idlocalita.value+"&idcampagna="+idcampagna+"&foto="+foto+"&provenienza="+provenienza+"&data="+data+"&risoluzione="+risoluzione);
	}

	else{
		var latitudine = document.getElementById("latitudine").value;
		var longitudine = document.getElementById("longitudine").value;
		var nome = document.getElementById("nome").value;
		var comune = document.getElementById("comune").value;
		var regione = document.getElementById("regione").value;
		x.send("opzione="+scelta+"&idcampagna="+idcampagna+"&foto="+foto+"&provenienza="+provenienza+"&data="+data+"&risoluzione="+risoluzione+"&latitudine="+latitudine+"&longitudine="+longitudine+"&nome="+nome+"&comune="+comune+"&regione="+regione);

	}
}

var divdata = document.getElementById ("datacon");
var datacontrollo = document.getElementById("data");
datacontrollo.addEventListener("keypress", function(e){
	divdata.innerHTML="";
	var espressione = /^[0-9]{4}-[0-9]{2}-[0-9]{2}$/;
	if (!espressione.test(datacontrollo.value+e.key)){
		divdata.appendChild(document.createTextNode("Il formato della data non e' valido, formato corretto yyyy-mm-dd"));



	}
}, false);


