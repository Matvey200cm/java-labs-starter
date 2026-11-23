package edu.course.lab05;

import java.util.List;

final class LoadResult {
    private final List<TaskItem> items;
    private final List<ValidationError> errors;

    public LoadResult(List<TaskItem> items, List<ValidationError> errors) {
        this.items = items;
        this.errors = errors;
    }

    public List<TaskItem> getItems() {
        return items;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
