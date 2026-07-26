# PasswordCracker v1

Outil en ligne de commande permettant de retrouver un mot de passe à partir de son
empreinte MD5, via une attaque par dictionnaire ou par force brute. Ce projet illustre
la mise en œuvre du patron de création **Simple Factory**.

## Table des matières
1. [Introduction](#1-introduction)
2. [Présentation du problème](#2-présentation-du-problème)
3. [Architecture](#3-architecture)
4. [Diagramme UML](#4-diagramme-uml)
5. [Usage du patron Simple Factory](#5-usage-du-patron-simple-factory)
6. [Résultats obtenus](#6-résultats-obtenus)
7. [Difficultés rencontrées](#7-difficultés-rencontrées)
8. [Conclusion](#8-conclusion)

---

## 1. Introduction

Dans le domaine de la cybersécurité, les mots de passe ne sont généralement pas
stockés en clair : ils sont transformés à l'aide de fonctions de hachage
cryptographiques comme MD5. Lors d'un audit de sécurité, il est souvent nécessaire de
vérifier la robustesse des mots de passe utilisés en tentant de retrouver le mot de
passe original à partir de son empreinte.

Ce mini-projet propose une première version de **PasswordCracker**, un outil en ligne
de commande capable de retrouver un mot de passe à partir de son hash MD5, en utilisant
soit une attaque par dictionnaire, soit une attaque par force brute. Cette version est
conçue autour du patron de création **Simple Factory**.

## 2. Présentation du problème

Étant donné un hash MD5, l'objectif est de retrouver le mot de passe en clair qui lui
correspond, à l'aide de deux stratégies :

- **Attaque par dictionnaire (`DICO`)** : le programme parcourt une liste de mots
  connus, calcule le hash MD5 de chacun, et le compare au hash recherché.
- **Attaque par force brute (`BRUTE`)** : le programme génère automatiquement toutes
  les combinaisons possibles à partir de l'alphabet `a-z`, jusqu'à une longueur
  maximale de 4 caractères, et teste chacune jusqu'à trouver une correspondance.

Le programme reçoit en argument une méthode de cassage et un hash MD5, et affiche le
mot de passe trouvé, ou un message d'échec si aucune correspondance n'est obtenue.

## 3. Architecture

L'application repose sur le patron de création **Simple Factory**, qui centralise
l'instanciation des différentes stratégies de cassage de mot de passe.

L'interface `HashCracker` définit un contrat unique — la méthode `crack(String hash)`
— que toute stratégie de cassage doit respecter. Deux implémentations concrètes
réalisent ce contrat : `DictionaryHashCracker` (attaque par dictionnaire) et
`BruteForceHashCracker` (attaque par force brute).

La classe `HashCrackerFactory` est l'unique point d'entrée pour obtenir une instance
de stratégie : elle reçoit une méthode (`"BRUTE"` ou `"DICO"`) et retourne
l'implémentation correspondante, sans jamais exposer les classes concrètes au reste
de l'application. Le programme principal (`Main`) ne connaît que l'interface
`HashCracker` : il ne manipule jamais directement `DictionaryHashCracker` ou
`BruteForceHashCracker`.

Cette architecture permet le **polymorphisme** : `Main` appelle `cracker.crack(hash)`
sans savoir quelle implémentation s'exécute réellement derrière l'interface.

### Responsabilités des classes

- **`HashCracker`** *(interface)* — Définit le contrat commun à toute stratégie de
  cassage de mot de passe. Une seule méthode : `crack(String hash) : String`, qui
  retourne le mot de passe trouvé ou `null` si aucune correspondance n'est obtenue.
- **`DictionaryHashCracker`** — Charge une liste de mots depuis un fichier, calcule le
  hash MD5 de chaque mot, et le compare au hash recherché jusqu'à trouver une
  correspondance ou épuiser la liste.
- **`BruteForceHashCracker`** — Génère automatiquement toutes les combinaisons
  possibles à partir de l'alphabet `a-z` (longueur max. 4), et teste chacune jusqu'à
  trouver une correspondance.
- **`HashCrackerFactory`** — Fabrique statique responsable de la création des objets
  `HashCracker`. Centralise toute la logique de création : aucune classe concrète
  n'est instanciée ailleurs dans le programme.
- **`Md5Utils`** — Classe utilitaire partagée responsable du calcul du hash MD5,
  utilisée par les deux stratégies pour éviter toute duplication de code.
- **`Main`** — Point d'entrée de l'application console. Parse les arguments `-m`
  (méthode) et `-h` (hash), délègue la création de la stratégie à
  `HashCrackerFactory`, puis affiche le résultat.

## 4. Diagramme UML

![Diagramme de classes UML](uml-diagramH.png)

## 5. Usage du patron Simple Factory

Le patron **Simple Factory** est utilisé pour centraliser la création des objets
`HashCracker` dans une unique classe, `HashCrackerFactory`. Le programme principal ne
crée jamais directement une instance de `DictionaryHashCracker` ou
`BruteForceHashCracker` : il demande à la fabrique de le faire à sa place, en lui
passant simplement le nom de la méthode souhaitée.

```java
HashCracker cracker = HashCrackerFactory.create("DICO");
String password = cracker.crack("e7247759c1633c0f9f1485f3690294a9");
```

Grâce au polymorphisme, le code appelant utilise toujours l'interface `HashCracker`,
sans jamais connaître la classe concrète réellement instanciée.

### Exemples d'exécution

```bash
passwordCracker -m BRUTE -h e7247759c1633c0f9f1485f3690294a9
passwordCracker -m DICO -h e7247759c1633c0f9f1485f3690294a9
```

Résultat attendu :

Password found: test

ou

Password not found

## 6. Résultats obtenus

L'application a été testée avec succès sur les deux stratégies de cassage, ainsi que
sur plusieurs cas d'erreur (méthode invalide, hash mal formé, mot de passe introuvable).

![Résultats des tests en ligne de commande](<img width="576" height="281" alt="test_results" src="https://github.com/user-attachments/assets/76eea810-54dd-4060-9b25-bf953ca25e6b" />
)

**Vidéo de démonstration** : https://drive.google.com/file/d/1UJiRQV8gBH2MXXbdsMwEafwpUt1rlp5u/view?usp=drive_link

| Méthode | Hash testé | Mot recherché | Résultat | Temps d'exécution | Tentatives |
|---------|-----------|----------------|----------|--------------------|------------|
| BRUTE   | `098f6bcd4621d373cade4e832627b4f6` | `test` | Password found: test | 2457 ms | 355 414 |
| DICO    | `5ebe2294ecd0e0f08eab7690d2a6ee69` | `secret` | Password found: secret | 83 ms | — |
| BRUTE   | `00000000000000000000000000000000` | *(inexistant)* | Password not found | 3135 ms | 475 254 |

**Tests de robustesse (gestion des erreurs)** :

| Commande | Résultat attendu | Résultat obtenu |
|----------|-------------------|-------------------|
| `-m XYZ -h ...` | Méthode inconnue rejetée | `Erreur : Méthode inconnue: XYZ`  |
| `-m DICO -h abc` | Hash mal formé rejeté | `Erreur : le hash fourni est invalide...`  |

**Observation** : la stratégie `DICO` est nettement plus rapide (83 ms) que `BRUTE`
(2457 à 3135 ms) sur ces exemples, ce qui illustre concrètement le compromis entre les
deux approches : le dictionnaire est rapide mais limité aux mots qu'il contient, tandis
que la force brute est exhaustive mais coûteuse en temps, même sur un alphabet réduit
(a-z) et une longueur maximale de seulement 4 caractères.

## 7. Difficultés rencontrées

- **Performance de la force brute** : même limitée à 4 caractères et à l'alphabet
  `a-z`, l'attaque par force brute nécessite plusieurs centaines de milliers de
  tentatives (jusqu'à ~475 000 dans nos tests) et prend déjà plusieurs secondes.
  Cela illustre concrètement pourquoi les attaques par force brute deviennent
  rapidement impraticables dès que la longueur ou l'alphabet augmentent.
- **Coordination du travail en parallèle** : les stratégies (`DictionaryHashCracker`,
  `BruteForceHashCracker`) et la fabrique ont été développées simultanément par
  différents membres de l'équipe. Fixer l'interface `HashCracker` avant de commencer
  le développement a été essentiel pour éviter les incompatibilités lors de la fusion.
- **Coordination Git** : plusieurs commits parallèles sur des sujets proches
  (README, diagramme UML, classes concrètes) ont nécessité une vigilance particulière
  pour éviter les conflits de fusion, notamment autour de la pull request ouverte
  pendant le développement.
- **Constructeur de `DictionaryHashCracker`** : cette classe nécessite un chemin de
  dictionnaire en paramètre de constructeur, ce qui a demandé une clarification sur la
  manière dont `HashCrackerFactory` devait fournir cette information (chemin par
  défaut codé en dur dans la fabrique).

## 8. Conclusion

Ce mini-projet a permis de mettre en pratique le patron de création **Simple Factory**
dans un contexte concret : centraliser la création de deux stratégies de cassage de
mot de passe interchangeables derrière une interface commune. L'exercice a illustré
clairement l'intérêt du **polymorphisme** : le programme principal (`Main`) manipule
uniquement le type `HashCracker`, sans jamais connaître les classes concrètes
utilisées, ce qui rend l'architecture modulaire et facile à faire évoluer.

Cette première version a toutefois révélé une limite structurelle de la fabrique
simple : toute nouvelle stratégie de cassage nécessite de modifier directement le
code de `HashCrackerFactory`, ce qui viole le principe Open/Closed. Cette limitation,
volontairement laissée en l'état dans ce mini-projet, sera corrigée dans la version
suivante à l'aide d'un patron de création plus flexible.

## Questions de réflexion

**1. Quels avantages apporte la fabrique simple ?**
Elle centralise la logique de création des objets en un seul endroit, ce qui
simplifie le code appelant (`Main`) : celui-ci n'a plus besoin de connaître les
classes concrètes, seulement l'interface commune. Cela réduit le couplage entre le
programme principal et les implémentations, et facilite la maintenance.

**2. Quels sont ses inconvénients ?**
La fabrique doit être modifiée à chaque ajout d'une nouvelle stratégie de cassage,
ce qui viole le principe Open/Closed (ouverte à l'extension, fermée à la
modification). De plus, la logique de sélection dans `create()` (un `switch` sur une
chaîne de caractères) peut devenir difficile à maintenir si le nombre de méthodes
augmente significativement, et toute erreur de frappe dans le paramètre `method`
n'est détectée qu'à l'exécution, pas à la compilation.

**3. Que faut-il modifier lorsqu'une nouvelle stratégie est ajoutée ?**
Il faut : (1) créer une nouvelle classe implémentant `HashCracker` (par exemple
`RainbowTableHashCracker`), puis (2) modifier le corps de
`HashCrackerFactory.create()` pour y ajouter un nouveau `case` correspondant à cette
stratégie. Cette seconde étape est justement le point faible identifié en question 2.

**4. La fabrique respecte-t-elle le principe Open/Closed ?**
Non. Le principe Open/Closed stipule qu'une classe devrait être ouverte à
l'extension mais fermée à la modification. Or, ajouter une nouvelle stratégie de
cassage oblige à modifier directement le code source de `HashCrackerFactory`
(ajout d'un `case` dans le `switch`), plutôt que d'étendre son comportement sans
toucher à l'existant. Cette limitation est assumée dans cette première version du
projet et sera corrigée dans le mini-projet suivant, probablement à l'aide d'un
patron de création plus flexible (Factory Method ou Abstract Factory).
