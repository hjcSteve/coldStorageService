//Access point per avviare la serviceaccessgui
package com.unibo.serviceaccessgui;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class ServiceAccessGuiMain {
    public static void main(String[] args) {SpringApplication.run(ServiceaccessguiApplication.class, args);}
}

/*SpringBoot viene avviato e comincia il processo di auto-configurazione + scansione componenti
 * web contener avviato: ascolta le richieste HTTP sulla porta configurata
 * app gestisce le richieste HTTP inviate dagli utenti attraverso la web interface
 * interagendo con la GUI e con il controller.
*/