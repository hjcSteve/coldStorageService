package unibo.sprint3;

import alice.tuprolog.Int;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Corebusiness {
    private final ClientConnectionManager clientConnectionManager;
    private final StatusObserver statusObserver;

    private Float current_weight = 0f;
    private Float reserved_weight = 0f;
    private Float max_weight = 0f;

    private int denied_ticket = 0;


    public Corebusiness(ClientConnectionManager clientConnectionManager) {
        this.clientConnectionManager = clientConnectionManager;
        this.clientConnectionManager.setManager(this);
        this.statusObserver = new StatusObserver(this);
    }

    public void gotUpdateFromCSS(String msg, String observed_res) {
        //System.out.println("-- ServiceStatusGUI --Received: " + msg);

        switch (observed_res){
            case "basicrobot": {
                String match= getPosition(msg);
                System.out.println("-- ServiceStatusGUI --basicrobot updated position ");
                this.clientConnectionManager.sendToClient("robotpos/" + match);
            }

            case "coldroom":{
                updateCount(msg);
            }

            case "coldstorageservice": {
                if(msg.contains("rejected")) {
                    String match = getValue(msg);
                    if (match.equals("")) {
                        System.out.println("Empty match for coldstorage");
                        //return;
                    }
                    System.out.println("-- ServiceStatusGUI --ticket Rejected: ");
                    denied_ticket = Integer.parseInt(match);
                    this.clientConnectionManager.sendToClient("rejected/" + denied_ticket);
                }
            }

            case "trolley" : {
                if (msg.contains("trolleystate")) {
                String match =getValue(msg);
                if(match.equals("")) {
                    //return;
                    System.out.println("Empty match for trolley");
                }

                System.out.println("-- ServiceStatusGUI --robot State Updated!: ");
                this.clientConnectionManager.sendToClient("robotstate/" +match);
                }
            }

            default:{}
        }
    }
    public String getValue(String msg) {
        Pattern pattern = Pattern.compile("\\d");
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {return matcher.group(0);}
        return "";
    }
    public String getPosition(String msg) {
        Pattern pattern = Pattern.compile("\\(\\d+,+\\d\\)");
        Matcher matcher = pattern.matcher(msg);
        if (matcher.find()) {return matcher.group(0);}
        return "";
    }
    private void updateCount(String data) {
        Pattern pattern = Pattern.compile("\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(data);
        List<Float> matches = new ArrayList<>();
        System.out.println("-- ServiceStatusGUI --Update: " + data);
        while (matcher.find()) {
            matches.add(Float.parseFloat(matcher.group()));
        }
        if (matches.size() == 3) {
            this.current_weight = matches.get(0);
            this.reserved_weight = matches.get(1);
            this.max_weight = matches.get(2);
            System.out.println("-- ServiceStatusGUI --current_weight: " + current_weight + " reserved_weight: " + reserved_weight + " max_weight: " + max_weight);
            this.clientConnectionManager.sendToClient("update/" + current_weight + ", " + reserved_weight + ", " + max_weight);
        }
    }
}
