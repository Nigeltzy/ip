public class UndefinedTaskException extends Exception {

    public UndefinedTaskException(String message) {
        super(message);
    }

    public UndefinedTaskException() {
        super("✿ Shnoop ✿: You could travel the world, but nothing comes close to choosing a task type.\n"
                + "✿ Shnoop ✿: Try typing 'todo', 'event' or 'deadline' followed by stating the task description.");
    }
}
