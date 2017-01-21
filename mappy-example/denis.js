

// Création de la carte
var myMap = new L.Mappy.Map("example-map-1", {
    clientId: 'dri_24hducode',
center: [48.00351,  0.19754],
    zoom: 18
});

var marker = L.marker([48.00351,  0.19754]).addTo(myMap);


var options = {
    vehicle: L.Mappy.Vehicles.comcar,
    cost: "length", // or "time" or "price"
    gascost: 1.0,
    gas: "petrol", // or diesel, lpg
    nopass: 0, // 1 pour un trajet sans col
    notoll: 0, // 1 pour un trajet sans péage
    infotraffic: 0 // 1 pour un trajet avec trafic
};
 
// On cherche les résultats pour "47 rue de charonne Paris"
// 48.00831/0.20022

L.Mappy.Services.route([L.latLng(48.00351,  0.197542), L.latLng(48.00831, 0.20022)],
    options,
    // Callback de succès
    function(result) {
        L.Mappy.route(result.routes).addTo(myMap);
        var summary = result.routes.route[0].summary;

        var action = result.routes.route[0].actions.action;

        for (i = 0 ; action.length;i++) {
            // var marker = L.marker([action[i].y,  action[i].x]).addTo(myMap);

            var circle = L.circle([action[i].y,  action[i].x], {
                color: 'red',
                fillColor: '#f03',
                fillOpacity: 0.5,
                radius: 3
            }).addTo(myMap);

        }

        var roadbook = result.routes.route[0].actions.action;
    },
    // Callback d'erreur
    function(errorType) {
        // Error during route calculation
Alert(errorType);
    }
);


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
                    alert(e.latlng);
                }

                var marker = L.marker([lat,longit]).on('click', onClick);
               
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

// https://api.apipagesjaunes.fr/pros/find?what=cci&where=le%20mans&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1

// latitude:48.003722,"longitude":0.197252

//https://api.apipagesjaunes.fr/pros/find?what=bar&where=0.197252,48.003722&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1

// xhttp.open('GET', 'https://api.apipagesjaunes.fr/pros/find?what=cci&where=le%20mans&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1', true);



