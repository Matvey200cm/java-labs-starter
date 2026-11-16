package edu.course.lab03;


public class TaskPrioritizer {
    private final PriorityPolicy policy;

    public TaskPrioritizer(PriorityPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Policy cannot be null");
        }
        this.policy = policy;
    }

    public ProjectTask pickMoreImportant(ProjectTask first, ProjectTask second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Tasks cannot be null");
        }
        int firstPriority = policy.calculatePriority(first);
        int secondPriority = policy.calculatePriority(second);
        return firstPriority >= secondPriority ? first : second;
    }
}
