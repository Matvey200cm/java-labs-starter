package edu.course.lab04;

/**
 * Минимальная версия ProjectTask для модуля lab04.
 */
public class ProjectTask {

    private final String id;
    private String title;
    private final int estimatedHours;

    public ProjectTask(String id, String title, int estimatedHours) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id не может быть null или пустым");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title не может быть null или пустым");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("estimatedHours должно быть положительным");
        }
        this.id = id;
        this.title = title;
        this.estimatedHours = estimatedHours;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }

    @Override
    public String toString() {
        return "ProjectTask{id='" + id + "', title='" + title + "', hours=" + estimatedHours + "}";
    }
}
