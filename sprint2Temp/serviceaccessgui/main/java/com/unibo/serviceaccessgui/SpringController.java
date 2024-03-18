
package com.unibo.serviceaccessgui;

import java.io.*;
import java.net.Socket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


/*Il Controller gestisce le richieste HTTP inviate dall'utente attraverso l'interfaccia grafica.*/
@Controller
public class SpringController {
    @Value("${spring.application.name}")
    String appName;

     /*Stabilisce una connessione TCP: IP_CS` e `PORT_CS`, ip e porta cold storage service
      * per comunicazione tra interfaccia e backend
     */
    String IP_CS = "127.0.0.1";
    int PORT_CS = 8038;

    Socket client;
    BufferedReader reader;
    BufferedWriter writer;


    /*annotazioni  `@GetMapping` e `@PostMapping` per definire le route */
    /*Route:
     * mapping tra requests HTTP e  handler methods definiti nei controller. 
     * Ogni route specifica un url e metodo HTTP (GET, POST,..) per cui quella route è valida. 
     * scopo: da richieste HTTP a metodi del controller
     */


     /*route getmapping: request to `ColdStorageService` per sapere quanto spazio è disponibile (freespace)*/
         /// HOMEPAGE
    @GetMapping("/")
    public String homePage(Model model) {
        String msg = "msg(freespace,request,webgui,coldstorageservice,freespace(ARG),1)\n";
        try {
            //---request to ColdStorageService
            connectToColdStorageService();
            writer.write(msg);
            writer.flush();
            System.out.println("msg sent");

            //---response from ColdStorageService
            String response = reader.readLine();

            //popolo modello con i dati ricevuti: freespace_available
            model.addAttribute("freespace_available", getParameters(response)[0]);
            model.addAttribute("arg", appName);
            return "serviceaccessgui";

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    /*route postmapping "/insert_ticket"": request to `ColdStorageService` per inserire un ticket;
     * Parametri: numero ticket da inserire
     * RIsposta: successo o fallimento dell'operazione + popolare il modello
    */
    @PostMapping("/insert_ticket")
    public String insert_ticket(Model model, @RequestParam(name = "ticket_number") String ticket_number) {
        System.out.println(ticket_number);

        String msg = "msg(send_ticket,request,webgui,coldstorageservice,send_ticket(" +ticket_number+"),1)\n";
        try {
            //---request to ColdStorageService
            connectToColdStorageService();
            writer.write(msg);
            writer.flush();
            System.out.println("msg sent");
            //---response from ColdStorageService
            String response = reader.readLine();

            //popolo modello con i dati ricevuti: response ok or not
            model.addAttribute("msgtype", getMsgType(response));
            return "insert_ticket";

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

  /*route postmapping "/storefood": request per depositare il carico nel cold storage;
     * Parametri: numero di kg fw da depositare
     * RIsposta: if successo, risposta=ticket accepted --> storefood
     *           else risposta=error --> error msg                
    */

    /// STOREFOOD USECASE
    @PostMapping("/store_food")
    public String store_food(Model model, @RequestParam(name = "fwg") String fw) {
        // storefood request
        String msg = "msg(store_food,request,webgui,coldstorageservice,store_food("+fw+"),1)\n";
        String response;
        try {
             //---request to ColdStorageService
            connectToColdStorageService();
            writer.write(msg);
            writer.flush();
            System.out.println("msg sent");


            //---response from ColdStorageService
            response = reader.readLine();
            System.out.println("msg received");
            System.out.println(response);
            String msgType = getMsgType(response);
            System.out.println(msgType);

            if (msgType.equals("ticket_accepted")) {
                String[] parameters = getParameters(response);
                model.addAttribute("ticket_number", parameters[0]);
                return "store_food";
            } else {
                return "error";
            }

        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

    }


    //connect_css: TCP Connection btw webgui and cold storage service
    // getParameters, getMsgType: parsing response from cold storage service
        private void connect_to_css() throws IOException {

            client = new Socket(IP_CSS, PORT_CSS);
            writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        }
        private String[] getParameters(String msg) {
            return msg.split("\\(")[2].split("\\)")[0].split(",");
        }
        private String getMsgType(String msg) {
            return msg.split("\\(")[1].split(",")[0];
        }
    


}



