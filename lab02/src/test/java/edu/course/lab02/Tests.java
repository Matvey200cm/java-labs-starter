package edu.course.lab02;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Tests {
    @Test
    void testSuccessfulWithdraw() {
        BankAccount account = new BankAccount(100);
        account.withdraw(30);
        assertEquals(70, account.getBalance());
    }

    @Test
    void testWithdrawExceedsBalance() {
        BankAccount account = new BankAccount(50);
        assertThrows(IllegalArgumentException.class, () -> {
            account.withdraw(100);
        });
    }

    @Test
    void testProjectTaskCreation() {
        ProjectTask task = new ProjectTask("1", "Test Task", 5);
        assertEquals("1", task.getId());
        assertEquals("Test Task", task.getTitle());
        assertEquals(5, task.getEstimatedHours());
        assertEquals(TaskStatus.CREATED, task.getStatus());
        assertFalse(task.isDone());
    }

    @Test
    void testChangeStatusToDone() {
        ProjectTask task = new ProjectTask("1", "Test Task", 5);
        task.changeStatus(TaskStatus.IN_PROGRESS);
        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());
        assertFalse(task.isDone());

        task.changeStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus());
        assertTrue(task.isDone());
    }

    @Test
    void testIncreaseEstimate() {
        ProjectTask task = new ProjectTask("1", "Test Task", 5);
        task.increaseEstimate(3);
        assertEquals(8, task.getEstimatedHours());
    }

    @Test
    void testProjectTaskNullId() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProjectTask(null, "Test Task", 5);
        });
    }

    @Test
    void testProjectTaskEmptyTitle() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProjectTask("1", "", 5);
        });
    }

    @Test
    void testProjectTaskNegativeHours() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProjectTask("1", "Test Task", -5);
        });
    }

    @Test
    void testIncreaseEstimateNegative() {
        ProjectTask task = new ProjectTask("1", "Test Task", 5);
        assertThrows(IllegalArgumentException.class, () -> {
            task.increaseEstimate(-3);
        });
    }
}
