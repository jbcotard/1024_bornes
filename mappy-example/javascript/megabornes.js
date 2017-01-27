/**
* Coordonnée des différentes étapes.
*/

// Création de la carte
var myMap = new L.Mappy.Map("example-map-1", {
    clientId: 'dri_24hducode',
center: [48.00351,  0.19754],
    zoom: 10
});

var etatPartieEnum = {
	enCours:1,
	termine:2,
	enAttenteJoueur:3		
};



var user;
var userCurrent = "inconnu";

var userActif = false;
var etatPartie = etatPartieEnum.enAttenteJoueur;

var imgEtat = document.getElementById("idEtat");

var options = {
    vehicle: L.Mappy.Vehicles.comcar,
    cost: "length", // or "time" or "price"
    gascost: 1.0,
    gas: "petrol", // or diesel, lpg
    nopass: 0, // 1 pour un trajet sans col
    notoll: 1, // 1 pour un trajet sans péage
    infotraffic: 0 // 1 pour un trajet avec trafic
};
 
function loadItineraire(localisation) {

    var iti = [];
    // Chargement des localisations.
    for (i = 0; i < localisation.length; i++) {
        iti.push(L.latLng(localisation[i].latitude, localisation[i].longitude));
    }

    L.Mappy.Services.route(iti,
                           options,
                           // Callback de succès
                           function(result) {
                               L.Mappy.route(result.routes).addTo(myMap);
                               var summary = result.routes.route[0].summary;
                               var action = result.routes.route[0].actions.action;

                           },
                           // Callback d'erreur
                           function(errorType) {
                               // Error during route calculation
                               Alert(errorType);
                           }
                          );

}

function loadCartesEnMains(listeJoueurs) {

    var sidebar = document.getElementById('cartesUser');
    removeAllItems(sidebar);

    for (i = 0; i <  listeJoueurs.length;i++) { 
        if (userCurrent == listeJoueurs[i].id ) {  
        	document.getElementById("idUser0").innerHTML = listeJoueurs[i].nom;
        	loadCartesEnMainsUser(listeJoueurs[i].listeCartesEnMain);
        }
    }
}

function loadCartesEnMainsUser(listeCartesEnMain) {

    var sidebar = document.getElementById('cartesUser');
    removeAllItems(sidebar);

    var div = document.createElement("div"); 
    for (j = 0; j <  listeCartesEnMain.length;j++) {
        var image = document.createElement("img");
        
        if (listeCartesEnMain[j].valeur == "Feu vert") {
            image.src = "cartes/FeuVert.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "Feu rouge") {
            image.src = "cartes/FeuRouge.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "256") {
            image.src = "cartes/256Bornes.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "128") {
            image.src = "cartes/128Bornes.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "96") {
            image.src = "cartes/96Bornes.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "64") {
            image.src = "cartes/64Bornes.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "32") {
            image.src = "cartes/32Bornes.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "Roue de secours") {
            image.src = "cartes/RouDeSecours.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "Réparation") {
            image.src = "cartes/Reparations.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "Fin de limitation de vitesse") {
            image.src = "cartes/FinLimitationVitesse.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "Essence") {
            image.src = "cartes/Essence.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "Panne Essence") {
            image.src = "cartes/PanneEssence.jpg";
        }
        if (listeCartesEnMain[j].valeur == "Crevaison") {
            image.src = "cartes/Crevaison.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "Accident de la route") {
            image.src = "cartes/Accident.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "Limitation de vitesse") {
            image.src = "cartes/LimitationVitesse.jpg";
        }
        
        if (listeCartesEnMain[j].valeur == "Increvable") {
            image.src = "cartes/Increvable.jpg";
        }
        
       if (listeCartesEnMain[j].valeur == "Camion-citerne") {
            image.src = "cartes/CamionCiterne.jpg";
        }

        image.id = listeCartesEnMain[j].idCarte;
        image.className = "carte";
        
        // Event
        image.addEventListener('click', function (e) {
            //  alert("Click" + e);
            action(e.currentTarget.id);
        });
        
        div.appendChild(image);
    }
    sidebar.appendChild(div);
    
}



function action(id) {
	
	if (!userActif) {
		window.Alert("Le joueur n'est pas actif!");
		return;
	}
	var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                userActif = false;                
                var jsText = xhttp.responseText;
                console.log(jsText);
                /*
                userCurrent = jsText.replace("\"", "");
                userCurrent = userCurrent.replace("\"", "");
                user =  L.marker([48.00351,  0.19755]).addTo(myMap);
                user.bindPopup("<b>" + userCurrent + "</b><br />").openPopup();

                inscrireJoueur(userCurrent);
               */
            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', "http://localhost:4567/api/parties/joueurs/" + userCurrent + "/action/" + id + "/false/toto", true);
    xhttp.send(null);
}

function updatePioche() {
   var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                var jsText = xhttp.responseText;
                var result = JSON.parse(jsText);
                console.log(result);
                loadCartesEnMainsUser(result.listeCartesEnMain);
            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', "http://localhost:4567/api/parties/joueurs/" + userCurrent, true);
    xhttp.send(null);
}


function pioche(typeCommerce) {
   var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                updatePioche();

            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', "http://localhost:4567/api/parties/joueurs/" + userCurrent + "/pioche/" + typeCommerce, true);
    xhttp.send(null);
}


function getJoueur() {

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                var jsText = xhttp.responseText;
                console.log(jsText);
                userCurrent = jsText.replace("\"", "");
                userCurrent = userCurrent.replace("\"", "");
                user =  L.marker([48.00351,  0.19755]).addTo(myMap);
                user.bindPopup("<b>" + userCurrent + "</b><br />").openPopup();

                inscrireJoueur(userCurrent);

            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', 'http://localhost:4567/api/joueurs/generate/', true);
    xhttp.send(null);
}


function inscrireJoueur(idJoueur) {

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                var jsText = xhttp.responseText;
                var tp = JSON.parse(jsText);
                if (tp.etat == "enCours" ) {
                    loadItineraire(tp.circuit.listePositions);

                    // Chargement des cartes en mains
                    loadCartesEnMains(tp.listeJoueurs);
                }
            } catch (e) {
                alert(e);
            }
        }
    };
    // Joueur inscrie
    xhttp.open('GET', 'http://localhost:4567/api/parties/inscrire/' + idJoueur , true);
    xhttp.send(null);
}

function reset() {

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
            	userActif = false;
                getJoueur();

                // user =  L.marker([48.00351,  0.19755]).addTo(myMap);
                // user.bindPopup("<b>" + userCurrent + "</b><br />").openPopup();

                // inscrireJoueur(userCurrent);
            } catch (e) {
                alert(e);
            }
        }
    };
    // Joueur inscrie
    xhttp.open('GET', 'http://localhost:4567/api/parties/reset/', true);
    xhttp.send(null);
}

// getJoueur();

// getMarker();

function rejoindre() {
	getJoueur();
    //inscrireJoueur(userCurrent);
}

function refreshMap() {
    reset();
}

function removeAllItems(list) {
    // remove all element in the list.
    while (list.firstChild) {
        list.removeChild(list.firstChild);
    }
}


window.setInterval(myCallback, 2000);

function myCallback() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            try {
                var jsText = xhttp.responseText;
                var result = JSON.parse(jsText);
                // console.log(result);

                displayEtatJeu(result[0].etat);
                
                if (userCurrent != "inconnu") {
	                for (i = 0; i < result[0].listeJoueurs.length; i++) {
	                    if (result[0].listeJoueurs[i].id == userCurrent) {
	                        if (result[0].listeJoueurs[i].etat == "actif" && userActif == false) {
	                            userActif = true;
	                            for (j = 0; j <  result[0].listeJoueurs[i].position.listeCommerces.length;j++) {
	
	                                var lat = result[0].listeJoueurs[i].position.listeCommerces[j].latitude;
	                                var longit = result[0].listeJoueurs[i].position.listeCommerces[j].longitude;
	
	                                function onClick(e) {
	                                    e.target.bindPopup("<b>" + e.target.id + "</b><br />").openPopup();
	                                    pioche(e.target.id);
	                                }
	
	                                var greenIcon = L.icon({
	                                    iconUrl: 'cartes/defausse.jpg',
	                                    // shadowUrl: '64Bornes.jpg',
	                                    iconSize:     [40, 53], // size of the icon
	                                    shadowSize:   [50, 64], // size of the shadow
	                                    iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
	                                    shadowAnchor: [4, 62],  // the same for the shadow
	                                    popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
	                                });
	
	                                var marker = L.marker([lat,longit], {icon: greenIcon}).on('click', onClick);
	                                marker.id =  result[0].listeJoueurs[i].position.listeCommerces[j].type;
	                                marker.addTo(myMap);
	                            }
	                            var lat = result[0].listeJoueurs[i].position.latitude + 0.001;
	                            var longi = result[0].listeJoueurs[i].position.longitude + 0.001;
	                            var newLatLng = new L.LatLng(lat, longi);
	                            user.setLatLng(newLatLng);
	                            imgEtat.src = "cartes/FeuVert.jpg";
	                        }
	                    }
	                }
	                if (etatPartie == etatPartieEnum.enCours && userActif != true) {
	                	imgEtat.src = "cartes/waiting.png";
	                }

                }

            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', 'http://localhost:4567/api/parties', true);
    xhttp.send(null);
}

/**
 * Affichage de l'état du Jeu
 * @param etat
 * @returns
 */
function displayEtatJeu(etat) {
    switch (etat) {
	case "enAttenteJoueur":
		etatPartie = etatPartieEnum.enAttenteJoueur; 
		imgEtat.src = "cartes/FeuOrange.jpg";
		break;
	case "enCours":
		imgEtat.src = "cartes/FeuVert.jpg";
		etatPartie = etatPartieEnum.enCours;
		break;
	case "termine":
		imgEtat.src = "cartes/FeuRouge.jpg";
		etatPartie = etatPartieEnum.termine;
	default:
		break;
	}

}

function affecterA() {

}

function play() {
}

/**
 * 
 */
function getMarker() {

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                var jsText = xhttp.responseText;
                var js = JSON.parse(jsText);
                for (i = 0; i <  js.search_results.listings.length;i++) {
                    // js.search_results.listings[i].merchant_name
                    var lat = js.search_results.listings[i].inscriptions[0].latitude;
                    var longit = js.search_results.listings[i].inscriptions[0].longitude;
                    // js.search_results.listings[i].inscriptions[0].distance

                    function onClick(e) {
                        // alert(e.latlng);
                        e.target.removeFrom(myMap);
                    }

                    var greenIcon = L.icon({
                        iconUrl: 'cartes/defausse.jpg',
                        // shadowUrl: '64Bornes.jpg',
                        iconSize:     [40, 53], // size of the icon
                        shadowSize:   [50, 64], // size of the shadow
                        iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
                        shadowAnchor: [4, 62],  // the same for the shadow
                        popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
                    });

                    var marker = L.marker([lat,longit], {icon: greenIcon}).on('click', onClick);
                    marker.addTo(myMap);
                }
            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', 'cci.json', true);
    // xhttp.open('GET', 'https://api.apipagesjaunes.fr/pros/find?what=cci&where=le%20mans&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1', true);

    xhttp.send(null);
}
