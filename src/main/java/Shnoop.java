import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class Shnoop {
    private boolean completion = false;
    private String mode;
    private static ArrayList<Task> tasks;
    private static java.nio.file.Path path;
    private int arrPointer;
    private String[] quotes;
    public enum TaskTypes {
        TODO,
        EVENT,
        DEADLINE,
        UNDEFINED
    }
    public Shnoop() {
    }

    /**
     * Configures class based on desired mode.
     *
     * @param input String to indicate mode.
     */
    public Shnoop(String input) {
        if (input == "todo") {
            mode = "todo";
            tasks = new ArrayList<Task>();
            arrPointer = 0;
            quotes = new String[] {
                    "You're unforgettable.",
                    "Coded, tanned, fit and ready.",
                    "You're undeniable."
            };
        } else {
            mode = "echo";
        }
    }

    public void addTask(Task task) {
        tasks.add(task);
        arrPointer++;
    }

    /**
     * Determines if a String can be converted into an Integer
     *
     * @param str String to be converted
     * @return True if String is an Integer
     */
    public boolean canBeInteger(String str) {
        if (str == null || str == "") {
            return false;
        }

        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Converts String to Integer if it can be converted.
     *
     * @param str String to be converted
     * @return Integer form of String, otherwise, arbitrary Integer Maximum Value
     */
    public int convertStrToInteger(String str) {
        if (canBeInteger(str)) {
            return Integer.parseInt(str);
        } else {
            return Integer.MAX_VALUE;
        }
    }

    /**
     * Does the required code for a mark or unmark CLI input.
     *
     * @param input Given by the user.
     * @return String indicating the action that was done.
     */
    public String parseInputMark(String input) {
        if (input.length() >= 6 && input.substring(0, 5).equals("mark ")) {
            if (canBeInteger(input.substring(5, input.length()))) {
                boolean result = tasks.get(convertStrToInteger(input.substring(5, input.length())) - 1).markTask();
                if (result) {
                    System.out.println("✿ Shnoop ✿: Warm, wet and wild! I've marked this task as done: ");
                } else {
                    System.out.println("✿ Shnoop ✿: Daisy dukes! This task was already done my love: ");
                }
                System.out.println(tasks.get(convertStrToInteger(input.substring(5, input.length())) - 1)
                        .getTaskWithStatus());
                return "mark_task";
            }
        } else if (input.length() >= 8 && input.substring(0, 7).equals("unmark ")) {
            if (canBeInteger(input.substring(7, input.length()))) {
                boolean result = tasks.get(convertStrToInteger(input.substring(7, input.length())) - 1).unmarkTask();
                if (result) {
                    System.out.println("✿ Shnoop ✿: Melted this popsicle! I've unmarked this task as done: ");
                } else {
                    System.out.println("✿ Shnoop ✿: Daisy dukes! This task was never done my love: ");
                }
                System.out.println(tasks.get(convertStrToInteger(input.substring(7, input.length())) - 1)
                        .getTaskWithStatus());
                return "mark_task";
            }
        }

        return "not_mark_or_unmark";
    }

    /**
     * Returns a quote from the quote bank.
     *
     * @param idx Should be a changing number.
     * @return Quote from quote bank.
     */
    public String getRandomQuote(int idx) {
        return quotes[idx];
    }

    /**
     * Determines if String indicates a Todo task.
     *
     * @param str String to be checked.
     * @param length Length of string (to avoid repeated str.length() calls).
     * @return True if String starts with "Todo "
     */
    public boolean startsWithTodo(String str, int length) {
        return (length >= 5 && str.toLowerCase().startsWith("todo "));
    }

    /**
     * Determines if String indicates a Event task.
     *
     * @param str String to be checked.
     * @param length Length of string (to avoid repeated str.length() calls).
     * @return True if String starts with "Event "
     */
    public boolean startsWithEvent(String str, int length) {
        return (length >= 6 && str.toLowerCase().startsWith("event "));
    }

    /**
     * Determines if String indicates a Deadline task.
     *
     * @param str String to be checked.
     * @param length Length of string (to avoid repeated str.length() calls).
     * @return True if String starts with "Deadline "
     */
    public boolean startsWithDeadline(String str, int length) {
        return (length >= 9 && str.toLowerCase().startsWith("deadline "));
    }

    public boolean startsWithDelete(String str, int length) {
        return (length >= 7 && str.toLowerCase().startsWith("delete "));
    }

    public String parseInputDelete(String str, int length) {
        String temp = (str.substring(7, length));
        if (canBeInteger(temp)) {
            int ptr = convertStrToInteger(temp);
            Task element = tasks.get(ptr - 1);
            tasks.remove(ptr - 1);
            System.out.println("✿ Shnoop ✿: I know a place, where the grass is really greener. "
                    + "I'll send this task there\n" + "Goodbye " + element + "!");
            return "task_removed";
        }

        return "task_not_removed";
    }

    public TaskTypes getTaskType(String str) {
        int length = str.length();
        if (startsWithTodo(str, length)) {
            return TaskTypes.TODO;
        } else if (startsWithEvent(str, length)) {
            return TaskTypes.EVENT;
        } else if (startsWithDeadline(str, length)) {
            return TaskTypes.DEADLINE;
        } else {
            return TaskTypes.UNDEFINED;
        }
    }

    /**
     * Returns the relevant string excluding Todo, Event, or Deadline tag.
     *
     * @param str User input to sieve through.
     * @return String excluding task type.
     */
    public String getTaskDetails(String str) {
        TaskTypes taskType = getTaskType(str);
        int length = str.length();

        switch (taskType) {
        case TODO:
            return str.substring(5, length);
        case EVENT:
            return str.substring(6, length);
        case DEADLINE:
            return str.substring(9, length);
        default:
            return str;
        }
    }

    /**
     * Returns String representing action done.
     * Performs a series of actions depending on input and mode.
     *
     * @param input Input given by user.
     * @return String action code.
     */
    public String parseInput(String input) throws UndefinedTaskException, IncompleteEventOrDeadlineException,
            EmptyDescriptionException, UnmarkableArrayException, IndexOutOfBoundsException, IOException {
        switch (mode) {

        // For Level-1 echo mode.
        case "echo":
            switch (input) {
            case "bye":
                System.out.println("\n✿ Shnoop ✿: I'll check ya later, cause you represent. Don't worry we got it on lock. ♡");
                completion = true;
                return "exit";
            default:
                System.out.println("\n✿ Shnoop ✿: " + input);
                return "echo";
            }

        // For Level-2 Add, List mode.
        case "todo":

            // For mark specific
            try {
                String parseInputMarkResult = parseInputMark(input);
                if (parseInputMarkResult != "not_mark_or_unmark") {
                    return parseInputMarkResult; // Exit if it is mark or unmark command
                } else if (startsWithDelete(input, input.length())) {
                    return parseInputDelete(input, input.length());
                }
            } catch (NullPointerException | IndexOutOfBoundsException npe) {
                throw new UnmarkableArrayException();
            }


            // Other than mark or unmark or delete
            switch (input) {

            case "bye":
            clearFile(path.toString());
            for (int i = 0; i < tasks.size(); i ++) {
                try {
                    writeToFile(tasks.get(i).toUString() + "\n");
                } catch (IOException e) {
                    System.out.println("Something went wrong when trying to writeToFile: " + e.getMessage());
                }
            }
                System.out.println("\n✿ Shnoop ✿: I'll check ya later, cause you represent. "
                        + "Don't worry we got it on lock. ♡");
                completion = true;
                return "exit";

            case "list":
                System.out.println("✿ Shnoop ✿: Find, fresh, fierce and ready.");
                for (int i = 0; i < tasks.size(); i ++) {
                    if (tasks.get(i) == null) {
                        break;
                    }
                    System.out.println((i + 1) + ". " + tasks.get(i).toString());
                }
                return "list";

            default:
                String x = getRandomQuote(arrPointer % 3);

                TaskTypes taskType = getTaskType(input);
                String taskDesc = getTaskDetails(input);
                Task newTask;

                try {
                    switch (taskType) {
                    case TODO:
                        newTask = new Todo(taskDesc);
                        break;
                    case EVENT:
                        newTask = new Event(taskDesc);
                        break;
                    case DEADLINE:
                        newTask = new Deadline(taskDesc);
                        break;
                    default:
                        throw new UndefinedTaskException();
                    }

                    addTask(newTask);
                    System.out.println("✿ Shnoop ✿: " + x + " I'll add that in for ya. \nTask Added: " + newTask);
                    System.out.println("✿ Shnoop ✿: You've got " + tasks.size() + " doggy-dogs on the stereo.");
                    return "add_task";
                } catch (EmptyDescriptionException e) {
                    throw new EmptyDescriptionException();
                } catch (IncompleteEventOrDeadlineException f) {
                    throw new IncompleteEventOrDeadlineException();
                } catch (UndefinedTaskException g) {
                    throw new UndefinedTaskException();
                }

            }

        // If mode is NIL, indicate bug
        default:
            return "empty_input_bug";
        }


    }

    /**
     * Prints out introductory speech at start of application.
     */
    public void startIntroSpeech() {
        System.out.println("\n ... Greetings loved one ʚ♡ɞ Let's take a journey ... \n\n\n ✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-"
                + "✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿ \n"
                + " ✿ It's Shnoop, my dawg. I'm all up on ya. Whatchu need? ✿ \n"
                + " ✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿-✿ \n");
    }

    public boolean isCompleted() {
        return completion;
    }

    private static Task readFileToTask(String line) throws EmptyDescriptionException, ImproperFileTypeException {
        boolean taskIsCompleted = false;
        if (line.substring(0, 1).equals("1")) {
            taskIsCompleted = true;
        } else if (!(line.substring(0,1).equals("0"))) {
            throw new ImproperFileTypeException();
        }
        String taskType = line.substring(1, 4);
        String desc;
        switch (taskType) {
        case ("001"):
            // Todo
            return new Todo(line.substring(4, line.length()), taskIsCompleted);
        case ("002"):
            // Event
            desc = line.substring(4, line.indexOf("/from/"));
            String from = line.substring(line.indexOf("/from/") + 6, line.indexOf("/to/"));
            String to = line.substring(line.indexOf("/to/") + 4, line.length());
            return new Event(desc, from, to, taskIsCompleted);
        case ("003"):
            // Deadline
            desc = line.substring(4, line.indexOf("/by/"));
            String by = line.substring(line.indexOf("/by/") + 4, line.length());
            return new Deadline(desc, by, taskIsCompleted);
        }
        return null;
    }

    // @@author CS2103T Website
    // Reused from https://nus-cs2103-ay2425s1.github.io/website/schedule/week3/topics.html
    // With minor modifications
    private static void writeToFile(String textToAdd) throws IOException {
        FileWriter fw = new FileWriter(path.toString() + "/shnoopstorage.txt", true);
        fw.write(textToAdd);
        fw.close();
    }

    private static void loadFileContents(String path) throws FileNotFoundException {
        File f = new File(path); // create a File for the given file path
        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                try {
                    tasks.add(readFileToTask(s.nextLine()));
                } catch (EmptyDescriptionException | ImproperFileTypeException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // @@author CS2103T Website

    public static void clearFile(String path) throws IOException {
        FileWriter fw = new FileWriter(path.toString() + "/shnoopstorage.txt", false);
        fw.write("");
        fw.close();
    }

    public static void main(String[] args) {
        // @@author Steve Hills
        // Reused from https://www.sghill.net/2014/how-do-i-make-cross-platform-file-paths-in-java/
        // with minor modifications
        String home = System.getProperty("user.home");
        path = java.nio.file.Paths.get(home, "my", "apps", "dir");
        boolean directoryExists = java.nio.file.Files.exists(path);
        // @@author Steve Hills

        if (!directoryExists) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
                // TODO handle the exception
                throw new RuntimeException(e);
            }
        }

        File file = new File(path.toFile(), "shnoopstorage.txt");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                // TODO handle the exception
                throw new RuntimeException(e);
            }
        }


        try (Scanner scanner = new Scanner(System.in)) {
            String input;
            Shnoop shnoop = new Shnoop("todo");

            shnoop.startIntroSpeech();

            // Load data up
            try { 
                loadFileContents(path.toString() + "/shnoopstorage.txt");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            while (!shnoop.isCompleted()) {
                input = scanner.nextLine();
                try {
                    String result = shnoop.parseInput(input);
                } catch (UndefinedTaskException | IncompleteEventOrDeadlineException | EmptyDescriptionException |
                         UnmarkableArrayException | IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (IndexOutOfBoundsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
