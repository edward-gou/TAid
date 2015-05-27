package com.hash.core;

//Student class
public class Student {
	String name = null;
	String email = null;
	int studentNum;
	boolean dropped;
	public boolean isDropped() {
		return dropped;
	}

	public void setDropped(boolean dropped) {
		this.dropped = dropped;
	}

	public boolean isFlagged() {
		return flagged;
	}

	public void setFlagged(boolean flagged) {
		this.flagged = flagged;
	}

	boolean flagged;

	// Parameters for student class
	public Student(String Name, String Email, int StudentNum) {
		name = Name;
		email = Email;
		studentNum = StudentNum;
	}
	
	public Student(String n, int num, boolean d, boolean f)
	{
		name = n;
		studentNum = num;
		dropped = d;
		flagged = f;
	}
	
	public Student()
	{
		
	}

	/**
	 * Creates a deep copy of student s.
	 * 
	 * @param s The student to copy into this student.
	 */
	public Student(Student s) {
		this.name = s.name;
		this.email = s.email;
		this.studentNum = s.studentNum;
	}

	@Override
	public int hashCode() {
		return ((Integer) this.studentNum).hashCode();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof Student))
			return false;
		Student sOther = (Student) other;
		return (this.studentNum == sOther.studentNum);
	}

	/**
	 * @return The name of this student.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name The new name for this student.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return The email of this student.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @param email The new email for this student.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return The student number (unique id) of this student.
	 */
	public int getStudentNum() {
		return this.studentNum;
	}

	/**
	 * @param email The new student number (unique id) for this student.
	 */
	public void setStudentNum(int stuNum) {
		this.studentNum = stuNum;
	}

}
