package com.hash.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import android.util.SparseArray;

//Tutorial class for tutorials in courses
public class Tutorial {

	String name = "";
	ArrayList<CalculatedMark> calculatedMarkList = new ArrayList<CalculatedMark>();
	ArrayList<Assignment> assignmentList;
	// ArrayList<Integer> studentList;
	SparseArray<ArrayList<Boolean>> studentList;
	ArrayList<String> sessionIds;
	HashMap<String, ArrayList<Integer>> groups;
	int dayIndex = 0;
	int hour = 0, minute = 0;

	// Declaring variables for use
	// SparseArray<ArrayList<Float>> marks;
	// // TODO add assignment name array.
	// ArrayList<String> assignmentsList = new ArrayList<String>();
	// int numAssignments = 0;

	@Deprecated
	public Tutorial() {

	}

	public int getDayIndex() {
		return dayIndex;
	}

	public void setDayIndex(int dayIndex) {
		this.dayIndex = dayIndex;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public Mark getAttendanceMark(int stuNum) {
		int aMark = 0;
		for (boolean a : this.getAttendanceArray(stuNum))
			if (a)
				aMark++;
		return new Mark(this.getNumTotalSessions(), aMark);
	}

	/**
	 * Initialize the tutorial using the given name.
	 * 
	 * @param name The name of the tutorial.
	 */
	public Tutorial(String name) {
		this.name = name;
		this.assignmentList = new ArrayList<Assignment>();
		this.studentList = new SparseArray<ArrayList<Boolean>>();
		this.sessionIds = new ArrayList<String>();
		this.groups = new HashMap<String, ArrayList<Integer>>();
		this.calculatedMarkList = new ArrayList<CalculatedMark>();
	}

	public HashMap<String, ArrayList<Integer>> getGroups() {
		return groups;
	}

	/**
	 * Add an assignment to this tutorial.
	 * 
	 * @param aName The name of the assignment.
	 * @param maxMark The maximum mark of the assignment.
	 * @return The number of assignments in this tutorial after the assignment
	 *         has been added. -1 If this assignment name already exists, or is
	 *         empty.
	 */
	public int addAssignment(String aName, int maxMark, boolean isGroup) {
		if (containsAssignment(aName) || aName.trim().isEmpty())
			return -1;
		// Assignment does not exist in tutorial. Add it.
		assignmentList.add(new Assignment(aName, maxMark, getStudentIds(),
				isGroup));
		return assignmentList.size();
	}

	public void addSession() {
		for (int stuNum : getStudentIds())
			studentList.get(stuNum).add(false);
		sessionIds
				.add("Session " + Integer.toString(getNumTotalSessions() + 1));
	}

	// Adds a student
	public boolean addStudent(Student stu) {
		return this.addStudentId(stu.studentNum);
	}

	/**
	 * Add the given student number to this tutorial.
	 * 
	 * @param stu A student number within the student bank.
	 * @return True if successfully added. False otherwise.
	 */
	public boolean addStudentId(int stu) {
		if (this.containsStudent(stu)) {
			return false;
		}
		// Create attendance entry
		ArrayList<Boolean> stuAttendance = new ArrayList<Boolean>();
		for (int i = 0; i < getNumTotalSessions(); i++)
			stuAttendance.add(false);
		// Add to tutorial student list
		studentList.put(stu, stuAttendance);
		// Add to assignments
		for (Assignment a : assignmentList)
			a.addEntry(stu);
		return true;
	}

	public String[] getSessionIds() {
		String[] ret = new String[this.getNumTotalSessions()];
		sessionIds.toArray(ret);
		return ret;
	}

	// Modify the marks of a student
	public void changeMark(int stu, int mark, int aIndex) {
		assignmentList.get(aIndex).setIndivMark(stu, mark);
	}

	public boolean changeGroupMark(String gName, int mark, String aName) {
		int aIndex = getAssignmentIndex(aName);
		if (aIndex == -1)
			return false;
		for (Integer sNum : groups.get(gName))
			changeMark(sNum, mark, aIndex);
		return true;
	}

	public int getAssignmentIndex(String aName) {
		for (int i = 0; i < assignmentList.size(); i++)
			if (assignmentList.get(i).getName().equals(aName))
				return i;
		return -1;
	}

	public boolean containsAssignment(String aName) {
		for (Assignment a : assignmentList) {
			if (a.getName().equals(aName))
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param stuId The student number.
	 * @return True if student is in this tutorial. False otherwise.
	 */
	public boolean containsStudent(int stuId) {
		for (Integer sNum : getStudentIds()) {
			if (sNum == stuId) {
				return true;
			}
		}
		return false;
	}

	public boolean containsStudent(Student stu) {
		return this.containsStudent(stu.studentNum);
	}

	/**
	 * Deletes the specified assignment from this tutorial.
	 * 
	 * @param index The index of the tutorial to remove.
	 * @return The number of assignments in this tutorial after the assignment
	 *         has been removed.
	 */
	public int delAssignment(int index) {
		this.assignmentList.remove(index);
		return this.assignmentList.size();
	}

	/**
	 * Delete the assigment with the given name.
	 * 
	 * @param aName The name of the assignment.
	 * @return The number of assignments left in this tutorial after the
	 *         deletion. Return -1 if given assignment name does not exist.
	 */
	public int delAssignment(String aName) {
		for (int i = 0; i < assignmentList.size(); i++) {
			if (assignmentList.get(i).getName().equals(aName))
				return delAssignment(i);
		}
		return -1;
	}

	public void deleteSession() {
		deleteSession(getNumTotalSessions() - 1);
	}

	public void deleteSession(int dayIndex) {
		for (int stuNum : getStudentIds())
			studentList.get(stuNum).remove(dayIndex);
		sessionIds.remove(getNumTotalSessions() - 1);
	}

	/**
	 * Return all the assignment marks.
	 * 
	 * @return a 2D array of all the marks stored in this tutorial.
	 */
	public Mark[][] getAllMarks() {
		Mark[][] allMarks = new Mark[studentList.size()][];
		// For each student
		for (int i = 0; i < studentList.size(); i++) {
			allMarks[i] = getStudentMarks(getStudentId(i));
		}
		return allMarks;
	}

	/**
	 * Get the assignment mark object.
	 * 
	 * @param stuId A student number.
	 * @param aIndex The assignment index within the tutorial's assignment list.
	 * @return The mark object for the specified student and assignment.
	 */
	public Mark getStudentMark(int stuId, int aIndex) {
		return assignmentList.get(aIndex).getMark(stuId);
	}

	/**
	 * @param aIndex Index of the assignment
	 * @return The assignment object at the given index.
	 */
	public Assignment getAssignment(int aIndex) {
		return assignmentList.get(aIndex);
	}

	/**
	 * Get a list of all the assignment names in this tutorial.
	 * 
	 * @return A list of assignment names.
	 */
	public String[] getAssignmentNames() {
		String[] aNames = new String[assignmentList.size()];
		for (int i = 0; i < assignmentList.size(); i++)
			aNames[i] = getAssignment(i).getName();
		return aNames;
	}

	public String[] getGroupAssignmentNames() {
		ArrayList<String> groupANames = new ArrayList<String>();
		for (Assignment a : assignmentList)
			if (a.isGroupAssignment())
				groupANames.add(a.getName());
		String[] retList = new String[groupANames.size()];
		groupANames.toArray(retList);
		return retList;
	}

	public int getNumGroupAssignments() {
		int i = 0;
		for (Assignment a : assignmentList)
			if (a.isGroupAssignment())
				i++;
		return i;
	}

	/**
	 * @param stuNum Student number of student.
	 * @return A array of booleans corresponding to the attendance of the
	 *         student.
	 */
	public boolean[] getAttendanceArray(int stuNum) {
		boolean[] attended = new boolean[getNumTotalSessions()];
		int i = 0;
		for (Boolean b : studentList.get(stuNum)) {
			attended[i] = b;
			i++;
		}
		return attended;
	}

	/**
	 * Get the name of this tutorial.
	 * 
	 * @return The name of this tutorial.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get number of assignments in this tutorial.
	 * 
	 * @return Number of assignments.
	 */
	public int getNumAssignments() {
		return assignmentList.size();
	}

	/**
	 * @param stuNum The student's student number
	 * @return The number of tutorial sessions this student attended.
	 */
	public int getNumAttendance(int stuNum) {
		int attended = 0;
		for (Boolean b : studentList.get(stuNum))
			if (b)
				attended += 1;
		return attended;
	}

	/**
	 * Get number of students in this tutorial.
	 * 
	 * @return Number of students in this tutorial.
	 */
	public int getNumStudents() {
		return studentList.size();
	}

	/**
	 * @return Total number of tutorial sessions held.
	 */
	public int getNumTotalSessions() {
		return sessionIds.size();
	}

	/**
	 * Return the student number at the given index.
	 * 
	 * @param index An index in the tutorial's student list.
	 * @return The student number stored at the index.
	 */
	public int getStudentId(int index) {
		return getStudentIds()[index];
	}

	/**
	 * Return a list of all the student numbers in this tutorial.
	 * 
	 * @return A list of all the student numbers in this tutorial.
	 */
	public int[] getStudentIds() {
		int[] stus = new int[studentList.size()];
		for (int i = 0; i < studentList.size(); i++) {
			stus[i] = studentList.keyAt(i);
		}
		return stus;
	}

	/**
	 * Get a list of mark objects for a student.
	 * 
	 * @param stu The student number of the student.
	 * @return An array of marks for the student for this tutorial.
	 */
	public Mark[] getStudentMarks(int stu) {
		Mark[] retMarks = new Mark[assignmentList.size()];
		for (int i = 0; i < assignmentList.size(); i++)
			retMarks[i] = assignmentList.get(i).getMark(stu);
		return retMarks;
	}

	/**
	 * @param stu The student's student number.
	 * @return Mark array for all the assignments, for the specified student.
	 */
	public Mark[] getStudentMarks(Student stu) {
		return this.getStudentMarks(stu.studentNum);
	}

	/**
	 * Modifies the mark entry of old student to use nu student. Effectively
	 * modifies a current student within the tutorial by removing the old
	 * student and replacing it with nu. Marks array stays the same.
	 * 
	 * @param old The student to remove.
	 * @param nu The new student.
	 */
	public boolean modifyStudent(Student old, Student nu) {
		return this.modifyStudentId(old.studentNum, nu.studentNum);
	}

	/**
	 * Modifies the mark entry with oldID to use newID. Effectively modifies a
	 * current student within the tutorial by removing oldID and replacing it
	 * with newID. Marks array stays the same.
	 * 
	 * @param oldId The student number of the student to remove.
	 * @param newId The student number of the new student.
	 */
	public boolean modifyStudentId(int oldId, int newId) {
		// Filter out invalid modifications:
		// 1. newId already exists in tutorial
		// 2. oldId does not exist in tutorial
		if (oldId == newId ) //Is a valid modification, but we need no edits.
			return true;
		if (containsStudent(newId) || !containsStudent(oldId)) 
			return false;
		ArrayList<Boolean> stuAttendance = studentList.get(oldId);
		studentList.remove(oldId);
		studentList.put(newId, stuAttendance);
		for (Assignment a : assignmentList)
			a.switchStuNum(oldId, newId);
		return true;
	}

	// Removes a student
	public int removeStudent(Student s) {
		return this.removeStudentId(s.studentNum);
	}

	/**
	 * Removes the student number from this tutorial.
	 * 
	 * @param stuNum The given student number to remove.
	 * @return 1 if removed successfully. 0 if removed successfully, and a group
	 *         was removed consequently. -1 otherwise.
	 */
	public int removeStudentId(int stuNum) {
		if (!containsStudent(stuNum))
			return -1;
		// Remove from tutorial
		int retVal = 1;
		studentList.remove(stuNum);
		// Remove from assignments.
		for (Assignment a : assignmentList)
			a.removeEntry(stuNum);
		String groupToRemove = null;
		for (String gName : groups.keySet()) {
			// Remove the student from group. Delete the group if less than
			// two students.
			if (groups.get(gName).remove((Integer) stuNum)) {
				// Student has been removed from group. No need to continue.
				// But, check to see if student's old group is still valid.
				if (groups.get(gName).size() < 2) {
					groupToRemove = gName;
					retVal = 0;
				}
				break;
			}
		}
		if (groupToRemove != null)
			groups.remove(groupToRemove);
		return retVal;
	}

	public void setAttendance(int stuNum, int dayIndex, boolean attended) {
		studentList.get(stuNum).set(dayIndex, attended);
	}
	
	public boolean setAttendance(int stuNum, boolean[] attended){
		if (attended.length != getNumTotalSessions())
			return false;
		for (int i = 0; i < getNumTotalSessions(); i++)
			setAttendance(stuNum, i, attended[i]);
		return true;
	}

	public boolean setAttendance(int stuNum, String sessionId, boolean attended) {
		if (!sessionIds.contains(sessionId))
			return false;
		setAttendance(stuNum, sessionIds.indexOf(sessionId), attended);
		return true;
	}

	// Sets the name of the tutorial
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds a group with the given name, and given student numbers to this
	 * tutorial.
	 * 
	 * @param gName The unique name of this group in relation to this
	 *        assignment.
	 * @param stuList A list of student numbers to put in this group.
	 * @return True if the add is successful. False otherwise.
	 */
	public boolean addGroup(String gName, int[] stuList) {
		// Only add if gName is unique, and stuList is valid group.
		if (groups.containsKey(gName) || !isValidGroup(stuList))
			return false;
		insertGroup(gName, stuList);
		return true;
	}

	public Integer[] getGroupMembers(String gName) {
		ArrayList<Integer> g = groups.get(gName);
		Integer[] retList = new Integer[g.size()];
		int i = 0;
		for (Integer s : g) {
			retList[i] = s;
			i++;
		}
		return retList;
	}

	/**
	 * @param stuNum The student's student number.
	 * @return The name of the group this student is in. Null if the student is
	 *         not in a group, or is not in this tutorial.
	 */
	public String getStudentGroupName(int stuNum) {
		for (String gName : groups.keySet())
			if (groups.get(gName).contains((Integer) stuNum))
				return gName;
		return null;
	}

	public String[] getGroupNames() {
		String[] retList = new String[groups.size()];
		int i = 0;
		for (String s : groups.keySet()) {
			retList[i] = s;
			i++;
		}
		return retList;
	}

	private void insertGroup(String gName, int[] stuList) {
		ArrayList<Integer> group = new ArrayList<Integer>();
		for (int sNum : stuList)
			group.add(sNum);
		groups.put(gName, group);
	}

	public boolean modifyGroup(String oldgName, String newgName, int[] stuList) {
		if ((groups.containsKey(oldgName) && (!groups.containsKey(newgName) || oldgName
				.equals(newgName)))) {
			// Valid modification condition.
			groups.remove(oldgName);
			return addGroup(newgName, stuList);
		}
		return false;
	}

	public boolean isIndividualStudent(int stuNum) {
		for (String gName : groups.keySet())
			for (Integer sn : groups.get(gName))
				if (sn.equals(stuNum))
					return false;
		return true;
	}

	public boolean isValidGroup(int[] stuList) {
		for (int n : stuList)
			if (!isIndividualStudent(n))
				return false;
		return true;
	}

	public boolean containsGroup(String gName) {
		return groups.containsKey(gName);
	}

	public boolean removeGroup(String gName) {
		return (groups.remove(gName) != null);
	}

	/**
	 * @return A list of student numbers of students in this tutorial that are
	 *         not in a group.
	 */
	public Integer[] getUngroupedStudents() {
		HashSet<Integer> ungroupedStus = new HashSet<Integer>(
				studentList.size());
		for (int i = 0; i < studentList.size(); i++)
			ungroupedStus.add(studentList.keyAt(i));
		// For each group
		for (String gName : groups.keySet())
			// for each individual in group
			for (Integer s : groups.get(gName))
				ungroupedStus.remove(s);
		Integer[] retList = new Integer[ungroupedStus.size()];
		ungroupedStus.toArray(retList);
		return retList;
	}

	public ArrayList<CalculatedMark> getCalculatedMarkList() {
		return calculatedMarkList;
	}

	public ArrayList<Assignment> getAssignmentList() {
		return assignmentList;
	}

	public void addAssignment(Assignment a) {
		this.addAssignment(a.getName(), a.getMaxMark(), a.isGroupAssignment());
	}

	public CalculatedMark getCalculatedMark(int i) {
		return calculatedMarkList.get(i);
	}

	public void addCalculatedMark(CalculatedMark c) {
		calculatedMarkList.add(c);
	}
}
