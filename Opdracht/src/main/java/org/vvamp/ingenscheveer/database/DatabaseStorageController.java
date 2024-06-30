package org.vvamp.ingenscheveer.database;

public class DatabaseStorageController {
    private static boolean useTest = false;
    private static final DatabaseAisController databaseAisController=new DatabaseAisController("AisSignals");
    private static final DatabaseAisController databaseAisControllerTest =new DatabaseAisController("AisSignalsTest");

    private static final DatabaseDrukteController databaseDrukteController=new DatabaseDrukteController();

    private static final DatabaseUserController databaseUserController=new DatabaseUserController();

    private static final DatabaseScheduleController databaseScheduleController=new DatabaseScheduleController();

    private static final DatabaseTokenController databaseTokenController=new DatabaseTokenController();

    public static DatabaseDrukteController getDatabaseDrukteController() {
        return databaseDrukteController;
    }

    public static DatabaseUserController getDatabaseUserController() {
        return databaseUserController;
    }

    public static DatabaseScheduleController getDatabaseScheduleController() {
        return databaseScheduleController;
    }

    public static DatabaseTokenController getDatabaseTokenController() {
        return databaseTokenController;
    }

    public static void setUseTest(boolean doUsetest) {
        useTest = doUsetest;
    }

    public static DatabaseAisController getDatabaseAisController() {
        if(useTest){
            return databaseAisControllerTest;
        }else{
            return databaseAisController;
        }
    }
}
