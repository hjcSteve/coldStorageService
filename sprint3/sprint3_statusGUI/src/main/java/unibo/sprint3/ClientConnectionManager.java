package unibo.sprint3;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;



public class ClientConnectionManager extends AbstractWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private Corebusiness logic;


    protected void setManager(Corebusiness logic) {
        this.logic = logic;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("-- ClientConnectionManager -- client session ok: " + session);

        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session,
                                      CloseStatus status) throws Exception {
        sessions.remove(session);
        System.out.println("-- ClientConnectionManager -- client session removed: " + session);

        super.afterConnectionClosed(session, status);
    }

    protected void sendToAll(String message) {
        System.out.println("-- ClientConnectionManager -- msg sent to all clients: " + message);
        for (WebSocketSession session : sessions) {
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                System.out.println("-- ClientConnectionManager -- error in sendToAll");
            }
        }
    }

    protected void sendToClient(String message) {
        //System.out.println("-- ClientConnectionManager --  msg sent to client: " + message);
        if (sessions.size() > 0) {
            WebSocketSession session = sessions.get(0);
            try {
                session.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                System.out.println("-- ClientConnectionManager -- error sendToClient, msg: " + message + ", to session: " + session);
            }
        }
    }
}