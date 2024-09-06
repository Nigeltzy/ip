import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;

public class Shnoop {
    private Storage storage;
    private Ui ui;
    private TaskList tasks;
    private static java.nio.file.Path path;

    public Shnoop(java.nio.file.Path path) {
        storage = new Storage(path);
        ui = new Ui();

        try {
            tasks = new TaskList(storage.load());
        } catch (ShnoopException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        } catch (FileNotFoundException efile) {
            ui.showError(efile.getMessage());
            throw new RuntimeException();
        }
    }

    public void run() {
        // @@author CS2103T Website
        // Reused from https://nus-cs2103-ay2425s1.github.io/website/schedule/week3/project.html
        // with minor modifications
        ui.showWelcome();
        boolean isExit = false;
        ui.showLine();
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand);
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (ShnoopException e) {
                ui.showError(e.getMessage());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (IncompleteEventOrDeadlineException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showLine();
            }
        }
        // @@author CS2103T Website
    }

    public static void main(String[] args) {
        // @@author Steve Hills
        // Reused from https://www.sghill.net/2014/how-do-i-make-cross-platform-file-paths-in-java/
        // with minor modifications
        String home = System.getProperty("user.home");
        path = java.nio.file.Paths.get(home, "my", "apps", "dir");
        // @@author Steve Hills

        Shnoop shnoop = new Shnoop(path);
        shnoop.run();
    }
}
