/**
 * @author Jorge Baier, based on EightPuzzleBoard by Ravi Mohan & R. Lunde
 */
package aima.core.environment.sliders;

import aima.core.agent.impl.DynamicAction;

public class SlidersAction extends DynamicAction {
	private SlidersActionType type;
	private int index;

	public SlidersAction(SlidersActionType type, int index) {
		super(type.toString() + index);
		this.type = type;
		this.index = index;
	}

	@Override
	public String toString() {
		return super.getName();
	}

	public SlidersActionType getType() {
		return type;
	}

	public void setType(SlidersActionType type) {
		this.type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
