	package com.hash.core;

import java.util.ArrayList;

import android.util.SparseArray;

import com.thoughtworks.xstream.XStream;

//Student bank class
public class StudentBank {

	SparseArray<Student> students;

	/**
	 * Constructs an empty student bank. For the future, the ability to load
	 * from a saved file must be implemented.
	 */
	public StudentBank() {
		students = new SparseArray<Student>();
	}
	
	public StudentBank(SparseArray<Student> s) {
		students = s;
	}

	/**
	 * Whether a given student is in this bank.
	 * 
	 * @param stu The student to search for
	 * @return True if exists in bank. False otherwise.
	 */
	public boolean contains(Student stu) {
		return this.contains(stu.getStudentNum());
	}
	
	public Student get(int i)
	{
		return students.valueAt(i);
	}

	public boolean contains(int id) {
		if (this.students.get(id) == null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Gets the size of the student bank
	 * @return size of student bank
	 */
	public int getSize()
	{
		return students.size();
	}

	/**
	 * Adds the given student to the bank. Does not allow duplicates.
	 * 
	 * @param stu The student to add.
	 * @return True if student added to bank. False otherwise.
	 */
	public boolean addStudent(Student stu) {
		return this.addStudent(stu.getName(), stu.getEmail(),
				stu.getStudentNum());
	}

	/**
	 * Adds a student with the given credentials to the bank. Does not allow
	 * duplicates.
	 * 
	 * @param name Name of student.
	 * @param email Email of student.
	 * @param id Unique student number of student.
	 * @return True if student added to bank. False otherwise.
	 */
	public boolean addStudent(String name, String email, int id) {
		if (this.contains(id)) {
			return false;
		}
		this.students.put(id, new Student(name, email, id));
		return true;
	}

	/**
	 * Remove given student from the bank.
	 * 
	 * @param stu The student to remove from bank.
	 * @return The removed student if removed, null if student doesnt exist in
	 *         bank.
	 */
	public Student removeStudent(Student stu) {
		return this.removeStudent(stu.getStudentNum());
	}

	/**
	 * Remove the student with the given id from the bank.
	 * 
	 * @param id The student id to identify the student to remove.
	 * @return The removed student if removed, null if student doesnt exist in
	 *         bank.
	 */
	public Student removeStudent(int id) {
		if (this.contains(id)) {
			Student s = this.students.get(id);
			this.students.remove(id);
			return s;
		}
		return null;
	}

	private Student getStudentReference(int id) {
		return this.students.get(id);
	}

	/**
	 * Searches for a student by their student number and returns null if
	 * student not found. The returned student object is a deep copy of the
	 * object that exists in the bank.
	 * 
	 * @param id The student number of the student to return.
	 */
	public Student getStudent(int id) {
		Student s = this.getStudentReference(id);
		if (s != null)
			return new Student(s);
		return null;
	}
	//returns name of student by using their id
	public String getStudentName(int id) {
		return this.getStudentReference(id).getName();
	}
	//returns email of student by using their id
	public String getStudentEmail(int id) {
		return this.getStudentReference(id).getEmail();
	}

	/**
	 * Modifies the old student within the bank to be identical to student nu.
	 * The algorithm removes the old student, then adds the new.
	 * 
	 * @param old The old student in bank.
	 * @param nu The new student to replace old student in bank.
	 * @return -1 if old student does not exist in bank.
	 * 
	 *         0 if student number of student nu already exists in bank. Nothing
	 *         is modified.
	 * 
	 *         1 if student number of student old == student number of nu. The
	 *         names and email of student old will be changed to match nu.
	 * 
	 *         2 if student number of student old != student number of nu We
	 *         create a new student with the credentials of nu.
	 */
	public int modify(Student old, Student nu) {
		// The user intended to modify a student. Check the following:
		// 1. User does not modify student number, but changes name, or
		// email.
		// 2. User modifies student number, and the specified student number
		// is valid.
		// 3. User modifies student number, but the specified number already
		// exists.

		if (!this.contains(old))
			return -1;

		if (old.studentNum == nu.studentNum) {
			// Case 1: We remove the old, and replace with new.
			this.removeStudent(old);
			this.addStudent(nu);
			return 1;
		} else if (!this.contains(nu.studentNum)) {
			// Case 2: We keep the old, and simply add the new.
			this.addStudent(nu);
			return 2;
		} else {
			// Case 3: Invalid modification. Do nothing.
			return 0;
		}

	}

	public String toString() {
		String r = "";
		for (int i = 0; i < this.students.size(); i++) {
			r += this.students.keyAt(i) + "\n";
		}
		return r;
	}
	
	public String getXml()
	{
		XStream xstream = new XStream();
		String studentBankXml = xstream.toXML(this);
		return studentBankXml;
	}
	
	public void load(String bankXml)
	{
		XStream xstream = new XStream();
		this.students = ((StudentBank)xstream.fromXML(bankXml)).students;
	}

	public SparseArray<ArrayList<Boolean>> toSparseArray()
	{
		SparseArray<ArrayList<Boolean>> s = new SparseArray<ArrayList<Boolean>>();
		for (int i = 0; i < students.size(); i++)
		{
			s.put(students.keyAt(i), new ArrayList<Boolean>());
		}
		return s;
	}

	public SparseArray<Student> getStudents() {
		return students;
	}

	public void setStudents(SparseArray<Student> students) {
		this.students = students;
	}
	
}
