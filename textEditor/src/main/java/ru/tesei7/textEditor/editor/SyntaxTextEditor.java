package ru.tesei7.textEditor.editor;

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
import ru.tesei7.textEditor.editor.document.dirtyState.DirtyStateListener;
import ru.tesei7.textEditor.editor.document.dirtyState.DirtyStateObservable;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocumentMemento;
import ru.tesei7.textEditor.editor.document.model.SyntaxTextEditorViewMode;
import ru.tesei7.textEditor.editor.frame.Direction;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollObservable;
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
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameEventType;
import ru.tesei7.textEditor.editor.scroll.FrameObservable;
import ru.tesei7.textEditor.editor.scroll.ScrollBarsManager;

/**
 * Editor with syntax highlighting.
 *
 * @author Ilya
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
     * Caretaker to store and restore state
     */
    private SyntaxTextEditorCaretaker caretaker = new SyntaxTextEditorCaretaker();
    /**
     * Can paint document content flag
     */
    volatile private boolean isReady = true;

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
    private SyntaxScrollObservable scrollObservable = new SyntaxScrollObservable();
    /**
     * Notify max height and width changes
     */
    private DimensionsObservable dimensionsObservable = new DimensionsObservable();
    /**
     * Notify first visible line and xOffset changes
     */
    private FrameObservable frameObservable = new FrameObservable();
    /**
     * Notify dirty state changes
     */
    private DirtyStateObservable dirtyObservable = new DirtyStateObservable();

    private CaretKeyListener caretKeyListener = new CaretKeyListener(caretObservable);
    private TextKeyListener textKeyListener = new TextKeyListener(documentEditObservable);
    private SyntaxMouseListener mouseListener = new SyntaxMouseListener(scrollObservable, frameObservable, caretObservable);
    private ScrollBarListener hScrollListener = new ScrollBarListener(scrollObservable, Direction.HORIZONTAL);
    private ScrollBarListener vScrollListener = new ScrollBarListener(scrollObservable, Direction.VERTICAL);

    private SyntaxDocumentEditor syntaxDocumentEditor;
    private SyntaxCaret caret;
    private SyntaxTextEditorFrame frame;
    private ScrollBarsManager scrollBarsManager;

    private FontProperties fontProperties;
    volatile private SyntaxDocumentPainter documentPainter;
    volatile private CaretPainter caretPainter;

    private GridBagLayout layout;
    private SyntaxTextPanel textPanel;
    private JScrollBar hBar;
    private JScrollBar vBar;

    public SyntaxTextEditor() {
        this(Language.PLAIN_TEXT, DEFAULT_ROWS, DEFAULT_COLS);
    }

    public SyntaxTextEditor(Language language, int rows, int cols) {
        super();

        this.document = new SyntaxDocument(frameObservable, dirtyObservable);
        document.setLanguage(language);

        createComponent();
        wireListeners();
        initUIListeners();

        document.setRows(rows);
        document.setCols(cols);
        recalculateSize();
    }

    protected void wireListeners() {
        this.syntaxDocumentEditor = new SyntaxDocumentEditor(document, caretObservable, dimensionsObservable);
        this.caret = new SyntaxCaret(document, fontProperties);
        this.frame = new SyntaxTextEditorFrame(document);
        this.scrollBarsManager = new ScrollBarsManager(document, hBar, vBar);
        this.caretPainter = new CaretPainter(document);
        this.documentPainter = new SyntaxDocumentPainter(document, fontProperties);

        caretObservable.removeAllListeners();
        caretObservable.addListener(caret);
        caretObservable.addListener(frame);
        caretObservable.addListener(textPanel);

        documentEditObservable.removeAllListeners();
        documentEditObservable.addListener(syntaxDocumentEditor);
        documentEditObservable.addListener(textPanel);

        scrollObservable.removeAllListeners();
        scrollObservable.addListener(frame);
        scrollObservable.addListener(textPanel);

        dimensionsObservable.removeAllListeners();
        dimensionsObservable.addListener(scrollBarsManager);
        frameObservable.removeAllListeners();
        frameObservable.addListener(scrollBarsManager);
        frameObservable.addListener(textPanel);
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

        hBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, getCols(), 0, getCols());
        vBar = new JScrollBar(JScrollBar.VERTICAL, 0, getRows(), 0, getRows());
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weighty = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(hBar, c);
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;
        c.weightx = 0;
        c.fill = GridBagConstraints.VERTICAL;
        add(vBar, c);

        recalculateSize();
    }

    protected void initUIListeners() {
        textPanel.addKeyListener(textKeyListener);
        textPanel.addKeyListener(caretKeyListener);
        textPanel.addMouseListener(mouseListener);
        textPanel.addMouseWheelListener(mouseListener);
        textPanel.addMouseMotionListener(mouseListener);

        hBar.addAdjustmentListener(hScrollListener);
        vBar.addAdjustmentListener(vScrollListener);
    }

    /**
     * @return {@code true} if can paint document, {@code false} - otherwise
     */
    public boolean isReady() {
        return isReady;
    }

    /**
     * Get editor text
     *
     * @return string, contains editor text with {@code \n} as line separator
     */
    public String getText() {
        return document.getText();
    }

    /**
     * Set editor text without syntax highlighting. {@code \n} is line separator
     *
     * @param text text to show in editor
     */
    public boolean setText(String text) {
        return setText(text, Language.PLAIN_TEXT);
    }

    /**
     * Set editor text and syntax type
     *
     * @param text     text to show
     * @param language syntax
     * @return {@code true} if text was set, {@code false} if operation was cancelled
     */
    public boolean setText(String text, Language language) {
        isReady = false;
        caretaker.setMemento(saveState());
        boolean wasInterrupted = false;
        try {
            document = new SyntaxDocument(frameObservable, dirtyObservable);
            document.setLanguage(language);
            document.setText(text);
            wireListeners();
            //update title
            document.setDirty(true);
            document.setDirty(false);
        } catch (InterruptedException e) {
            restoreState();
            wasInterrupted = true;
        } finally {
            isReady = true;

            //update frame
            dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
            caretObservable.notifyListeners(new SyntaxCaretEvent());
            textPanel.repaint();
        }
        return !wasInterrupted;
    }

    private SyntaxDocumentMemento saveState() {
        return new SyntaxDocumentMemento(document);
    }

    private void restoreState() {
        SyntaxDocumentMemento memento = caretaker.getMemento();
        if (memento != null) {
            document = memento.getState();
            wireListeners();
        }
    }


    /**
     * @return current editor view mode
     */
    public SyntaxTextEditorViewMode getViewMode() {
        return document.getViewMode();
    }

    /**
     * Set editor view mode
     *
     * @param mode view mode
     */
    public void setViewMode(SyntaxTextEditorViewMode mode) {
        document.setViewMode(mode);
        dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
        caretObservable.notifyListeners(new SyntaxCaretEvent());
    }

    public Language getLanguage() {
        return document.getLanguage();
    }

    public boolean setLanguage(Language language) {
        isReady = false;
        try {
            document.setLanguage(language);
            document.recalculateTokens(0, document.getSize());
            textPanel.repaint();
        } finally {
            isReady = true;
        }
        //TODO cancellable action
        return true;
    }

    // Size

    /**
     * @return number of lines to show
     */
    public int getRows() {
        return document.getRows();
    }

    /**
     * Set visible number of lines
     *
     * @param rows number of lines
     */
    public void setRows(int rows) {
        document.setRows(rows);
        recalculateSize();
        textPanel.repaint();
    }

    /**
     * @return number of columns to show
     */
    public int getCols() {
        return document.getCols();
    }

    /**
     * Set visible number of columns
     *
     * @param cols number of columns
     */
    public void setCols(int cols) {
        document.setCols(cols);
        recalculateSize();
        textPanel.repaint();
    }

    public int getMaxCols() {
        return document.getMaxCols();
    }

    public void setMaxCols(int maxCols) {
        document.setMaxCols(maxCols);

        dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.ONLY_X));
        caretObservable.notifyListeners(new SyntaxCaretEvent());
        textPanel.repaint();
    }

    /**
     * Recalculate number of rows and columns to show depending on current size
     */
    public void resizeToFitParent() {
        int rows = textPanel.getHeight() / fontProperties.getLineHeight();
        int cols = textPanel.getWidth() / fontProperties.getCharWidth();
        document.setRows(rows);
        document.setCols(cols);
        recalculateSize();
        dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
        caretObservable.notifyListeners(new SyntaxCaretEvent());
        frameObservable.notifyListeners(new FrameEvent(FrameEventType.HORIZONTAL, document.getFirstVisibleCol()));
        frameObservable.notifyListeners(new FrameEvent(FrameEventType.VERTICAL, document.getFirstVisibleRow()));
    }

    protected void recalculateSize() {
        FontMetrics fm = textPanel.getFontMetrics(textPanel.getFont());
        fontProperties = new FontProperties(fm.charWidth('a'), fm.getHeight(), fm.getDescent());
        int height = fontProperties.getLineHeight() * getRows();
        int width = fontProperties.getCharWidth() * getCols() + CARET_WIDTH;
        layout.columnWidths = new int[]{width, vBar.getWidth()};
        layout.rowHeights = new int[]{height, hBar.getHeight()};
        setLayout(layout);
    }

    // Dirty state

    /**
     * Was text edited since last save or not
     *
     * @return dirty flag
     */
    public boolean isDirty() {
        return document.isDirty();
    }

    /**
     * Set dirty state to false
     */
    public void clearDirty() {
        document.setDirty(false);
    }

    public void addDirtyStateListener(DirtyStateListener listener) {
        dirtyObservable.addListener(listener);
    }

    public void removeDirtyStateListener(DirtyStateListener listener) {
        dirtyObservable.removeListener(listener);
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
