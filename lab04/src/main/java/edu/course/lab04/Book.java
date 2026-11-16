package edu.course.lab04;

/**
 * Неизменяемый класс книги.
 * Равенство определяется по полю isbn.
 */
public final class Book {

    private final String isbn;
    private final String title;

    public Book(String isbn, String title) {
        if (isbn == null || isbn.isEmpty()) {
            throw new IllegalArgumentException("ISBN не может быть null или пустым");
        }
        if (title == null || title.isEmpty()) {
            throw new IllegalArgumentException("Title не может быть null или пустым");
        }
        this.isbn = isbn;
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book other = (Book) obj;
        return this.isbn.equals(other.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }

    @Override
    public String toString() {
        return "Book{isbn='" + isbn + "', title='" + title + "'}";
    }
}
