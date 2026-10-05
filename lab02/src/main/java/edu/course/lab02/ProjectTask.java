package edu.course.lab02;
//Создайте `ProjectTask`:

//- `String id`;
//- `String title`;
//- `TaskStatus status`;
//- `int estimatedHours`.
//Операции:
//- создать задачу;
//- изменить статус через `changeStatus`;
//- определить, завершена ли задача;
//- увеличить оценку трудоемкости на положительное число часов.



enum TaskStatus {
    CREATED,
    IN_PROGRESS,
    DONE
}

public class ProjectTask {
    private String id;
    private String title;
    private TaskStatus status;
    private int estimatedHours;

    public ProjectTask(String id, String title, int estimatedHours) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Id cannot be null or empty");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title cannot be null or empty");
        }
        if (estimatedHours <= 0) {
            throw new IllegalArgumentException("Estimated hours must be positive");
        }
        this.id = id;
        this.title = title;
        this.status = TaskStatus.CREATED;
        this.estimatedHours = estimatedHours;
    }

    public void changeStatus(TaskStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = newStatus;
    }

    public boolean isDone() {
        return this.status == TaskStatus.DONE;
    }

    public void increaseEstimate(int additionalHours) {
        if (additionalHours <= 0) {
            throw new IllegalArgumentException("Additional hours must be positive");
        }
        this.estimatedHours += additionalHours;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public int getEstimatedHours() {
        return estimatedHours;
    }
}