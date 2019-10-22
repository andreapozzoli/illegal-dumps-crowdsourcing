var ruolo = document.getElementById("selezioneRuolo");
var tipo = document.getElementById("tipo");
var email = document.getElementById("email");


ruolo.addEventListener("change", updateRuolo);
window.addEventListener("load", function (){
	document.getElementById("username").focus();
});

email.addEventListener("keyup",function emailIsValid () {
	if(!(/\S+@\S+\.\S+/.test(this.value))){

		document.getElementById("correttezza").textContent = "This email address isn't correct.";
	}
	else{
		document.getElementById("correttezza").textContent = " ";

	}
});

window.addEventListener("load",function inizio() {
	var e = document.createEvent("HTMLEvents");
	e.initEvent("change", false, true);
	// in browser moderni basta: var e = new Event("change"); 
	ruolo.dispatchEvent(e); // simula la scelta dell'utente
}
);

function updateRuolo(e) {
	var l = document.getElementById("l");
	var m = document.getElementById("m");
	if (ruolo.value === "lavoratore") {
		tipo.value = "lavoratore";
		var elements = document.getElementsByClassName("nascosto"); // it will shrink
		var num = elements.length; // save number of items before changing them
		for (var i = 0; i < num; i++) {
			elements[0].className = "visibile"; // change the class of the 1st item
		}
	} else if (ruolo.value === "manager") {
		tipo.value = "manager";

		var elements = document.getElementsByClassName("visibile"); // it will shrink
		var num = elements.length; // save number of items before changing them
		for (var i = 0; i < num; i++) {
			elements[0].className = "nascosto"; // change the class of the 1st item
		}
	}

}

var registrati = document.getElementById("registrati");
var nomeUtente = document.getElementById("username");
var password = document.getElementById("password");
var email = document.getElementById("email");
var foto = document.getElementById("foto");
var livespe = document.getElementById("esperienza");
var form = document.getElementById("formRegistrazione");

registrati.addEventListener("click", function (e){
	e.preventDefault();
	var err = document.getElementsByClassName("err");
	var err1 = document.getElementsByClassName("err1");
	var err2 = document.getElementsByClassName("err2");
	var err3 = document.getElementsByClassName("err3");
	var err4 = document.getElementsByClassName("err4");

	err[0].style.display = "none";
	err1[0].style.display="none";
	err2[0].style.display = "none";
	err3[0].style.display="none";
	err4[0].style.display="none";

	if(nomeUtente.value==="" || password.value==="" || email.value===""){
		err4[0].style.display="block";
	}
	else if(ruolo.value==="lavoratore" && foto.value==="" ){
		err4[0].style.display="block";
	}
	else{	

		var x = new XMLHttpRequest();
		x.onreadystatechange = function(){
			if (this.readyState == 4){
				if (this.status == 200){
					var object = this.responseText;
					if (object==="nonvabene"){
						window.location.href = "/ConvalidaImmagini/ApriPaginaErrore";

					}
					else{
					if(object==="manager" || object==="lavoratore"){
						var regUrl ;
						if (object==="manager"){
							regUrl = "/ApriHomeManager";
						}
						else{
							regUrl = "/ApriHomeLavoratore";
						}
						window.location="/ConvalidaImmagini"+regUrl;
					}
					else{
						if(object==="ko"){
							err[0].style.display = "block";
						}
						else if(object==="ko1"){
							err1[0].style.display = "block";
						}
						else if(object==="ko2"){
							err2[0].style.display = "block";
						}
						else if(object==="ko3"){
							err3[0].style.display = "block";
						}
					}
				}}
				else{
					alert("error");
					console.log("Error");
				}
			}
		};
		x.open("POST", "/ConvalidaImmagini/CreaUtente");
		x.setRequestHeader("Content-type","application/x-www-form-urlencoded");

		var body="username="+nomeUtente.value+"&password="+password.value+"&email="+email.value+"&tipo="+ruolo.value;

		if (ruolo.value==="lavoratore"){
			body=body+"&livelloEsperienza="+livespe.options[livespe.selectedIndex].value+"&foto="+foto.value;
		}
		x.send(body);
	}
});

