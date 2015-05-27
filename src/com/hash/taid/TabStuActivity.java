package com.hash.taid;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hash.core.Assignment;
import com.hash.core.CalculatedMark;
import com.hash.core.FileManager;
import com.hash.core.SendMailTask;
import com.hash.core.StudentBank;
import com.hash.core.Tutorial;
import com.hash.taid.customs.NonSwipeableViewPager;
import com.hash.taid.customs.TAidActivity;
import com.hash.taid.fragments.AssignmentListFragment;
import com.hash.taid.fragments.GroupListFragment;
import com.hash.taid.fragments.MarkTableFragment;
import com.hash.taid.fragments.StuListFragment;

public class TabStuActivity extends TAidActivity implements
		ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	static int[] sNumArray;
	FileManager fm = new FileManager();

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	NonSwipeableViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tab_stu_activity);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// Show the Up button in the action bar.
		actionBar.setDisplayHomeAsUpEnabled(true);

		// Get the tutorial we're in
		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, 0);
		tabId = intent.getIntExtra(startAtTabTag, 0);

		setTitle("Tutorial: " + clist.getCourse(cId).getTutorial(tId).getName());

		// Set the student array
		sNumArray = clist.getCourse(cId).getTutorial(tId).getStudentIds();

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (NonSwipeableViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		getActionBar().setSelectedNavigationItem(tabId);
	}

	// Adds an assignment to this tutorial.
	// Note that this activity finishes to enable refreshing.
	// private void addAssignment() {
	// Intent intent = new Intent(this, TutAddAssignmentActivity.class);
	// intent.putExtra(modCourseTag, cId);
	// intent.putExtra(modTutorialTag, tId);
	// startActivity(intent);
	// finish();
	// }
	//
	// private void delAssignment() {
	// Intent intent = new Intent(this, TutDelAssignmentActivity.class);
	// intent.putExtra(modCourseTag, cId);
	// intent.putExtra(modTutorialTag, tId);
	// startActivity(intent);
	// finish();
	// }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tab_stu, menu);
		return true;
	}

	String importString;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Object[] d;
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1: {
			if (resultCode == RESULT_OK) {
				importString = data
						.getStringExtra("com.hash.taid.FileChooserActivity.content");
				// Toast.makeText(this, "Parsing file...",
				// Toast.LENGTH_SHORT).show();
				// Toast.makeText(this, importString,
				// Toast.LENGTH_SHORT).show();
				try {
					d = fm.importDCS(importString);
					Tutorial newTut = new Tutorial(clist.getCourse(cId)
							.getTutorial(tId).getName());
					clist.getCourse(cId).setTutorial(tId, newTut);

					// clist.getCourse(cId).getTutorial(tId).setAssignmentList((ArrayList<Assignment>)d[0]);
					for (int i = 0; i < ((ArrayList<Assignment>) d[0]).size(); i++) {
						clist.getCourse(cId)
								.getTutorial(tId)
								.addAssignment(
										((ArrayList<Assignment>) d[0]).get(i));
					}
					// clist.getCourse(cId).getTutorial(tId).setCalculatedMarkList((ArrayList<CalculatedMark>)d[1]);
					for (int i = 0; i < ((ArrayList<CalculatedMark>) d[1])
							.size(); i++) {
						clist.getCourse(cId)
								.getTutorial(tId)
								.addCalculatedMark(
										((ArrayList<CalculatedMark>) d[1])
												.get(i));
					}
					// clist.getCourse(cId).getTutorial(tId).setStudentList(((StudentBank)d[2]).toSparseArray());
					for (int i = 0; i < ((StudentBank) d[2]).getSize(); i++) {
						clist.getCourse(cId)
								.getTutorial(tId)
								.addStudent(
										((StudentBank) d[2]).getStudents()
												.valueAt(i));
						for (int k = 0; k < ((ArrayList<Assignment>) d[0])
								.size(); k++) {
							clist.getCourse(cId)
									.getTutorial(tId)
									.changeMark(
											((StudentBank) d[2]).getStudents()
													.valueAt(i).getStudentNum(),
											((ArrayList<Assignment>) d[0])
													.get(k)
													.getMark(
															((StudentBank) d[2])
																	.getStudents()
																	.valueAt(i)
																	.getStudentNum())
													.getMark(), k);
						}
					}

					for (int i = 0; i < ((StudentBank) d[2]).getSize(); i++) {
						if (sbank.contains(((StudentBank) d[2]).getStudents()
								.keyAt(i))) {
							sbank.removeStudent(((StudentBank) d[2])
									.getStudents().keyAt(i));
						}
						sbank.addStudent(((StudentBank) d[2]).getStudents()
								.valueAt(i));
					}
					/*
					 * Intent intent = new Intent(this, MainActivity.class);
					 * intent.putExtra(modCourseTag, cId);
					 * intent.putExtra(modTutorialTag, tId);
					 * intent.putExtra(startAtTabTag, tabId);
					 * NavUtils.navigateUpTo(this, intent);; finish();
					 */

					Toast.makeText(this, "File successfully loaded",
							Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(this, TabStuActivity.class);
					intent.putExtra(modCourseTag, data.getIntExtra(modCourseTag, 0));
					intent.putExtra(modTutorialTag, data.getIntExtra(modTutorialTag, 0));
					startActivity(intent);
					finish();
				} catch (Exception e) {
					Toast.makeText(this, "File format is invalid",
							Toast.LENGTH_SHORT).show();
				}
			}
		}
		}
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, TutorialActivity.class);
		intent.putExtra(modCourseTag, cId);
		NavUtils.navigateUpTo(this, intent);
		finish();
	}

	public void sendEmail(){
		String body = new String();
		StudentBank sb = new StudentBank();
		int[] stuList = clist.getCourse(cId).getTutorial(tId)
				.getStudentIds();
		for (int i = 0; i < stuList.length; i++) {
			sb.addStudent(sbank.getStudent(stuList[i]));
		}
		body = fm.exportDCS(clist.getCourse(cId).getTutorial(tId)
				.getAssignmentList(), clist.getCourse(cId).getTutorial(tId)
				.getCalculatedMarkList(), sb);

		String filename = "/" + clist.getCourse(cId).getCourseName()
				+ " - " + clist.getCourse(cId).getTutorial(tId).getName();
		try {
			File f = new File(getApplicationContext().getFilesDir()
					.getPath().toString()
					+ filename);
			FileWriter w = new FileWriter(f, false);
			w.write(body);
			w.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Check to see if the user data and instructor email is filled
		if (user.isComplete()
				&& clist.getCourse(cId).getInstructorEmail() != null) {
			SendMailTask task = new SendMailTask();
			// parameters: (context, name, username, password, server, port,
			// sockport, recipient, subject, body, marks directory)
			task.execute(this.getApplicationContext(), user.getName(), user
					.getUsername(), user.getPassword(), user
					.getSmtpServer(), user.getSmtpPort(), user
					.getSocketPort(), clist.getCourse(cId)
					.getInstructorEmail(), clist.getCourse(cId)
					.getCourseName()
					+ " - "
					+ clist.getCourse(cId).getTutorial(tId).getName()
					+ " Marks.", body, getApplicationContext()
					.getFilesDir().getPath().toString()
					+ filename);
		}
		// Fields not filled
		else {
			Toast.makeText(
					this,
					"Cannot send. Make sure your user data and instructor email is filled.",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			onBackPressed();
			return true;
		case R.id.action_addSession:
			// User wants to add a session.
			clist.getCourse(cId).getTutorial(tId).addSession();
			Toast.makeText(this, "Session added", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.action_delSession:
			intent = new Intent(this, DelSessionDialogActivity.class);
			intent.putExtra(modCourseTag, cId);
			intent.putExtra(modTutorialTag, tId);
			intent.putExtra(modStuTag, sId);
			intent.putExtra(startAtTabTag, mViewPager.getCurrentItem());
			startActivity(intent);
			return true;
		case R.id.action_sendEmail:
			// User wants to send marks
			sendEmail();
			return true;
		case R.id.action_importFile:
			// User wants to import the tutorial.
			intent = new Intent(this, FileChooserActivity.class);
			intent.putExtra(modCourseTag, cId);
			intent.putExtra(modTutorialTag, tId);
			startActivityForResult(intent, 1);
			
			return true;

		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			android.app.FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			android.app.FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			android.app.FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Bundle args = new Bundle();
			switch (position) {
			case 0:
				// User is in student list tab.
				Fragment sListFrag = new StuListFragment();
				args.putInt(modCourseTag, cId);
				args.putInt(modTutorialTag, tId);
				args.putInt(startAtTabTag, 0);
				sListFrag.setArguments(args);
				return sListFrag;
			case 1:
				// User is in groups tab
				Fragment gListFrag = new GroupListFragment();
				args.putInt(modCourseTag, cId);
				args.putInt(modTutorialTag, tId);
				args.putInt(startAtTabTag, 1);
				gListFrag.setArguments(args);
				return gListFrag;
			case 2:
				// User is in assignment list tab
				Fragment aListFrag = new AssignmentListFragment();
				args.putInt(modCourseTag, cId);
				args.putInt(modTutorialTag, tId);
				args.putInt(startAtTabTag, 2);
				aListFrag.setArguments(args);
				return aListFrag;
			default:
				// User is in marks table tab.
				Fragment mTableFrag = new MarkTableFragment();
				args.putInt(modCourseTag, cId);
				args.putInt(modTutorialTag, tId);
				args.putInt(startAtTabTag, 3);
				mTableFrag.setArguments(args);
				return mTableFrag;
			}

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.stu_tab_names).toUpperCase(l);
			case 1:
				return getString(R.string.stu_tab_groups).toUpperCase(l);
			case 2:
				return getString(R.string.stu_tab_assignments).toUpperCase(l);
			case 3:
				return getString(R.string.stu_tab_marktable).toUpperCase(l);
			}
			return null;
		}

	}

}
