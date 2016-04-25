package ru.tesei7.textEditor.editor.document.model;

import static junit.framework.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class LineTest {
	private Line l = new Line();

	@Test
	public final void testLinkWith() throws Exception {
		l.printChar('a');
		l.printChar('b');
	}

	@Test
	public final void testPrintChar() throws Exception {
		l.printChar('a');
		assertTrue(l.getChars().get(0).equals('a'));
		assertTrue(l.getLength() == 1);
		assertTrue(l.getOffset() == 1);
	}

	@Test
	public final void testDelete() throws Exception {
		l.printChar('a');
		l.delete();
		assertTrue(l.getChars().get(0).equals('a'));
		assertTrue(l.getLength() == 1);
		assertTrue(l.getOffset() == 1);

		l.setOffset(0);
		l.delete();
		assertTrue(l.getLength() == 0);
		assertTrue(l.getOffset() == 0);
	}

	@Test
	public final void testBackspace() throws Exception {
		l.printChar('a');
		l.backspace();
		assertTrue(l.getLength() == 0);
		assertTrue(l.getOffset() == 0);

		l.backspace();
		assertTrue(l.getLength() == 0);
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
	public void testSetChars() throws Exception {
		l.setChars(Arrays.asList('a', 'b', 'c'));

		assertThat(l.getChars()).containsExactly('a', 'b', 'c');
		assertTrue(l.getOffset() == 3);
	}

	@Test
	public void testInsertChar() throws Exception {
		l.setText(new char[] { 'a', 'b', 'c' });
		l.setOffset(1);
		l.insertChar('1');

		assertTrue(Arrays.equals(new char[] { 'a', '1', 'c' }, l.getText()));

		l.setOffset(3);
		l.insertChar('2');

		assertTrue(Arrays.equals(new char[] { 'a', '1', 'c', '2' }, l.getText()));
	}

	@Test
	public void testGetOffestByOffsetToPaint() throws Exception {
		l.setText(new char[] { 'a', 'b', 'c' });
		assertTrue(l.getOffestByOffsetToPaint(2) == 2);

		l.setText(new char[] { 'a', '\t', 'c' });
		assertTrue(l.getOffestByOffsetToPaint(4) == 1);
		assertTrue(l.getOffestByOffsetToPaint(5) == 2);
		assertTrue(l.getOffestByOffsetToPaint(-1) == 0);
		assertTrue(l.getOffestByOffsetToPaint(567) == 3);
	}

	@Test
	public void testGetOffsetToPaint() throws Exception {
		l.setText(new char[] { 'a', 'b', 'c' });
		l.setOffset(2);
		assertTrue(l.getOffsetToPaint() == 2);

		l.setText(new char[] { 'a', '\t', 'c' });
		l.setOffset(2);
		assertTrue(l.getOffsetToPaint() == 5);
	}

	@Test
	public void testToString() throws Exception {
		l.setText(new char[] { 'a', 'b', 'c' });
		assertEquals("abc", l.toString());

		l.setText(new char[] { 'a', '\t', 'c' });
		assertEquals("a	c", l.toString());
	}

	@Test
	public void testGetLengthToPaint() throws Exception {
		l.setText(new char[] { 'a', 'b', 'c' });
		assertTrue(l.getLengthToPaint() == 3);

		l.setText(new char[] { 'a', '\t', 'c' });
		assertTrue(l.getLengthToPaint() == 6);
	}

}
