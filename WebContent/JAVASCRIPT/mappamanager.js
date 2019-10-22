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

var redIcon = new L.Icon({
	iconUrl: 'CSS/marker-icon-red.png',
	//shadowUrl: 'img/marker-shadow.png',
	iconSize: [25, 41],
	iconAnchor: [12, 41],
	popupAnchor: [1, -34],
	shadowSize: [41, 41],
	id:1
});

var yellowIcon = new L.Icon({
	iconUrl: 'CSS/marker-icon-yellow.png',
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

		var colore=generaColore(localita.jaB[i].immagini);
		var marker = L.marker([latitudine,longitudine], {icon: colore, id: i}).addTo(group);

		marker.addEventListener("click", function apriLocalita(){
			stampaDati(this.options.id, localita);
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

function stampaDati(id, loc){
	var div = document.getElementById("localita");
	div.removeChild(div.firstChild);
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
			newpar.appendChild(img);
			if(loc.jaB[id].immagini[y].annotazioni.length>0){
				for(var g=0;g<loc.jaB[id].immagini[y].annotazioni.length ; g++){
					newpar.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Annotazione "+(g+1)+" :"));
					newpar.appendChild(strong);
					newpar.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Id lavoratore : "));
					newpar.appendChild(strong);
					newpar.appendChild(document.createTextNode(loc.jaB[id].immagini[y].annotazioni[g].idlavoratore));
					newpar.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Data : "));
					newpar.appendChild(strong);
					newpar.appendChild(document.createTextNode(loc.jaB[id].immagini[y].annotazioni[g].data));
					newpar.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Validita' : "));
					newpar.appendChild(strong);
					newpar.appendChild(document.createTextNode(loc.jaB[id].immagini[y].annotazioni[g].validita));
					newpar.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Fiducia : "));
					newpar.appendChild(strong);
					newpar.appendChild(document.createTextNode(loc.jaB[id].immagini[y].annotazioni[g].fiducia));
					newpar.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Note : "));
					newpar.appendChild(strong);
					newpar.appendChild(document.createTextNode(loc.jaB[id].immagini[y].annotazioni[g].note));
                    newpar.appendChild(document.createElement("br"));
				}
				newpar.appendChild(document.createElement("br"));
			}
		}

	}
	div1.appendChild(newpar);
	div.appendChild(div1);
	
	document.getElementById("cardDati").className="visibile";

}

function generaColore(arr){
	var colore;
	var presenzaAnnotazioni=false;

	if (arr.length===0){
		colore = yellowIcon;
	}
	
	else if(arr.length>0){
		for(let k=0;k<arr.length;k++){
			if(arr[k].annotazioni.length === 0){
				presenzaAnnotazioni=true;
				break;
			}
		}
		if(haConflitto(arr)){
			colore = redIcon;
		}
		else if (!presenzaAnnotazioni)
			colore = greenIcon;
		else 
			colore = yellowIcon;
	}
	return colore;
}

function haConflitto(arr){

	for(var i=0; i<arr.length;i++){
		var conflitto;
		if(arr[i].annotazioni.length!==0){
			if(arr[i].annotazioni[0].validita==="vera"){
				conflitto=true;
			}
			else{
				conflitto=false;
			}
			for(var j=1; j<arr[i].annotazioni.length;j++){
				if ((conflitto && arr[i].annotazioni[j].validita==="falsa" ) ||(!conflitto && arr[i].annotazioni[j].validita==="vera" )){
					return true;
				}
			}
		}
	}
	return false;
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
	xhttp.open("POST", "/ConvalidaImmagini/setLocalita?idcampagna="+idcampagna+"&tipoUtente=manager");
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



