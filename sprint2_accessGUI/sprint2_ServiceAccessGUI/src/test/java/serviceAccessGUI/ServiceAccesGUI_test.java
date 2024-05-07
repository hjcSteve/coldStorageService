
package serviceAccessGUI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import unibo.basicomm23.interfaces.IApplMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServiceAccesGUI_test {

    public final ClientConnectionManager clientConnectionManager = new ClientConnectionManager();
    // manterrà le sessioni con i Client connessi, intercettando le richieste e
    // inoltrando le risposte
    public final String clientPath = "accessgui";

    public final ServiceAccessGUI accessgui = new ServiceAccessGUI(clientConnectionManager);

    ServiceConnectionManager serviceConnectionManager = new ServiceConnectionManager(accessgui, "handler",
            "coldstorageservice");

    /*
     * 
     * //MESSAGI TRA IL TRANSPORT TROLLEY E il COLD STORAGE
     * //manda trolley a servire un truck (da idle a serving)
     * Dispatch dischargeTrolley : dischargeTrolley(TICKETID)
     * 
     * //Dispatch truckArrived : truckArrived(TICKETID)
     * Dispatch chargeTaken : chargeTaken(TICKETID)
     * 
     * 
     * //Quando il truck driver fa una richiesta di store con un certo peso
     * Request storerequest : storerequest(FW)
     * Reply ticketAccepted :ticketAccepted(TICKETNUMBER) for storerequest
     * Reply replyTicketDenied: ticketDenied(ARG) for storerequest
     * 
     * 
     * //Quando il truck driver arriva e chiede di scaricare immettendo il
     * TICKETNUMBER
     * Request dischargefood : dischargefood(TICKETNUM) //insertticket
     * Reply replyChargeTaken : replyChargeTaken(ARG) for dischargefood
     * Reply replyTicketExpired: replyTicketExpired(ARG) for dischargefood
     */

    // metodo per fare il parsing, stesso metot di accessgui
    public String getValue(String msg) {
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }

    // @Test
    // public void test_dischargefood_ok() {
    // System.out.println("---Test partito: test_dischargefood_ok");
    // String reqid1="1"; //request id
    // String weight="10";
    // IApplMessage response = serviceConnectionManager.storerequest(weight,reqid1);
    // System.out.println("test_dischargefood_ok--
    // storerequest("+weight+","+reqid1+")");
    // boolean res = response.isReply() &&
    // response.msgContent().contains("ticketAccepted");
    // Assertions.assertTrue(res);
    // System.out.println("test_dischargefood_ok-- ticketAccepted ok
    // (res="+res+")");
    // String ticket = accessgui.getValue(response.msgContent());
    // response = serviceConnectionManager.dischargefood(ticket,reqid1);
    // System.out.println("test_dischargefood_ok--
    // dischargefood("+weight+","+reqid1+")");
    // System.out.println(response.msgContent());
    // boolean res2 = response.isReply() &&
    // response.msgContent().contains("replyChargeTaken");
    // Assertions.assertTrue(res2);
    // System.out.println("test_dischargefood_ok--replyChargeTaken(res="+res2+")");
    // }
    // @Test
    // public void test_dischargefood_ko() {
    // System.out.println("---Test partito: test_dischargefood_ko");
    // String reqid1="1"; //request id
    // String ticketNum="9999"; //request id
    // IApplMessage response =
    // serviceConnectionManager.dischargefood(ticketNum,reqid1);
    // boolean res = response.isReply() &&
    // response.msgContent().contains("ticketDenied");
    // Assertions.assertTrue(res);
    // System.out.println("test_dischargefood_ko: ticketDenied(res="+res+")");
    // }
    // @Test
    // public void test_storerequest_ok() throws InterruptedException {
    // System.out.println("---Test partito: test_storerequest_ok");
    // String reqid1="1"; //request id
    // String weight="10";
    // IApplMessage response = serviceConnectionManager.storerequest(weight,reqid1);
    // boolean res = response.isReply() &&
    // response.msgContent().contains("ticketAccepted");
    // Assertions.assertTrue(res);
    // System.out.println("test_storerequest_ok: ticket num(res="+res+")");
    // System.out.println("test_storerequest_ok: ticket num= " +
    // accessgui.getValue(response.msgContent()));
    // }

    // @Test
    // public void test_storerequest_ko() throws InterruptedException {
    // System.out.println("---Test partito: test_storerequest_ko");
    // String reqid1="1"; //request id
    // IApplMessage response = serviceConnectionManager.storerequest("10",reqid1);
    // Assertions.assertTrue(response.isReply() &&
    // response.msgContent().contains("ticketAccepted"));
    // System.out.println("test_storerequest_ko: ticketAccepted");
    // String ticket = accessgui.getValue(response.msgContent());
    // System.out.println("test_storerequest_ko: ticket num= " + ticket);
    // Thread.sleep(2222);//attesa per vedere se il ticket è valido
    // response = serviceConnectionManager.storerequest(ticket,reqid1);
    // boolean res = response.isReply() &&
    // response.msgContent().contains("replyTicketExpired");
    // Assertions.assertTrue(res);
    // System.out.println("test_storerequest_ko: ticket replyTicketExpired
    // (res="+res+")");
    // }

    // @Test
    // public void test_coda_tickets() throws InterruptedException {

    // System.out.println("---Test partito:test_coda_tickets");
    // String reqid1="1"; //request id
    // System.out.println("test_coda_tickets: storerequest numero 1");
    // IApplMessage resp1 = serviceConnectionManager.storerequest("10",reqid1);
    // boolean res = resp1.isReply() &&
    // resp1.msgContent().contains("ticketAccepted");
    // Assertions.assertTrue(res);
    // String ticket1 = accessgui.getValue(resp1.msgContent());
    // System.out.println("test_coda_tickets reception ticket1: " + ticket1);

    // System.out.println("test_coda_tickets: storerequest numero 2");
    // String reqid2="2"; //request id
    // String weight="10";
    // IApplMessage second_store_response =
    // serviceConnectionManager.storerequest(weight,reqid2);
    // boolean res2 = resp1.isReply() &&
    // resp1.msgContent().contains("ticketAccepted");
    // Assertions.assertTrue(res2);
    // String ticket2 = accessgui.getValue(second_store_response.msgContent());
    // System.out.println("test_coda_tickets reception ticket2: " + ticket2);

    // System.out.println("test_coda_tickets: storerequest numero 1");
    // System.out.println("test_coda_tickets: dischargefood con ticket1");
    // IApplMessage replyCharge1 =
    // serviceConnectionManager.dischargefood(ticket1,reqid1);
    // boolean res3 = replyCharge1.isReply() &&
    // replyCharge1.msgContent().contains("replyChargeTaken");
    // Assertions.assertTrue(res3);
    // System.out.println(("test_coda_tickets: dischargefood con ticket1 ->
    // replyChargeTaken"));

    // System.out.println("test_coda_tickets: storerequest numero 2");
    // System.out.println("test_coda_tickets: dischargefood con ticket2");
    // IApplMessage replyCharge2 =
    // serviceConnectionManager.dischargefood(ticket2,reqid2);
    // boolean res4 = replyCharge2.isReply() &&
    // replyCharge2.msgContent().contains("replyTicketExpired");
    // Assertions.assertTrue(res4);
    // System.out.println(("test_coda_tickets: dischargefood con ticket1 ->
    // replyTicketExpired"));
    // }

}
