package edu.course.lab04;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Реестр задач на основе HashMap.
 * Ключ — IssueId, значение — ProjectTask.
 */
public class IssueRegistry {

    private final Map<IssueId, ProjectTask> store;

    public IssueRegistry() {
        this.store = new HashMap<>();
    }

    /**
     * Добавить новую задачу или заменить существующую по ключу.
     * @return предыдущее значение, если ключ уже был, или null
     */
    public ProjectTask addOrUpdate(IssueId key, ProjectTask task) {
        return store.put(key, task);
    }

    /**
     * Получить задачу по ключу.
     */
    public Optional<ProjectTask> get(IssueId key) {
        return Optional.ofNullable(store.get(key));
    }

    /**
     * Удалить задачу по ключу.
     * @return удалённая задача, или null если ключ не найден
     */
    public ProjectTask remove(IssueId key) {
        return store.remove(key);
    }

    /**
     * Проверить наличие ключа.
     */
    public boolean containsKey(IssueId key) {
        return store.containsKey(key);
    }

    /**
     * Количество записей в реестре.
     */
    public int size() {
        return store.size();
    }

    /**
     * Проверить, пуст ли реестр.
     */
    public boolean isEmpty() {
        return store.isEmpty();
    }
}
