# 1024_bornes
24h du code 2017

Exemple appel via coord. API PJ :
https://api.apipagesjaunes.fr/pros/find?what=cci&where=cZ-0.197252,48.003722&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=15&page=1

format json cartes
[
{"type":"km", "valeur":"32"},
{"type":"botte", "valeur":"Increvable"},
...
]

Correspondance Carte du jeu - "What" dans Pages Jaunes
Carte                            Nombre    "What"            Tirage (si applicable)
Accident de la route             3         Hôpital
Panne d’essence                  3         Gare
Crevaison                        3         Gendarmerie
Limitation de vitesse            4         Police
Feu rouge                        5         Préfecture
Réparations                      6         Carrossier        (6/7)
Essence                          6         Station Essence   (6/7)
Roue de secours                  6         Garagiste         (6/7)
Fin de limitation de vitesse     6         DDE               (6/7)
Feu vert                        14         Mairie
As du volant                     1         Carossier         (1/7)
Camion-Citerne                   1         Station Essence   (1/7)
Increvable                       1         Garagiste         (1/7)
Prioritaire                      1         DDE               (1/7)
25 kms (escargot)               10         Bar               (10/46)
50 kms (canard)                 10         Bar               (10/46)
75 kms (papillon)               10         Bar               (10/46)
100 kms (lièvre)                12         Bar               (12/46)
200 kms (hirondelle)             4         Bar               (4/46)
Total                          106           

API Pages Jaunes :
In : appel REST
https://api.apipagesjaunes.fr/pros/find?
what=plombier&
where=rennes&
app_id=d140a6f6&
app_key=26452728b034374bccb462e880bfb0e5&
return_urls=false


https://api.apipagesjaunes.fr/pros/find?what=cci&where=le%20mans&app_id=d140a6f6&app_key=26452728b034374bccb462e880bfb0e5&return_urls=false&proximity=true&max=6&page=1
 

Out : retourne JSON
$.search_results.listings[0].merchant_name
$.search_results.listings[0].inscriptions[0].latitude
$.search_results.listings[0].inscriptions[0].latitude
$.search_results.listings[0].inscriptions[0].distance

Itinéraire 1 (de référence) :
Le Mans, CCI
Ecommoy 47.8231, 0.2875

Château du Loir 47.6996, 0.4321

Neuillé Pont Pierre 47.5468, 0.5537

Tours 47.3943, 0.6951

Chinon 47.1692, 0.2433

Marigny-Marmande 46.9771, 0.4809

Châtellerault 46.8148, 0.5584

Poitiers 46.5846, 0.3714

Verrières 46.4019, 0.5979

Bussière Poitevine 46.2402, 0.8962

Bellac 46.1014, 1.0326

Guéret 46.1587, 1.8705

Bourganeuf 45.9473, 1.7467

Limoges
45.85, 1.25
Vicq-sur-Breuilh
45.6461, 1.3814
Uzerche
45.425556, 1.564167
Brive-la-Gaillarde
45.158317, 1.532078
Lachapelle-Auzac
44.909167, 1.473333
Ussel
45.5489, 2.31
Cahors
44.448333, 1.441389
Montpezat-de-Quercy
44.239167, 1.477222
Montauban
44.018056, 1.355833
Grenade
43.772222, 1.293611
Toulouse
43.604482, 1.443962
Baziège
43.4554, 1.616
Castelnaudary
43.319167, 1.954444
Carassonne
43.21306, 2.352028
Fabrezan
43.136389, 2.697778
Saint-Jean-de-Barrou
42.9583, 2.8406
Perpignan
42.698611, 2.895556
Le Boulou
42.524722, 2.830833
Banyuls-sur-Mer
42.483056, 3.128056
