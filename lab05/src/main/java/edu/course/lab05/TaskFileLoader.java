package edu.course.lab05;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

final class TaskFileLoader {

    private static final String EXPECTED_HEADER = "id;title;category;estimatedHours;status";
    private static final int EXPECTED_FIELD_COUNT = 5;

    LoadResult load(Path path) {
        List<TaskItem> items = new ArrayList<>();
        List<ValidationError> errors = new ArrayList<>();

        List<String> lines;
        try {
            lines = Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataLoadingException("Cannot read " + path, e);
        }

        if (lines.isEmpty()) {
            return new LoadResult(items, errors);
        }

        // Check header
        String header = lines.get(0).trim();
        if (!header.equals(EXPECTED_HEADER)) {
            errors.add(new ValidationError(1, "header", "Invalid header: expected '" + EXPECTED_HEADER + "' but got '" + header + "'"));
            return new LoadResult(items, errors);
        }

        // Parse data rows
        for (int i = 1; i < lines.size(); i++) {
            String line = lines.get(i);
            int lineNumber = i + 1;

            // Skip empty lines
            if (line.trim().isEmpty()) {
                continue;
            }

            ParseResult result = parseLine(line, lineNumber);
            if (result.item != null) {
                items.add(result.item);
            } else if (result.error != null) {
                errors.add(result.error);
            }
        }

        return new LoadResult(items, errors);
    }

    private ParseResult parseLine(String line, int lineNumber) {
        String[] parts = line.split(";");

        if (parts.length != EXPECTED_FIELD_COUNT) {
            return new ParseResult(null, new ValidationError(
                lineNumber,
                "all",
                "Expected " + EXPECTED_FIELD_COUNT + " fields but got " + parts.length
            ));
        }

        String id = parts[0].trim();
        String title = parts[1].trim();
        String category = parts[2].trim();
        String estimatedHoursStr = parts[3].trim();
        String statusStr = parts[4].trim();

        // Validate id
        if (id.isEmpty()) {
            return new ParseResult(null, new ValidationError(lineNumber, "id", "Id cannot be empty"));
        }

        // Validate title
        if (title.isEmpty()) {
            return new ParseResult(null, new ValidationError(lineNumber, "title", "Title cannot be empty"));
        }

        // Validate category
        if (category.isEmpty()) {
            return new ParseResult(null, new ValidationError(lineNumber, "category", "Category cannot be empty"));
        }

        // Validate estimatedHours
        int estimatedHours;
        try {
            estimatedHours = Integer.parseInt(estimatedHoursStr);
        } catch (NumberFormatException e) {
            return new ParseResult(null, new ValidationError(lineNumber, "estimatedHours", "Invalid number: '" + estimatedHoursStr + "'"));
        }

        // Validate status
        TaskStatus status;
        try {
            status = TaskStatus.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            return new ParseResult(null, new ValidationError(lineNumber, "status", "Unknown status: '" + statusStr + "'"));
        }

        TaskItem item = new TaskItem(id, title, category, estimatedHours, status);
        return new ParseResult(item, null);
    }

    private static final class ParseResult {
        final TaskItem item;
        final ValidationError error;

        ParseResult(TaskItem item, ValidationError error) {
            this.item = item;
            this.error = error;
        }
    }
}
