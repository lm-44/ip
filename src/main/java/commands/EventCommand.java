package commands;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import cook.Storage;
import cook.TaskList;
import cook.Ui;
import exceptions.InvalidInputException;
import tasks.Event;

/**
 * EventCommand class to process Event commands.
 */
public class EventCommand extends Command {
    protected String description;
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructor for EventCommand class.
     */
    public EventCommand(String description, String from, String to) throws InvalidInputException {
        super("event");
        this.description = description;

        try {
            this.from = LocalDateTime.parse(from);
            this.to = LocalDateTime.parse(to);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("Date & time must be in a valid format, e.g. YYYY-MM-DD HH:mm.");
        } catch (NullPointerException e) {
            throw new InvalidInputException("Tasks.Event command format: event [desc] "
                    + "/from [YYYY-MM-DD HH:mm] /to [YYYY-MM-DD HH:mm].");
        }

        if (this.to.isAfter(this.from)) {
            throw new InvalidInputException("The starting date & time cannot be "
                    + "after the ending date & time");
        }
    }

    /**
     * Adds Event task and saves file.
     */
    @Override
    public String execute(TaskList tasks, Ui ui, Storage storage) {
        StringBuilder content = new StringBuilder();
        tasks.addTask(new Event(this.description, this.from, this.to));
        content.append("Event task has been added.\n");
        try {
            storage.createFile();
            storage.writeFile(tasks.toString());
            content.append("File saved.");
            return content.toString();
        } catch (IOException e) {
            content.append("File cannot be saved.");
            return content.toString();
        }
    }
}
