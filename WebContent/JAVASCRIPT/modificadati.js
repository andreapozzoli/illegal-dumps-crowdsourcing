window.addEventListener("load", function(e){
	var utente = document.getElementById("utentetipo").value;
	if (utente==="lavoratore"){
		var sel = document.getElementById("livelloEsperienza");
		var utenteesp = document.getElementById("utenteesp").value;

		if (utenteesp==="alta"){
			var opzione = document.getElementById("a");
			opzione.selected=true;
		}
		else if (utenteesp==="media"){
			var opzione = document.getElementById("m");
			opzione.selected=true;		}
		else{
			var opzione = document.getElementById("b");
			opzione.selected=true;		}
	}

}, false);

var tipo=document.getElementById("utentetipo").value;
var idform;
if(tipo==="manager")
	idform="formManager";
else
	idform="formLavoratore"

var aggiorna=document.getElementById(idform);

aggiorna.elements["aggiorna"].addEventListener("click",function (e){
	e.preventDefault();
	var err = document.getElementsByClassName("err");
	var err1 = document.getElementsByClassName("err1");
	
	var err2 = document.getElementsByClassName("err2");
	err[0].style.display = "none";
	err1[0].style.display="none";
	err2[0].style.display="none";
		var x = new XMLHttpRequest();
		x.onreadystatechange = function(){
			if (this.readyState == 4){
				if (this.status == 200){
				
					var object = this.responseText;
				
					if(object==="rotto"){
						window.location.href = "/ConvalidaImmagini/ApriPaginaErrore";
					}
					else{
					if(object==="manager" || object==="lavoratore"){
						aggiorna.submit();
					}
					else{
						if(object==="ko")
						err[0].style.display = "block";
						else if(object==="ko1")
							err1[0].style.display = "block";
						else if(object==="ko2")
							err2[0].style.display = "block";
					}
				}
				}
				else{
					alert("error");
					console.log("Error");
				}
			}
		};
		x.open("POST", "/ConvalidaImmagini/AggiornaUtente");
		x.setRequestHeader("Content-type","application/x-www-form-urlencoded");

		var body="nome="+aggiorna.elements["nome"].value+"&password="+aggiorna.elements["password"].value+"&email="+aggiorna.elements["email"].value;

		if (tipo==="lavoratore"){
			var livespe=document.getElementById("livelloEsperienza");

			var foto = aggiorna.elements["foto"];

			body=body+"&livelloEsperienza="+livespe.options[livespe.selectedIndex].value+"&foto="+foto.value;
		}
		x.send(body);
	
}
);


