package ru.tesei7.textEditor.editor;

import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

import ru.tesei7.textEditor.editor.caret.SyntaxCaret;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.DocumentEditObservable;
import ru.tesei7.textEditor.editor.document.SyntaxDocumentEditor;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
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
	JScrollBar hbar;
	JScrollBar vbar;

	public SyntaxTextEditor() {
		super();

		this.document = new SyntaxDocument(frameObserverable);

		createComponent();

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

		initUIListeners();
	}

	protected void createComponent() {
		layout = new GridBagLayout();
		setLayout(layout);

		textPanel = new SyntaxTextPanel(this);
		GridBagConstraints textPanelConstraints = new GridBagConstraints();
		textPanelConstraints.gridx = 0;
		textPanelConstraints.gridy = 0;
		textPanelConstraints.fill = GridBagConstraints.BOTH;
		add(textPanel, textPanelConstraints);

		hbar = new JScrollBar(JScrollBar.HORIZONTAL, 0, getCols(), 0, getCols());
		vbar = new JScrollBar(JScrollBar.VERTICAL, 0, getRows(), 0, getRows());
		GridBagConstraints hbarConstraints = new GridBagConstraints();
		hbarConstraints.gridx = 0;
		hbarConstraints.gridy = 1;
		hbarConstraints.fill = GridBagConstraints.HORIZONTAL;
		add(hbar, hbarConstraints);
		GridBagConstraints vbarConstraints = new GridBagConstraints();
		vbarConstraints.gridx = 1;
		vbarConstraints.gridy = 0;
		vbarConstraints.fill = GridBagConstraints.VERTICAL;
		add(vbar, vbarConstraints);
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
		document.setText(text);
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		textPanel.repaint();
	}

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

	protected void recalcSize() {
		FontMetrics fm = textPanel.getFontMetrics(textPanel.getFont());
		fontProperties = new FontProperties(fm.charWidth('a'), fm.getHeight(), fm.getDescent());
		int height = fontProperties.getLineHeight() * getRows();
		int width = fontProperties.getCharWidth() * getCols() + CARET_WIDTH;
		layout.columnWidths = new int[] { width, 15 };
		layout.rowHeights = new int[] { height, 15 };
	}

	CaretPainter getCaretPainter() {
		return caretPainter;
	}

	SyntaxDocumentPainter getDocumentPainter() {
		return documentPainter;
	}

}
