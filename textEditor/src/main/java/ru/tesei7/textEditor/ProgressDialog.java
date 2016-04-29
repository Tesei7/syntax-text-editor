package ru.tesei7.textEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class ProgressDialog extends JDialog {
	private static final long serialVersionUID = -2001379600243197970L;

	public ProgressDialog(JFrame frame, String title, Runnable task) {
		super(frame, title, true);

		JProgressBar pb = new JProgressBar(0, 100);
		pb.setIndeterminate(true);
		pb.setPreferredSize(new Dimension(300, 20));
		
		add(BorderLayout.CENTER, pb);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();		

		SwingWorker<Void, Void> sw = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				task.run();
				return null;
			}

			@Override
			protected void done() {
				dispose();
			}
		};

		sw.execute();
		setVisible(true);
	}
}
