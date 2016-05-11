package ru.tesei7.textEditor.editor.document;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import ru.tesei7.textEditor.editor.caret.SyntaxCaretEvent;
import ru.tesei7.textEditor.editor.caret.SyntaxCaretObservable;
import ru.tesei7.textEditor.editor.document.model.Line;
import ru.tesei7.textEditor.editor.document.model.SyntaxDocument;
import ru.tesei7.textEditor.editor.scroll.DimensionType;
import ru.tesei7.textEditor.editor.scroll.DimensionsEvent;
import ru.tesei7.textEditor.editor.scroll.DimensionsObservable;

public class SyntaxDocumentEditor implements DocumentEditListener {

	private SyntaxDocument document;
	SyntaxCaretObservable caretObservable;
	DimensionsObservable dimensionsObservable;

	public SyntaxDocumentEditor(SyntaxDocument document, SyntaxCaretObservable syntaxCaretObservable,
			DimensionsObservable dimensionsObservable) {
		this.document = document;
		this.caretObservable = syntaxCaretObservable;
		this.dimensionsObservable = dimensionsObservable;
	}

	@Override
	public void onDocumentEdited(DocumentEditEvent e) {
		switch (e.getType()) {
		case BACKSPACE:
			backspace();
			break;
		case DELETE:
			delete();
			break;
		case PRINT_CHAR:
			printChar(e.getChar());
			break;
		case NEW_LINE:
			addNewLine();
			break;
		case COPY:
			copy();
			break;
		case COPY_REMOVE:
			copy();
			removeSelection();
			break;
		case PASTE:
			paste();
			break;
		default:
			break;
		}
		document.setDirty(true);
	}

	// COPY / PASTE

	void copy() {
		StringSelection contents = new StringSelection(document.getSelectedString());
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, null);
	}

	void paste() {
		if (document.getSelection().notSelected()) {
			pasteCaret();
		} else {
			removeSelection();
			pasteCaret();
		}
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		caretObservable.notifyListeners(new SyntaxCaretEvent());
	}

	void pasteCaret() {
		String bufferString = getBufferString();
		if (bufferString.isEmpty()) {
			return;
		}
		// -1 in split methods used to not omit \n at the end of file
		String[] split = bufferString.split("\n", -1);

		Line line = document.getCurrentLine();
		List<Character> curLineChars = line.getChars();
		List<Character> afterCaret = curLineChars.subList(line.getOffset(), line.getLength());

		LinkedList<Line> newLines = new LinkedList<>();
		for (int i = 0; i < split.length; i++) {
			char[] chars = split[i].toCharArray();
			if (i == 0) {
				line.printChars(chars);
			} else {
				Line newLine = new Line();
				newLine.setText(chars);
				newLines.add(newLine);
			}
		}
		if (split.length > 1) {
			int length = newLines.getLast().getLength();
			newLines.getLast().printChars(afterCaret);
			newLines.getLast().setOffset(length);
			document.addLinesAfter(document.getCurLineIndex(), newLines);
			document.setCurLineIndex(document.getCurLineIndex() + newLines.size());
		}

		document.recalculateTokens(document.getCurLineIndex() - split.length, split.length + 1);
	}

	private String getBufferString() {
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
			} catch (UnsupportedFlavorException | IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	// PRINT CHAR

	void printChar(char c) {
		if (document.getSelection().notSelected()) {
			printCharCaret(c);
		} else {
			printCharSelection(c);
		}
	}

	void printCharSelection(char c) {
		document.removeSelection();
		document.getCurrentLine().printChar(c);
		document.recalculateTokens(document.getCurLineIndex(), 1);
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		caretObservable.notifyListeners(new SyntaxCaretEvent());
	}

	void printCharCaret(char c) {
		switch (document.getCaretType()) {
		case NORMAL:
			document.getCurrentLine().printChar(c);
			break;
		case INSERT:
			document.getCurrentLine().insertChar(c);
			break;
		default:
			break;
		}

		document.recalculateTokens(document.getCurLineIndex(), 1);
		// dimensions should be changed first
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.ONLY_X));
		caretObservable.notifyListeners(new SyntaxCaretEvent());
	}

	// ADD LINE

	void addNewLine() {
		if (document.getSelection().notSelected()) {
			addNewLineCaret();
		} else {
			addNewLineSelection();
		}
	}

	void addNewLineSelection() {
		document.removeSelection();
		addNewLineCaret();
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		caretObservable.notifyListeners(new SyntaxCaretEvent());
	}

	void addNewLineCaret() {
		Line currentLine = document.getCurrentLine();
		Line newLine = new Line();

		document.addLineAfter(document.getCurLineIndex(), newLine);

		int offset = currentLine.getOffset();
		List<Character> curLineText = currentLine.getChars();
		newLine.setChars(curLineText.subList(offset, curLineText.size()));
		currentLine.setChars(curLineText.subList(0, offset));

		document.recalculateTokens(document.getCurLineIndex(), 2);
		// dimensions should be changed first
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		document.moveCurLineIndex(1);
		newLine.setOffset(0);
		caretObservable.notifyListeners(new SyntaxCaretEvent());
	}

	// DELETE & BACKSPACE

	void delete() {
		if (document.getSelection().notSelected()) {
			deleteChar();
		} else {
			removeSelection();
		}
	}

	void deleteChar() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == currentLine.getLength()) {
			int curLineIndex = document.getCurLineIndex();
			concatLines(curLineIndex, curLineIndex + 1, false);
			document.recalculateTokens(document.getCurLineIndex(), 2);
		} else {
			currentLine.delete();
			document.recalculateTokens(document.getCurLineIndex(), 1);
			dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.ONLY_X));
		}
	}

	void backspace() {
		if (document.getSelection().notSelected()) {
			backspaceChar();
		} else {
			removeSelection();
		}
	}

	void backspaceChar() {
		Line currentLine = document.getCurrentLine();
		if (currentLine.getOffset() == 0) {
			int curLineIndex = document.getCurLineIndex();
			concatLines(curLineIndex - 1, curLineIndex, true);
			document.recalculateTokens(document.getCurLineIndex(), 2);
		} else {
			currentLine.backspace();
			document.recalculateTokens(document.getCurLineIndex(), 1);
			// dimensions should be changed first
			dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.ONLY_X));
			caretObservable.notifyListeners(new SyntaxCaretEvent());
		}
	}

	void removeSelection() {
		document.removeSelection();
		document.recalculateTokens(document.getCurLineIndex(), 2);
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		caretObservable.notifyListeners(new SyntaxCaretEvent());
	}

	void concatLines(int l1Index, int l2Index, boolean moveCaretUp) {
		if (!document.isCorrectLineIndex(l1Index) || !document.isCorrectLineIndex(l2Index)) {
			return;
		}
		Line l1 = document.getLineByIndex(l1Index);
		Line l2 = document.getLineByIndex(l2Index);

		int l1Length = l1.getLength();
		LinkedList<Character> concat = new LinkedList<>();
		concat.addAll(l1.getChars());
		concat.addAll(l2.getChars());
		l1.setChars(concat);
		document.removeLineAfter(l1Index);

		// dimensions should be changed first
		dimensionsObservable.notifyListeners(new DimensionsEvent(DimensionType.X_AND_Y));
		l1.setOffset(l1Length);
		if (moveCaretUp) {
			document.moveCurLineIndex(-1);
			caretObservable.notifyListeners(new SyntaxCaretEvent());
		}

	}

}
