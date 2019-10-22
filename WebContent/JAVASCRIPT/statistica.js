var immagini = document.getElementsByTagName("img");
var idimmagine;
for (i=0; i<immagini.length; i++){
	immagini[i].addEventListener("click", function(e){
		idimmagine = this.id;
		var x = new XMLHttpRequest();
		x.onreadystatechange = function aggiornaAnnotazioni(e){
			if (x.readyState == 4 && x.status == 200){
				var annotazioniConflittuali = JSON.parse(x.responseText);
				if (annotazioniConflittuali.ris.name==="ko"){
					window.location.href = "/ConvalidaImmagini/ApriPaginaErrore";

				}else{
				var l = annotazioniConflittuali.jaB.length;
				var elem;
				var k;
				var conflitti=document.querySelectorAll(".conflitti");
				for(var w=0;w<conflitti.length;w++){
					conflitti[w].parentNode.removeChild(conflitti[w]);
				}
				for (k=0; k<l; k++){
					var ann = document.getElementById("div"+idimmagine);
					let td = document.createElement("td");
					td.className="conflitti";
					let strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Id Lavoratore: "));
					td.appendChild(strong);
					td.appendChild(document.createTextNode( annotazioniConflittuali.jaB[k].idlavoratore));
					td.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Data: "));
					td.appendChild(strong);
					td.appendChild(document.createTextNode( annotazioniConflittuali.jaB[k].data));
					td.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Validita': "));
					td.appendChild(strong);
					td.appendChild(document.createTextNode( annotazioniConflittuali.jaB[k].validita));
					td.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Fiducia :"));
					td.appendChild(strong);
					td.appendChild(document.createTextNode(annotazioniConflittuali.jaB[k].fiducia));
					td.appendChild(document.createElement("br"));
					strong = document.createElement("strong");
					strong.appendChild(document.createTextNode("Note: "));
					td.appendChild(strong);
					td.appendChild(document.createTextNode(annotazioniConflittuali.jaB[k].note));
					td.appendChild(document.createElement("br"));
					td.appendChild(document.createElement("br"));
					ann.appendChild(td);
				}
			}
			}
		};
		x.open("POST", "/ConvalidaImmagini/SetAnnotazioniConflittuali?idimmagine="+idimmagine);
		x.send();
		
	}, false);
}
