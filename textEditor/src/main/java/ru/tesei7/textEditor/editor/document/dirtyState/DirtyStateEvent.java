package ru.tesei7.textEditor.editor.document.dirtyState;

public class DirtyStateEvent {
	private boolean oldDirtyState;
	private boolean newDirtyState;

	public DirtyStateEvent(boolean oldDirtyState, boolean newDirtyState) {
		this.oldDirtyState = oldDirtyState;
		this.newDirtyState = newDirtyState;
	}

	public boolean getOldDirtyState() {
		return oldDirtyState;
	}

	public boolean getNewDirtyState() {
		return newDirtyState;
	}

}
