# MinervHub 🎓

**MinervHub** è una piattaforma software progettata per facilitare l’incontro tra studenti e tutor universitari, consentendo la pubblicazione, la ricerca e la gestione di annunci di tutoraggio in modo semplice e strutturato.

Il progetto nasce con un forte focus sulla **modellazione software**, sull’uso corretto dei **diagrammi UML** e sull’applicazione dei principi di progettazione orientata agli oggetti, risultando particolarmente adatto a contesti accademici e didattici.

---

## 🎯 Obiettivo del progetto

L’obiettivo di MinervHub è offrire un sistema che permetta:

* la **visualizzazione di annunci di tutoraggio**
* la **ricerca e il filtraggio** degli annunci in base a criteri specifici (corso, esame, modalità, ecc.)
* l’**invio di richieste di contatto** tra studenti e tutor

Il sistema è pensato per essere utilizzabile sia da **utenti registrati** sia da **utenti non registrati**, garantendo un accesso semplice alle informazioni principali.

---

## 🧩 Funzionalità principali

* 📋 **Bacheca annunci**
  Visualizzazione dell’elenco degli annunci di tutoraggio disponibili.

* 🔍 **Filtraggio avanzato**
  Applicazione di filtri per affinare la ricerca (corso, esame, modalità di erogazione, ecc.).

* ✉️ **Invio richiesta di contatto**
  Possibilità di inviare un messaggio al tutor associato a un annuncio.

---

## 🏗️ Architettura e modellazione

MinervHub è progettato seguendo il pattern **Boundary – Control – Entity**, al fine di separare chiaramente:

* la **logica di presentazione**
* la **logica applicativa**
* la **gestione dei dati**

### Entity principali

* **Studente**
* **Annuncio**
* **RichiestaDiContatto**
* **Bacheca** (raccolta degli annunci)

### Boundary

Gestiscono l’interazione con l’utente (form, pulsanti, visualizzazioni).

### Control

Coordinano i flussi applicativi, come:

* visualizzazione della bacheca
* applicazione dei filtri
* invio delle richieste di contatto

---

## 📐 UML

Il progetto include diversi **diagrammi UML**, in particolare:

* **Sequence Diagram**

  * Visualizzazione della bacheca con filtri
  * Selezione di un annuncio
  * Invio di una richiesta di contatto

* **Class Diagram**

  * Modellazione delle Entity
  * Relazioni tra Boundary, Control ed Entity

Questi diagrammi sono utilizzati come strumento centrale per analizzare e giustificare le scelte progettuali.

---

## 👥 Team

Il progetto è stato sviluppato da un **team di 3 persone**, con particolare attenzione alla collaborazione, alla suddivisione dei compiti e alla coerenza architetturale.

---

## 📚 Contesto accademico

MinervHub è un progetto realizzato in ambito universitario, con finalità didattiche, e si concentra su:

* analisi dei requisiti
* progettazione software
* modellazione UML
* corretto uso dei pattern architetturali

---

## 🚀 Possibili estensioni future

* Sistema di autenticazione completo
* Gestione dei profili tutor/studenti
* Storico delle richieste di contatto
* Sistema di feedback e valutazioni
* Implementazione completa lato backend e frontend

---
