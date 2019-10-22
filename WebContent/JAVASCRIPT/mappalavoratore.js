var margine = 0.001;
var greenIcon = new L.Icon({
	iconUrl: 'CSS/marker-icon-green.png',
	//shadowUrl: 'img/marker-shadow.png',
	iconSize: [25, 41],
	iconAnchor: [12, 41],
	popupAnchor: [1, -34],
	shadowSize: [41, 41],
	id:1
});

function configuraMappa(localita){
	var mymap = L.map('mapid').setView([51.505, -0.09], 13);

	L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw', {
		maxZoom: 18,
		attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, ' +
		'<a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
		'Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
		id: 'mapbox.streets'
	}).addTo(mymap);

	var group = new L.featureGroup();
	group.addTo(mymap);

	var sopra = -90;
	var sotto = 90;
	var destra = -180;
	var sinistra = 180;
	
	for (var i=0 ; i<localita.jaB.length ; i++){
		var latitudine = localita.jaB[i].latitudine;
		var longitudine = localita.jaB[i].longitudine;
		
		var marker = L.marker([latitudine,longitudine],{icon: greenIcon, id: i}).addTo(group);

		marker.addEventListener("click", function apriLocalita(){
			stampaImmagini(this.options.id, localita);
		});

		//marker.bindPopup("<b>"+localita.jaB[i].nome+"</b><br>"+localita.jaB[i].comune+"<br>"+localita.jaB[i].regione).openPopup();

		if (latitudine > sopra){
			sopra = latitudine + margine;
			if (sopra>90){
				sopra=90;
			}
		}

		if (latitudine < sotto){
			sotto = latitudine - margine;
			if (sotto<-90){
				sotto=-90;
			}
		}

		if (longitudine > destra){
			destra = longitudine + margine;
			if (destra>180){
				destra=180;
			}
		}

		if (longitudine < sinistra){
			sinistra = longitudine - margine;
			if (sinistra<-180){
				sinistra=-180;
			}
		}


	}

	mymap.fitBounds(group.getBounds());
	L.rectangle([[sotto,sinistra],[sopra,destra]], {weight: 1}).addTo(mymap);


}

function stampaImmagini(id,loc){
	var div = document.getElementById("localita");
	div.removeChild(div.firstChild);
	var divAnnotazione = document.getElementById("formAnnotazione");
	divAnnotazione.innerHTML="";
	var div1=document.createElement("div"); 
	div.appendChild(div1);
	var newpar=document.createElement("p");
	var strong = document.createElement("strong");
	strong.appendChild(document.createTextNode("Nome localita' : "));
	newpar.appendChild(strong);
	newpar.appendChild(document.createTextNode(loc.jaB[id].nome));
	newpar.appendChild(document.createElement("br"));
	strong = document.createElement("strong");
	strong.appendChild(document.createTextNode("Latitudine : "));
	newpar.appendChild(strong);
	newpar.appendChild(document.createTextNode(loc.jaB[id].latitudine));
	newpar.appendChild(document.createElement("br"));
	strong = document.createElement("strong");
	strong.appendChild(document.createTextNode("Longitudine : " ));
	newpar.appendChild(strong);
	newpar.appendChild(document.createTextNode(loc.jaB[id].longitudine));
	newpar.appendChild(document.createElement("br"));
	strong = document.createElement("strong");
	strong.appendChild(document.createTextNode("Comune di appartenenza : "));
	newpar.appendChild(strong);
	newpar.appendChild(document.createTextNode(loc.jaB[id].comune));
	newpar.appendChild(document.createElement("br"));
	strong = document.createElement("strong");
	strong.appendChild(document.createTextNode("Regione di appartenenza : "));
	newpar.appendChild(strong);
	newpar.appendChild(document.createTextNode(loc.jaB[id].regione));
	newpar.appendChild(document.createElement("br"));
	
	if(loc.jaB[id].immagini.length>0){
		newpar.appendChild(document.createElement("br"));
		newpar.appendChild(document.createTextNode("(Cliccare sull'immagine che si vuole annotare, la form comparira' in fondo alla pagina)"));
		newpar.appendChild(document.createElement("br"));
		for(var y=0;y<loc.jaB[id].immagini.length ; y++){
			newpar.appendChild(document.createElement("br"));
			strong = document.createElement("strong");
			strong.appendChild(document.createTextNode("Immagine "+(y+1)+" :"));
			newpar.appendChild(strong);
			newpar.appendChild(document.createElement("br"));
			var img=document.createElement("img");
			img.src="data:image/png;base64,"+loc.jaB[id].immagini[y].foto;
			img.width=300;
			img.height=300;
			img.id=loc.jaB[id].immagini[y].id;
			img.addEventListener("click", inserisciAnnotazione);
			newpar.appendChild(img);
			newpar.appendChild(document.createElement("br"));
			}
		}
		div1.appendChild(newpar);
		div.appendChild(div1);
		document.getElementById("cardDati").className="visibile";

	
}

function inserisciAnnotazione(event){
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4){
			if (this.status == 200){
				var presenza = this.responseText;
				if (presenza==="erroredati"){
					window.location.href = "/ConvalidaImmagini/ApriPaginaErrore";

				}
				else{
				if(presenza==="assente"){
					creaForm(event.target.id);
				}
				else{
					var divAnnotazione = document.getElementById("formAnnotazione");
					 divAnnotazione.innerHTML="";
					 divAnnotazione.appendChild(document.createTextNode("Hai gia' inserito un'annotazione per questa immagine"));
				}
			}}
			else{
				alert("error");
				console.log("Error");
			}
		}
	};
	var idcampagna = getParameterByName("idcampagna");
	xhttp.open("POST", "/ConvalidaImmagini/GetPresenzaAnnotazione?idImmagine="+event.target.id);
	xhttp.send();
}

function creaForm(idImm){
	var divAnnotazione = document.getElementById("formAnnotazione");
	divAnnotazione.innerHTML="";


	var form = document.createElement("form");
	form.id="nuovaAnnotazione";
	var selectValidita=document.createElement("select");
	selectValidita.name="validita";
	var optionValidita=document.createElement("option");
	form.appendChild(document.createTextNode("Validita' : "));

	optionValidita.value="vera";
	optionValidita.innerHTML="vera";
	selectValidita.appendChild(optionValidita);
	optionValidita=document.createElement("option");
	optionValidita.value="falsa";
	optionValidita.innerHTML="falsa";
	selectValidita.appendChild(optionValidita);
	form.appendChild(selectValidita);
	form.appendChild(document.createElement("br"));

	var selectFiducia=document.createElement("select");
	selectFiducia.name="fiducia";
	var optionFiducia=document.createElement("option");
	form.appendChild(document.createTextNode("Fiducia : "));
	optionFiducia.value="alta";
	optionFiducia.innerHTML="alta";
	selectFiducia.appendChild(optionFiducia);
	optionFiducia=document.createElement("option");
	optionFiducia.value="media";
	optionFiducia.innerHTML="media";
	selectFiducia.appendChild(optionFiducia);
	optionFiducia=document.createElement("option");
	optionFiducia.value="bassa";
	optionFiducia.innerHTML="bassa";
	selectFiducia.appendChild(optionFiducia);
	form.appendChild(selectFiducia);
	form.appendChild(document.createElement("br"));
	form.appendChild(document.createTextNode("Note : "));
	var inputNote=document.createElement("input");
	inputNote.type="text";
	inputNote.name="note";
	form.appendChild(inputNote);
	var submit=document.createElement("input");
	form.appendChild(document.createElement("br"));
	submit.type="submit";
	submit.value="Invia Annotazione"
	
	
	submit.addEventListener("click",function mandaForm(e){
		e.preventDefault();
		var form=document.getElementById("nuovaAnnotazione");
		var xhttp = new XMLHttpRequest();
		
		xhttp.onreadystatechange = function() {
			if (this.readyState == 4){
				if (this.status == 200){
					var ris = this.responseText;
					if(ris==="ko"){
						window.location.href = "/ConvalidaImmagini/ApriPaginaErrore";
					}
					
					
					divAnnotazione.innerHTML="";
					
				}
				else{
					alert("error");
					console.log("Error");
				}
			}
		};
		

		var idcampagna = getParameterByName("idcampagna");
		xhttp.open("POST", "/ConvalidaImmagini/CreaAnnotazione?idimmagine="+idImm+"&idcampagna="+idcampagna);
		 xhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
		xhttp.send("validita="+selectValidita.value+"&fiducia="+selectFiducia.value+"&note="+inputNote.value);
		
	});
	
	
	form.appendChild(submit);
	divAnnotazione.appendChild(document.createTextNode("Iserisci annotazione"));
	divAnnotazione.appendChild(document.createElement("br"));
	divAnnotazione.appendChild(form);
	
}



function getLocalita(){
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		if (this.readyState == 4){
			if (this.status == 200){
				var object = JSON.parse(this.responseText);
				if(object.ris.name==="ko"){
					window.location.href = "/ConvalidaImmagini/ApriPaginaErrore";

				}
				else{
				configuraMappa(object);
			}
			}
			else{
				alert("error");
				console.log("Error");
			}
		}
	};
	var idcampagna = getParameterByName("idcampagna");
	xhttp.open("POST", "/ConvalidaImmagini/setLocalita?idcampagna="+idcampagna+"&tipoUtente=lavoratore");
	xhttp.send();
}

function getParameterByName(name, url) {
	if (!url) url = window.location.href;
	name = name.replace(/[\[\]]/g, '\\$&');
	var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
	results = regex.exec(url);
	if (!results) return null;
	if (!results[2]) return '';
	return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

getLocalita();




