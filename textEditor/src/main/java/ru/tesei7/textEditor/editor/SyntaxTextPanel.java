package ru.tesei7.textEditor.editor;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.Timer;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretListener;
import ru.tesei7.textEditor.editor.document.DocumentEditEvent;
import ru.tesei7.textEditor.editor.document.DocumentEditListener;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollEvent;
import ru.tesei7.textEditor.editor.frame.SyntaxScrollListener;
import ru.tesei7.textEditor.editor.scroll.FrameEvent;
import ru.tesei7.textEditor.editor.scroll.FrameListener;
import ru.tesei7.textEditor.editor.utils.Fonts;

/**
 * Text editor component
 *
 * @author Ilya
 */
class SyntaxTextPanel extends JPanel
        implements SyntaxCaretListener, DocumentEditListener, SyntaxScrollListener, FrameListener {
    private static final long serialVersionUID = -8378730572715369064L;

    private SyntaxTextEditor editor;

    /**
     * Flag of caret visibility for blinking caret
     */
    private boolean caretVisible = true;
    /**
     * Do not blink caret if edited
     */
    private boolean skipNextBlink = false;

    SyntaxTextPanel(SyntaxTextEditor editor) {
        this.editor = editor;

        setBackground(Color.WHITE);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setFont(Fonts.DEFAULT);
        setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        initCaretBlinker();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (editor.isReady()) {
            editor.getCaretPainter().paintBackground(g);
            editor.getCaretPainter().highlightBrackets(g);
            editor.getDocumentPainter().paint(g, editor.getLanguage());
            editor.getCaretPainter().paintCaret(g, caretVisible);
        }
    }

    private void initCaretBlinker() {
        Timer timer = new Timer(SyntaxTextEditor.CARET_BLINK_PERIOD, this::blinkCaretAndRepaint);
        timer.start();
    }

    private void blinkCaretAndRepaint(ActionEvent e) {
        if (!skipNextBlink) {
            //Blink caret
            caretVisible = !caretVisible;
        }
        repaint();
        skipNextBlink = false;
    }

    private void freezeCaretAndRepaint() {
        freezeCaret();
        repaint();
    }

    /**
     * Prevent caret from blinking while text editing
     */
    private void freezeCaret() {
        caretVisible = true;
        skipNextBlink = true;
    }

    @Override
    public void onCaretChanged(SyntaxCaretEvent e) {
        freezeCaretAndRepaint();
    }

    @Override
    public void onDocumentEdited(DocumentEditEvent e) {
        freezeCaretAndRepaint();
    }

    @Override
    public void onScrollChanged(SyntaxScrollEvent e) {
        freezeCaretAndRepaint();
    }

    @Override
    public void onFrameChanged(FrameEvent e) {
        freezeCaretAndRepaint();
    }
}
