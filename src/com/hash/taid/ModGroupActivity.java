package com.hash.taid;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hash.core.Tutorial;
import com.hash.taid.customs.MultiSelectionSpinner;
import com.hash.taid.customs.TAidActivity;

public class ModGroupActivity extends TAidActivity {

	EditText gName;
	MultiSelectionSpinner stuSp;
	Tutorial tut;
	String[] displayedStus;
	Integer[] displayedStuNums;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_group);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, 0);
		gId = intent.getStringExtra(modGroupTag);
		tabId = intent.getIntExtra(startAtTabTag, 0);
		tut = clist.getCourse(cId).getTutorial(tId);

		// Get the view elements.
		gName = (EditText) findViewById(R.id.groupNameEditText);
		stuSp = (MultiSelectionSpinner) findViewById(R.id.groupSelectionSpinner);

		// Get students in current group.
		Integer[] curGroup = tut.getGroupMembers(gId);
		// Get ungrouped students.
		Integer[] availStus = tut.getUngroupedStudents();
		// Combine the above two lists to form the group choices.
		displayedStuNums = new Integer[curGroup.length + availStus.length];
		displayedStus = new String[displayedStuNums.length];
		for (int i = 0; i < displayedStuNums.length; i++) {
			if (i < curGroup.length)
				displayedStuNums[i] = curGroup[i];
			else
				displayedStuNums[i] = availStus[i - curGroup.length];
			displayedStus[i] = sbank.getStudentName(displayedStuNums[i]);
		}
		gName.setText(gId);
		stuSp.setItems(displayedStus);
		// Select all the students in current group
		int[] indicies = new int[curGroup.length];
		for (int i = 0; i < curGroup.length; i++) 
			indicies[i] = i;
		stuSp.setSelection(indicies);
		stuSp.invalidate();
	}

	public void saveButton(View view) {
		// Get info from edit text fields.
		String newGName = gName.getText().toString();
		List<Integer> stuList = stuSp.getSelectedIndicies();
		if (newGName.length() == 0 || stuList.size() < 2) {
			Toast.makeText(this,
					"Group cannot have empty name, or less than 2 students.",
					Toast.LENGTH_LONG).show();
			return;
		}
		int[] groupList = new int[stuList.size()];

		for (int i = 0; i < stuList.size(); i++)
			groupList[i] = displayedStuNums[stuList.get(i)];

		// If group is successfully added to tutorial,
		if (tut.modifyGroup(gId, newGName, groupList)) {
			Toast.makeText(this, "Group successfully modified.",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "New group name already exists.",
					Toast.LENGTH_SHORT).show();
			return;
		}
		onBackPressed();
	}

	public void cancelButton(View view) {
		onBackPressed();
	}

	public void deleteButton(View view) {
		tut.removeGroup(gId);
		Toast.makeText(this, "Group deleted.", Toast.LENGTH_SHORT).show();
		onBackPressed();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mod_group, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, TabStuActivity.class);
		intent.putExtra(modCourseTag, cId);
		intent.putExtra(modTutorialTag, tId);
		intent.putExtra(startAtTabTag, tabId);
		NavUtils.navigateUpTo(this, intent);;
		finish();
	}

}
