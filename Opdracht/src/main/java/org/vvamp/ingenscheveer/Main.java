package org.vvamp.ingenscheveer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@WebListener
public class Main implements ServletContextListener {

    private ScheduledExecutorService executorService;
    public static StorageController storageController= new LocalFileStorageController("cache.json");

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.submit(() -> {
            WebSocketClient.main(new String[0]);
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
