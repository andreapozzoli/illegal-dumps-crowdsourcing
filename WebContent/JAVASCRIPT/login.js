window.addEventListener("load", function (){
	document.getElementById("username").focus();
});

var login = document.getElementById("login");
var nomeUtente = document.getElementById("username");
var password = document.getElementById("password");
var form = document.getElementById("formLogin");

login.addEventListener("click", function (e){
	e.preventDefault();
	var err = document.getElementsByClassName("err");
	var err1 = document.getElementsByClassName("err1");
	err[0].style.display = "none";
	err1[0].style.display="none";

	if(nomeUtente.value==="" || password.value===""){
		err1[0].style.display="block";
	}
	else{
		var x = new XMLHttpRequest();
		x.onreadystatechange = function(){
			if (this.readyState == 4){
				if (this.status == 200){
					var object = this.responseText;
					if (object==="errore"){
						window.location.href = "/ConvalidaImmagini/ApriPaginaErrore";

					}
					else{
					if(object==="manager" || object==="lavoratore"){
						var loginUrl ;
						if (object==="manager"){
							loginUrl = "/ApriHomeManager";
						}
						else{
							loginUrl = "/ApriHomeLavoratore";
						}
						window.location="/ConvalidaImmagini"+loginUrl;
					}
					else{
						err[0].style.display = "block";
					}
				}
				}
				else{
					alert("error");
					console.log("Error");
				}
			}
		};

		x.open("POST", "/ConvalidaImmagini/ControlloLogin");
		x.setRequestHeader("Content-type","application/x-www-form-urlencoded");
		x.send("username="+nomeUtente.value+"&password="+password.value);
	}
});
