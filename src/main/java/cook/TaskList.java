package cook;

import java.util.ArrayList;

import tasks.Task;

public class TaskList {
    protected ArrayList<Task> taskArrayList;

    public TaskList() {
        this.taskArrayList = new ArrayList<>();
    }

    public void addTask(Task task) {
        this.taskArrayList.add(task);
    }

    public boolean markTask(int taskNo, boolean toMark) throws IndexOutOfBoundsException {
        int indexNo = taskNo - 1;
        return this.taskArrayList.get(indexNo).mark(toMark);
    }

    public void deleteTask(int taskNo) {
        int indexNo = taskNo - 1;
        this.taskArrayList.remove(indexNo);
    }

    public String findTask(String keyword) {
        StringBuilder taskStringBuilder = new StringBuilder();
        for (int i = 0; i < this.taskArrayList.size(); i++) {
            int taskNo = i + 1;
            Task task = this.taskArrayList.get(i);
            if (task.toString().contains(keyword.toLowerCase())) {
                taskStringBuilder.append(taskNo).append(".").append(task.toString()).append("\n");
            }
        }
        return taskStringBuilder.toString().strip();
    }

    @Override
    public String toString() {
        StringBuilder taskStringBuilder = new StringBuilder();
        for (int i = 0; i < this.taskArrayList.size(); i++) {
            int taskNo = i + 1;
            Task task = this.taskArrayList.get(i);
            taskStringBuilder.append(taskNo).append(".").append(task.toString()).append("\n");
        }
        return taskStringBuilder.toString().strip();
    }
}
