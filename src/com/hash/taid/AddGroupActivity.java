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

public class AddGroupActivity extends TAidActivity {

	EditText gName;
	MultiSelectionSpinner stuSp;
	Tutorial tut;
	String[] displayedStus;
	Integer[] availableStus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_group);
		// Show the Up button in the action bar.
		setupActionBar();
		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, tId);
		tabId = intent.getIntExtra(startAtTabTag, 0);
		tut = clist.getCourse(cId).getTutorial(tId);

		// Get the view elements.
		gName = (EditText) findViewById(R.id.groupNameEditText);
		stuSp = (MultiSelectionSpinner) findViewById(R.id.groupSelectionSpinner);

		// Fill in available students list (students to be grouped)
		availableStus = tut.getUngroupedStudents();
		displayedStus = new String[availableStus.length];
		for (int i = 0; i < availableStus.length; i++)
			displayedStus[i] = sbank.getStudentName(availableStus[i]);
		// Set the options for spinner
		stuSp.setItems(displayedStus);
	}

	public void saveButton(View view) {
		// Get info from edit text fields.
		String newGName = gName.getText().toString();
		List<Integer> stuList = stuSp.getSelectedIndicies();
		if (newGName.length() == 0 || stuList.size() < 2){
			Toast.makeText(this, "Group cannot have empty name, or less than 2 students.",
					Toast.LENGTH_LONG).show();
			return;
		}
		int[] groupList = new int[stuList.size()];

		for (int i = 0; i < stuList.size(); i++)
			groupList[i] = availableStus[stuList.get(i)];
		
		//If group is successfully added to tutorial, 
		if (tut.addGroup(newGName, groupList))
			Toast.makeText(this, "Group successfully added.",
					Toast.LENGTH_SHORT).show();
		else{
			Toast.makeText(this, "Group name already exists.", Toast.LENGTH_SHORT)
				.show();
			return;
		}
		onBackPressed();
	}

	public void cancelButton(View view) {
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
		getMenuInflater().inflate(R.menu.add_group, menu);
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
