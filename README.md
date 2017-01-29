# Remarque denis
J'ai modifié la méthode org.vqhcode.megabornes.model.Partie.joueurSuivant(), pour désactiver le joueur précedent.

* Le Drag & Drop des cartes fonctionne, le drop doit être fait sur l'icône du joueur.

# 1024_bornes
Sujet au 24h du code 2017

## Principes

revisite des milles bornes
une partie se joue à 3 joueurs
il s'agit d'une map interactive, où chaque joueur peut visualiser son avancement
c'est un itineraire commun à tous les joueurs
chaque joueur joue à tour de role les actions suivantes
- pioche d'une carte dans un des magasins disponibles autour de la position du joueur
- joue une carte en main (soit une carte kilométrique, soit une carte defense, soit une carte d'attaque, soit une botte)

= instructions pour builder
== prerequis
installer jdk8
installer maven 3.3
== commande maven
mvn package

= instructions pour executer
java -jar target/megabornes-1.0-jar-with-dependencies.jar

= instructions pour executer dans une image docker
docker-compose up

= instructions pour jouer
acces à http://localhost:4567/index.html




