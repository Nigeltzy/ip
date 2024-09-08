package shnoop.storage;

import org.junit.jupiter.api.Test;
import shnoop.exceptions.EmptyDescriptionException;
import shnoop.exceptions.ImproperFileTypeException;
import shnoop.tasks.Todo;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class StorageTest {

    @Test
    public void readFileToTask_Todo_success() throws EmptyDescriptionException, ImproperFileTypeException {
        String line = "0001new todotask for me to do! yay";
        assertEquals(new Todo("new todotask for me to do! yay", false),
                Storage.readFileToTask(line));

        String line2 = "1001new todotask for me to do! yay... except i've already done it muahahaha";
        assertEquals(new Todo("new todotask for me to do! yay... except i've already done it muahahaha",
                        true), Storage.readFileToTask(line2));

        String line3 = "1001   this is weird but there's a hole in my heart";
        assertEquals(new Todo("   this is weird but there's a hole in my heart", true),
                Storage.readFileToTask(line3));
    }

    @Test
    public void anotherDummyTest(){
        assertEquals(4, 4);
    }
}
