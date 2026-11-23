package edu.course.lab04;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для Book (подготовительная часть).
 */
class BookTest {

    @Test
    void hashMapLookupByEqualKey() {
        Book book1 = new Book("978-3-16-148410-0", "Java Programming");
        Book book2 = new Book("978-3-16-148410-0", "Java Programming");

        HashMap<Book, String> map = new HashMap<>();
        map.put(book1, "Value for Java book");

        // Поиск по другому экземпляру с тем же ISBN должен найти значение
        assertEquals("Value for Java book", map.get(book2));
    }

    @Test
    void differentIsbnMeansNotEqual() {
        Book book1 = new Book("978-3-16-148410-0", "Book One");
        Book book2 = new Book("978-0-07-12345-6", "Book Two");

        assertNotEquals(book1, book2);
        assertFalse(book1.equals(book2));
    }

    @Test
    void equalBooksHaveEqualHashCode() {
        Book book1 = new Book("978-3-16-148410-0", "Java Programming");
        Book book2 = new Book("978-3-16-148410-0", "Different Title");

        assertEquals(book1.hashCode(), book2.hashCode());
    }
}
