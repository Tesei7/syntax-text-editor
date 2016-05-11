package ru.tesei7.textEditor.progressDialog;

import javax.swing.*;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;

/**
 * Builder for {@link ProgressDialog}.
 * Created by Ilya on 11.05.2016.
 */
public class ProgressDialogBuilder {
    private final JFrame parent;
    private final String title;
    private final String message;
    private final BooleanSupplier task;
    private boolean canCancel;
    private IntSupplier percentLoader;

    public ProgressDialogBuilder(JFrame parent, String title, String message, BooleanSupplier task) {
        this.parent = parent;
        this.title = title;
        this.message = message;
        this.task = task;
        canCancel = false;
    }

    public ProgressDialogBuilder canCancel() {
        canCancel = true;
        return this;
    }

    public ProgressDialogBuilder showPercents(IntSupplier percentLoader) {
        this.percentLoader = percentLoader;
        return this;
    }

    public ProgressDialog build(){
        return new ProgressDialog(this);
    }

    JFrame getParent() {
        return parent;
    }

    String getTitle() {
        return title;
    }

    String getMessage() {
        return message;
    }

    BooleanSupplier getTask() {
        return task;
    }

    boolean isCanCancel() {
        return canCancel;
    }

    public IntSupplier getPercentLoader() {
        return percentLoader;
    }
}
