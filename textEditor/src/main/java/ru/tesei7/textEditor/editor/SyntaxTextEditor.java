package ru.tesei7.textEditor.editor;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import ru.tesei7.textEditor.editor.caret.SyntaxCaret;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.DocumentEditObservable;
import ru.tesei7.textEditor.editor.document.SyntaxDocumentEditor;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.document.model.SyntaxTextEditorViewMode;
import ru.tesei7.textEditor.editor.frame.Direction;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollObserverable;
import ru.tesei7.textEditor.editor.frame.SyntaxTextEditorFrame;
import ru.tesei7.textEditor.editor.listeners.CaretKeyListener;
import ru.tesei7.textEditor.editor.listeners.ScrollBarListener;
import ru.tesei7.textEditor.editor.listeners.SyntaxMouseListener;
import ru.tesei7.textEditor.editor.listeners.TextKeyListener;
import ru.tesei7.textEditor.editor.painter.CaretPainter;
import ru.tesei7.textEditor.editor.painter.SyntaxDocumentPainter;
import ru.tesei7.textEditor.editor.scroll.DimensionType;
import ru.tesei7.textEditor.editor.scroll.DimensionsEvent;
import ru.tesei7.textEditor.editor.scroll.DimensionsObservable;
import ru.tesei7.textEditor.editor.scroll.FrameObserverable;
import ru.tesei7.textEditor.editor.scroll.ScrollBarsManager;

/**
 * Editor with syntax highlighting.
 * 
 * @author Ilya
 *
 */
public class SyntaxTextEditor extends JPanel {
	private static final long serialVersionUID = 1485541136343010484L;
	/**
	 * Number of spaces of '\t' character representation
	 */
	public static final int TAB_INDENT = 4;
	/**
	 * Frequency of caret blinking in ms
	 */
	public static final int CARET_BLINK_PERIOD = 500;
	/**
	 * Default caret width in pixels
	 */
	public static final int CARET_WIDTH = 2;
	/**
	 * Number of lines to scroll with mouse wheel turn
	 */
	public static final int MOUSE_WHEEL_SCROLL_LINES = 3;
	/**
	 * Initial maximum number of lines for new document
	 */
	public static final int DEFAULT_LINES_COUNT = 1000000;
	/**
	 * Default number of visible lines
	 */
	public static final int DEFAULT_ROWS = 30;
	/**
	 * Default number of visible columns
	 */
	public static final int DEFAULT_COLS = 100;
	/**
	 * Default maximum columns number in fixed width mode
	 */
	public static final int DEFAULT_MAX_COLS = 256;

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

	private CaretKeyListener caretKeyListener = new CaretKeyListener(caretObservable);
	private TextKeyListener textKeyListener = new TextKeyListener(documentEditObservable);
	private SyntaxMouseListener mouseListener;
	private ScrollBarListener hScrollListener = new ScrollBarListener(scrollObserverable, Direction.HORIZONTAL);
	private ScrollBarListener vScrollListener = new ScrollBarListener(scrollObserverable, Direction.VERTICAL);

	private SyntaxDocumentEditor syntaxDocumentEditor;
	private SyntaxCaret caret;
	private SyntaxTextEditorFrame frame;
	private ScrollBarsManager scrollBarsManager;

	private FontProperties fontProperties;
	private SyntaxDocumentPainter documentPainter;
	private CaretPainter caretPainter;

	private GridBagLayout layout;
	private SyntaxTextPanel textPanel;
	private JScrollBar hbar;
	private JScrollBar vbar;

	public SyntaxTextEditor() {
		this(Language.PLAIN_TEXT, DEFAULT_ROWS, DEFAULT_COLS);
	}

	public SyntaxTextEditor(Language language, int rows, int cols) {
		super();

		this.document = new SyntaxDocument(frameObserverable);
		document.setLanguage(language);

		createComponent();
		wireListeners();
		initUIListeners();

		document.setRows(rows);
		document.setCols(cols);
		recalcSize();
	}

	protected void wireListeners() {
		this.syntaxDocumentEditor = new SyntaxDocumentEditor(document, caretObservable, dimensionsObservable);
		this.caret = new SyntaxCaret(document, fontProperties);
		this.frame = new SyntaxTextEditorFrame(document);
		this.scrollBarsManager = new ScrollBarsManager(document, hbar, vbar);
		this.caretPainter = new CaretPainter(document);
		this.documentPainter = new SyntaxDocumentPainter(document, fontProperties);
		this.mouseListener = new SyntaxMouseListener(document, caretObservable);

		caretObservable.addListener(caret);
		caretObservable.addListener(frame);
		caretObservable.addListener(textPanel);

		documentEditObservable.addListener(syntaxDocumentEditor);
		documentEditObservable.addListener(textPanel);

		scrollObserverable.addListener(frame);
		scrollObserverable.addListener(textPanel);

		dimensionsObservable.addListener(scrollBarsManager);
		frameObserverable.addListener(scrollBarsManager);
		frameObserverable.addListener(textPanel);
	}

	protected void createComponent() {
		layout = new GridBagLayout();
		setLayout(layout);

		textPanel = new SyntaxTextPanel(this);
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		add(textPanel, c);

		hbar = new JScrollBar(JScrollBar.HORIZONTAL, 0, getCols(), 0, getCols());
		vbar = new JScrollBar(JScrollBar.VERTICAL, 0, getRows(), 0, getRows());
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.PAGE_END;
		c.weighty = 0;
		c.fill = GridBagConstraints.HORIZONTAL;
		add(hbar, c);
		c.gridx = 1;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		c.weightx = 0;
		c.fill = GridBagConstraints.VERTICAL;
		add(vbar, c);

		recalcSize();
	}

	protected void initUIListeners() {
		textPanel.addKeyListener(textKeyListener);
		textPanel.addKeyListener(caretKeyListener);
		textPanel.addMouseListener(mouseListener);
		textPanel.addMouseWheelListener(mouseListener);
		textPanel.addMouseMotionListener(mouseListener);

		hbar.addAdjustmentListener(hScrollListener);
		vbar.addAdjustmentListener(vScrollListener);
	}

	public String getText() {
		return document.getText();
	}
	
	public void setText(String text) {
		this.setText(text, Language.PLAIN_TEXT);
	}

	public void setText(String text, Language language) {
		document.setLanguage(language);
		document.setText(text);
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		caretObservable.notifyListeners(new SyntaxCaretEvent());
		textPanel.repaint();
	}

	public SyntaxTextEditorViewMode getViewMode() {
		return document.getViewMode();
	}

	public void setViewMode(SyntaxTextEditorViewMode mode) {
		document.setViewMode(mode);
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		caretObservable.notifyListeners(new SyntaxCaretEvent());
	}

	public Language getLanguage() {
		return document.getLanguage();
	}

	public void setLanguage(Language language) {
		document.setLanguage(language);
		document.recalcTokens(0, document.getSize());
		textPanel.repaint();
	}

	// Size

	public int getRows() {
		return document.getRows();
	}

	public void setRows(int rows) {
		document.setRows(rows);
		recalcSize();
		textPanel.repaint();
	}

	public int getCols() {
		return document.getCols();
	}

	public void setCols(int cols) {
		document.setCols(cols);
		recalcSize();
		textPanel.repaint();
	}

	public void setTextAreaSize(Dimension size) {
		int rows = (size.height - 87) / fontProperties.getLineHeight();
		int cols = (size.width - 37) / fontProperties.getCharWidth();
		document.setRows(rows);
		document.setCols(cols);
		recalcSize();
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		caretObservable.notifyListeners(new SyntaxCaretEvent());
	}

	protected void recalcSize() {
		FontMetrics fm = textPanel.getFontMetrics(textPanel.getFont());
		fontProperties = new FontProperties(fm.charWidth('a'), fm.getHeight(), fm.getDescent());
		int height = fontProperties.getLineHeight() * getRows();
		int width = fontProperties.getCharWidth() * getCols() + CARET_WIDTH;
		layout.columnWidths = new int[] { width, vbar.getWidth() };
		layout.rowHeights = new int[] { height, hbar.getHeight() };
		setLayout(layout);
	}

	// Painters

	CaretPainter getCaretPainter() {
		return caretPainter;
	}

	SyntaxDocumentPainter getDocumentPainter() {
		return documentPainter;
	}
	
	SyntaxDocument getDocument() {
		return document;
	}

}
