package com.hash.taid.customs;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.hash.core.CourseList;
import com.hash.core.FileManager;
import com.hash.core.StudentBank;
import com.hash.core.User;

/**
 * This activity will be the parent of all the activities used in this app.
 * 
 * @author Leeonadoh
 */
public abstract class TAidActivity extends FragmentActivity {
	// Course list storing courses, tutorials, and students.
	public static CourseList clist = new CourseList();
	// Database for students
	public static StudentBank sbank = new StudentBank();
	// User data
	public static User user = new User();

	// The below are tags used to pass information through intents.
	public final static String modCourseTag = "com.hash.TAid.course";
	public final static String modTutorialTag = "com.hash.TAid.tutorial";
	public final static String modStuTag = "com.hash.TAid.stu";
	public final static String modAsmtTag = "com.hash.Taid.assignment";
	public final static String modGroupTag = "com.hash.Taid.group";
	public final static String startAtTabTag = "com.hash.Taid.stuTab";

	// Used to store information passed to and from intents.
	protected int cId, tId, sId, aId, tabId;
	protected String gId;

	/**
	 * Whenever something is paused, we will save persistent information. Ie:
	 * Student database and course data structure.
	 */
	@Override
	public void onPause() {
		save(getApplicationContext());
		super.onPause();
	}

	/**
	 * Saves course list and student bank, given the context.
	 * 
	 * @param c The context
	 */
	public static void save(Context c) {
		FileManager fm = new FileManager();
		fm.save(sbank, clist, user, c);
	}

	/**
	 * Force start the given activity class. Note that c must be an instance of
	 * TAidActivity, or a subclass thereof.
	 * 
	 * @param c The TAidActivity class.
	 */
	public void forceActivity(Class<?> c) {
		this.forceActivity(c, cId, tId, sId, aId, gId, tabId);
	}

	public void forceActivity(Class<?> c, int cId, int tId, int sId, int aId, String gId, int tabId) {
		Intent intent = new Intent(this, c);
		intent.putExtra(modCourseTag, cId);
		intent.putExtra(modTutorialTag, tId);
		intent.putExtra(modStuTag, sId);
		intent.putExtra(modAsmtTag, aId);
		intent.putExtra(modGroupTag, gId);
		intent.putExtra(startAtTabTag, tabId);
		startActivity(intent);
	}
}
