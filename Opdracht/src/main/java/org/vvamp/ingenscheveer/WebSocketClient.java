package org.vvamp.ingenscheveer;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.models.json.AisSignal;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.websocket.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint

public class WebSocketClient {
    private static CountDownLatch latch;
    private static ObjectMapper objectMapper = JsonMapper.builder().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false).build();

    public static ArrayList<AisSignal> shipMessages = new ArrayList<AisSignal>();
    @OnOpen
    public void onOpen(Session session) {
//        System.out.println("Connected to server");
        String message = "{\"APIKey\":\"3c9a8ccfe72d2907179fdbc0bb5a68811af1fdd7\",\"BoundingBoxes\":[[[50,5],[53,8]]],\"FiltersShipMMSI\":[\"244780155\"], \"FilterMessageTypes\": [\"PositionReport\"]}";
//        String message = "{\"APIKey\":\"3c9a8ccfe72d2907179fdbc0bb5a68811af1fdd7\",\"BoundingBoxes\":[[[50,5],[53,8]]],\"FiltersShipMMSI\":[\"244780155\"]}";
        sendMessage(session, message);
    }

    @OnMessage
    public void onMessage(String message) {
//        System.out.println("Received message: " + message);
    }

    @OnMessage
    public void onBinaryMessage(ByteBuffer message) {
        String json = new String(message.array(), StandardCharsets.UTF_8);


        try {
            AisSignal data = objectMapper.readValue(json, AisSignal.class);
            shipMessages.add(data);
//            System.out.println("Received known message: " + json);

//            Main.storageController.save(data);
            DatabaseStorageController.getDatabaseAisController().writeAisData(data);
        } catch (Exception e) {
            System.err.println("Failed to deserialize message: " + e.getMessage());
//            e.printStackTrace();
//            System.out.println("Unknown message received: ");
//            System.out.println(json);
        }
    }
    @OnClose
    public void onClose(Session session, javax.websocket.CloseReason closeReason) {
//        System.out.println("Session closed: " + closeReason);
        latch.countDown(); // Signal that the connection is closed
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
//        System.err.println("Error occurred: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void sendMessage(Session session, String message) {
        try {
//            System.out.println("Sending message: " + message);
            session.getBasicRemote().sendText(message);
//            System.out.println("Message sent successfully");
        } catch (Exception e) {
//            System.err.println("Failed to send message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        latch = new CountDownLatch(1);
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "wss://stream.aisstream.io/v0/stream";
        try {
//            System.out.println("Connecting to " + uri);
            container.connectToServer(WebSocketClient.class, URI.create(uri));
            latch.await(); // Wait for the connection to close
//            System.out.println("Connection closed");
        } catch (Exception e) {
//            System.err.println("Failed to connect to server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
