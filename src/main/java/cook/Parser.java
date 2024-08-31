package cook;

import java.util.HashMap;

import exceptions.InvalidCommandException;

public class Parser {
    HashMap<String, Integer> commandArgs;

    public Parser(HashMap<String, Integer> commandArgs) {
        this.commandArgs = commandArgs;
    }

    public HashMap<String, String> readInput(String input) throws InvalidCommandException {
        String[] arguments = input.split(" ");
        String command = arguments[0].toLowerCase();
        HashMap<String, String> argumentsHashMap = new HashMap<>();

        if (this.commandArgs.containsKey(command)) {
            int argsExpected = this.commandArgs.get(command);
            argumentsHashMap.put("command", command);

            if (arguments.length < argsExpected) {
                throw new InvalidCommandException("The command should have at least " +
                        argsExpected + " arguments.");
            } else if (argsExpected == 2) {
                argumentsHashMap.put(command, input.replace(command, "").strip());
            } else if (argsExpected > 3) {
                String commandKey = command;
                StringBuilder valueSB = new StringBuilder();

                for (int i = 1; i < arguments.length; i++) {
                    String currentArgument = arguments[i];

                    if (!currentArgument.startsWith("/")) {
                        valueSB.append(currentArgument).append(" ");
                    }

                    if (currentArgument.startsWith("/") || i + 1 == arguments.length) {
                        argumentsHashMap.put(commandKey.toLowerCase(), valueSB.toString().strip());
                        commandKey = currentArgument;
                        valueSB.setLength(0);
                    }
                }
            }
            return argumentsHashMap;
        }
        throw new InvalidCommandException("The command does not match any that I know of.");
    }
}
