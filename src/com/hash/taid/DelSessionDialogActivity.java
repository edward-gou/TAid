package com.hash.taid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.hash.core.Tutorial;
import com.hash.taid.customs.TAidActivity;

public class DelSessionDialogActivity extends TAidActivity {

	Tutorial tut;
	String[] sessIds;
	Spinner sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_del_session_dialog);
		this.setFinishOnTouchOutside(false);
		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, 0);
		sId = intent.getIntExtra(modStuTag, 0);
		tabId = intent.getIntExtra(startAtTabTag, 0);

		tut = clist.getCourse(cId).getTutorial(tId);
		sessIds = tut.getSessionIds();
		// Set adapter for spinner (The list of assignment names);
		ArrayAdapter<String> spAdpt = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, sessIds);
		spAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		sp = (Spinner) findViewById(R.id.sessionSpinner);
		sp.setAdapter(spAdpt);
	}

	public void deleteButton(View view) {
		tut.deleteSession(sp.getSelectedItemPosition());
		Toast.makeText(this, "Session deleted.", Toast.LENGTH_SHORT).show();
		onBackPressed();
	}

	public void cancelButton(View view) {
		onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_session_dialog, menu);
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
			NavUtils.navigateUpFromSameTask(this);
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
		NavUtils.navigateUpTo(this, intent);
		;
		finish();
	}

}
