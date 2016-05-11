package ru.tesei7.textEditor.progressDialog;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.function.BooleanSupplier;

import javax.swing.*;

/**
 * Progress dialog for long running operations
 */
public class ProgressDialog extends JDialog {
    private static final long serialVersionUID = -2001379600243197970L;
    private final String message;
    private final boolean canCancel;
    private final BooleanSupplier task;
    private SwingWorker<Void, Void> worker;

    private boolean result;

    ProgressDialog(ProgressDialogBuilder builder) {
        super(builder.getParent(), builder.getTitle(), true);
        this.message = builder.getMessage();
        this.canCancel = builder.isCanCancel();
        this.task = builder.getTask();
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                cancelTask();
            }
        });
        worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                result = task.getAsBoolean();
                return null;
            }

            @Override
            protected void done() {
                dispose();
            }
        };

        createUI();
        pack();
        setLocationRelativeTo(builder.getParent());
    }


    private void createUI() {
        Container contentPane = getContentPane();
        GridBagLayout layout = new GridBagLayout();
        contentPane.setLayout(layout);

        JLabel label = new JLabel(message);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.LINE_START;
        contentPane.add(label, c);

        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(420, 20));
        c.insets = new Insets(0, 10, 0, 10);
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(progressBar, c);

        JButton cancelButton = new JButton(new AbstractAction("Cancel") {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                cancelTask();
            }
        });
        cancelButton.setEnabled(canCancel);
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.NONE;
        contentPane.add(cancelButton, c);
    }

    private void cancelTask() {
        if (canCancel) {
            worker.cancel(true);
        }
    }

    /**
     * Start task and show dialog
     *
     * @return task result. {@code true} if finished successfully, {@code false} - otherwise
     */
    public boolean showDialog() {
        worker.execute();
        setVisible(true);
        return result;
    }
}
