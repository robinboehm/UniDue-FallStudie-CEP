package de.uni.due.paluno.casestudy.cep.wsapi;

import de.uni.due.paluno.casestudy.cep.cosm.COSMWebSocketEngine;
import de.uni.due.paluno.casestudy.cep.cosm.event.COSMWebSocketEvent;
import de.uni.due.paluno.casestudy.cep.cosm.event.COSMWebSocketListener;
import de.uni.due.paluno.casestudy.cep.esper.eventProcessing.factory.ESPERTriggerFactory;
import de.uni.due.paluno.casestudy.cep.events.command.WaypointTemperatureDumper;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;

@WebServlet(name = "WorldWebSocketServlet", urlPatterns = {"/world"})
public class WorldWebSocketServlet extends WebSocketServlet implements COSMWebSocketListener {

    private List<MessageInbound> connections = new CopyOnWriteArrayList<MessageInbound>();
    private static final World world = new MockWorld();

    public WorldWebSocketServlet() throws IOException, ExecutionException, InterruptedException {

        ESPERTriggerFactory etf = new ESPERTriggerFactory();
        etf.addToConfig(new WaypointTemperatureDumper());
        etf.createTriggers();

        COSMWebSocketEngine engine = createEngine();
        engine.addAdListener(etf);
        engine.addAdListener(this);
        engine.start();
    }

    private COSMWebSocketEngine createEngine() throws IOException, ExecutionException, InterruptedException {
        List<String> list = new LinkedList<String>();
        list.add("/feeds/80263");
        list.add("/feeds/42055");

        COSMWebSocketEngine engine = new COSMWebSocketEngine(list);
        engine.start();
        return engine;
    }

    @Override
    protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {

        return new MyMessageInbound();
    }

    @Override
    public void handleWebSocketEvent(COSMWebSocketEvent e) {
        for (MessageInbound inbound : connections) {
            CharBuffer buffer = CharBuffer.wrap(world.toString());
            try {
                inbound.getWsOutbound().writeTextMessage(buffer);
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
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
