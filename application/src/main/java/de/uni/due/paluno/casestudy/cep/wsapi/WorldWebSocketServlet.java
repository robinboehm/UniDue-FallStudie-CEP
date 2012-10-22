package de.uni.due.paluno.casestudy.cep.wsapi;

import de.uni.due.paluno.casestudy.cep.model.World;
import de.uni.due.paluno.casestudy.cep.model.mock.MockWorld;
import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@WebServlet(name = "WorldWebSocketServlet", urlPatterns = {"/world"})
public class WorldWebSocketServlet extends WebSocketServlet {

    private List<StreamInbound> connections = new CopyOnWriteArrayList<StreamInbound>();
    private static final World world = new MockWorld();

    public WorldWebSocketServlet() {
    }

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {

        return new MyMessageInbound();
    }

    private final class MyMessageInbound extends MessageInbound {
        @Override
        protected void onBinaryMessage(ByteBuffer message) throws IOException {

        }

        @Override
        protected void onTextMessage(CharBuffer message) throws IOException {

        }

        @Override
        protected void onOpen(WsOutbound outbound) {
            connections.add(this);
            CharBuffer buffer = CharBuffer.wrap(world.toString());
            try {
                outbound.writeTextMessage(buffer);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

    }
}
