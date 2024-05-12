package unibo.sprint3;

import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import unibo.basicomm23.coap.CoapConnection;

public class StatusObserver {
    private final Corebusiness logic;

    private final CoapConnection coap_connection_robot;
    private final CoapConnection coap_connection_coldroom;
    private final CoapConnection coap_connection_trolley;
    private final CoapConnection coap_connection_coldstorage;


    public StatusObserver(Corebusiness logic) {

        this.logic = logic;

        //---------------coap connection
        //Per ricevere informazioni dal robotpos uso coap perché il qak lo usa
        this.coap_connection_robot = new CoapConnection("127.0.0.1:8020", "ctxbasicrobot/robotpos");
        coap_connection_robot.observeResource(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                System.out.println("--OBSERVER BASICROBOT-- onLoad: CoapResponse=  " + response.getResponseText());
                logic.gotUpdateFromCSS(response.getResponseText(), "basicrobot");
            }
            @Override
            public void onError() { System.out.println("-- ServiceConnectionManager -- observeResource, error"); }
        });

        this.coap_connection_coldroom = new CoapConnection("127.0.0.1:8055", "ctxcoldstorageservice/coldroom");
        coap_connection_coldroom.observeResource(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                System.out.println("--OBSERVER COLDROOM-- onLoad: CoapResponse=  " + response.getResponseText());
                logic.gotUpdateFromCSS(response.getResponseText(), "coldroom");
            }
            @Override
            public void onError() { System.out.println("-- ServiceConnectionManager -- observeResource, error"); }
        });

        this.coap_connection_trolley = new CoapConnection("127.0.0.1:8055", "ctxcoldstorageservice/transporttrolley");
        coap_connection_trolley.observeResource(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                System.out.println("--OBSERVER TROLLEY-- onLoad: CoapResponse=  " + response.getResponseText());
                logic.gotUpdateFromCSS(response.getResponseText(), "trolley");
            }
            @Override
            public void onError() { System.out.println("-- ServiceConnectionManager -- observeResource, error"); }
        });

        this.coap_connection_coldstorage = new CoapConnection("127.0.0.1:8055", "ctxcoldstorageservice/coldstorageservice");
        coap_connection_coldstorage.observeResource(new CoapHandler() {
            @Override
            public void onLoad(CoapResponse response) {
                System.out.println("--OBSERVER COLDSTORAGE-- onLoad: CoapResponse=  " + response.getResponseText());
                logic.gotUpdateFromCSS(response.getResponseText(), "coldstorageservice");
            }
            @Override
            public void onError() { System.out.println("-- ServiceConnectionManager -- observeResource, error"); }
        });
    }
}
