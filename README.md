# CreaTikZ : projet de génie logiciel et gestion de projet (info-f-307)

CreaTikZ est une application graphique écrite en Java/Swing permettant de créer en WYSIWYG des diagrammes TikZ.

Ce projet est écrit dans de cadre du cours "Génie logiciel et gestion de projets" (info-f-307)
donné en bachelier en sciences informatiques à l'ULB.

# Utilisation

CreaTikZ utilise `gradle` comme système de build et Java8, assurez vous d'avoir les deux installés sur votre machine.

Compilation:

    gradle assemble # installation des dépendances java

Démarrage : 

    gradle :client:run

Créer un fichier zip pour le client et le serveur (localisé dans ´zip/´):

    gradle zip

Pour lancer le client ou le serveur depuis les zips générés précédemment, il faut les extraire et lancer le bon exécutable dans le dossier ´bin´.
Le fichier en ´.bat´ sous windows et celui sans extension sous UNIX.

Créer un jar pour le client :

    gradle onejar && gradle onejar
    
    
Les jar résulatantes se trouvent dans `jar/`.
Pour les lancer, utilisez `java -jar jar/client.jar` et `java -jar jar/server.jar`

# Configuration :

## Serveur 

Configurez l'envoi de mail avec le serveur smtp avec le fichier `server/config.ini`
(un exemple se trouve dans `server/config.ini.example`).
Si le fichier `server/config.ini` n'existe pas, les mails ne seront pas envoyés au smtp mais simplement écrits dans `stdout`

## Client

Le client va se connecter par défaut sur `http://localhost:5555` pour qu'il se connecte ailleurs,
modifiez la valeur `HOSTNAME` et `PORT` dans `shared/resources/network.properties`.

Vu que le client ne doit pas changer le serveur lui même, les valeurs de configuration sont fixées à la compilation.

# Tests

Les tests se trouvent dans `/tests` et peuvent être exécutés avec `gradle test`.
Ils sont aussi exécutés automatiquement pour chaque commit et avant chaque merge par [Travis](http://travis-ci.com)

# Misc

## Développement

Ce projet est développé avec IntelliJ.

## Screenshot

![a](http://i.imgur.com/Lb8OelD.png)

## License

Le code de ce projet est sous licence MIT (voir `/LICENSE`)
