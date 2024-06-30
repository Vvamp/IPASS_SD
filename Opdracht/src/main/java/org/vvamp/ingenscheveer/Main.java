package org.vvamp.ingenscheveer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.database.models.AisData;
import org.vvamp.ingenscheveer.models.json.AisSignal;
import org.vvamp.ingenscheveer.security.authentication.LoginManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@WebListener
public class Main implements ServletContextListener {

    private ScheduledExecutorService executorService;
    public static StorageController storageController= new LocalFileStorageController("cache.json");
    public static LoginManager loginManager = new LoginManager();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        loginManager.populate();

//        List<AisSignal> signals = storageController.load();
//        DatabaseStorageController.getDatabaseAisController().SaveAllAisSignals(signals);

        List<AisData> newSignals = DatabaseStorageController.getDatabaseAisController().getAllAisData(); // precache

        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.submit(() -> {
            WebSocketClient.main(new String[0]);
        });
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseStorageController.getDatabaseTokenController().removeAllTokens();

        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
