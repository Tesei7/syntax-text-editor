package ru.tesei7.textEditor.editor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.Timer;

import ru.tesei7.textEditor.editor.caret.SyntaxCaret;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretListener;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.DocumentEditEvent;
import ru.tesei7.textEditor.editor.document.DocumentEditListener;
import ru.tesei7.textEditor.editor.document.DocumentEditObservable;
import ru.tesei7.textEditor.editor.document.SyntaxDocumentEditor;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.listeners.ScrollBarListener;
import ru.tesei7.textEditor.editor.listeners.key.CaretKeyListener;
import ru.tesei7.textEditor.editor.listeners.key.TextKeyListener;
import ru.tesei7.textEditor.editor.painter.CaretPainter;
import ru.tesei7.textEditor.editor.painter.SyntaxDocumentPainter;
import ru.tesei7.textEditor.editor.scroll.Direction;
import ru.tesei7.textEditor.editor.scroll.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.scroll.SyntaxScrollListener;
import ru.tesei7.textEditor.editor.scroll.SyntaxScrollObserverable;
import ru.tesei7.textEditor.editor.scroll.SyntaxTextEditorFrame;
import ru.tesei7.textEditor.editor.scroll.bar.DimensionType;
import ru.tesei7.textEditor.editor.scroll.bar.DimensionsEvent;
import ru.tesei7.textEditor.editor.scroll.bar.DimensionsObservable;
import ru.tesei7.textEditor.editor.scroll.bar.FrameObserverable;
import ru.tesei7.textEditor.editor.scroll.bar.ScrollBarsManager;
import ru.tesei7.textEditor.editor.utils.FontUtils;

/**
 * Editor with syntax highlighting.
 * 
 * @author Ilya
 *
 */
public class SyntaxTextEditor extends JPanel
		implements SyntaxCaretListener, DocumentEditListener, SyntaxScrollListener {
	private static final long serialVersionUID = 1485541136343010484L;
	public static final int TAB_INDENT = 4;

	/**
	 * Data model to store text and representation information
	 */
	private SyntaxDocument document;

	/**
	 * Notify caret changes
	 */
	private SyntaxCaretObservable caretObservable = new SyntaxCaretObservable();
	/**
	 * Notify document changes
	 */
	private DocumentEditObservable documentEditObservable = new DocumentEditObservable();
	/**
	 * Notify scroll changes
	 */
	private SyntaxScrollObserverable scrollObserverable = new SyntaxScrollObserverable();
	/**
	 * Notify max height and width changes
	 */
	private DimensionsObservable dimensionsObservable = new DimensionsObservable();
	/**
	 * Notify first visible line and xOffset changes
	 */
	private FrameObserverable frameObserverable = new FrameObserverable();

	protected CaretKeyListener caretKeyListener = new CaretKeyListener(caretObservable);
	protected TextKeyListener textKeyListener = new TextKeyListener(documentEditObservable);
	protected ScrollBarListener hScrollListener = new ScrollBarListener(scrollObserverable, Direction.HORIZONTAL);
	protected ScrollBarListener vScrollListener = new ScrollBarListener(scrollObserverable, Direction.VERTICAL);

	private SyntaxDocumentEditor syntaxDocumentEditor;
	private SyntaxCaret caret;
	private SyntaxTextEditorFrame frame;
	private ScrollBarsManager scrollBarsManager;

	private SyntaxDocumentPainter documentPainter;
	private CaretPainter caretPainter;

	JScrollBar hbar;
	JScrollBar vbar;

	/**
	 * Flag of caret visibility for blinking caret
	 */
	volatile private boolean caretVisible = true;
	/**
	 * Do not blink caret if edited
	 */
	volatile private boolean skipNextBlink = false;

	public SyntaxTextEditor() {
		super();

		this.document = new SyntaxDocument(frameObserverable);

		createComponent();

		this.syntaxDocumentEditor = new SyntaxDocumentEditor(document, caretObservable, dimensionsObservable);
		this.caret = new SyntaxCaret(document, caretObservable);
		this.frame = new SyntaxTextEditorFrame(document);
		this.scrollBarsManager = new ScrollBarsManager(document, hbar, vbar);
		this.caretPainter = new CaretPainter(caret);
		this.documentPainter = new SyntaxDocumentPainter(document);

		caretObservable.addListener(caret);
		caretObservable.addListener(frame);
		caretObservable.addListener(this);

		documentEditObservable.addListener(syntaxDocumentEditor);
		documentEditObservable.addListener(this);

		scrollObserverable.addListener(frame);
		scrollObserverable.addListener(this);

		dimensionsObservable.addListener(scrollBarsManager);
		frameObserverable.addListener(scrollBarsManager);

		Timer timer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!skipNextBlink) {
					toggleCaretVisible();
				}
				SyntaxTextEditor.this.repaint();
				skipNextBlink = false;
			}
		});
		timer.start();

		initUIListeners();
	}

	void toggleCaretVisible() {
		this.caretVisible = !caretVisible;
	}

	private void createComponent() {
		recalcSize();

		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		setBackground(Color.WHITE);
		setFont(FontUtils.DEFAULT);

		setLayout(new BorderLayout());
		hbar = new JScrollBar(JScrollBar.HORIZONTAL, 0, getCols(), 0, getCols());
		vbar = new JScrollBar(JScrollBar.VERTICAL, 0, getRows(), 0, getRows());
		add(hbar, BorderLayout.SOUTH);
		add(vbar, BorderLayout.EAST);
	}

	private void initUIListeners() {
		addKeyListener(textKeyListener);
		addKeyListener(caretKeyListener);
		hbar.addAdjustmentListener(hScrollListener);
		vbar.addAdjustmentListener(vScrollListener);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		caretPainter.paint(g, caretVisible);
		documentPainter.paint(g);
	}

	public String getText() {
		return document.getText();
	}

	public void setText(String text) {
		document.setText(text);
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		repaint();
	}

	public int getRows() {
		return document.getRows();
	}

	public void setRows(int rows) {
		document.setRows(rows);
		recalcSize();
		repaint();
	}

	public int getCols() {
		return document.getCols();
	}

	public void setCols(int cols) {
		document.setCols(cols);
		recalcSize();
		repaint();
	}

	void recalcSize() {
		FontMetrics fm = getFontMetrics(getFont());
		int height = fm.getHeight() * (getRows() + 1) + fm.getDescent();
		int width = fm.charWidth('a') * getCols();
		setPreferredSize(new Dimension(width + 100, height + 160));
	}

	private void caretFreze() {
		caretVisible = true;
		skipNextBlink = true;
	}

	@Override
	public void onCaretChanged(SyntaxCaretEvent e) {
		caretFreze();
		repaint();
	}

	@Override
	public void onDocumentEdited(DocumentEditEvent e) {
		caretFreze();
		repaint();
	}

	@Override
	public void onScrollChanged(SyntaxScrollEvent e) {
		caretFreze();
		repaint();
	}

}
