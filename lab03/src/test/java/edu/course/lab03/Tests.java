package edu.course.lab03;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Tests {

    private ProjectTask task(String id, int hours) {
        return new ProjectTask(id, "Task " + id, hours);
    }

    // --- ProjectTask ---

    @Test
    void testProjectTaskCreation() {
        ProjectTask task = task("1", 5);
        assertEquals("1", task.getId());
        assertEquals("Task 1", task.getTitle());
        assertEquals(TaskStatus.CREATED, task.getStatus());
        assertEquals(5, task.getEstimatedHours());
    }

    @Test
    void testProjectTaskChangeStatus() {
        ProjectTask task = task("1", 5);
        task.changeStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, task.getStatus());
    }

    // --- EffortPriorityPolicy ---

    @Test
    void testEffortPolicyReturnsEstimatedHours() {
        EffortPriorityPolicy policy = new EffortPriorityPolicy();
        ProjectTask task = task("1", 8);
        assertEquals(8, policy.calculatePriority(task));
    }

    @Test
    void testEffortPolicyPrefersLargerTask() {
        EffortPriorityPolicy policy = new EffortPriorityPolicy();
        ProjectTask large = task("large", 12);
        ProjectTask small = task("small", 4);
        assertTrue(policy.calculatePriority(large) > policy.calculatePriority(small));
    }

    @Test
    void testEffortPolicyRejectsNullTask() {
        EffortPriorityPolicy policy = new EffortPriorityPolicy();
        assertThrows(IllegalArgumentException.class, () -> policy.calculatePriority(null));
    }

    // --- StatusPriorityPolicy ---

    @Test
    void testStatusPolicyRanksInProgressAboveCreated() {
        StatusPriorityPolicy policy = new StatusPriorityPolicy();
        ProjectTask inProgress = task("1", 5);
        inProgress.changeStatus(TaskStatus.IN_PROGRESS);
        ProjectTask created = task("2", 5);
        assertTrue(policy.calculatePriority(inProgress) > policy.calculatePriority(created));
    }

    @Test
    void testStatusPolicyRanksCreatedAboveDone() {
        StatusPriorityPolicy policy = new StatusPriorityPolicy();
        ProjectTask created = task("1", 5);
        ProjectTask done = task("2", 5);
        done.changeStatus(TaskStatus.DONE);
        assertTrue(policy.calculatePriority(created) > policy.calculatePriority(done));
    }

    @Test
    void testStatusPolicyRejectsNullTask() {
        StatusPriorityPolicy policy = new StatusPriorityPolicy();
        assertThrows(IllegalArgumentException.class, () -> policy.calculatePriority(null));
    }

    // --- TaskPrioritizer ---

    @Test
    void testPrioritizerPicksLargerEffortTask() {
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        ProjectTask small = task("1", 2);
        ProjectTask large = task("2", 20);
        assertSame(large, prioritizer.pickMoreImportant(small, large));
        assertSame(large, prioritizer.pickMoreImportant(large, small));
    }

    @Test
    void testPrioritizerPicksMoreImportantStatus() {
        TaskPrioritizer prioritizer = new TaskPrioritizer(new StatusPriorityPolicy());
        ProjectTask done = task("1", 5);
        done.changeStatus(TaskStatus.DONE);
        ProjectTask inProgress = task("2", 5);
        inProgress.changeStatus(TaskStatus.IN_PROGRESS);
        assertSame(inProgress, prioritizer.pickMoreImportant(done, inProgress));
    }

    @Test
    void testPrioritizerSupportsPolicyReplacement() {
        ProjectTask inProgressSmall = task("1", 2);
        inProgressSmall.changeStatus(TaskStatus.IN_PROGRESS);
        ProjectTask createdLarge = task("2", 20);

        TaskPrioritizer byEffort = new TaskPrioritizer(new EffortPriorityPolicy());
        assertSame(createdLarge, byEffort.pickMoreImportant(inProgressSmall, createdLarge));

        TaskPrioritizer byStatus = new TaskPrioritizer(new StatusPriorityPolicy());
        assertSame(inProgressSmall, byStatus.pickMoreImportant(inProgressSmall, createdLarge));
    }

    @Test
    void testPrioritizerReturnsFirstOnEqualPriority() {
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        ProjectTask first = task("1", 10);
        ProjectTask second = task("2", 10);
        assertSame(first, prioritizer.pickMoreImportant(first, second));
    }

    @Test
    void testPrioritizerDoesNotModifyTasks() {
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        ProjectTask first = task("1", 2);
        first.changeStatus(TaskStatus.IN_PROGRESS);
        ProjectTask second = task("2", 20);
        prioritizer.pickMoreImportant(first, second);
        assertEquals(TaskStatus.IN_PROGRESS, first.getStatus());
        assertEquals(2, first.getEstimatedHours());
        assertEquals(TaskStatus.CREATED, second.getStatus());
        assertEquals(20, second.getEstimatedHours());
    }

    @Test
    void testPrioritizerRejectsNullPolicy() {
        assertThrows(IllegalArgumentException.class, () -> new TaskPrioritizer(null));
    }

    @Test
    void testPrioritizerRejectsNullTask() {
        TaskPrioritizer prioritizer = new TaskPrioritizer(new EffortPriorityPolicy());
        assertThrows(IllegalArgumentException.class, () -> prioritizer.pickMoreImportant(null, task("2", 5)));
        assertThrows(IllegalArgumentException.class, () -> prioritizer.pickMoreImportant(task("1", 5), null));
    }
}
