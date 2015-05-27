package com.hash.core;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.XStream;

//List of courses to enable the android app to properly access information
public class CourseList {

	ArrayList<Course> courses;

	// Creates new array of courses
	public CourseList() {
		courses = new ArrayList<Course>();
	}

	// Sets courses
	public CourseList(ArrayList<Course> c) {
		courses = c;
	}

	/**
	 * Add a course to the course list.
	 * 
	 * @param course The course object to add.
	 * @return True if successfully added. False if course with given name
	 *         already exists.
	 */
	public boolean addCourse(Course course) {
		if (this.containsCourse(course.name)) {
			return false;
		}
		this.courses.add(course);
		return true;
	}
	

	/**
	 * Removes, and returns the last course in this list.
	 * @return The last course in this list.
	 */
	public Course popCourse() {
		return this.courses.remove(this.courses.size() - 1);
	}

	/**
	 * Removes the course at the given index, and returns it.
	 * @param index The index of the coures to remove.
	 * @return The removed course.
	 */
	public Course popCourse(int index) {
		return this.courses.remove(index);
	}

	// Removes a course with no return
	public boolean removeCourse(Course course) {
		return this.courses.remove(course);
	}

	/**
	 * Searches and returns the course, if such course exists in list.
	 * 
	 * @param name The name of the course.
	 * @return The course object if exists. Null otherwise.
	 */
	public Course searchCourseByName(String name) {
		for (int i = 0; i < this.courses.size(); i++) {
			if (this.courses.get(i).getCourseName().equals(name)) {
				return this.courses.get(i);
			}
		}
		return null;
	}

	/**
	 * If this list contains the specified course.
	 * 
	 * @param name The name of the course.
	 * @return True if exists. False otherwise.
	 */
	public boolean containsCourse(String name) {
		if (this.searchCourseByName(name) == null) {
			return false;
		}
		return true;
	}

	// Returns course list
	public List<Course> getCourseList() {
		return courses;
	}

	// Returns number of courses
	public int numCourses() {
		return this.courses.size();
	}

	// Returns the course at index
	public Course getCourse(int index) {
		return this.courses.get(index);
	}

	// Returns the student bank xml file
	public String getXml() {
		XStream xstream = new XStream();
		String studentBankXml = xstream.toXML(this);
		return studentBankXml;
	}

	public void load(String listXml) {
		XStream xstream = new XStream();
		this.courses = ((CourseList) xstream.fromXML(listXml)).courses;
	}

}
