# Notes sur la couche Service (Atelier 4)

Les services sont dans `tn.esprit.rayan_mahmoudi_4cce11.service` (interfaces `I…Service`, implémentations `…ServiceImpl`).
L'exception métier `ResourceNotFoundException` est dans le package `exception`.

## A. Modes d'injection comparés

| Critère | Constructeur | Attribut (`@Autowired`) |
|---|---|---|
| Champ `final` possible ? | Oui | Non |
| Dépendance visible dans la signature de la classe ? | Oui | Non (cachée) |
| `new` hors conteneur sans dépendance `null` ? | Oui : le repository est obligatoire | Non : le champ reste `null` |
| Signalé par SonarQube for IDE ? | Non | Oui (règle S6813, à confirmer dans le panneau) |

## B. Services

| Service | Dépendances injectées | Mode | Justification |
|---|---|---|---|
| AgenceServiceImpl | IAgenceRepository | Constructeur | Dépendance obligatoire, typée par interface, service sans état |
| EmployeServiceImpl | IEmployeRepository | Constructeur | Idem |
| VehiculeServiceImpl | IVehiculeRepository | Constructeur | Idem |
| EquipementServiceImpl | IEquipementRepository | Constructeur | Idem |
| ClientServiceImpl | IClientRepository | Constructeur | Idem |
| ReservationServiceImpl | IReservationRepository | Constructeur | Idem |
| ContratServiceImpl | IContratRepository | Constructeur | Idem |
| PaiementServiceImpl | IPaiementRepository | Constructeur | Idem, lecture seule : les paiements passent par le Contrat |
| MaintenanceServiceImpl | IMaintenanceRepository | Constructeur | Idem |

Règles de gestion appliquées (`IllegalArgumentException`) :

| Service | Règle |
|---|---|
| Agence | `nom` et `ville` non vides |
| Employe | `nom`, `prenom` non vides, `role` renseigné |
| Vehicule | `immatriculation` non vide, `tarifJournalier` renseigné et non négatif |
| Equipement | `libelle` non vide |
| Client | `nom` et `email` non vides |
| Reservation | `dateDebut` et `dateFin` renseignées, `dateFin` pas antérieure à `dateDebut` |
| Contrat | `montantTotal` renseigné et non négatif |
| Maintenance | `dateDebut` renseignée, `dateFin` (facultative) pas antérieure à `dateDebut` |
| Paiement | Aucune : `findById` et `findAll` uniquement |

Dans `update`, on charge l'existant puis on recopie les attributs simples. L'identifiant et les associations
(collections, clés étrangères) ne sont pas touchés : leur gestion est l'objet du chapitre 9.

## C. Messages d'erreur du conteneur

**Message 1 — `NoSuchBeanDefinitionException` (bean `IContratService` introuvable)**
- Causes probables : `@Service` oublié sur `ContratServiceImpl` ; classe placée hors du package de scan `tn.esprit.rayan_mahmoudi_4cce11`.
- Correction : ajouter `@Service` ; vérifier le package.

**Message 2 — `NoUniqueBeanDefinitionException` (`emailNotificateur` et `smsNotificateur`)**
- Correction 1 : `@Primary` sur la classe du bean par défaut (par exemple `EmailNotificateur`).
- Correction 2 : `@Qualifier("emailNotificateur")` sur le paramètre du constructeur de `NotificationController`.

**Message 3 — dépendance circulaire (`clientServiceImpl` ↔ `reservationServiceImpl`)**
- Avec l'injection par constructeur, chaque bean doit exister avant d'être injecté : aucun des deux ne peut être créé en premier, le cycle est impossible à contourner.
- Piste : extraire la responsabilité commune dans un troisième service dont les deux dépendent.

## D. Anomalies SonarQube for IDE

| Règle | Explication | Correction apportée |
|---|---|---|
| S6813 | Injection par attribut (`@Autowired` sur un champ) | Retour à l'injection par constructeur et au champ `final` |
| S112 | Lever une `RuntimeException` générique | Utilisation de `ResourceNotFoundException` |
| À compléter | Troisième anomalie à relever dans l'analyse des packages `service` et `exception` | À compléter |

## E. Questions de compréhension

1. Le `new` de `ContratServiceImpl` est exécuté par le conteneur Spring au démarrage : `@Service` et le scan de composants le détectent, puis il passe le proxy de `IContratRepository` au constructeur.
2. Un contrôleur dépend de l'interface pour être indépendant de l'implémentation : on peut la changer ou la remplacer par une dépendance de test sans toucher au contrôleur.
3. Un singleton est partagé par tous les threads. Avec un attribut modifiable (par exemple un « contrat courant »), deux requêtes simultanées s'écraseraient et un client pourrait recevoir les données d'un autre.
4. `save` avec l'objet reçu écraserait toutes les colonnes et, avec `orphanRemoval`, pourrait vider la collection de paiements. On charge donc l'existant et on recopie les attributs modifiables.
5. `@Component` s'applique à une classe détectée par le scan ; `@Bean` est une méthode de configuration, pour une classe qu'on ne peut pas annoter. `@Primary` désigne le candidat par défaut (sur la classe) ; `@Qualifier` fait un choix explicite (à l'injection). `IPaiementService` est en lecture seule car les paiements sont créés, modifiés et supprimés via le Contrat (cascade, `orphanRemoval`).
