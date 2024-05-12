package unibo.sprint3;

import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;


@Configuration
@EnableWebSocket
//WebSocketConfiguration:

/*IDEA
 * il nostro Server Spring deve gestire una serie di comunicazioni:
 *  - Da una parte, deve connettersi ai vari client (pagine web) e scambiare i messaggi;
 *  - dall’altra, deve mandare e ricevere messaggi al/dal CSService.
 *
 * Comunicazione con i Client:
 * Preso spunto dal prof, usando le websocket come nel suo esempio
 *
 * Comunicazione con il CSService:
 * Per parlare con il CSService sono possibili due diverse tipologie di comunicazione:
 *      - una è il semplice invio di richieste e ricezione di relative risposte (per cui usiamo tcp)
 *      - L’altra è “osservare” il CSService e ricevere gli update:
 *          In questo modo il CSService è un oggetto assolutamente indipendente dalla Gui:
 *          => il CSService presenta solo:
 *                             1) una interfaccia per le richieste (IApplMessage)
 *                             2) una serie di parametri osservabili con un Observer
 *          => per questa seconda funzionalità si usa Coap
 */
public class WebSocketConf implements WebSocketConfigurer {

    public final ClientConnectionManager clientConnectionManager = new ClientConnectionManager();
    //manterrà le sessioni con i Client connessi, intercettando le richieste e inoltrando le risposte
    public final String clientPath = "accessgui";

    public final Corebusiness logic = new Corebusiness(clientConnectionManager);

    public WebSocketConf() {
        clientConnectionManager.setManager(logic);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(clientConnectionManager, clientPath).setAllowedOrigins("*");
    }
}
