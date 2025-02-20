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
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@WebListener
public class Main implements ServletContextListener {

    private ScheduledExecutorService executorService;
    private ScheduledExecutorService gcTimer;

    public static LoginManager getLoginManager() {
        return loginManager;
    }

    private static LoginManager loginManager = new LoginManager();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.setProperty("user.timezone", "UTC");
        ZoneId.systemDefault().normalized();

        loginManager.populate();
        List<AisData> newSignals = DatabaseStorageController.getDatabaseAisController().getAllAisData(); // precache

//        executorService = Executors.newSingleThreadScheduledExecutor();
//        executorService.submit(() -> {
//            WebSocketClient.main(new String[0]);
//        });

        gcTimer = Executors.newSingleThreadScheduledExecutor();
        gcTimer.scheduleAtFixedRate(this::pruneSignals,0,15, TimeUnit.MINUTES);
    }

    private void pruneSignals() {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        DatabaseStorageController.getDatabaseTokenController().removeAllTokens();

        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
