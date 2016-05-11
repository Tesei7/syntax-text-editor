package ru.tesei7.textEditor.editor.document.model;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by Ilya on 11.05.2016.
 */
public class LongRunProgressCalculatorTest {
    private LongRunProgressCalculator calculator = new LongRunProgressCalculator(100);

    @Test
    public void getProgress() throws Exception {
        assertThat(calculator.getProgress(), is(0));
        calculator.doTask();
        assertThat(calculator.getProgress(), is(1));
        calculator.doTask();
        assertThat(calculator.getProgress(), is(2));
        calculator = new LongRunProgressCalculator(0);
        assertThat(calculator.getProgress(), is(0));
    }

}