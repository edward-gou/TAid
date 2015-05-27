package com.hash.core;

/**
 * Class for storing and modifying marks.
 * 
 * @author Leeonadoh
 * 
 */
public class Mark {
	private int maxMark;
	private int mark;

	public Mark(int maxMark, int mark) {
		this.maxMark = maxMark;
		this.mark = mark;
	}
	
	public Mark(){
		this(100, -1);
		
	}


	/**
	 * Creates a mark with the given maximum mark. 
	 * 
	 * @param maxMark the maximum mark of this assignment. Bonous marks are
	 *        allowed.
	 */
	public Mark(int maxMark) {
		this(maxMark,-1);
	}

	/**
	 * Returns the percentage representation.
	 * 
	 * @return percent in range 0 to 1.
	 */
	public float getPercentage() {
		return (float) mark / maxMark;
	}
	
	public int getMaxMark() {
		return maxMark;
	}

	public void setMaxMark(int maxMark) {
		this.maxMark = maxMark;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	/**
	 * Tag as unmarked.
	 */
	public void setUnmarked() {
		this.mark = -1;
	}

	/**
	 * Whether this is a valid mark.
	 * 
	 * @return True if marked. False otherwise.
	 */
	public boolean isUnmarked() {
		return (this.mark == -1);
	}

	/**
	 * Return the amount of bonus marks.
	 * 
	 * @return A positive number, which is the bonus mark. If negative, there
	 *         arn't any bonus marks given.
	 */
	public int getBonusMarks() {
		return this.mark - this.maxMark;
	}
	
	public String toString(){
		return mark + "/" + maxMark;
	}
}
