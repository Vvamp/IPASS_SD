package org.vvamp.ingenscheveer.webservices;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

@Path("events")
public class SseTestResource {
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void getServerSentEvents(@Context SseEventSink eventSink, @Context Sse sse, @Context HttpServletResponse response) {
        if (eventSink == null) {
            System.out.println("eventSink is null!");
            return;
        }

        // Set proper SSE headers
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("X-Accel-Buffering", "no");  // For NGINX
        response.setHeader("Connection", "keep-alive");

        System.out.println("SSE connection opened");

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    if (eventSink.isClosed()) {
                        System.out.println("Client disconnected, stopping SSE.");
                        return;
                    }

                    final OutboundSseEvent event = sse.newEventBuilder()
                            .name("message-to-client")
                            .data(String.class, "Hello world " + i + "!")
                            .build();

                    eventSink.send(event).toCompletableFuture().get();
                    System.out.println("SSE sent: " + i);

                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    eventSink.close();
                    System.out.println("SSE connection closed.");
                } catch (Exception ignored) {
                    System.out.println("Close connection error");
                }
            }
        }).start();
    }

}
