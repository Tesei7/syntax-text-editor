package ru.tesei7.textEditor.editor.document.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import ru.tesei7.textEditor.editor.document.model.Line;

public class LineTest {
	private Line l = new Line();
	private Line l2 = new Line();

	@Test
	public final void testLinkWith() throws Exception {
		l.printChar('a');
		l.printChar('b');
		l.linkWith(null);
		assertFalse(l.hasNext());

		l2.printChar('c');
		l.linkWith(l2);
		assertTrue(l.equals(l2.getPrevious()));
		assertTrue(l2.equals(l.getNext()));
	}

	@Test
	public final void testPrintChar() throws Exception {
		l.printChar('a');
		assertTrue(l.getChars().get(0).equals('a'));
		assertTrue(l.getLenght() == 1);
		assertTrue(l.getOffset() == 1);
	}

	@Test
	public final void testDelete() throws Exception {
		l.printChar('a');
		l.delete();
		assertTrue(l.getChars().get(0).equals('a'));
		assertTrue(l.getLenght() == 1);
		assertTrue(l.getOffset() == 1);

		l.setOffset(0);
		l.delete();
		assertTrue(l.getLenght() == 0);
		assertTrue(l.getOffset() == 0);
	}

	@Test
	public final void testBackspace() throws Exception {
		l.printChar('a');
		l.backspace();
		assertTrue(l.getLenght() == 0);
		assertTrue(l.getOffset() == 0);

		l.backspace();
		assertTrue(l.getLenght() == 0);
		assertTrue(l.getOffset() == 0);
	}

	@Test
	public void testSetOffset() throws Exception {
		l.setOffset(1);
		assertTrue(l.getOffset() == 0);

		l.printChar('a');
		l.setOffset(1);
		assertTrue(l.getOffset() == 1);

		l.setOffset(-1);
		assertTrue(l.getOffset() == 0);
	}

	@Test
	public void testGetCharsToShow() throws Exception {
		assertTrue(Arrays.equals(new char[0], l.getCharsToShow()));

		l.printChar('a');
		assertTrue(Arrays.equals(new char[] { 'a' }, l.getCharsToShow()));

		l.printChar('\t');
		assertTrue(Arrays.equals(new char[] { 'a', ' ', ' ', ' ', ' ' }, l.getCharsToShow()));
	}

	@Test
	public void testSetChars() throws Exception {
		l.setChars(Arrays.asList('a', 'b', 'c'));

		assertThat(l.getChars()).containsExactly('a', 'b', 'c');
		assertTrue(l.getOffset() == 3);
	}

}
