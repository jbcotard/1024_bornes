/**
* Coordonnée des différentes étapes.
*/

// Création de la carte
var myMap = new L.Mappy.Map("example-map-1", {
    clientId: 'dri_24hducode',
center: [48.00351,  0.19754],
    zoom: 10
});

var user;
var userCurrent;

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
            var div = document.createElement("div"); 
            for (j = 0; j <  listeJoueurs[i].listeCartesEnMain.length;j++) {
                var image = document.createElement("img");

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "Feu vert") {
                    image.src = "cartes/FeuVert.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "Feu rouge") {
                    image.src = "cartes/FeuRouge.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "256") {
                    image.src = "cartes/256Bornes.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "128") {
                    image.src = "cartes/128Bornes.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "96") {
                    image.src = "cartes/96Bornes.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "64") {
                    image.src = "cartes/64Bornes.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "32") {
                    image.src = "cartes/32Bornes.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "Roue de secours") {
                    image.src = "cartes/RouDeSecours.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "Réparation") {
                    image.src = "cartes/Reparations.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "Fin de limitation de vitesse") {
                    image.src = "cartes/FinLimitationVitesse.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "Essence") {
                    image.src = "cartes/Essence.jpg";
                }

                if (listeJoueurs[i].listeCartesEnMain[j].valeur == "Panne Essence") {
                    image.src = "cartes/PanneEssence.jpg";
                }

                image.id = listeJoueurs[i].listeCartesEnMain[j].valeur;
 
                // Event
                image.addEventListener('click', function (e) {
                    alert("Click" + e);
                });

                div.appendChild(image);
            }
            sidebar.appendChild(div);
            break;
        }
    }
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

// loadItineraire();

// getMarker();

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

    xhttp.open('GET', 'http://192.168.1.24:4567/api/joueurs/generate/', true);
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
    xhttp.open('GET', 'http://192.168.1.24:4567/api/parties/inscrire/' + idJoueur , true);
    xhttp.send(null);
}

function reset() {

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
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
    xhttp.open('GET', 'http://192.168.1.24:4567/api/parties/reset/', true);
    xhttp.send(null);
}

// reset();

getJoueur();

getMarker();

function nextMap() {
    inscrireJoueur(userCurrent);
}

function refreshMap() {
   /*  myMap.remove();
    myMap = new L.Mappy.Map("example-map-1", {
        clientId: 'dri_24hducode',
        center: [47.3943,  0.6951],
        zoom: 10
    });
    user = L.marker([47.3943, 0.7]).addTo(myMap);
    user.bindPopup("<b>User1</b><br />").openPopup();

    loadItineraire(); */
    reset();
}

function removeAllItems(list) {
    // remove all element in the list.
    while (list.firstChild) {
        list.removeChild(list.firstChild);
    }
}


// window.setInterval(myCallback, 1000);

function myCallback() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                var jsText = xhttp.responseText;
                var result = JSON.parse(jsText);
            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', 'http://192.168.1.24:4567/api/parties', true);
    xhttp.send(null);
}

function affecterA() {

}

function play() {
}

// https://api.apipagesjaunes.fr/pros/find?what=cci&where=le%20mans&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1

// latitude:48.003722,"longitude":0.197252

//https://api.apipagesjaunes.fr/pros/find?what=bar&where=0.197252,48.003722&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1

// xhttp.open('GET', 'https://api.apipagesjaunes.fr/pros/find?what=cci&where=le%20mans&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1', true);



// function loadItineraire() {

//     var xhttp = new XMLHttpRequest();
//     xhttp.onreadystatechange = function() {
//         if (this.readyState == 4 && this.status == 200) {
//             // Typical action to be performed when the document is ready:
//             try {
//                 var jsText = xhttp.responseText;
//                 var localisation = JSON.parse(jsText);
//                 var iti = [];
//                 // Chargement des localisations.
//                 for (i = 0; i < localisation.length; i++) {
//                     iti.push(L.latLng(localisation[i].latitude, localisation[i].longitude));
//                 }

//                 L.Mappy.Services.route(iti,
//                                        options,
//                                        // Callback de succès
//                                        function(result) {
//                                            L.Mappy.route(result.routes).addTo(myMap);
//                                            var summary = result.routes.route[0].summary;
//                                            var action = result.routes.route[0].actions.action;

//                                        },
//                                        // Callback d'erreur
//                                        function(errorType) {
//                                            // Error during route calculation
//                                            Alert(errorType);
//                                        }
//                                       );
//             } catch (e) {
//                 alert(e);
//             }
//         }
//     };

//     xhttp.open('GET', 'It1.json', true);
//     xhttp.send(null);
// }
