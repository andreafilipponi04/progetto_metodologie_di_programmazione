# 🚉 Sistema di Gestione Stazione Ferroviaria

Un sistema software modulare in Java per la gestione operativa di una stazione
ferroviaria. Il sistema simula e controlla il ciclo di vita dei treni all'interno
dell'area di competenza della stazione, garantendo il rispetto dei vincoli di
sicurezza e delle risorse disponibili.

> **Progetto per il corso di Metodologie di Programmazione**
> Studente: Andrea Filipponi — Matricola: 7137945
> Anno Accademico: 2024/25 — Eclipse 2025-12

---

## 📋 Indice

- [Descrizione del Sistema](#descrizione-del-sistema)
- [Funzionalità Principali](#funzionalità-principali)
- [Design Pattern Utilizzati](#design-pattern-utilizzati)
- [Struttura del Progetto](#struttura-del-progetto)
- [Formule di Calcolo](#formule-di-calcolo)
- [Test](#test)
- [Tecnologie](#tecnologie)

---

## Descrizione del Sistema

La classe `Station` agisce come autorità centrale che autorizza ogni transizione
di stato dei treni (arrivo → sosta → partenza). Nessun treno può cambiare stato
senza l'esplicito consenso della stazione, garantendo il rispetto dei vincoli di
sicurezza e delle risorse disponibili.

---

## Funzionalità Principali

| Funzionalità | Descrizione |
|---|---|
| **Controllo del Traffico** | `Station` è l'unica autorità che autorizza le transizioni di stato dei treni |
| **Gestione Eterogenea dei Convogli** | Supporto per `PassengerTrain` (posti) e `FreightTrain` (capacità di carico) |
| **Monitoraggio in Tempo Reale** | Ogni cambio di stato viene notificato automaticamente agli osservatori registrati (es. `TrainLogger`) |
| **Gestione del Personale** | Prima di autorizzare la partenza, il sistema verifica la disponibilità del personale; in caso di carenza il treno viene bloccato |
| **Stima dei Costi Operativi** | Calcolo dinamico dei costi basato sulle caratteristiche specifiche di ciascun treno |

---

## Design Pattern Utilizzati

### 🔄 State — Gestione del Ciclo di Vita

La classe astratta `TrainState` definisce il contratto per le operazioni dipendenti
dallo stato (`stop()`, `leave()`). Tre stati concreti (privati e immutabili) sono
forniti tramite inner class:

- `IN_ARRIVAL` → può transitare solo verso `STOPPED`
- `STOPPED` → può transitare verso `DEPARTED`
- `DEPARTED` → stato terminale

Le transizioni illegali (es. `IN_ARRIVAL` → `DEPARTED`) sollevano una
`IllegalTrainStateTransitionException`. L'utilizzo di una gerarchia di classi al posto
di un `enum` evita la violazione dell'**Open/Closed Principle** e consente la
gestione di comportamenti complessi per ogni stato.

### 🏭 Static Factory Methods — Accesso agli Stati

Le classi concrete degli stati sono inner class private di `TrainState`, non
istanziabili dall'esterno. L'unico modo per ottenere un'istanza di stato è tramite
i metodi factory statici:

```java
TrainState.ofInArrival()
TrainState.ofStopped()
TrainState.ofDeparted()
```

### 👁️ Observer — Reattività e Disaccoppiamento

`Train` mantiene una lista di `TrainObserver`. Ad ogni transizione di stato completata
con successo, tutti gli osservatori registrati vengono notificati tramite
`notifyObservers()`.

Sono presenti due implementazioni concrete:

- **`TrainLogger`** — traccia i cambiamenti di stato tramite l'interfaccia `TrainPrinter`
  (in produzione stampa su console; nei test usa un `MockPrinter` che salva le stringhe
  in una lista, disaccoppiando la logica di I/O dalla logica di business)
- **`StaffCheckObserver`** — osservatore "attivo" che, alla transizione verso `STOPPED`,
  verifica automaticamente la disponibilità del personale e blocca il treno se le risorse
  sono insufficienti

### 🧩 Visitor — Operazioni su Gerarchie Eterogenee

Le classi dei treni espongono un metodo `accept(TrainVisitor)` che implementa il
meccanismo del **Double Dispatch**. La logica di business è spostata in visitor dedicati:

- **`CostEstimator`** — calcola i costi operativi con tariffe diverse per passeggeri o
  tonnellate di carico
- **`StaffPlanner`** — determina il numero di controllori o operai necessari

Per aggiungere nuove metriche (es. consumo energetico) è sufficiente creare una nuova
classe che implementa `TrainVisitor`, senza modificare le classi dei treni esistenti.

---

## Struttura del Progetto

```text
src/
├── main/
│   └── java/
│       ├── station/
│       │   └── Station.java                              # Implementazione di TrafficController
│       ├── trains/
│       │   ├── Train.java                                # Classe base astratta
│       │   ├── PassengerTrain.java                       # Treno passeggeri (posti)
│       │   ├── FreightTrain.java                         # Treno merci (peso massimo)
│       │   ├── TrainState.java                           # Stato astratto + Static Factory Methods
│       │   ├── TrafficController.java                    # Interfaccia del controllore del traffico
│       │   ├── IllegalTrainStateTransitionException.java # Eccezione di stato
│       │   ├── TrainNotInStationException.java           # Eccezione di presenza
│       │   └── UnauthorizedTrainActionException.java     # Eccezione di autorizzazione
│       └── utils/
│           ├── TrainObserver.java                        # Interfaccia Observer
│           ├── TrainVisitor.java                         # Interfaccia Visitor
│           ├── TrainPrinter.java                         # Astrazione per la stampa
│           ├── TrainLogger.java                          # Observer: logging
│           ├── StaffCheckObserver.java                   # Observer: verifica personale
│           ├── CostEstimator.java                        # Visitor: stima costi
│           └── StaffPlanner.java                         # Visitor: pianificazione personale
└── test/
    └── java/
        ├── trains/
        │   ├── MockTrafficController.java                # Mock per simulare la stazione nei test
        │   ├── TrainStateTest.java                       # Test sulle transizioni di stato
        │   └── TrainTest.java                            # Test su eccezioni e observer
        └── utils/
            ├── CostEstimatorTest.java                    # Test di calcolo dei costi
            ├── StaffCheckObserverTest.java               # Test sul blocco dei treni per mancanza di staff
            └── StaffPlannerTest.java                     # Test sul calcolo del personale necessario
```

---

## Formule di Calcolo

### CostEstimator

| Tipo di Treno | Formula |
|---|---|
| `PassengerTrain` | `500.0 + (posti × 10.0)` |
| `FreightTrain` | `2000.0 + (pesoMassimoCarico × 0.5)` |

### StaffPlanner

| Tipo di Treno | Formula |
|---|---|
| `PassengerTrain` | `2 + (posti / 50)` |
| `FreightTrain` | `1 + (pesoMassimoCarico / 1000)` |

---

## Test

I test sono scritti con **JUnit 4** e **AssertJ**. La suite copre:

- `TrainStateTest` — transizioni di stato valide e invalid, eccezioni attese
- `TrainTest` — gestione degli osservatori, azioni non autorizzate
- `StaffCheckObserverTest` — blocco del treno in caso di personale insufficiente
- `CostEstimatorTest` — correttezza del calcolo dei costi per entrambi i tipi di treno
- `StaffPlannerTest` — correttezza del conteggio del personale per entrambi i tipi di treno

Vengono utilizzati `MockTrafficController` e `MockPrinter` per isolare la logica di
business dall'I/O e dall'infrastruttura durante i test.

```java
// Esempio: un PassengerTrain con 100 posti ha un costo operativo di 1500.0
PassengerTrain pt = new PassengerTrain(1, 100);
CostEstimator estimator = new CostEstimator();
pt.accept(estimator);
assertThat(estimator.getTotalCost()).isEqualTo(1500.0);
```

---

## Tecnologie

- **Java** (Eclipse 2025-12)
- **JUnit 4** — testing unitario
- **AssertJ** — asserzioni fluenti per i test
