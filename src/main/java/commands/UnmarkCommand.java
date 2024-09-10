package commands;

import java.io.IOException;

import cook.Storage;
import cook.TaskList;
import cook.Ui;
import exceptions.InvalidInputException;

/**
 * UnmarkCommand class to process mark commands.
 */
public class UnmarkCommand extends Command {
    protected int taskNumber;

    /**
     * Constructor for UnmarkCommand class.
     */
    public UnmarkCommand(String taskNumber) throws InvalidInputException {
        super("unmark");
        try {
            this.taskNumber = Integer.parseInt(taskNumber);
        } catch (NumberFormatException e) {
            throw new InvalidInputException("A task must be selected.");
        }
    }

    /**
     * Unmarks task and saves file.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        try {
            tasks.unmarkTask(this.taskNumber);
            storage.createFile();
            storage.writeFile(tasks.toString());
            ui.say("File saved.");
        } catch (IndexOutOfBoundsException e) {
            ui.say("The task is not in the list.");
        } catch (IOException e) {
            ui.say("File cannot be saved.");
        }
    }
}
