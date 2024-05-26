package serviceAccessGUI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;




public class ClientConnectionManager extends AbstractWebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();
    private final Map<String, WebSocketSession> pendingRequests = new HashMap<>();

    private ServiceAccessGUI accessGui;


    protected void setManager(ServiceAccessGUI accessGui) {
        this.accessGui = accessGui;
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
        pendingRequests.remove(session);
        System.out.println("-- ClientConnectionManager -- client session removed: " + session);

        super.afterConnectionClosed(session, status);
    }

    // Messages sent from client
    @Override
    protected void handleTextMessage(WebSocketSession session,
                                     TextMessage message) {
        String msg = message.getPayload();
        System.out.println("-- ClientConnectionManager -- msg received: " + msg);

        this.accessGui.gotReqFromClient(msg, newRequest(session));
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

    protected void sendToClient(String message, String requestId) {
        System.out.println("-- ClientConnectionManager --  msg sent to client: " + message);
        WebSocketSession session = this.pendingRequests.get(requestId);
        try {
            session.sendMessage(new TextMessage(message));
        } catch (Exception e) {
            System.out.println("-- ClientConnectionManager -- error sendToClient, msg: " + message + ", to session: " + session);
        }
        this.pendingRequests.remove(requestId);
    }

    //new session request
    protected String newRequest(WebSocketSession session) {
        String requestId = "req" + session.getId().substring(session.getId().lastIndexOf('-') + 1);
        System.out.println("-- ClientConnectionManager -- newRequest: " + requestId);
        pendingRequests.put(requestId, session);
        return requestId;
    }
}
