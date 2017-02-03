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

var greenIcon = L.icon({
    iconUrl: 'images/marker-icon-green.png',
    iconSize:     [40, 53], // size of the icon
    shadowSize:   [50, 64], // size of the shadow
    iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
    shadowAnchor: [4, 62],  // the same for the shadow
    popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
});

var redIcon = L.icon({
    iconUrl: 'images/marker-icon-red.png',
    iconSize:     [40, 53], // size of the icon
    shadowSize:   [50, 64], // size of the shadow
    iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
    shadowAnchor: [4, 62],  // the same for the shadow
    popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
});

var user1 = L.marker([48.00351 - 0.001,  0.19755 - 0.001], {icon: greenIcon}).addTo(myMap);
var user2 = L.marker([48.00351 + 0.001,  0.19755 - 0.001], {icon: redIcon}).addTo(myMap);

// Marker pour les autres joueurs.
var users = [user1, user2];

var idMapUser0 = document.getElementById("idMapUser0");
var idMapUser1 = document.getElementById("idMapUser1");

var mapUsers = [idMapUser0, idMapUser1];

var userCurrent = "inconnu";

// L'utilisateur courant est-il actif.
var userActif = false;

// Etat de la partie.
var etatPartie = etatPartieEnum.enAttenteJoueur;

var imgEtat = document.getElementById("idEtat");

// Lieux courant pour le joueur.
var markerCurrent = document.getElementById("idMapCurrent");

// Div des cartes Exposes pour le joueur.
var cartesExposeesCurrent = document.getElementById("cartesExposeesCurrent");

var cartesExposees0 = document.getElementById("cartesExposees0");
var cartesExposees1 = document.getElementById("cartesExposees1");

var cartesExposees = [cartesExposees0, cartesExposees1];

var partieId = 0;


var gagne = 0;

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
	console.log("loadItineraire");
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
	console.log("loadCartesEnMains");
    var sidebar = document.getElementById('cartesUser');
    removeAllItems(sidebar);

    for (i = 0; i <  listeJoueurs.length;i++) { 
        if (userCurrent == listeJoueurs[i].id ) {  
        	document.getElementById("idUserCurrent").innerHTML = listeJoueurs[i].nom;
        	loadCartesEnMainsUser(listeJoueurs[i].listeCartesEnMain);
        }
    }
}

function loadCartesEnMainsUser(listeCartesEnMain) {
	console.log("loadCartesEnMainsUser");
    var sidebar = document.getElementById('cartesUser');
    removeAllItems(sidebar);

    var div = document.createElement("div"); 
    for (j = 0; j <  listeCartesEnMain.length;j++) {
        var image = document.createElement("img");
        
        setImage(image, listeCartesEnMain[j].valeur);
                
        image.id = listeCartesEnMain[j].idCarte;
        image.className = "carte";
        image.draggable = true;
        // Event
        image.addEventListener('dragstart', function (ev) {
            // alert("Click" + e);
            ev.dataTransfer.setData("text", ev.currentTarget.id);
            
        });
        
        // Event
        image.addEventListener('click', function (e) {
            //  alert("Click" + e);
            action(e.currentTarget.id, false, userCurrent);
        });
        
        /* image.addEventListener("touchstart", handleStart, false);
        
        image.addEventListener("touchend", handleEnd, false);
        */
        
        div.appendChild(image);
    }
    sidebar.appendChild(div);
    
}

function handleStart(evt) {
	evt.preventDefault();
	console.log("handleStart");
	console.log(evt);
}


function handleEnd(evt) {
	evt.preventDefault();
	console.log("handleEnd");
	console.log(evt);
	console.log(evt.changedTouches[0])
	console.log(evt.changedTouches[0].target.id);
	var target = evt.changedTouches[0].target;
	console.log(target);
	console.log(evt.targetTouches);
	console.log(evt.touches);
	//document.write('Remaining on element: ' + evt.targetTouches.length);
    // document.write('Remaining on document: ' + evt.touches.length);
	action(evt.changedTouches[0].target.id, false, userCurrent);
}
/**
 * Affichage des cartes exposés pour l'utilisateur.
 * @param div
 * @param listeCartesExposees
 * @returns
 */
function loadCartesExposees(div, listeCartesExposees) {
	console.log("loadCartesExposes");
    
    removeAllItems(div);
    
    var divImage = document.createElement("div"); 
    for (j = 0; j <  listeCartesExposees.length;j++) {
    	var image = document.createElement("img");
    
    	setImage(image, listeCartesExposees[j].valeur);
            
    	image.id = listeCartesExposees[j].idCarte;
    	image.className = "carte";
    	divImage.appendChild(image);
    }
    div.appendChild(divImage);
} 

/**
 * 
 * @param image objet Image 
 * @param valeur la valeur
 * @returns
 */
function setImage(image, valeur) {
	
	switch (valeur) {
	
	case "Feu vert":
        image.src = "cartes/FeuVert.jpg";
		break;
    
	case "Feu rouge":
        image.src = "cartes/FeuRouge.jpg";
    break;
    
	case "256":
        image.src = "cartes/256Bornes.jpg";
        break;
	case "128":
        image.src = "cartes/128Bornes.jpg";
        break;
	case "96":
        image.src = "cartes/96Bornes.jpg";
        break;
    
	case "64":
        image.src = "cartes/64Bornes.jpg";
        break;
    
	case  "32":
        image.src = "cartes/32Bornes.jpg";
        break;
    
	case "Roue de secours":
        image.src = "cartes/RouDeSecours.jpg";
        break;
    
	case "Réparation":
        image.src = "cartes/Reparations.jpg";
        break;
    
    case "Fin de limitation de vitesse":
        image.src = "cartes/FinLimitationVitesse.jpg";
        break;
    
    case "Essence":
        image.src = "cartes/Essence.jpg";
        break;
    
    case "Panne d’essence":
        image.src = "cartes/PanneEssence.jpg";
        break;
        
    case "Crevaison":
        image.src = "cartes/Crevaison.jpg";
        break;
    
    case "Accident de la route":
        image.src = "cartes/Accident.jpg";
        break;
    
    case "Limitation de vitesse":
        image.src = "cartes/LimitationVitesse.jpg";
        break;
    
    case "Increvable":
        image.src = "cartes/Increvable.jpg";
        break;
    
    case "Camion-citerne":
        image.src = "cartes/CamionCiterne.jpg";
        break;

    case "As du volant":
        image.src = "cartes/AsDuVolant.jpg";
        break;
        
    case "Prioritaire":
        image.src = "cartes/Prioritaire.jpg";
        break;        

	default:
		console.log(valeur);
	break;
	}
}

function action(id, etat, user) {
	
	if (!userActif) {
		window.Alert("Le joueur n'est pas actif!");
		return;
	}
	var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            try {
                // userActif = false;                
                var jsText = xhttp.responseText;
                console.log(jsText);
            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', "apiv2/parties/" + partieId + "/joueurs/" + userCurrent + "/action/" + id + "/" + etat + "/" + user, true);
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

    xhttp.open('GET', "apiv2/parties/" + partieId + "/joueurs/" + userCurrent, true);
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

    xhttp.open('GET', "apiv2/parties/" + partieId + "/joueurs/" + userCurrent + "/pioche/" + typeCommerce, true);
    xhttp.send(null);
}


function getJoueur() {
	console.log("getJoueur");
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

                document.getElementById("idUserCurrent").innerHTML = userCurrent;
                
                inscrireJoueur(userCurrent);

            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', 'api/joueurs/generate/', true);
    xhttp.send(null);
}


function inscrireJoueur(idJoueur) {
	console.log("inscrireJoueur");
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                var jsText = xhttp.responseText;
                var tp = JSON.parse(jsText);
               
                // id de la partie
                partieId = tp.partieId;
                loadItineraire(tp.circuit.listePositions);
                
                if (tp.etat == "enCours" ) {
                    // Chargement des cartes en mains
                    loadCartesEnMains(tp.listeJoueurs);
                }
            } catch (e) {
                alert(e);
            }
        }	
    };
    // Joueur inscrie
    xhttp.open('GET', 'api/parties/inscrire/' + idJoueur , true);
    xhttp.send(null);
}

function reset() {

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
            	userActif = false;
            	
            	var sidebar = document.getElementById('cartesUser');
                removeAllItems(sidebar);
            	
                getJoueur();
            } catch (e) {
                alert(e);
            }
        }
    };
    // Joueur inscrie
    xhttp.open('GET', 'api/parties/reset/', true);
    xhttp.send(null);
}

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
                
                
                if (jsText == "null") {
                	return;
                }
                var result = JSON.parse(jsText);
                // console.log(result);

                if (result == null) {
                	return;
                }
                
                var etatPartieOld = etatPartie;
                 
                displayEtatJeu(result.etat);
                
                if (etatPartie != etatPartieOld && etatPartieEnum.enCours == etatPartie) {
                	var idx = 0;
                	for (i = 0; i < result.listeJoueurs.length; i++) {
	                    if (result.listeJoueurs[i].id == userCurrent) {
	                    	document.getElementById("idUserCurrent").innerHTML = result.listeJoueurs[i].id;
	                    } else {
	                    	document.getElementById("idUser" + idx).innerHTML = result.listeJoueurs[i].id;
	                    	var lat = result.listeJoueurs[i].position.latitude + 0.001;
	                    	var longi = result.listeJoueurs[i].position.longitude + 0.001;
	                    	var newLatLng = new L.LatLng(lat, longi);
	                    	users[idx].setLatLng(newLatLng);
	                    	users[idx].addTo(myMap);
	                    	users[idx].bindPopup("<b>" + result.listeJoueurs[i].id + "</b><br />").openPopup();
	                    	
	                    	mapUsers[idx].innerHTML = result.listeJoueurs[i].position.nom;
	                    	idx++;
	                    }
                	}
                }  
                
                if (userCurrent != "inconnu") {
                	var idx = 0;
	                for (i = 0; i < result.listeJoueurs.length; i++) {
	                    if (result.listeJoueurs[i].id == userCurrent) {

                            if (result.listeJoueurs[i].etat == "winner" && gagne == 0) {
                                window.alert("Vous avez gagné!");
                                gagne = 1;
                                break;
                            }

	                    	var userActifMaj = false;
	                    	if (result.listeJoueurs[i].etat == "actif") {
	                    		userActifMaj = true;
	                    	}

	                    	// Maj de la carte et position si changement d'état du Joueur.
	                    	if (userActifMaj != userActif ) {
	                    		// if (result.listeJoueurs[i].etat == "actif" && userActif == false) {
	                            userActif = true;
	                            
	                            // affichage des commerces.
	                            for (j = 0; j <  result.listeJoueurs[i].position.listeCommerces.length;j++) {
	
	                                var lat = result.listeJoueurs[i].position.listeCommerces[j].latitude;
	                                var longit = result.listeJoueurs[i].position.listeCommerces[j].longitude;
	
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
	                                marker.id =  result.listeJoueurs[i].position.listeCommerces[j].type;
	                                marker.addTo(myMap);
	                            }
	                            var lat = result.listeJoueurs[i].position.latitude + 0.001;
	                            var longi = result.listeJoueurs[i].position.longitude + 0.001;
	                            var newLatLng = new L.LatLng(lat, longi);
	                            user.setLatLng(newLatLng);
	                            imgEtat.src = "cartes/FeuVert.jpg";
	                            
	                            markerCurrent.innerHTML = result.listeJoueurs[i].position.nom;
        
	                            loadCartesEnMainsUser(result.listeJoueurs[i].listeCartesEnMain);
	                            
	                            loadCartesExposees(cartesExposeesCurrent, result.listeJoueurs[i].listeCartesExposees);
	                        }
		                    userActif = userActifMaj;
		                    //break;
	                    } else {
                            var lat = result.listeJoueurs[i].position.latitude + 0.001;
                            var longi = result.listeJoueurs[i].position.longitude + 0.001;
                            
                            var lstLatLn = users[idx].getLatLng();
                            if (lstLatLn.lat != lat || lstLatLn.lng != longi) {                            
                            	var newLatLng = new L.LatLng(lat, longi);
                            	users[idx].setLatLng(newLatLng);
                            	mapUsers[idx].innerHTML = result.listeJoueurs[i].position.nom;
                            }
                            
                            loadCartesExposees(cartesExposees[idx], result.listeJoueurs[i].listeCartesExposees);
	                    }
	                }
	                
	                // Une partie en cours mais l'utilisateur est inactif (En attente).
	                if (etatPartie == etatPartieEnum.enCours && userActif != true) {
	                	imgEtat.src = "cartes/waiting.png";
	                }

                }

            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', 'api/parties/' + partieId, true);
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
        if (gagne == 1) {
            imgEtat.src = "images/lol-icon.png";
        } else {
            imgEtat.src = "images/crying-icon.png";
        }

		etatPartie = etatPartieEnum.termine;
		break;
	default:
		break;
	}

}


function play() {
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function drop(ev) {
	try {
	    ev.preventDefault();
	    var data = ev.dataTransfer.getData("text");
	    
	    var target = ev.currentTarget.id;
	    
	    var idx = target.substr(3);
	    var label = document.getElementById("idUser" + idx);
	    var userName =label.innerHTML;    
	    action(data, false, userName);
	} catch(e) {
		console.log(e);
	}
    // alert(data);
    //ev.target.appendChild(document.getElementById(data));
}

function onDropTrash(ev) {
    ev.preventDefault();
    var data = ev.dataTransfer.getData("text");
    
    var target = ev.currentTarget.id;
    
    action(data, true, userCurrent);
	
} 
