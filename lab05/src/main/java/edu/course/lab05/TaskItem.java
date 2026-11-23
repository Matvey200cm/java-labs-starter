package edu.course.lab05;

final class TaskItem {
    private final String id;
    private final String title;
    private final String category;
    private final int estimatedHours;
    private final TaskStatus status;

    public TaskItem(String id, String title, String category, int estimatedHours, TaskStatus status) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.estimatedHours = estimatedHours;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    public TaskStatus getStatus() {
        return status;
    }
}
