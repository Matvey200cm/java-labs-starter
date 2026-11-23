package edu.course.lab05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskFileLoaderTest {

    @TempDir
    Path tempDir;

    private TaskFileLoader createLoader() {
        return new TaskFileLoader();
    }

    private Path createFile(String... lines) throws Exception {
        Path file = tempDir.resolve("tasks.txt");
        Files.write(file, List.of(lines), StandardCharsets.UTF_8);
        return file;
    }

    // 1. Correct file
    @Test
    void shouldLoadCorrectFile() throws Exception {
        Path file = createFile(
            "id;title;category;estimatedHours;status",
            "T-101;Prepare release;backend;8;READY",
            "T-102;Write docs;frontend;5;DONE"
        );

        LoadResult result = createLoader().load(file);

        assertEquals(2, result.getItems().size());
        assertEquals(0, result.getErrors().size());

        TaskItem item1 = result.getItems().get(0);
        assertEquals("T-101", item1.getId());
        assertEquals("Prepare release", item1.getTitle());
        assertEquals("backend", item1.getCategory());
        assertEquals(8, item1.getEstimatedHours());
        assertEquals(TaskStatus.READY, item1.getStatus());

        TaskItem item2 = result.getItems().get(1);
        assertEquals("T-102", item2.getId());
        assertEquals("Write docs", item2.getTitle());
        assertEquals("frontend", item2.getCategory());
        assertEquals(5, item2.getEstimatedHours());
        assertEquals(TaskStatus.DONE, item2.getStatus());
    }

    // 2. Cyrillic / Unicode text
    @Test
    void shouldHandleCyrillicUnicode() throws Exception {
        Path file = createFile(
            "id;title;category;estimatedHours;status",
            "T-201;Подготовка релиза;backend;10;READY",
            "T-202;Документация;frontend;3;CREATED"
        );

        LoadResult result = createLoader().load(file);

        assertEquals(2, result.getItems().size());
        assertEquals(0, result.getErrors().size());

        assertEquals("Подготовка релиза", result.getItems().get(0).getTitle());
        assertEquals("backend", result.getItems().get(0).getCategory());
    }

    // 3. Empty field
    @Test
    void shouldReportEmptyField() throws Exception {
        Path file = createFile(
            "id;title;category;estimatedHours;status",
            ";Empty id;backend;8;READY"
        );

        LoadResult result = createLoader().load(file);

        assertEquals(0, result.getItems().size());
        assertEquals(1, result.getErrors().size());

        ValidationError error = result.getErrors().get(0);
        assertEquals(2, error.getLineNumber());
        assertEquals("id", error.getField());
    }

    // 4. Wrong number (not a valid integer)
    @Test
    void shouldReportInvalidNumber() throws Exception {
        Path file = createFile(
            "id;title;category;estimatedHours;status",
            "T-301;Bad hours;backend;abc;READY"
        );

        LoadResult result = createLoader().load(file);

        assertEquals(0, result.getItems().size());
        assertEquals(1, result.getErrors().size());

        ValidationError error = result.getErrors().get(0);
        assertEquals(2, error.getLineNumber());
        assertEquals("estimatedHours", error.getField());
    }

    // 5. Unknown status
    @Test
    void shouldReportUnknownStatus() throws Exception {
        Path file = createFile(
            "id;title;category;estimatedHours;status",
            "T-401;Bad status;backend;8;UNKNOWN_STATUS"
        );

        LoadResult result = createLoader().load(file);

        assertEquals(0, result.getItems().size());
        assertEquals(1, result.getErrors().size());

        ValidationError error = result.getErrors().get(0);
        assertEquals(2, error.getLineNumber());
        assertEquals("status", error.getField());
    }

    // 6. Wrong number of columns
    @Test
    void shouldReportWrongColumnCount() throws Exception {
        Path file = createFile(
            "id;title;category;estimatedHours;status",
            "T-501;Too few fields;backend"
        );

        LoadResult result = createLoader().load(file);

        assertEquals(0, result.getItems().size());
        assertEquals(1, result.getErrors().size());

        ValidationError error = result.getErrors().get(0);
        assertEquals(2, error.getLineNumber());
        assertEquals("all", error.getField());
    }

    // 7. Mix of valid and invalid rows
    @Test
    void shouldMixValidAndInvalidRows() throws Exception {
        Path file = createFile(
            "id;title;category;estimatedHours;status",
            "T-601;Valid task;backend;8;READY",
            ";Empty id;frontend;5;DONE",
            "T-603;Another valid;backend;10;IN_PROGRESS",
            "T-604;Bad hours;frontend;xyz;CREATED",
            "T-605;Bad status;backend;3;INVALID"
        );

        LoadResult result = createLoader().load(file);

        assertEquals(2, result.getItems().size());
        assertEquals(3, result.getErrors().size());

        assertEquals("T-601", result.getItems().get(0).getId());
        assertEquals("T-603", result.getItems().get(1).getId());

        assertEquals(3, result.getErrors().get(0).getLineNumber());
        assertEquals(5, result.getErrors().get(1).getLineNumber());
        assertEquals(6, result.getErrors().get(2).getLineNumber());
    }

    // 8. Missing file
    @Test
    void shouldThrowDataLoadingExceptionForMissingFile() {
        Path missingFile = tempDir.resolve("nonexistent.txt");

        DataLoadingException exception = assertThrows(
            DataLoadingException.class,
            () -> createLoader().load(missingFile)
        );

        assertNotNull(exception.getCause());
        assertTrue(exception.getCause() instanceof java.nio.file.NoSuchFileException
            || exception.getCause() instanceof java.io.IOException);
    }

    // Additional: invalid header
    @Test
    void shouldReportInvalidHeader() throws Exception {
        Path file = createFile(
            "wrong;header;format",
            "T-701;Task;backend;8;READY"
        );

        LoadResult result = createLoader().load(file);

        assertEquals(0, result.getItems().size());
        assertEquals(1, result.getErrors().size());

        ValidationError error = result.getErrors().get(0);
        assertEquals(1, error.getLineNumber());
        assertEquals("header", error.getField());
    }

    // Additional: empty file
    @Test
    void shouldHandleEmptyFile() throws Exception {
        Path file = createFile();

        LoadResult result = createLoader().load(file);

        assertEquals(0, result.getItems().size());
        assertEquals(0, result.getErrors().size());
    }

    // Additional: header only, no data rows
    @Test
    void shouldHandleHeaderOnly() throws Exception {
        Path file = createFile(
            "id;title;category;estimatedHours;status"
        );

        LoadResult result = createLoader().load(file);

        assertEquals(0, result.getItems().size());
        assertEquals(0, result.getErrors().size());
    }

    // Additional: empty lines are skipped
    @Test
    void shouldSkipEmptyLines() throws Exception {
        Path file = createFile(
            "id;title;category;estimatedHours;status",
            "",
            "T-801;Task after empty;backend;5;READY",
            "",
            ""
        );

        LoadResult result = createLoader().load(file);

        assertEquals(1, result.getItems().size());
        assertEquals(0, result.getErrors().size());
    }
}
