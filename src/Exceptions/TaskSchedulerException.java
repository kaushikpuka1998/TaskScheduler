package Exceptions;

public class TaskSchedulerException {
    private String message;

    public TaskSchedulerException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
