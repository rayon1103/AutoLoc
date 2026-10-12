# Notes sur la couche Repository (Atelier 3)

Tous les repositories sont dans le package `tn.esprit.rayan_mahmoudi_4cce11.repository`, préfixés par `I`,
et étendent `JpaRepository<Entité, Long>`. Aucune annotation `@Repository` n'est nécessaire : Spring Data
génère un proxy au démarrage.

## Choix de l'interface

| Interface | Étend | Justification |
|---|---|---|
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, `findAll` renvoie une `List`, `saveAndFlush` disponible. Les suppressions via `delete` / `deleteById` passent par le contexte de persistance : la cascade et l'orphanRemoval vers les paiements s'appliquent. |
| IPaiementRepository | JpaRepository<Paiement, Long> | Sert surtout à lire les paiements. Le code métier crée ou retire un paiement en passant par le `Contrat`, car c'est une composition. |
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD complet avec listes, tri et pagination pour lister les agences. |
| IEmployeRepository | JpaRepository<Employe, Long> | Même besoin : CRUD complet, tri et pagination. |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | Entité la plus consultée : liste, tri, pagination et filtres à venir. Remplace `CrudRepository`, dont `findAll` renvoie un `Iterable`. |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD complet. |
| IClientRepository | JpaRepository<Client, Long> | CRUD complet, tri et pagination pour la liste des clients. |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD complet, avec `getReferenceById` utile pour renseigner une clé étrangère sans charger l'entité. |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD complet. |

## Pourquoi JpaRepository plutôt que CrudRepository

- `findAll`, `findAllById` et `saveAll` renvoient une `List` (et non un `Iterable`), plus facile à manipuler.
- Il apporte le tri et la pagination (`PagingAndSortingRepository`), sans avoir à déclarer deux interfaces.
- Il ajoute `flush`, `saveAndFlush` et `getReferenceById`.

## Limite à retenir

`deleteAllInBatch` et `deleteAllByIdInBatch` contournent le contexte de persistance : la cascade et
l'orphanRemoval ne s'appliquent pas. Supprimer ainsi des contrats laisserait des paiements orphelins ou
ferait échouer la requête sur la contrainte de clé étrangère. On utilise `deleteById` ou `delete`.

## Anomalies SonarQube for IDE

| Anomalie | Règle / explication | Correction apportée |
|---|---|---|
| Import inutilisé `java.util.List` dans Agence | java:S1128 : un import jamais utilisé alourdit le code et peut masquer une erreur | Suppression de l'import |
| Import inutilisé `java.util.ArrayList` dans Vehicule | java:S1128 : même règle | Suppression de l'import |
| Import inutilisé `java.util.List` dans Vehicule | java:S1128 : même règle | Suppression de l'import |