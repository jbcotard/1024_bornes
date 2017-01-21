# 1024_bornes
24h du code 2017

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
