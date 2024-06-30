package org.vvamp.ingenscheveer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.database.DatabaseUserController;
import org.vvamp.ingenscheveer.models.schedule.ScheduleTask;
import org.vvamp.ingenscheveer.models.schedule.TaskType;
import org.vvamp.ingenscheveer.security.authentication.User;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleTest {


    @BeforeEach
    public void init(){
        DatabaseStorageController.setUseTest(true);
        DatabaseStorageController.getDatabaseScheduleController().deleteAllScheduleTasks();
    }

    @Test
    public void test_ScheduleTask_TaskAdded(){
        User randomuser = new User("testuser", "testpassword", "testrole");
        int id= DatabaseStorageController.getDatabaseUserController().findUserId(randomuser.getName());
        User dbUser = DatabaseStorageController.getDatabaseUserController().findUserById(id);

        ScheduleTask task = new ScheduleTask(LocalDateTime.ofEpochSecond(0,0 , ZoneOffset.UTC), LocalDateTime.ofEpochSecond(10,0,ZoneOffset.UTC), dbUser, TaskType.Dienst);

        List<ScheduleTask> userTasks =  DatabaseStorageController.getDatabaseScheduleController().getTaskForUser(id);
        assertEquals(1, userTasks.size(), "Only one task should be added when creating a new task");
        ScheduleTask dbTask  = userTasks.get(0);
        assertEquals(task, dbTask, "The task should be the same");

    }

    @Test
    public void test_ScheduleTask_TaskNotAdded(){
        User randomuser = new User("testuser", "testpassword", "testrole");
        int id= DatabaseStorageController.getDatabaseUserController().findUserId(randomuser.getName());
        User dbUser = DatabaseStorageController.getDatabaseUserController().findUserById(id);

        List<ScheduleTask> userTasks =  DatabaseStorageController.getDatabaseScheduleController().getTaskForUser(id);
        assertEquals(0, userTasks.size(), "Task shouldn't be in the database when it's not added");

    }
    @Test
    public void test_ScheduleTask_TaskDeleted(){
        User randomuser = new User("testuser", "testpassword", "testrole");
        int id= DatabaseStorageController.getDatabaseUserController().findUserId(randomuser.getName());
        User dbUser = DatabaseStorageController.getDatabaseUserController().findUserById(id);

        ScheduleTask task = new ScheduleTask(LocalDateTime.ofEpochSecond(0,0 , ZoneOffset.UTC), LocalDateTime.ofEpochSecond(10,0,ZoneOffset.UTC), dbUser, TaskType.Dienst);

        DatabaseStorageController.getDatabaseScheduleController().deleteScheduleTask(task.getUuid());
        List<ScheduleTask> userTasks =  DatabaseStorageController.getDatabaseScheduleController().getTaskForUser(id);
        assertEquals(0, userTasks.size(), "Task should no longer exist when deleted");
    }
}
