package org.vvamp.ingenscheveer.database;

public class DatabaseStorageController {
    private static final DatabaseAisController databaseAisController=new DatabaseAisController();

    private static final DatabaseDrukteController databaseDrukteController=new DatabaseDrukteController();

    private static final DatabaseUserController databaseUserController=new DatabaseUserController();

    private static final DatabaseScheduleController databaseScheduleController=new DatabaseScheduleController();

    private static final DatabaseTokenController databaseTokenController=new DatabaseTokenController();

    public static DatabaseAisController getDatabaseAisController() {
        return databaseAisController;
    }
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


}
