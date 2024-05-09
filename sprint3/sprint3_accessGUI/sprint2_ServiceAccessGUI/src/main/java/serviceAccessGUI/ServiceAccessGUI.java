package serviceAccessGUI;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/*

//MESSAGI TRA IL TRANSPORT TROLLEY E il COLD STORAGE 
//manda trolley a servire un truck (da idle a serving)
Dispatch dischargeTrolley : dischargeTrolley(TICKETID)

//Dispatch truckArrived : truckArrived(TICKETID)
Dispatch chargeTaken : chargeTaken(TICKETID)


//Quando il truck driver fa una richiesta di store con un certo peso
Request storerequest       : storerequest(FW)
Reply ticketAccepted :ticketAccepted(TICKETNUMBER) for storerequest
Reply replyTicketDenied: ticketDenied(ARG) for storerequest


//Quando il truck driver arriva e chiede di scaricare immettendo il TICKETNUMBER
Request dischargefood  : dischargefood(TICKETNUM) //insertticket
Reply replyChargeTaken : replyChargeTaken(ARG) for dischargefood
Reply replyTicketExpired:  replyTicketExpired(ARG) for dischargefood
 */
   /*
     Comunicazione con il CSService:
 *      Per parlare con il CSService sono possibili due diverse tipologie di comunicazione:
 *      - una è il semplice invio di richieste e ricezione di relative risposte (per cui usiamo tcp)
 *
 *      - L’altra è “osservare” il CSService e ricevere gli update:
 *          In questo modo il CSService è un oggetto assolutamente indipendente dalla Gui:
 *          => il CSService presenta solo:
 *                             1) una interfaccia per le richieste (IApplMessage)
 *                             2) una serie di parametri osservabili con un Observer
 *          => per questa seconda funzionalità si usa Coap
     *
     */


public class ServiceAccessGUI {

    private final ClientConnectionManager clientConnectionManager;
    private final ServiceConnectionManager serviceConnectionManager;

    private Float current_weight = 0f;
    private Float reserved_weight = 0f;
    private Float max_weight = 0f;

    public ServiceAccessGUI(ClientConnectionManager clientConnectionManager) {
        this.clientConnectionManager = clientConnectionManager;
        this.clientConnectionManager.setManager(this);
        this.serviceConnectionManager = new ServiceConnectionManager(this, "handler", "coldstorageservice");
    }

    public void gotRespFromCSS(String msg, String requestId) {
        System.out.println("-- ServiceAccessGUI --Received: " + msg);
        if (requestId.equals("")) {
            updateCount(msg);
        } else {
            String value = getValue(msg);
            if (!value.equals("")) {
                if (msg.contains("ticketAccepted")) { //store accepted
                    String ticket=value;
                    this.clientConnectionManager.sendToClient("ticket/" + ticket, requestId);
                    System.out.println("-- ServiceAccessGUI --ticketAccepted, ticket= " + ticket);

                } else if (msg.contains("ticketDenied")) {
                    this.clientConnectionManager.sendToClient("error/ticketDenied", requestId);
                    System.out.println("-- ServiceAccessGUI --ticketDenied: " + value);

                } else if (msg.contains("replyChargeTaken")) { 
                    System.out.println("-- ServiceAccessGUI --notifyMsg, message=replyChargeTaken ");
                    this.clientConnectionManager.sendToClient("notify/" + "replyChargeTaken", requestId);

                } else if (msg.contains("replyTicketExpired")) {
                    this.clientConnectionManager.sendToClient("error/replyTicketExpired", requestId);
                    System.out.println("-- ServiceAccessGUI --replyTicketExpired: " + value);
                }
            }
        }
    }
    public String getValue(String msg) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {return matcher.group(1);}
        return "";
    }
    private void updateCount(String data) {
        Pattern pattern = Pattern.compile("\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(data);
        List<Float> matches = new ArrayList<>();
        System.out.println("-- ServiceAccessGUI --Update: " + data);
        while (matcher.find()) {
            matches.add(Float.parseFloat(matcher.group()));
        }
        if (matches.size() == 3) {
            this.current_weight = matches.get(0);
            this.reserved_weight = matches.get(1);
            this.max_weight = matches.get(2);
            System.out.println("-- ServiceAccessGUI --current_weight: " + current_weight + " reserved_weight: " + reserved_weight + " max_weight: " + max_weight);
            this.clientConnectionManager.sendToAll("update/" + current_weight + ", " + reserved_weight + ", " + max_weight);
        }
    }
    public void gotReqFromClient(String msg, String requestId) {
        System.out.println("-- ServiceAccessGUI --Received: " + msg);
        String[] parts = msg.split("/");
        String message = parts[0];
        String payload = parts[1];
        System.out.println("-- ServiceAccessGUI --RECEIVED MESSAGE:" + message);
        switch (message) {
            case "storerequest": //storerequest
                String weight=payload;
                System.out.println("-- ServiceAccessGUI --storerequest, weight= " + weight);
                this.serviceConnectionManager.storerequest(weight, requestId);
                break;
            case "dischargefood": 
                String ticket=payload;
                System.out.println("-- ServiceAccessGUI --dischargefood, ticket= " + ticket);
                this.serviceConnectionManager.dischargefood(ticket, requestId);
                break;
            default:
                break;
        }
    }
}