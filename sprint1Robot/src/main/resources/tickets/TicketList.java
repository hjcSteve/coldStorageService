package tickets;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class TicketList {
    private List<Ticket> tickets;
    private int lastNumber;
    private long expirationTime;

    public TicketList(long expirationTime) {
        tickets = new ArrayList<>();
        //fetchTicktesformFIle 
        lastNumber = 0;
        this.expirationTime = expirationTime;
    }

    public synchronized Ticket createTicket(int kgToStore) {
        Ticket ticket = new Ticket();
        ticket.setKgToStore(kgToStore);
        lastNumber++;
        ticket.setTicketNumber(lastNumber);
        ticket.setStatus(0);
        //ticket.setTicketSecret(generateSecret(7));
        ticket.setTimestamp(Instant.now().toEpochMilli());
        tickets.add(ticket);
        return ticket;
    }

    public synchronized Ticket getTicket(int ticketNumber) {
        for (Ticket ticket : tickets) {
            if (ticket.getTicketNumber() == ticketNumber) {
                return ticket;
            }
        }
        return null;
    }

    public synchronized void  removeTicket(int ticketNumber) {
        //cycle this way to avoid concurrent oeration exception on the last element
        for (int i=0; i<tickets.size();i++) {
            Ticket ticket=tickets.get(i);
            if (ticket.getTicketNumber() == ticketNumber) {
                tickets.remove(ticket);
            }
        }
    }

    public synchronized int getTotalKgToStore() {
        int result = 0;
        for (Ticket ticket : tickets) {
            if((!this.isExpired(ticket) && ticket.getStatus()==0)||ticket.getStatus()==1){
                result += ticket.getKgToStore();

            }
        }
        return result;
    }

    public synchronized boolean isExpired(Ticket ticket) {
        if (ticket == null) return true;
        return Instant.now().toEpochMilli() - ticket.getTimestamp() >= this.expirationTime;
    }
    public String toString(){
        String result="";
        for(Ticket ticket: tickets){
            result+="ticketNumber:"+ticket.getTicketNumber()+"ticketSecret:"+"kgToStore:"+ticket.getKgToStore()+"timestamp:"+ticket.getTimestamp()+"\n";
        }
        return result;
    }

}
