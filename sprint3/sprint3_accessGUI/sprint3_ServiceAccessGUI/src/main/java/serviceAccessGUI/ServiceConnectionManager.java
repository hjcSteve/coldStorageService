package serviceAccessGUI;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.basicomm23.coap.CoapConnection;
import unibo.basicomm23.interfaces.IApplMessage;
import unibo.basicomm23.interfaces.Interaction;
import unibo.basicomm23.tcp.TcpClientSupport;
import unibo.basicomm23.utils.CommUtils;

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


public class ServiceConnectionManager {
    private final ServiceAccessGUI accessGUI;
    private final Interaction tcp_connection;
    private final String senderId;
    private final String destActor;

    public ServiceConnectionManager(ServiceAccessGUI accessGUI, String senderId, String destActor) {
        this.accessGUI = accessGUI;

        this.senderId = senderId; //Spring Server
        this.destActor = destActor; //CSService

        //---------------tcp connection
        try {

            //Context ctxcoldstorageservice ip [host="localhost" port=8055]  //Qactor coldstorageservice
            //context coldstorage
            tcp_connection = TcpClientSupport.connect("localhost", 8055, 10);
            System.out.println("--ServiceConnectionManager-- tcp session ok: " + tcp_connection);
            //-- ServiceConnectionManager -- Stabilita connessione tcp: unibo.basicomm23.tcp.TcpConnection@689c51cb
        } catch (Exception e) {
            System.out.println("--ServiceConnectionManager-- tcp session error");
            throw new RuntimeException(e);
            
        }

        //---------------coap connection 
        //Per ricevere informazioni dal CSS uso coap perché il qak lo usa
        CoapConnection coap_connection = new CoapConnection("127.0.0.1:8055", "ctxcoldstorageservice/coldroom");
        coap_connection.observeResource(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                System.out.println("--ServiceConnectionManager-- onLoad: CoapResponse=  " + response.getResponseText());
                accessGUI.gotRespFromCSS(response.getResponseText(), "");
            }
            @Override
            public void onError() { System.out.println("-- ServiceConnectionManager -- observeResource, error"); }
        });
    }


    private void sendToCSS(IApplMessage message, String requestId) {
        IApplMessage response = null;
        try {
            System.out.println("--ServiceConnectionManager-- sendToCSS, msg sent: " + message);
            response = tcp_connection.request(message);
            System.out.println("--ServiceConnectionManager-- response arrived: " + response);
        } catch (Exception e) {
            System.out.println("--ServiceConnectionManager-- sendToCSS error");
        }
        if (response != null) {
            System.out.println("--ServiceConnectionManager--  response=" + response.msgContent());
            accessGUI.gotRespFromCSS(response.msgContent(), requestId);
        }
    }

    protected void storerequest(String weight, String requestId) {
        String msgId="storerequest"; //storerequest
        IApplMessage message = CommUtils.buildRequest(senderId, msgId, "storerequest(" + weight + ")", destActor);
        System.out.println("--ServiceConnectionManager-- senderId=" + senderId + ", destActor=" + destActor + ", message=" + message);
        sendToCSS(message, requestId);
    }

    protected void dischargefood(String ticket, String requestId) {
        String msgId="dischargefood"; //dischargefood
        IApplMessage message = CommUtils.buildRequest(senderId, msgId, "dischargefood(" + ticket + ")", destActor);
        System.out.println("--ServiceConnectionManager-- senderId=" + senderId + " destActor=" + destActor + " message=" + message);
        sendToCSS(message, requestId);
    }
}
