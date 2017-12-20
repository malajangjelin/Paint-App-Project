package com.coder.stack;

import java.util.Stack;

/**
* Created by angjelinmalaj on 9/12/17.
		*/
public class SizedStack<T> extends Stack<T> {
	/**
	 * THERE CREATING STACK TO BE ABLE STORE THE IMAGE AND RETURN IT AGAIN (UNDO)
	 */
	private static final long serialVersionUID = 1L;
	private final int maxSize;

	public SizedStack(int size) {
		super();
		this.maxSize = size;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object push(Object object) {
		while (this.size() > maxSize) {
			this.remove(0);
		}
		return super.push((T) object);
	}

}
