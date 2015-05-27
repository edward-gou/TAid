package com.hash.core;

import java.util.ArrayList;

import android.util.SparseArray;

public class Assignment {
	// A map mapping student number to marks.
	private SparseArray<Mark> marks;
	private int maxMark; // Maximum mark of this assignment.
	private String aName; // Assignment name unique within the tutorial.
	private boolean isGroup;

	@Deprecated
	public Assignment() {
	}

	/**
	 * Create an assignment.
	 * 
	 * @param aName The name of the assignment, unique within the tutorial.
	 * @param maxMark The Maximum mark of this assignment.
	 */
	public Assignment(String aName, int maxMark, boolean isGroup) {
		this.marks = new SparseArray<Mark>();
		this.maxMark = maxMark;
		this.aName = aName;
		this.isGroup = isGroup;
		
	}

	protected Assignment(String aName, int maxMark, int[] studentNums, boolean isGroup) {
		this(aName, maxMark, isGroup);
		for (int s : studentNums)
			addEntry(s);
	}

	protected void addEntry(int stuNum) {
		this.marks.append(stuNum, new Mark(maxMark));
	}

	/**
	 * @param stuNum The student number
	 * @return The mark for this assignment for the student with the specified student number.
	 */
	public Mark getMark(int stuNum) {
		return marks.get(stuNum);
	}

	/**
	 * @return The maximum mark for this assignment.
	 */
	public int getMaxMark() {
		return maxMark;
	}

	/**
	 * @return The name of this assignment.
	 */
	public String getName() {
		return aName;
	}

	protected void removeEntry(int stuNum) {
		this.marks.remove(stuNum);
	}

	/**
	 * Assigns an individual mark to a single student.
	 * 
	 * @param stuNum The student's student number.
	 * @param mark The mark to assign.
	 */
	public void setIndivMark(int stuNum, int mark) {
		marks.get(stuNum).setMark(mark);
	}
	
	public void setIsGroup(boolean isGroup){
		this.isGroup = isGroup;
	}
	
	/**
	 * Set the maximum mark of this assignment.
	 * @param maxMark The maximum mark to set.
	 */
	public void setMaxMark(int maxMark) {
		this.maxMark = maxMark;
		for (int i = 0; i < marks.size(); i++) {
			marks.get(marks.keyAt(i)).setMaxMark(maxMark);
		}
	}

	public void setName(String aName) {
		this.aName = aName;
	}

	protected void switchStuNum(int oNum, int nNum) {
		if (oNum == nNum)
			return;
		Mark tmpMark = marks.get(oNum);
		marks.remove(oNum);
		marks.put(nNum, tmpMark);
	}
	
	public boolean isGroupAssignment(){
		return this.isGroup;
	}

}
