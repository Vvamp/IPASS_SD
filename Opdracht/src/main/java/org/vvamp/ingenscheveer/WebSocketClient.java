package org.vvamp.ingenscheveer;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class WebSocketClient {
    private static final String URL = "wss://stream.aisstream.io/v0/stream";
    private static final int RECONNECT_DELAY = 10; // Seconds
    private static final int PERIODIC_RECONNECT = 2 * 60 * 60; // 2 hours in seconds

    private static CountDownLatch latch;
    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false)
            .build();
    private static final ArrayList<AisSignal> shipMessages = new ArrayList<>();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static WebSocketContainer container;
    private static Session session;

    public static void main(String[] args) {
        container = ContainerProvider.getWebSocketContainer();
        connect();

        // Schedule periodic reconnect every 2 hours
        scheduler.scheduleAtFixedRate(WebSocketClient::reconnect, PERIODIC_RECONNECT, PERIODIC_RECONNECT, TimeUnit.SECONDS);
    }

    private static void connect() {
        latch = new CountDownLatch(1);
        try {
            session = container.connectToServer(WebSocketClient.class, URI.create(URL));
            System.out.println("Connected to " + URL);
            latch.await(); // Wait for the session to close
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            retryConnection();
        }
    }

    private static void retryConnection() {
        System.out.println("Retrying connection in " + RECONNECT_DELAY + " seconds...");
        sleep(RECONNECT_DELAY);
        connect();
    }

    private static void reconnect() {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
            connect();
        } catch (Exception e) {
            System.err.println("Reconnection failed: " + e.getMessage());
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket opened.");
        String message = "{\"APIKey\":\"642615ebb912e3975369877e2166ff2f7466ddcc\",\"BoundingBoxes\":[[[50,5],[53,8]]],\"FiltersShipMMSI\":[\"244780155\"], \"FilterMessageTypes\": [\"PositionReport\"]}";
        sendMessage(session, message);
    }

    @OnMessage
    public void onMessage(String message) {
        // Handle text messages if needed
    }

    @OnMessage
    public void onBinaryMessage(ByteBuffer message) {
        String json = new String(message.array(), StandardCharsets.UTF_8);
        try {
            AisSignal data = objectMapper.readValue(json, AisSignal.class);
            shipMessages.add(data);
            DatabaseStorageController.getDatabaseAisController().writeAisData(data);
            pingHealthcheck();
        }catch(Exception e) {
            System.err.println("Failed to deserialize message: " + e.getMessage());
            reconnect();

        }
    }

    private void pingHealthcheck(){
        String healthCheckUrl = "https://healthcheck.vincentvansetten.com/ping/20a8b713-20b1-4d9c-911e-d30300611d85";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(healthCheckUrl))
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.err.println("Failed to get HC ping response: " + e.getMessage());
        }

    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        System.out.println("WebSocket closed: " + closeReason);
        latch.countDown();
        retryConnection(); // Reconnect automatically
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("WebSocket error: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void sendMessage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            System.err.println("Failed to send message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
