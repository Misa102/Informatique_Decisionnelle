**README - Projet Chargement d'Entrepôt de Données**

Ce projet vise à mettre en place un entrepôt de données pour une société de vente en ligne de produits alimentaires, permettant l'analyse des ventes et des clients. Il comprend la création d'un schéma en étoile sous Oracle et la mise en place d'un processus ETL pour charger les données initiales et les mettre à jour régulièrement.

**1. Création du Schéma en Étoile**

Pour créer le schéma en étoile, nous avons utilisé le logiciel BI Modeler pour la conception et SQL Developer pour l'implantation sous Oracle. Le schéma se compose de tables de faits et de dimensions, permettant d'analyser les ventes en fonction de différents axes tels que le client, le produit, et le temps.

- Script SQL nommé schema_etoile.sql pour la création du schéma sous Oracle.

**2. Phase ETL**

La phase ETL comprend le nettoyage des données, le chargement initial de l'entrepôt et la mise à jour régulière lorsque les sources de données évoluent. Pour ce faire, une partie du processus ETL a été codée en Java.

- Classe Java nommée ChargementED.java pour charger les dimensions à partir de la base de données SQLite et du fichier Excel. 
- Classe Java nommée MiseAJourED.java permettant de mettre à jour la dimension Produit lorsque de nouvelles lignes sont ajoutées dans la table Produit de la base de données SQLite.

**3. Utilisation de Talend Studio**

Une autre solution pour la phase ETL est d'utiliser un outil ETL comme Talend Studio.
