package commands;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import cook.Storage;
import cook.TaskList;
import cook.Ui;
import exceptions.InvalidInputException;
import tasks.Deadline;

/**
 * DeadlineCommand class to process Deadline commands.
 */
public class DeadlineCommand extends Command {
    protected String description;
    protected LocalDateTime by;

    /**
     * Constructor for DeadlineCommand class.
     */
    public DeadlineCommand(String description, String by) throws InvalidInputException {
        super("deadline");
        this.description = description;
        try {
            this.by = LocalDateTime.parse(by);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Date & time must be in a valid format, e.g. YYYY-MM-DD HH:mm.");
        } catch (NullPointerException e) {
            throw new InvalidInputException("Deadline command format: deadline [desc] /by [YYYY-MM-DD HH:mm].");
        }
    }

    /**
     * Adds Deadline task and saves file.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.addTask(new Deadline(this.description, this.by));
        ui.say("Deadline task has been added.");
        try {
            storage.createFile();
            storage.writeFile(tasks.toString());
            ui.say("File saved.");
        } catch (IOException e) {
            ui.say("File cannot be saved.");
        }
    }
}
