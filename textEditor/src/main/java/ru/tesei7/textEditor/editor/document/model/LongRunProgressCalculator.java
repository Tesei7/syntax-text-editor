package ru.tesei7.textEditor.editor.document.model;

/**
 * Calculates progress of long run operations
 * Created by Ilya on 11.05.2016.
 */
public class LongRunProgressCalculator {
    /**
     * Total number of tasks in long operation
     */
    private int totalTasks;
    /**
     * Tasks complete to current moment
     */
    volatile private int done = 0;

    public LongRunProgressCalculator(int totalTasks) {
        this.totalTasks = totalTasks;
    }

    /**
     * @return progress in percents
     */
    public int getProgress() {
        return totalTasks == 0 ? 0 : (100 * done) / totalTasks;
    }

    /**
     * Make one task complete
     */
    public void doTask(){
        done++;
    }
}
