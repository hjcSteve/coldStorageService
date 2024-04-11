package tickets;

import java.util.HashMap;

public class TicketManager {

    private final HashMap<String, Ticket> Tickets;
    private final Integer TIMEOUT;
    private final String FORMAT;
    private String waiting = "";
    private String working = "";


    public TicketManager(Integer timeout, String format) {
        TIMEOUT = timeout;
        FORMAT = format;
        Tickets = new HashMap<>();
    }

    public HashMap<String, Ticket> getTickets() {
        return Tickets;
    }

    public Ticket getTicket(String timestamp) {
        return Tickets.get(timestamp);
    }

    public Ticket newTicket(Float weight) {
        Ticket ticket = new Ticket(weight, TIMEOUT, FORMAT);
        Tickets.put(ticket.getId(), ticket);
        return ticket;
    }

    public void remove(String id) {
        Tickets.remove(id);
    }

    public void remove(Ticket ticket) {
        Tickets.remove(ticket.getId());
    }

    public String waitingNowWorking() {
        working = waiting;
        this.stopWaiting();
        return working;
    }

    public boolean isWorking() {
        return !working.equals("");
    }

    public boolean isWaiting() {
        return !waiting.equals("");
    }

    public void stopWorking() {
        working = "";
    }

    public void stopWaiting() {
        waiting = "";
    }

    public Ticket waitingTicket() {
        return this.getTicket(waiting);
    }

    public String getWaiting() {
        return waiting;
    }

    public void setWaiting(String waiting) {
        this.waiting = waiting;
    }

    public Ticket workingTicket() {
        return this.getTicket(working);
    }

    public String getWorking() {
        return working;
    }

    public void setWorking(String working) {
        this.working = working;
    }
}
