
Suite de tests (sommaire) visant à tester le scénario d'ecnhaînement d'appels des WS du BO

Implémenté :
- récupération de l'id du joueur après enregistrement
- appel de l'initialisation de la partie pour le joueur

Reste à faire :
- enchaînement des appels de mise à jour avec les actions simulant l'utilisateur



#RG : un joueur s'enregistre dans la premiere partie disponible
#RG : si aucune partie n'est disponible, une nouvelle partie est créée.
#RG : une partie ne demarre que lorsque la partie est complete

TODO : penser au generate des joueurs

scenario :
when le joueur "joueur1" s'inscrit à une partie
and le joueur "joueur2" s'inscrit à la partie
and le joueur "joueur3" s'inscrit à la partie
and tant qu'un joueur n'a pas gagné, les joueurs jouent à tour de role

