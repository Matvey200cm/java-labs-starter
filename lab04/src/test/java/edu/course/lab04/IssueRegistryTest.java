package edu.course.lab04;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Тесты для IssueRegistry — обязательные эксперименты.
 *
 * Пошаговый план:
 * 1. Поля, определяющие логическое равенство ключа: projectCode и issueNumber.
 * 2. equals и hashCode согласованы: используют оба поля.
 * 3. Registry реализован через композицию с HashMap.
 * 4. Обычные тесты операций (add, get, remove, containsKey, size).
 * 5. Тест коллизии и эксперимент изменяемого ключа.
 */
class IssueRegistryTest {

    // ======================== Обычные операции ========================

    @Test
    void addAndGet() {
        IssueRegistry registry = new IssueRegistry();
        IssueId key = new IssueId("PROJ", 1);
        ProjectTask task = new ProjectTask("T1", "First task", 5);

        registry.addOrUpdate(key, task);

        assertTrue(registry.containsKey(key));
        assertEquals(task, registry.get(key).orElse(null));
        assertEquals(1, registry.size());
    }

    @Test
    void remove() {
        IssueRegistry registry = new IssueRegistry();
        IssueId key = new IssueId("PROJ", 1);
        ProjectTask task = new ProjectTask("T1", "First task", 5);

        registry.addOrUpdate(key, task);
        ProjectTask removed = registry.remove(key);

        assertEquals(task, removed);
        assertFalse(registry.containsKey(key));
        assertEquals(0, registry.size());
    }

    @Test
    void getNonExistentReturnsEmpty() {
        IssueRegistry registry = new IssueRegistry();
        IssueId key = new IssueId("PROJ", 99);

        assertTrue(registry.get(key).isEmpty());
    }

    // ======================== Эксперимент 1: равные ключи находят одно значение ========================

    @Test
    void experiment1_equalKeysFindSameValue() {
        IssueRegistry registry = new IssueRegistry();
        IssueId key1 = new IssueId("PROJ", 1);
        IssueId key2 = new IssueId("PROJ", 1); // логически равный ключ
        ProjectTask task = new ProjectTask("T1", "Task one", 3);

        registry.addOrUpdate(key1, task);

        // Два разных экземпляра логически равного ключа находят одно значение
        assertEquals(task, registry.get(key2).orElse(null));
        assertSame(task, registry.get(key2).orElse(null));
    }

    // ======================== Эксперимент 2: повторное добавление заменяет значение ========================

    @Test
    void experiment2_reAddReplacesValue() {
        IssueRegistry registry = new IssueRegistry();
        IssueId key = new IssueId("PROJ", 1);
        ProjectTask task1 = new ProjectTask("T1", "Old task", 3);
        ProjectTask task2 = new ProjectTask("T2", "New task", 7);

        registry.addOrUpdate(key, task1);
        assertEquals(1, registry.size());

        ProjectTask previous = registry.addOrUpdate(key, task2);
        assertEquals(task1, previous); // предыдущее значение возвращено
        assertEquals(1, registry.size()); // размер НЕ увеличился

        assertEquals(task2, registry.get(key).orElse(null));
    }

    // ======================== Эксперимент 3: коллизия hashCode ========================

    /**
     * Ключ с намеренно одинаковым hashCode, но разным equals.
     */
    private static class CollidingIssueId extends IssueId {

        private final int forcedHash;

        public CollidingIssueId(String projectCode, int issueNumber, int forcedHash) {
            super(projectCode, issueNumber);
            this.forcedHash = forcedHash;
        }

        @Override
        public int hashCode() {
            return forcedHash;
        }
    }

    @Test
    void experiment3_collisionKeepsDifferentValues() {
        // Создаём два неравных ключа с одинаковым hashCode
        CollidingIssueId key1 = new CollidingIssueId("A", 1, 42);
        CollidingIssueId key2 = new CollidingIssueId("B", 2, 42);

        assertNotEquals(key1, key2); // неравные ключи
        assertEquals(key1.hashCode(), key2.hashCode()); // одинаковый хеш

        IssueRegistry registry = new IssueRegistry();
        ProjectTask task1 = new ProjectTask("T1", "Task A", 2);
        ProjectTask task2 = new ProjectTask("T2", "Task B", 4);

        registry.addOrUpdate(key1, task1);
        registry.addOrUpdate(key2, task2);

        // HashMap корректно обрабатывает коллизию — разные ключи хранят разные значения
        assertEquals(task1, registry.get(key1).orElse(null));
        assertEquals(task2, registry.get(key2).orElse(null));
        assertEquals(2, registry.size());
    }

    // ======================== Эксперимент 4: сравнение с null и другим типом ========================

    @Test
    void experiment4_equalsWithNullAndDifferentType() {
        IssueId key = new IssueId("PROJ", 1);

        // equals с null возвращает false
        assertFalse(key.equals(null));

        // equals с объектом другого типа возвращает false
        assertFalse(key.equals("PROJ-1"));
        assertFalse(key.equals(42));
        assertFalse(key.equals(new Object()));
    }

    // ======================== Эксперимент 5: изменяемый ключ ========================

    /**
     * Изменяемый ключ для демонстрации проблемы.
     * После изменения projectCode hashCode меняется, и поиск перестаёт работать.
     */
    private static class MutableIssueId extends IssueId {

        private String projectCode;

        public MutableIssueId(String projectCode, int issueNumber) {
            super(projectCode, issueNumber);
            this.projectCode = projectCode;
        }

        public void setProjectCode(String projectCode) {
            if (projectCode == null || projectCode.isEmpty()) {
                throw new IllegalArgumentException("projectCode не может быть null или пустым");
            }
            this.projectCode = projectCode;
        }

        @Override
        public String getProjectCode() {
            return projectCode;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            MutableIssueId other = (MutableIssueId) obj;
            return this.issueNumber == other.issueNumber
                    && this.projectCode.equals(other.projectCode);
        }

        @Override
        public int hashCode() {
            int result = projectCode.hashCode();
            result = 31 * result + Integer.hashCode(issueNumber);
            return result;
        }
    }

    @Test
    void experiment5_mutableKeyBreaksLookup() {
        MutableIssueId key = new MutableIssueId("PROJ", 1);
        ProjectTask task = new ProjectTask("T1", "Task for mutable key", 5);

        IssueRegistry registry = new IssueRegistry();
        registry.addOrUpdate(key, task);

        // До изменения — поиск работает
        assertTrue(registry.containsKey(key));
        assertNotNull(registry.get(key).orElse(null));

        // Изменяем поле, участвующее в hashCode и equals
        key.setProjectCode("CHANGED");

        // После изменения — поиск перестаёт работать
        // Потому что hashCode изменился и HashMap ищет в другой корзине
        assertFalse(registry.containsKey(key));
        assertTrue(registry.get(key).isEmpty());

        // Задача всё ещё хранится в реестре, но найти её по изменённому ключу нельзя
        // Это и есть проблема изменяемого ключа — данные потеряны для поиска
        assertEquals(1, registry.size());
    }

    // ======================== Дополнительные проверки ========================

    @Test
    void isEmptyInitially() {
        IssueRegistry registry = new IssueRegistry();
        assertTrue(registry.isEmpty());
    }

    @Test
    void multipleEntries() {
        IssueRegistry registry = new IssueRegistry();
        registry.addOrUpdate(new IssueId("A", 1), new ProjectTask("T1", "Task A1", 1));
        registry.addOrUpdate(new IssueId("A", 2), new ProjectTask("T2", "Task A2", 2));
        registry.addOrUpdate(new IssueId("B", 1), new ProjectTask("T3", "Task B1", 3));

        assertEquals(3, registry.size());
        assertFalse(registry.isEmpty());
    }
}
