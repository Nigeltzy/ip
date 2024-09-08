package shnoop.command;

import shnoop.exceptions.*;
import shnoop.storage.Storage;
import shnoop.tasks.*;
import shnoop.ui.*;

import java.io.IOException;

/**
 * Encapsulates all the relevant actions to be taken when an Add Command is issued.
 */
public class AddCommand extends Command {
    private String taskDescription;
    private Parser.Commands taskType = Parser.Commands.UNDEFINED;

    /**
     * Creates an instance of an AddCommand to be executed after.
     *
     * @param taskDescription Description of Task to be added.
     * @param taskType Type of Task to be added.
     */
    public AddCommand(String taskDescription, Parser.Commands taskType) {
        this.taskDescription = taskDescription;
        this.taskType = taskType;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws IOException, IncompleteEventOrDeadlineException {
        Task task = null;
        switch (taskType) {
        case TODO:
            task = new Todo(taskDescription);
            break;
        case DEADLINE:
            task = new Deadline(taskDescription);
            break;
        case EVENT:
            task = new Event(taskDescription);
            break;
        }
        tasks.add(task);
        ui.addTask(task, tasks.size());
        storage.save(tasks, task);
    }
}
