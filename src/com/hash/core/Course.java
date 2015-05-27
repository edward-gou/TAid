package com.hash.core;

import java.util.ArrayList;

// Course class to store courses
public class Course {
	String name = null;
	String instructorEmail = null;
	String courseNotes;

	ArrayList<Tutorial> tutorials = new ArrayList<Tutorial>();

	/**
	 * Creates a course with the given info.
	 * 
	 * @param Name The name of the course.
	 * @param Instructor The email of the instructor
	 * @param Tutorials A list of tutorials to add to this course.
	 */
	public Course(String Name, String Instructor, ArrayList<Tutorial> Tutorials) {
		name = Name;
		instructorEmail = Instructor;
		tutorials = Tutorials;
	}
	
	public String getCourseNotes() {
		return courseNotes;
	}

	public void setCourseNotes(String courseNotes) {
		this.courseNotes = courseNotes;
	}

	public Course() {
	}

	/**
	 * See if given tutorial name exists in this course.
	 * 
	 * @param tName The tutorial name.
	 * @return True if exists. False otherwise.
	 */
	public boolean containsTutorial(String tName) {
		for (Tutorial t : this.tutorials) {
			if (t.name.equals(tName)) {
				return true;
			}
		}
		return false;
	}

	// For creating tutorials in a course
	public boolean addTutorial(Tutorial tut) {
		if (this.containsTutorial(tut.name))
			return false;
		this.tutorials.add(tut);
		return true;
	}

	// Removes existing tutorial
	public boolean removeTutorial(Tutorial tut) {
		return this.tutorials.remove(tut);
	}

	// Returns tutorials in the course
	public ArrayList<Tutorial> getTutorials() {
		return this.tutorials;
	}

	// Returns a single tutorial
	public Tutorial getTutorial(int i) {
		return this.tutorials.get(i);
	}
	

	// Sets a single tutorial
	public Tutorial setTutorial(int i, Tutorial tut) {
		return this.tutorials.set(i, tut);
	}

	// Returns name of the course
	public String getCourseName() {
		return this.name;
	}

	// Sets the name of the course
	public void setCourseName(String cName) {
		this.name = cName;
	}

	// Sets instructor email
	public void setInstrEmail(String email) {
		this.instructorEmail = email;
	}

	// Returns instructor email
	public String getInstructorEmail() {
		return this.instructorEmail;
	}

	// Returns the number of tutorials
	public int numTuts() {
		return this.tutorials.size();
	}

	// Removes a tutorial from the course and returns the course
	public Tutorial popTut(int i) {
		return this.tutorials.remove(i);
	}

	// Removes empty tutorial slot in array and returns course
	public Tutorial popTut() {
		return this.popTut(this.tutorials.size() - 1);
	}

}
