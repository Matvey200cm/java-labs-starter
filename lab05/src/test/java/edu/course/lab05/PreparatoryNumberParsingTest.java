package edu.course.lab05;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class PreparatoryNumberParsingTest {

    @TempDir
    Path tempDir;

    @Test
    void shouldReadAndParseNumbersWithFormatExceptionOnBad() throws Exception {
        Path file = tempDir.resolve("numbers.txt");
        Files.write(file, List.of("10", "bad", "20"), StandardCharsets.UTF_8);

        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);

        // Verify file content
        if (lines.size() != 3) {
            throw new AssertionError("Expected 3 lines but got " + lines.size());
        }
        if (!lines.get(0).equals("10")) {
            throw new AssertionError("First line should be '10'");
        }
        if (!lines.get(1).equals("bad")) {
            throw new AssertionError("Second line should be 'bad'");
        }
        if (!lines.get(2).equals("20")) {
            throw new AssertionError("Third line should be '20'");
        }

        // Parse numbers with separate test for NumberFormatException
        int first = Integer.parseInt(lines.get(0));
        if (first != 10) {
            throw new AssertionError("Parsed first number should be 10");
        }

        int third = Integer.parseInt(lines.get(2));
        if (third != 20) {
            throw new AssertionError("Parsed third number should be 20");
        }

        // Test that "bad" causes NumberFormatException
        try {
            Integer.parseInt(lines.get(1));
            throw new AssertionError("Should have thrown NumberFormatException for 'bad'");
        } catch (NumberFormatException e) {
            // Expected
        }
    }
}
