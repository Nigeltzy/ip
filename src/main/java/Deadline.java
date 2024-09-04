public class Deadline extends Task {
    protected String by;
    public Deadline (String description, String by) throws EmptyDescriptionException {
        super(description);
        this.by = by;
    }

    public Deadline (String description, String by, boolean done) throws EmptyDescriptionException {
        super(description);
        this.by = by;
        if (done) {
            this.markTask();
        }
    }

    /**
     * Constructs new Deadline task by extracting /by from the String
     *
     * @param str Un-separated String
     */
    public Deadline (String str) throws IncompleteEventOrDeadlineException, EmptyDescriptionException {
        if (!str.contains("/by ")) {
            throw new IncompleteEventOrDeadlineException();
        } else {
            String desc = (str.substring(0, str.toLowerCase().indexOf("/by ")));
            if (desc.isEmpty()) {
                throw new EmptyDescriptionException();
            }
            this.description = desc;
            this.by = (str.substring(str.toLowerCase().indexOf("/by ") + 4, str.length()));
            if (by.isEmpty()) {
                throw new IncompleteEventOrDeadlineException();
            }
            isDone = false;
        }
    }

    @Override
    public String toString() {
        return "[D] " + super.toString() + " (by: " + by + ")";
    }

    @Override
    public String toUString() {
        String s = super.toUString();
        s += "003"; // Unique identifier for Event Tasktype
        s += super.description;
        s += "/by/";
        s += by;
        return s;
    }
}
