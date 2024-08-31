package cook;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;

import exceptions.InvalidCommandException;
import tasks.Deadline;
import tasks.Event;
import tasks.ToDo;

/**
 * Cook class to store main logic and program.
 */
public class Cook {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    /**
     * Constructor for Cook class.
     */
    public Cook(File file) {
        this.storage = new Storage(file);
        this.tasks = new TaskList();
        this.ui = new Ui();

        HashMap<String, Integer> validCommandArgs = new HashMap<>();

        validCommandArgs.put("bye", 1);
        validCommandArgs.put("list", 1);
        validCommandArgs.put("mark", 2);
        validCommandArgs.put("unmark", 2);
        validCommandArgs.put("delete", 2);
        validCommandArgs.put("find", 2);
        validCommandArgs.put("todo", 2);
        validCommandArgs.put("deadline", 4);
        validCommandArgs.put("event", 6);

        this.parser = new Parser(validCommandArgs);
    }

    /**
     * Runs main logic.
     */
    public void run() {
        this.ui.welcome();

        while (true) {
            String input = this.ui.getInput();
            HashMap<String, String> argumentsHashMap;

            try {
                argumentsHashMap = this.parser.readInput(input);
            } catch (InvalidCommandException e) {
                this.ui.say(e.getMessage());
                continue;
            }

            String command = argumentsHashMap.get("command");
            if (command.equals("bye")) {
                return;
            } else if (command.equals("list")) {
                this.ui.say(this.tasks.toString());
            } else if (command.contains("mark") || command.equals("delete")) {
                int taskNo;

                try {
                    taskNo = Integer.parseInt(argumentsHashMap.get(command));
                } catch (NumberFormatException e) {
                    this.ui.say("A task must be selected.");
                    continue;
                }

                boolean toMark = command.equals("mark");
                boolean isSuccessful;

                try {
                    if (command.equals("delete")) {
                        this.tasks.deleteTask(taskNo);
                    } else {
                        isSuccessful = this.tasks.markTask(taskNo, toMark);
                    }
                } catch (IndexOutOfBoundsException e) {
                    this.ui.say("The task is not in the list.");
                    continue;
                }
            } else if (command.equals("find")) {
                String keyword = argumentsHashMap.get("find");
                this.ui.say(this.tasks.findTask(keyword));
            } else {
                if (command.equals("todo")) {
                    String taskDesc = argumentsHashMap.get("todo");
                    this.tasks.addTask(new ToDo(taskDesc));
                } else if (command.equals("deadline")) {
                    String taskDesc = argumentsHashMap.get("deadline");
                    LocalDateTime deadlineDateTime;

                    try {
                        deadlineDateTime = LocalDateTime.parse(argumentsHashMap.get("/by"));
                    } catch (DateTimeParseException e) {
                        this.ui.say("Date & time must be in a valid format, e.g. YYYY-MM-DD HH:mm.");
                        continue;
                    } catch (NullPointerException e) {
                        this.ui.say("Tasks.Deadline command format: deadline [desc] /by [YYYY-MM-DD HH:mm].");
                        continue;
                    }

                    this.tasks.addTask(new Deadline(taskDesc, deadlineDateTime));
                } else if (command.equals("event")) {
                    String taskDesc = argumentsHashMap.get("deadline");
                    LocalDateTime startDateTime;
                    LocalDateTime endDateTime;

                    try {
                        startDateTime = LocalDateTime.parse(argumentsHashMap.get("/from"));
                        endDateTime = LocalDateTime.parse(argumentsHashMap.get("/to"));
                        if (startDateTime.isAfter(endDateTime)) {
                            this.ui.say("The starting date & time cannot be "
                                    + "after the ending date & time");
                        }
                    } catch (DateTimeParseException e) {
                        this.ui.say("Date & time must be in a valid format, e.g. YYYY-MM-DD HH:mm.");
                        continue;
                    } catch (NullPointerException e) {
                        this.ui.say("Tasks.Event command format: event [desc] "
                                + "/from [YYYY-MM-DD HH:mm] /to [YYYY-MM-DD HH:mm].");
                        continue;
                    }

                    this.tasks.addTask(new Event(taskDesc, startDateTime, endDateTime));
                }
                try {
                    this.storage.createFile();
                    this.storage.writeFile(this.tasks.toString());
                    this.ui.say("File saved.");
                } catch (IOException e) {
                    this.ui.say("File cannot be saved.");
                    continue;
                }
            }
        }
    }

    /**
     * Runs main.
     */
    public static void main(String[] args) {
        new Cook(new File("data", "tasks.txt")).run();
    }
}
