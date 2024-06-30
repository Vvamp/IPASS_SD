package org.vvamp.ingenscheveer.database;

public class DatabaseStorageController {
    private static final DatabaseAisController databaseAisController = new DatabaseAisController("AisSignals");
    private static final DatabaseAisController databaseAisControllerTest = new DatabaseAisController("AisSignalsTest");
    private static final DatabaseDrukteController databaseDrukteController = new DatabaseDrukteController();
    private static final DatabaseUserController databaseUserController = new DatabaseUserController("Users");
    private static final DatabaseUserController databaseUserControllerTest = new DatabaseUserController("UsersTest");
    private static final DatabaseScheduleController databaseScheduleController = new DatabaseScheduleController("ScheduleTasks");
    private static final DatabaseScheduleController databaseScheduleControllerTest = new DatabaseScheduleController("ScheduleTasksTest");
    private static final DatabaseTokenController databaseTokenController = new DatabaseTokenController();
    private static boolean useTest = false;

    public static DatabaseDrukteController getDatabaseDrukteController() {
        return databaseDrukteController;
    }

    public static DatabaseUserController getDatabaseUserController() {
        if (useTest) {
            return databaseUserControllerTest;
        } else {
            return databaseUserController;
        }    }

    public static DatabaseScheduleController getDatabaseScheduleController() {
        if (useTest) {
            return databaseScheduleControllerTest;
        } else {
            return databaseScheduleController;
        }
    }

    public static DatabaseTokenController getDatabaseTokenController() {
        return databaseTokenController;
    }

    public static DatabaseAisController getDatabaseAisController() {
        if (useTest) {
            return databaseAisControllerTest;
        } else {
            return databaseAisController;
        }
    }

    public static void setUseTest(boolean doUsetest) {
        useTest = doUsetest;
    }
}
