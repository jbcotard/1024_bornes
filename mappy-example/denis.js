/**
* Coordonnée des différentes étapes.
*/
// var itineraires = {
//     "villes": [
//        { "nom": "Le Mans", "lattitude": 48.00351, "longitude": 0.19754 },
//        { "nom": "Le Mans",
//         "lattitude": 48.00351,
//         "longitude": 0.19754 },
//        { "nom": "Le Mans",
//         "lattitude": 48.00351,
//         "longitude": 0.19754 }, 


// Création de la carte
var myMap = new L.Mappy.Map("example-map-1", {
    clientId: 'dri_24hducode',
center: [48.00351,  0.19754],
    zoom: 10
});

var user = L.marker([48.00351,  0.19755]).addTo(myMap);


var options = {
    vehicle: L.Mappy.Vehicles.comcar,
    cost: "length", // or "time" or "price"
    gascost: 1.0,
    gas: "petrol", // or diesel, lpg
    nopass: 0, // 1 pour un trajet sans col
    notoll: 0, // 1 pour un trajet sans péage
    infotraffic: 0 // 1 pour un trajet avec trafic
};
 


function loadItineraire() {

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                var jsText = xhttp.responseText;
                var localisation = JSON.parse(jsText);
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
            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', 'itineraire.json', true);
    xhttp.send(null);
}

function getMarker() {

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            // Typical action to be performed when the document is ready:
            try {
                var jsText = xhttp.responseText;
                // alert( xhttp.responseText);

                var js = JSON.parse(jsText);
                // aler(js);
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
                        iconUrl: '64Bornes.jpg',
                        // shadowUrl: '64Bornes.jpg',
                        iconSize:     [40, 53], // size of the icon
                        shadowSize:   [50, 64], // size of the shadow
                        iconAnchor:   [22, 94], // point of the icon which will correspond to marker's location
                        shadowAnchor: [4, 62],  // the same for the shadow
                        popupAnchor:  [-3, -76] // point from which the popup should open relative to the iconAnchor
                    });

// L.marker([51.5, -0.09], {icon: greenIcon} ).addTo(map)

                    var marker = L.marker([lat,longit], {icon: greenIcon}).on('click', onClick);
                    marker.addTo(myMap);

                // var circle = L.circle([lat, longit ], {
                //     color: 'blue',
                //     fillColor: 'blue',
                //     fillOpacity: 0.5,
                //     radius: 2
                // }).addTo(myMap);
                }
            } catch (e) {
                alert(e);
            }
        }
    };

    xhttp.open('GET', 'cci.json', true);
    xhttp.send(null);
}

loadItineraire();

getMarker();

function refreshMap() {
    // var map = document.getElementById("example-map-1");

    myMap.remove();
    myMap = new L.Mappy.Map("example-map-1", {
    clientId: 'dri_24hducode',
center: [47.3943,  0.6951],
    zoom: 10
}); 

loadItineraire();

    // user.setLatLng([47.3943,0.6955]).update();
    // try {
    //     myMap.setView(user.latLng);
    // } catch(e) {
    //     alert(e);
    // }
}


// https://api.apipagesjaunes.fr/pros/find?what=cci&where=le%20mans&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1

// latitude:48.003722,"longitude":0.197252

//https://api.apipagesjaunes.fr/pros/find?what=bar&where=0.197252,48.003722&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1

// xhttp.open('GET', 'https://api.apipagesjaunes.fr/pros/find?what=cci&where=le%20mans&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1', true);



