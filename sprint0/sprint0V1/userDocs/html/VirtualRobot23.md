### **Navigation**
- [index]
- [next] |
- [previous] |
- [iss23 1.0 documentation](index.html) »
- VirtualRobot23
# <a name="virtualrobot23"></a>**VirtualRobot23[¶**](#virtualrobot23 "Permalink to this headline")**
Nel progetto it.unibo.virtualRobot2023, il DISI ha sviluppato un ambiente virtuale (denominato WEnv) che include un simulatore di *Differential Drive robot* (**DDR**).

Un [DDR Robot](https://www.youtube.com/watch?v=aE7RQNhwnPQ) possiede due ruote motrici sullo stesso asse e una terza ruota condotta (non motrice). La tecnica *differential drive* consiste nel far muovere le ruote motrici a velocità indipendenti l’una dall’altra.

WEnv fa riferimento a una forma semplificata di DDR in cui le possibìili mosse sono:

- muoversi avanti-indietro lungo una direzione costante
- fermarsi
- ruotare di 90° a destra o sinistra

Queste mosse sono realizzate inviando opportuni comandi al robot simulato.

Il robot virtuale (e in futuro anche quelli reali) viene considerato un oggetto inscrivibile in un cerchio di raggio R.

Usare WEnv
## <a name="la-scena-di-wenv"></a>**La scena di WEnv[¶**](#la-scena-di-wenv "Permalink to this headline")**
La scena del WEnv è costruita da una descrizione che può essere facilmente definita da un progettista di applicazioni modificando il file node\WEnv\WebGLScene\sceneConfig.js.

Nel seguito, faremo riferimento a una stanza rettangolare (vuota o con ostacoli), racchiusa entro quattro pareti. Procedendo dal bordo superiore e muovendoci in senso orario, i nomi delle pareti sono: wallUp, wallRight, wallDown, wallLeft.

La scena può anche presentare uno o più dispositivi Sonar che rilevano la presenza del robot e ne misurano la distanza.

Si veda [NaiveGui page](#naivegui-page).
## <a name="come-attivare-wenv"></a>**Come attivare WEnv[¶**](#come-attivare-wenv "Permalink to this headline")**
Usando il progetto it.unibo.virtualRobot2023

- Installare [Node.js](https://nodejs.org/it/)
- In it.unibo.virtualRobot2023\node\WEnv\server, eseguire **npm install**
- In it.unibo.virtualRobot2023\node\WEnv\WebGLScene, eseguire **npm install**
- In it.unibo.virtualRobot2023\node\WEnv\server\src, eseguire **node WebpageServer.js**

Usando Docker

WEnv viene anche distribuito come immagine Docker, attivabile direttamente o con *docker-compose*.

docker run -ti -p 8090:8090 -p 8091:8091 --rm  docker.io/natbodocker/virtualrobotdisi23:1.0

docker-compose -f virtualRobot23.yaml  up

//Per terminare: docker-compose -f virtualRobot23.yaml  down

Per un overview su Docker e DockerCompose si veda: [Introduction to Docker and DockerCompose](./_static/IntroDocker23.html).
## <a name="come-interagire-con-wenv"></a>**Come interagire con WEnv[¶**](#come-interagire-con-wenv "Permalink to this headline")**
Stringhe che esprimono comandi di movimento al robot possono essere inviate a WEnv in due modi:

- come messaggi HTTP POST inviati sulla porta **8090**. E’ una forma di comunicazione sincrona (request-response).
- come messaggi inviati su un Websocket alla porta **8091**. E’ una forma di comunicazione asincrona (fire-and-forget) che implica l’emissione, da parte di WEnv, di un [Messaggio di stato](#messaggio-di-stato)[](_images/logicInteraction.PNG)

![_images/logicInteraction.PNG][](_images/logicInteraction.PNG) 

WEnv non acetta altre connessioni hhtp://HOSTADDR:8090 oltre la prima
### <a name="naivegui-html"></a>**NaiveGui.html[¶**](#naivegui-html "Permalink to this headline")**
Per consentire agli utenti umani prove di interazione con WEnv, il progetto definisce una pagina HTML (file node/clients/NaiveGui.html) che permette di:

- visualizzare la scena corrente (il virtual robot deve essere stato attivato);
- inviare comandi [cril](#comandi-base-per-il-robot-in-cril) al VirtualRobot in modo sincrono (via HTTP) e in modo asicrono (via WebSocket)
- visualizzare nella DisplayArea le informazioni emesse da WEnv ([Messaggio di stato](#messaggio-di-stato)).

Ponendo il mouse sulla scena, si possono inviare al robot comandi tramite tastiera:

- tasto w: avanti
- tasto s: indietro
- tasto a: ruota a sinistra di *90°*
- tasto d: ruota a destra di *90°*

Sulla scena compare un menu Open controls che presenta comandi con cui modificare la scena, inserendo/eliminando oggetti. Le modifiche vengono perse al reload della pagina.
#### <a name="naivegui-page"></a>**NaiveGui page[¶**](#naivegui-page "Permalink to this headline")**
La pagina si presenta come segue:[](_images/NaiveGui.PNG)

![_images/NaiveGui.PNG][](_images/NaiveGui.PNG) 

Attraverso questa GUI possiamo effettuare esperimenti come quelli che seguono:

- inviare un comando SYNCH e vedere l’esito sulla DISPLAY AREA
- inviare un comando SYNCH e interromperlo con i pulsanti HALTPOST oppure HALT
- inviare un comando SYNCH e subito dopo un altro comando ASYNCH o SYNCH (not allowed)
- inviare un comando ASYNCH e vedere l’esito sulla DISPLAY AREA.
- inviare un comando ASYNCH e subito dopo un comando SYNCH (not allowed)

Si noti che un valore di tempo -1 significa ‘forever’ e occorre sempre inviare un comando HALT per poterne effettuare un altro. Si consiglia di evitare l’uso del valore -1, a favore di valori interi positivi adeguatamenti alti in relazione al dominio applicativo.
#### <a name="esperimento-misto"></a>**Esperimento misto[¶**](#esperimento-misto "Permalink to this headline")**
1. Eseguire il comando

   curl -d "{\"robotmove\":\"moveForward\", \"time\":\"2000\"}"

   `     `-H "Content-Type: application/json"  -X POST http://localhost:8090/api/move

1. Entro 2000 msec premere il pulsante HALT (o HALTPOST) di [NaiveGui.html](#naivegui-html)

WEnv invia la risposta {"endmove":false,"move":"moveForward-interrupted"} per il comando curl e la stessa informazione ai client-WS (che viene visualizzata sulla DISPLAY AREA di [NaiveGui.html](#naivegui-html)):
### <a name="comandi-base-per-il-robot-in-cril"></a>**Comandi-base per il robot in cril[¶**](#comandi-base-per-il-robot-in-cril "Permalink to this headline")**
Il linguaggio per esprimere comandi di movimento del robot virtuale viene detto cril (*concrete-robot interaction language*) e consiste di stringhe JSON secondo la sintassi che segue:

{"robotmove":"CMDMOVE", "time":T}

CMDMOVE ::= "turnLeft" | "turnRight" | "moveForward" | "moveBackward" | "alarm"

T    ::= <NaturalNum>
#### <a name="alarm"></a>**alarm[¶**](#alarm "Permalink to this headline")**
Il comando **“alarm”** non è stato denominato halt per introdurre l’idea che il robot può fermarsi anche in caso di situazioni anomale, come ad esempio un calo dell’alimentazione o un incendio.

- Il comando *alarm* inviato con [Interazione sincrona](#interazione-sincrona) produce sempre la risposta {"endmove":true,"move":"halt"}.
- Il comando *alarm* inviato con [Interazione asincrona](#interazione-asincrona) non produce messaggi di risposta.

Comandi di tipo sincrono
### <a name="interazione-sincrona"></a>**Interazione sincrona[¶**](#interazione-sincrona "Permalink to this headline")**
In una interazione request-response, la risposta è espressa in forme di stringhe JSON che assumono valori diversi in relazione alle situazioni che si possono avere.

{"endmove":"RESULT", "move":"MINFO" }

RESULT  ::= true | false  | notallowed

MINFO   ::= MOVEID | MOVEID-collision | MOVEID-interrupted

MOVEID  ::= moveForward | moveBackward | turnLeft | turnRight

Un comando attivato con HTTP POST con durata T, potrebbe terminare prima del tempo T in quanto:

- il robot ha ricevuto (prima del tempo T) il comando [alarm](#alarm).

|![_images/MoveForwardOk.png]|mossa che termina con successo|
| :- | :- |
|![_images/MoveForwardHalted.png][_images/MoveForwardOk.png]|mossa interrotta da [alarm](#alarm)|

Nel caso di interazione sincrona, si ha che:

- Non si può interrompere un comando con un altro comando sincrono diverso da alarm.
### <a name="collision"></a>**Collision[¶**](#collision "Permalink to this headline")**
Se un comando provoca il contatto del robot con un ostacolo, il comando ha comunque durata T.

|![_images/MoveForwardCollision.png][_images/MoveForwardOk.png]|mossa sincrona che provoca collisione|
| :- | :- |

Tuttavia, i client-WS ricevono ANCHE l’informazione ([Messaggio di stato](#messaggio-di-stato)) *collision*, che assume la forma che segue:

{"collision":"moveForward","target":"OBSTACLEID"}

Il mondo virtuale permette di inserire nel messaggio anche il nome dell’ostacolo (OBSTACLEID). Questo ovviamente non è possibile nel mondo reale, ma al momento può essere utile per comprendere meglio il comportamento delle applicazioni.

Riportiamo nel seguito alcuni casi rilevanti, dopo avere collocato il robot nello stato iniziale convenzionale HOME.

HOME
### <a name="stato-iniziale-del-virtualrobot"></a>**Stato iniziale del virtualrobot[¶**](#stato-iniziale-del-virtualrobot "Permalink to this headline")**
- il robot è rivolto verso il basso e si trova nell’angolo superiore sinistro (locazione denotata d’ora in poi come HOME).
#### <a name="esempi-di-comandi-al-virtualrobot23"></a>**Esempi di comandi al VirtualRobot23[¶**](#esempi-di-comandi-al-virtualrobot23 "Permalink to this headline")**
I comandi sono inviati, al momento, usando il tool [curl](https://curl.se/) e hanno la forma:

curl CMD  -H "Content-Type: application/json" -X POST http://localhost:8090/api/move

Gli esempi che seguono sono riferiti alla scena-base del progetto it.unibo.virtualRobot2023; in essi riporteremo solo la forma di CMD.

   1. Movimento in avanti normale

      **Comando**: muovi in avanti per *1300 msec*:

curl -d "{\"robotmove\":\"moveForward\", \"time\":\"1300\"}" ...

**Risposta**:

{"endmove":true,"move":"moveForward"}

   1. Movimento in avanti che provoca collisione con la parete *wallDown*:

      **Comando**: muovi in avanti per *2200 msec*:

curl -d "{\"robotmove\":\"moveForward\", \"time\":\"2200\"}" ...

**Risposta**:

{"endmove":false,"move":"moveForward-collision"}

   1. Movimento in avanti con interruzione

      **Comando**: *comando1* seguito da [alarm](#alarm) prima della fine:

curl -d "{\"robotmove\":\"alarm\", \"time\":\"10\"}" ...

**Risposta**:

{"endmove":false,"move":"moveForward-interrupted"}

   1. Ritazione a sinistra normale

      **Comando**:ruota a sinistra con time *300 msec*:

curl -d "{\"robotmove\":\"turnLeft\", \"time\":\"300\"} ..."

**Risposta**:

{"endmove":true,"move":"turnLeft"}

   1. Movimento in avanti subito seguito da rotazione

**Comando**: *comando1* seguito (prima della fine) da *comando4* o altro comando SYNCH, diverso da HALTPOST **Risposta**:

{"endmove":"notallowed","move":"turnLeft"}

{"endmove":true,"move":"moveForward"}

Esempi di interazione sincrona con WEnv basati su programmi Java saranno introdotti nella sezione [TestMovesUsingHttp](#testmovesusinghttp).

Comandi di tipo asincrono
### <a name="interazione-asincrona"></a>**Interazione asincrona[¶**](#interazione-asincrona "Permalink to this headline")**
Inviare un comando in modo *fire-and-forget* significa non attendere risposta.
#### <a name="messaggio-di-stato"></a>**Messaggio di stato[¶**](#messaggio-di-stato "Permalink to this headline")**
Una volta eseguto il comando, il server WEnv invia a tutti i client connessi attraverso la connessione Websocket informazioni sull’esito del comando (*Messaggio di stato*), con la seguente sintassi:

{"endmove":"RESULT", "move":"MINFO"}

RESULT  ::= true | false

MINFO   ::= MOVEID |  MOVEID\_notallowed (asynch) | MOVEID-interrupted

MOVEID  ::= moveForward | moveBackward | turnLeft | turnRight

Il significato dei valori di MINFO è il seguente:

- **MOVEID-interrupted**: mossa MOVEID interrotta perchè il robot ha ricevuto un comando alarm
- **MOVEID\_notallowed (asynch)**: mossa MOVEID rifiutata (non eseguita) in quanto la mossa relativa al comando precedente non è ancora terminata.

L’invio asincrono di un comando non blocca il chiamante; di conseguenza, un client può inviare un nuovo comando su Websocket prima che il precedente sia terminato o sia stato interrotto. Vale la seguente regola:

- Wenv NON esegue un comando, se un comando precedente non è terminato.

Dunque:

- si invio un comando che dura un tempo T e questo provoca **collisione**, non posso inviare un altro comando (che non sia [alarm](#alarm)) prima della scadenza di T.

Test di interazione asincrona con WEnv basati su programmi Java saranno introdotti più avanti (si veda [TestMovesUsingWs](#testmovesusingws)).

Un cliente connesso a WEnv mediante Websocket può ricevere anche *informazioni su variazioni dello stato del ‘mondo’*, quali:

- dati emessi dai *sonar* presenti nella scena quando rilevano un oggetto in movimento;
- dati emessi dai *sensori di impatto* posti davanti e dietro al robot, quando rilevano un ostacolo. Questi dati sono automaticamente convertiti in messaggi [Collision](#collision).

Se nell’ambiente è presente un sonar e il robot vi passa davanti, WEnv genera:

{"sonarName": "<sonarName>", "distance": <int>, "axis": "AXIS" }

AXIS = x | y  //a seconda dell'orientamento del sonar

//Esempio:

{"sonarName":"sonar1","distance":-6,"axis":"y"}

Come è fatto WEnv
## <a name="wenv-note-di-implementazione"></a>**WEnv - note di implementazione[¶**](#wenv-note-di-implementazione "Permalink to this headline")**
L’implementazione di WEnv si basa su due componenti principali:

- **server**: che definisce il programma WebpageServer.js scritto con il framework [Node Express](https://www.tutorialspoint.com/nodejs/nodejs_express_framework.htm)
- **WebGLScene**: componente che gestisce la scena
### <a name="architettura-di-wenv"></a>**Architettura di WEnv[¶](#architettura-di-wenv "Permalink to this headline")[](_images/WenvArch.PNG)**
![_images/WenvArch.PNG][](_images/WenvArch.PNG) 

WebpageServer.js utilizza due diversi tipi di WebSocket:

- un socket (detto **sceneSocket**) basato sulla libreria [socket.io](https://socket.io/docs/v4/) che viene utilizzato per gestire l’interazione con **WebGLScene**.

  socket.io non è un’implementazione WebSocket.

  Sebbene [socket.io](https://socket.io/docs/v4/) utilizzi effettivamente WebSocket come trasporto quando possibile, aggiunge alcuni metadati a ciascun pacchetto: il tipo di pacchetto, lo spazio dei nomi e l’ID di riconoscimento quando è necessario un riconoscimento del messaggio. Ecco perché un client WebSocket non sarà in grado di connettersi correttamente a un server Socket.IO e un client [socket.io](https://socket.io/docs/v4/) non sarà in grado di connettersi a un server WebSocket.

- il websocker **8091** basato sulla libreria [ws](https://www.npmjs.com/package/ws) : questo socket viene utilizzato per gestire comandi applicativi asincroni per muovere il robot inviati da client remoti e per inviare a client remoti un [Messaggio di stato](#messaggio-di-stato).

  WEnv utilizza la libreria Node [einaros](https://github.com/einaros/ws) per accettare questi comendi.

  Il modulo ws non funziona nel browser: bisogna utilizzare l’oggetto WebSocket nativo.

Quando WebvGLScene rileva una collisione tra il robot virtuale e un ostacolo, invoca l’utilità eventBus.js per ‘emettere un evento collisione’ oltre lo **sceneSocket**.

Questo evento è gestito da un apposito handler (vedi sceneSocketInfoHandler in WebpageServer.js), che reindirizza le informazioni a tutti i client connessi sulla 8091.

Programmi naive di uso di WEnv
## <a name="programmi-naive"></a>**Programmi naive[¶**](#programmi-naive "Permalink to this headline")**
L’ambiente WEnv verrà utilizzato nello sviluppo di applicazioni proattive/reattive basate su componenti che interagiscono a scambio di messaggi con interazioni sincrone e/o asincrone.

Durante lo sviluppo di queste applicazioni, a complessità via via crescente, avremo modo di costruire infrastrutture di supporto e astrazioni di comunicazione capaci di agevolare il compito dell’application-designer.

Prima di procedere in questa direzione, introduciamo alcuni esempi di controllo del robot attraverso programmi Java scritti in modo ‘naive’, avvalendoci nel modo più semplice e diretto delle librerie disponibili come supporto alle comunicazioni vie rete.

Uso di HTTP library
### <a name="testmovesusinghttp"></a>**TestMovesUsingHttp[¶**](#testmovesusinghttp "Permalink to this headline")**
In javasrc/it/unibo/virtualRobot2023/clients

|<p>TestMovesUsingHttp.java</p><p>Esegue mosse di base del robot inviando vie HTTP comandi espressi in in [cril](#comandi-base-per-il-robot-in-cril)</p>|<p>Key point: Richiesta sincrona.</p><p>Richiede 1 thread.</p>|
| :- | :- |

Dal punto di vista ‘sistemistico’ osserviamo che:

- Il codice di comunicazione è scritto completamente dal progettista dell’applicazione che usa la libreria *org.apache.http*.
- La gestione delle risposte JSON viene eseguita utilizzando la libreria [json-simple](https://code.google.com/archive/p/json-simple/).

Dal punto di vista ‘applicativo’, osserviamo che:

- Il chiamante esegue comandi nella forma *request-response*.
- Un eventuale [Messaggio di stato](#messaggio-di-stato) inviato da WEnv non viene percepito.
- E’ possibile interrompere la esecuzione di una mossa inviando il comando [alarm](#alarm).
- Una mossa che termina prima del tempo indicato nel comando (per interruzione o [Collision](#collision), restituisce la risposta {"endmove":"false", "move":"MINFO" } introdotta in [Interazione sincrona](#interazione-sincrona).

L’interazione mediante HTTP viene realizzata da un client org.apache.http.client invocato entro una procedura di utilità definita come segue:
#### <a name="callhttp"></a>**callHTTP[¶**](#callhttp "Permalink to this headline")**
private  JSONParser simpleparser = new JSONParser();

protected JSONObject callHTTP( String crilCmd )  {

JSONObject jsonEndmove = null;

` `try {

`   `StringEntity entity = new StringEntity(crilCmd);

`   `HttpUriRequest httppost = RequestBuilder.post()

.setUri(new URI(URL))

.setHeader("Content-Type", "application/json")

.setHeader("Accept", "application/json")

.setEntity(entity)

.build();

`   `CloseableHttpResponse response = httpclient.execute(httppost);

`   `String jsonStr = EntityUtils.toString( response.getEntity() );

`   `jsonEndmove    = (JSONObject) simpleparser.parse(jsonStr);

` `} catch(Exception e){...}

` `return jsonEndmove;

}

Operazioni di test
#### <a name="testmovesusinghttp-tests"></a>**TestMovesUsingHttp tests[¶**](#testmovesusinghttp-tests "Permalink to this headline")**

|![_images/testHttpForwardOk.png][_images/MoveForwardOk.png]|<p>Muovi in avanti senza collisioni</p><p>public void doForward() {</p><p>`  `String forwardcmd="{\"robotmove\":\"moveForward\",\"time\":\"1000\"}";</p><p>`  `CommUtils.waitTheUser("PUT ROBOT in HOME  and hit");</p><p>`  `JSONObject result = callHTTP(  forwardcmd  );</p><p>`  `CommUtils.outblue("moveForward endmove=" + result);</p><p>}</p>|
| :- | :- |
|![_images/testHttpForwardCollision.png][_images/MoveForwardOk.png]|<p>Muovi in avanti con collisione</p><p>public void doCollision() {</p><p>`  `String forwardcmd="{\"robotmove\":\"moveForward\",\"time\":\"3000\"}";</p><p>`  `JSONObject result = callHTTP(  forwardcmd  );</p><p>`  `CommUtils.outblue("moveForward endmove=" + result);</p><p>}</p>|
|![_images/testHttpForwardHalted.png][_images/MoveForwardOk.png]|<p>Muovi in avanti e ferma prima della fine</p><p>public void doHalt() {</p><p>`  `String forwardcmd="{\"robotmove\":\"moveForward\",\"time\":\"3000\"}";</p><p>`  `sendAlarmAfter(1000);</p><p>`  `JSONObject result = callHTTP(  forwardcmd );</p><p>`  `CommUtils.outblue("moveForward endmove=" + result);</p><p>}</p>|
##### <a name="invio-di-alarm"></a>**Invio di alarm[¶**](#invio-di-alarm "Permalink to this headline")**
Per inviare al robot un comando *halt* (messaggio [alarm](#alarm)) si possono usare diversi modi:

- Usare [NaiveGui.html](#naivegui-html).
- Lanciare una nuova applicazione Java.
- Attivare (come fatto in doHalt) un Thread interno a [TestMovesUsingHttp](#testmovesusinghttp), ma **senza riusare** lo stesso metodo [callHTTP](#callhttp):

  protected void sendAlarmAfter( int time ){

  `  `new Thread(){

  `    `protected JSONObject mycallHTTP( String crilCmd )  {

       ...

  `    `}

  `    `public void run(){

  `      `CommUtils.delay(time);

  `      `JSONObject result = mycallHTTP(  haltcmd  );

  `      `CommUtils.outgreen("sendAlarmAfter result=" + result);

  `    `}

  `  `}.start();

  }
#### <a name="automatictesthttp"></a>**AutomaticTestHTTP[¶**](#automatictesthttp "Permalink to this headline")**
Grazie a JUnit, possiamo includere le azioni di [TestMovesUsingHttp tests](#testmovesusinghttp-tests) all’interno di una unità di testing che le esegue in modo automatizzato.

import org.junit.Before;

import org.junit.Test;

import org.apache.http.impl.client.CloseableHttpClient;

import org.apache.http.impl.client.HttpClients;

public class AutomaticTestHTTP {

private CloseableHttpClient httpclient;

` `@Before

` `public void init(){

`     `httpclient = HttpClients.createDefault();

`     `//ASSUNZIONE: prima di ogni test il robot deve essere in HOME

` `}

` `@Test

` `public void doForward() {

`     `JSONObject result = callHTTP(  forwardcmd  );

`     `assert( result.get("endmove").equals("true") && result.get("move").equals("moveForward")) ;

`     `//BACK TO HOME

`     `JSONObject result1 = callHTTP(  backwardcmd  );

`     `assert( result1.get("move").toString().contains("moveBackward")) ;

` `}

}

Uso di javax.websocket library
### <a name="testmovesusingws"></a>**TestMovesUsingWs[¶**](#testmovesusingws "Permalink to this headline")**
In javasrc/it/unibo/virtualRobot2023/clients

|<p>TestMovesUsingWs.java</p><p>Esegue mosse di base del robot inviando via WebSocket comandi espressi in [cril](#comandi-base-per-il-robot-in-cril)</p>|<p>Key point: Richiesta asincrona</p><p>Richiede 4 thread, a causa della libreria javax.websocket.</p>|
| :- | :- |

Dal punto di vista ‘sistemistico’, osserviamo che:

- Il codice di comunicazione è scritto completamente dal progettista dell’applicazione, che utilizza la libreria javax.websocket (vedi anche [I WebSocket Comunicazione Asincrona Full-Duplex Per Il Web](http://losviluppatore.it/i-websocket-comunicazione-asincrona-full-duplex-per-il-web/))

*Annotazioni*

Gli eventi del ciclo di vita dell’endpoint WebSocket sono gestiti mediante [Annotazioni](Annotazioni.html#annotazioni) secondo lo schema che segue:

@ClientEndpoint  //La classe viene trattata come un client WebSocket

public class ... {

`   `@OnOpen //chiamato quando si avvia una nuova connessione WebSocket

`   `public void onOpen(Session userSession){ ... }

`   `@OnMessage //chiamato quando  arriva un  messaggio di stato

`   `public void onMessage(String message){ ... }

`   `@OnError //chiamato quando si verifica un problema con la comunicazione

`   `public void onError (sessione di sessione, errore lanciabile){...}

`   `@Chiudi //chiamato alla chiusura della connessione WebSocket

`   `public void onClose(Session userSession,CloseReason reason){...}

` `}

Dal punto di vista ‘applicativo’, osserviamo che:

- Il chiamante esegue concettualmente una *fire-and-forget*.
- Un eventuale [Messaggio di stato](#messaggio-di-stato) viene ‘iniettato’ nell’applicazione tramite una chiamata al metodo annotato con @OnMessage.
- E’ possibile interrompere la esecuzione di una mossa inviando il comando [alarm](#alarm).
- Per una mossa che termina prima del tempo indicato nel comando (per interruzione o [Collision](#collision), vengono percepiti due [Messaggio di stato](#messaggio-di-stato): {"collision":MOVEID, "target":"..." } e {"endmove":"false", "move":"MINFO" }

L’interazione mediante WebSocket viene realizzata dalla libreria javax.websocket invocato entro una procedura di utilità definita come segue:
#### <a name="callws"></a>**callWS[¶**](#callws "Permalink to this headline")**
//Fase iniziale di connessione

private Session userSession  = null;

WebSocketContainer container = ContainerProvider.getWebSocketContainer();

container.connectToServer(this, new URI("ws://"+addr));

@OnOpen

public void onOpen(Session userSession) {

`     `this.userSession = userSession;

}

protected void callWS(String msg )   {

`    `this.userSession.getAsyncRemote().sendText(msg);

`    `// try {

`    `//   this.userSession.getBasicRemote().sendText(msg);

`    `//   //synch version: blocks until the message has been transmitted

`    `// }catch(Exception e) {}

}
#### <a name="testmovesusingws-onmesage"></a>**TestMovesUsingWs onMesage[¶**](#testmovesusingws-onmesage "Permalink to this headline")**
@OnMessage

public void onMessage(String message)  {

`    `long duration = System.currentTimeMillis() - startTime;

`    `try {

`      `//{"collision":"true ","move":"..."} or

`      `//{"sonarName":"sonar2","distance":19,"axis":"x"}

`      `JSONObject jsonObj = (JSONObject) simpleparser.parse(message);

`      `if (jsonObj.get("endmove") != null ) {

`            `boolean endmove = jsonObj.get("endmove").toString().equals("true");

`            `String  move    = (String) jsonObj.get("move") ;

`            `CommUtils.outgreen("TestMovesUsingWs | endmove:" + endmove + " move="+move);

`            `if( count++ == 0 ) { //test

`                `callWS(  turnleftcmd  );CommUtils.delay(350);

`                `callWS(  turnrightcmd  );

`            `}

`      `}else if (jsonObj.get("collision") != null ) {

`            `String move   = (String) jsonObj.get("collision");

`            `String target = (String) jsonObj.get("target");

`            `//halt();

`            `//senza halt il msg {"endmove":"false","move":"moveForward-collision"} arriva dopo T

`      `}else if (jsonObj.get("sonarName") != null ) { //JUST TO SHOW ...

`            `String sonarName = (String) jsonObj.get("sonarName") ;

`            `String distance  = jsonObj.get("distance").toString();

`      `}

`    `} catch (Exception e) {

`            `CommUtils.outred("onMessage " + message + " " +e.getMessage());

`    `}

}
#### <a name="testmovesusingws-tests"></a>**TestMovesUsingWs tests[¶**](#testmovesusingws-tests "Permalink to this headline")**
|![_images/testHttpForwardOk.png][_images/MoveForwardOk.png]|<p>Muovi in avanti senza collisioni</p><p>public void doForward() {</p><p>`  `String forwardcmd="{\"robotmove\":\"moveForward\",\"time\":\"1000\"}";</p><p>`  `CommUtils.waitTheUser("PUT ROBOT in HOME  and hit");</p><p>`  `startTime = System.currentTimeMillis();</p><p>`  `callWS(  forwardcmd  );</p><p>`  `CommUtils.waitTheUser("Hit to terminate doForward");</p><p>}</p><p></p><p>//MESSAGGI DI STATO</p><p>onMessage:{"endmove":"true","move":"moveForward"} duration=1055</p>|
| :- | :- |
|![_images/testWsForwardCollision.png][_images/MoveForwardOk.png] ![_images/testWsForwardCollisionWithHalt.png][_images/MoveForwardOk.png]|<p>Muovi in avanti con collisione</p><p>public void doCollision() {</p><p>`  `String forwardcmd   = "{\"robotmove\":\"moveForward\", \"time\":\"3000\"}";</p><p>`  `startTime = System.currentTimeMillis();</p><p>`  `callWS(  forwardcmd  );</p><p>}</p><p></p><p>//MESSAGGI DI STATO</p><p>//SENZA halt in onMessage relativo a collision</p><p>onMessage:{"collision":"moveForward","target":"wallDown"} duration=841</p><p>onMessage:{"endmove":"false","move":"moveForward-collision"} duration=3019</p><p></p><p>//CON halt in onMessage relativo a collision</p><p>onMessage:{"collision":"moveForward","target":"wallDown"} duration=1256</p><p>onMessage:{"endmove":"false","move":"moveForward-collision"} duration=1310</p>|
|![_images/testWsNotallowed.png][_images/MoveForwardOk.png]|<p>Muovi in avanti e ruota a sinistra prima della fine</p><p>public void doNotAllowed() {</p><p>`  `String forwardcmd="{\"robotmove\":\"moveForward\",\"time\":\"1200\"}";</p><p>`  `callWS(  forwardcmd  );</p><p>`  `CommUtils.delay(400);</p><p>`  `callWS(  turnleftcmd  );</p><p>}</p><p></p><p>//MESSAGGI DI STATO</p><p>onMessage:{"endmove":"false","move":"turnLeft\_notallowed (asynch)"}</p><p>onMessage:{"endmove":"true","move":"moveForward"}</p>|
|![_images/testHttpForwardHalted.png][_images/MoveForwardOk.png]|<p>Muovi in avanti e ferma prima della fine</p><p>public void doHalt() {</p><p>`  `String forwardcmd="{\"robotmove\":\"moveForward\",\"time\":\"3000\"}";</p><p>`  `callWS(  forwardcmd  );</p><p>`  `CommUtils.delay(1000);</p><p>`  `callWS(  haltcmd  );</p><p>}</p><p></p><p>//MESSAGGI DI STATO</p><p>onMessage:{"endmove":"false","move":"moveForward-interrupted"} duration=1028</p>|
#### <a name="automatictestws"></a>**AutomaticTestWs[¶**](#automatictestws "Permalink to this headline")**
La definizione di una classe di testing automatizzato analoga [AutomaticTestHTTP](#automatictesthttp) potrebbe non essere di immediata realizzazione. Il lettore è inviatato a individuare i problemi che sorgono a livello di scrittura del codice, sui quali noi torneremo in seguito.

TODO: Deliverable BoundaryWalk
## <a name="deliverableboundarywalk"></a>**DeliverableBoundaryWalk[¶**](#deliverableboundarywalk "Permalink to this headline")**
1. Compilare la sezione Problem analysis del [template2023](_static/templateToFill.html) ponendo in luce i key-points del problema della interazione con il VirtualRobot23.

   In Lab2: Collegarsi a <https://app.wooclap.com/VR1DISI>

1. Ridenominare il file BoundaryWalkCognomeNome e inserirlo nella directory Deliverables del proprio GIT repo privato.
1. Compilare la sezione Project descrivendo una strategia di comandi (sincroni e/o asincroni) che induce il robot:
   1. Req1 : a percorrere (una volta) il bordo perimetrale della stanza rappresentata ne [La scena di WEnv](#la-scena-di-wenv)

In Lab2: Collegarsi a <https://app.wooclap.com/VR1DISI>

In futuro il robot dovrà anche:

1. Req2 : fermarsi di 5 sec quando rilevato dal sonar
1. Compilare la sezione Testing descrivendo una strategia di collaudo.
### <a name="vr-problem"></a>**VR problem[¶**](#vr-problem "Permalink to this headline")**
Problema: Due protocolli => due codifiche

E’ possibile evitarlo?
### [**Table of Contents**](index.html)
- [VirtualRobot23]()
  - [La scena di WEnv](#la-scena-di-wenv)
  - [Come attivare WEnv](#come-attivare-wenv)
  - [Come interagire con WEnv](#come-interagire-con-wenv)
    - [NaiveGui.html](#naivegui-html)
      - [NaiveGui page](#naivegui-page)
      - [Esperimento misto](#esperimento-misto)
    - [Comandi-base per il robot in cril](#comandi-base-per-il-robot-in-cril)
      - [alarm](#alarm)
    - [Interazione sincrona](#interazione-sincrona)
    - [Collision](#collision)
    - [Stato iniziale del virtualrobot](#stato-iniziale-del-virtualrobot)
      - [Esempi di comandi al VirtualRobot23](#esempi-di-comandi-al-virtualrobot23)
    - [Interazione asincrona](#interazione-asincrona)
      - [Messaggio di stato](#messaggio-di-stato)
  - [WEnv - note di implementazione](#wenv-note-di-implementazione)
    - [Architettura di WEnv](#architettura-di-wenv)
  - [Programmi naive](#programmi-naive)
    - [TestMovesUsingHttp](#testmovesusinghttp)
      - [callHTTP](#callhttp)
      - [TestMovesUsingHttp tests](#testmovesusinghttp-tests)
        - [Invio di alarm](#invio-di-alarm)
      - [AutomaticTestHTTP](#automatictesthttp)
    - [TestMovesUsingWs](#testmovesusingws)
      - [callWS](#callws)
      - [TestMovesUsingWs onMesage](#testmovesusingws-onmesage)
      - [TestMovesUsingWs tests](#testmovesusingws-tests)
      - [AutomaticTestWs](#automatictestws)
  - [DeliverableBoundaryWalk](#deliverableboundarywalk)
    - [VR problem](#vr-problem)
#### **Previous topic**
[Principi](Principi.html "previous chapter")
#### **Next topic**
[Applicazione1](Applicazione1.html "next chapter")
### **This Page**
- [Show Source](_sources/VirtualRobot23.rst.txt)
###
### **Navigation**
- [index]
- [next] |
- [previous] |
- [iss23 1.0 documentation](index.html) »
- VirtualRobot23

© Copyright 2022, Antonio Natali. Created using [Sphinx](https://www.sphinx-doc.org/) 4.4.0. 

[index]: genindex.html "General Index"
[next]: Applicazione1.html "Applicazione1"
[previous]: Principi.html "Principi"
[_images/logicInteraction.PNG]: Aspose.Words.0f8cc616-aff2-41a9-900f-a61b8017cdd2.001.png
[_images/NaiveGui.PNG]: Aspose.Words.0f8cc616-aff2-41a9-900f-a61b8017cdd2.002.png
[_images/MoveForwardOk.png]: Aspose.Words.0f8cc616-aff2-41a9-900f-a61b8017cdd2.003.png
[_images/WenvArch.PNG]: Aspose.Words.0f8cc616-aff2-41a9-900f-a61b8017cdd2.004.png
