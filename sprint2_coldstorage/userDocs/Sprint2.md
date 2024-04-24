
# <span style="color:green;"> Sprint2 </span>
## <span style="color:green;">Obiettivo </span>
Il presente sprint si propone di approfondire il tema dell'osservabilità su ambiente distribuito, implementandolo per la realizzazione di una Graphical User Interface al sistema ColdStorage. 

## <span style="color:green;">Analisi dei requisiti </span>
A puro scopo informativo si ripresentano i requisiti di interesse dello Sprint2, in particolare:
La ServiceAccessGUI è protagonista, e da requisito si richiede esplicitamente che questa si comporti come un pannello da cui un utente possa immettere un peso per richiedere un ticket. Successivamente, se questo è stato accettato, si inserirà il codice identificativo del ticket per innescare il deposito (scenario di arrivo di un truck all' INDOOR). Inoltre, la GUI deve presentare all'utente una visione aggiornata dello stato di occupazione della ColdRoom.
L'analisi ha prodotto una semplice valutazione: Nel modello embrionale del sistema, un simile comportamento è attivo, deve interagire col sistema mediante scambio di messaggi e pertanto viene modellato come un attore.

## <span style="color:green;">Analisi del problema </span>

Viene discusso, in sede di analisi, la necessità di distribuire il sistema su nodi fisici distinti. Fino a questo momento, la GUI veniva simulata in locale con l'unità di Testing J-Unit, per svolgere le principali funzionalità (richiesta ticket, richiesta di scarico e osservazione di Eventi secondo pattern Observer). Tuttavia, questo pone seri vincoli alla mobilità dei Truck Drivers che per richiedere i ticket dovrebbero già trovarsi in sede del sistema Cold Storage Service. Questo ha portato al confronto di diverse soluzioni, volte a garantire una ottima versatilità nell'interazione con molteplici dispositivi mobili e allo stesso tempo nell'interazione con il sistema. Si è dunque pensato all'ideazione di un Server Web come nodo intermedio che ha lo scopo di gestire le interazioni tra Client (utente) e Cold Storage Service. 
Un server Web offre i vantaggi di:
- Accessibilità via rete utilizzando un comune browser.
- Gestione sincrona o asincrona dei messaggi lato client mediante HTTP o WebSocket
- Flessibilità nella gestione delle comunicazioni secondarie col Cold Storage agente da Backend mediante altri protocolli applicativi (esempio: Coap/ MQTT), già testati e funzionanti per la comunicazione con Il Service.

Ai fini dell'Analisi, le tecnologie discusse basate su Java sono essenzialmente due: TomCat e SpringBoot. Si è deciso di optare per la seconda opzione in quanto semplificata nello sviluppo e configurazione gestita in automatico dal framework spring e basata su MVC. La gestione delle dipendenze viene affidata comunemente a Gradle.

Nello sviluppo di una siffatta applicazione, SpringBoot dispone al **Controller** la possibilità di impostare metadati utili all'esecuzione mediante le **Java annotations**, in particolare i mapping utili per l'elaborazione di una Get HTTP lato client. Trattandosi di una pagina semplice, abbiamo gestito solo il mapping root `"/"` , risultando in:

 ![MVC](/MVC.png)

All'avviamento, il framework predispone la configurazione del server in maniera automatica, fissando come porta 8086 per la connessione(`/resources/application.properties`).
Riferendo il Model, Questo viene organizzato tenendo conto del principio di singola responsabilità, secondo un'architettura gerarchica triangolare:

![Arch|400](/Arch.png)

In ordine:
- Il Client si connette al Client Connection Manager richiedendo la pagina HTTP associata all'url `"/"`. 
- Il Client riceve la pagina HTTP che presenta il necessario per inserire i valori interi di ticket e e peso (Input text) e i bottoni per innescare l'invio da parte del sorgente "main.js".
- Alla pressione del bottone, una WebSocket inoltra i messaggi al ClientConnectionManager, che lo gestisce, preprocessa ed inoltra al Service Access Gui per la valutazione ( rappresenta quest'ultimo il Core Business).
- Il Service Connection Manager riceve poi il messaggio e a seconda della richiesta, fabbrica un messaggio apposito per l'inoltro al ColdStorageService. La connessione è in questo caso di tipo CoapConnection() e poggia sul TCP.
- Il processo viene effettuato in retroazione per propagare la risposta fino al Client.

### Client Connection Manager
Componente "passivo" rappresentato da un POJO e realizzato come classe JAVA. Ha il solo scopo di gestire coi suoi metodi le connessioni verso Client esterni. Estende la classe `AbstractWebSocketHandler` che realizza coi suoi metodi la gestione di basso livello del Binding tra le socket. 

![Ciient](./ClientManager.png)

Si identifica il problema dell'associazione: In ogni momento il Client Manager deve tenere traccia di tutti i client collegati (Una comune Lista java) con i token di sessione e, in aggiunta di coloro i quali, tra i client, hanno inviato una richiesta e attendono risposta. In questo secondo caso, l'associazione è fatta tra una stringa (contenente la tipologia di messaggio, e.g. "request", concatenato ad un identificativo) e l'id di sessione, mediante HashMap.

Si definiscono inoltre i metodi "custom":

![](./ClientMAN.png)
per l'inoltro alla SAGui, o per l'inserimento di una request nell'HashMap.


### Service Access GUI

Componente per il coordinamento ad alto livello. Il principio dell'inversione delle dipendenze porta alla delegazione della maggior parte delle operazioni ai due POJO (Client e Service Connection Manager), di cui la GUI è essa stessa un POJO tramite i loro attributi private di classe. Viene pertanto acceduta da questi mediante metodi definiti **public**.

![](/SAGui.png)

I metodi definiti per la classe sono:
- **public void gotReqFromClient(String msg, String requestId)**: a seconda che il messaggio sia una `storerequest` o `dischargefood` innesca due gestioni differenziate del Service Manager sottostante.
- **public void gotRespFromCSS(String msg, String requestId)**: è di contro il metodo con cui si gestiscono le risposte o i dispatch del Service. (In caso di dispatch, l'id di sessione è **Null**) e delega una risposta differenziata per casi al Client Manager sottostante.
- **public String getValue(String msg)**: serve ad estrarre il payload dei messaggi tramite pattern matching.
- **private void updateCount(String data)**: serve ad estrarre i parametri di stato dal payload dei dispatch tramite pattern matching.

### Service Connection Manager
Si tratta del componente speculare al Client Connection Manager. Gestisce l' interazione col solo Cold Storage Service, mediante due modalità definite dal costruttore:
- Viene attivata una connessione TCP verso il ColdStorage (porta 8055), con cui effettuare gli scambi request - response
- Viene attivata una CoapConnection verso la stessa porta con un Handler customizzato, mediante la ridefinizione dei metodi "onError" e "onLoad" innescati dal listener della connessione all'arrivo di un dispatch.

Ulteriori metodi vengono definiti per differenziare il comportamento all'arrivo di una `storerequest` o `dischargefood` ed effettivamente inoltrare e ricevere sulla socket TCP una request-response (metodo **private sendToCSS**).
Vale la pena notare che il pattern implementato è il pattern observer, per il quale gli attori del metamodello qak (interazione Coap-based) offrono già API specifiche per l'aggiornamento degli osservatori.

E' nostro interesse osservare gli aggiornamenti da parte del ColdRoom sullo stato in tempo reale di peso, spazio riservato e Max storage.
Lato Cold Storage Service, sarà sufficiente apportare una modifica mediante il tag `UpdateResource` nell'attore ColdRoom.

![](./Coldroom.png)

## Client

## <span style="color:green;">Progettazione </span>


