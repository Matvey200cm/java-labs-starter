package edu.course.lab04;

/**
 * Неизменяемый ключ для IssueRegistry.
 * Состоит из кода проекта и номера задачи.
 * Равенство определяется обоими полями.
 */
public class IssueId {

    private final String projectCode;
    protected final int issueNumber;

    public IssueId(String projectCode, int issueNumber) {
        if (projectCode == null || projectCode.isEmpty()) {
            throw new IllegalArgumentException("projectCode не может быть null или пустым");
        }
        this.projectCode = projectCode;
        this.issueNumber = issueNumber;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IssueId other = (IssueId) obj;
        return this.issueNumber == other.issueNumber
                && this.projectCode.equals(other.projectCode);
    }

    @Override
    public int hashCode() {
        int result = projectCode.hashCode();
        result = 31 * result + Integer.hashCode(issueNumber);
        return result;
    }

    @Override
    public String toString() {
        return "IssueId{projectCode='" + projectCode + "', issueNumber=" + issueNumber + "}";
    }
}
