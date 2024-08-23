public class IncompleteEventOrDeadlineException extends Exception {

    public IncompleteEventOrDeadlineException(String message) {
        super(message);
    }

    public IncompleteEventOrDeadlineException() {
        super("✿ Shnoop ✿: We don't mind sand in our stilettos, but you've came to the beach without sunscreen.\n"
                + "✿ Shnoop ✿: Try adding '/by xxx', '/from xxx' or '/to xxx' after stating the task description.");
    }
}
