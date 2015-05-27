package com.hash.taid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.hash.core.Mark;
import com.hash.core.Tutorial;
import com.hash.taid.customs.TAidActivity;

public class ModMarksActivity extends TAidActivity {

	String[] aNames;
	Spinner sp;
	TextView markText;
	EditText markEdit;
	Tutorial tut;
	CheckBox ifMarked;
	String unmarkedText = "-";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mod_marks);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();

		cId = intent.getIntExtra(modCourseTag, 0);
		tId = intent.getIntExtra(modTutorialTag, 0);
		sId = intent.getIntExtra(modStuTag, 0);
		tabId = intent.getIntExtra(startAtTabTag, 0);
		tut = clist.getCourse(cId).getTutorial(tId);

		// Get student mark array. Should contain at least one assignment entry.
		aNames = clist.getCourse(cId).getTutorial(tId).getAssignmentNames();

		// Set adapter for spinner (The list of assignment names);
		ArrayAdapter<String> spAdpt = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, aNames);
		spAdpt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		// Get the views from resource
		markText = (TextView) findViewById(R.id.markTextView);
		markEdit = (EditText) findViewById(R.id.markEditText);
		sp = (Spinner) findViewById(R.id.assignmentSpinner);
		ifMarked = (CheckBox) findViewById(R.id.ifMarkedCheckBox);
		// Set adapter for spinner
		sp.setAdapter(spAdpt);

		// Set spinner selection behavior
		sp.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parentView,
					View selectedItemView, int position, long id) {
				// negative value on marks implies an unspecified mark.
				Mark stuMark = tut.getStudentMark(sId, position);
				markText.setText("/" + stuMark.getMaxMark());
				if (stuMark.getMark() < 0) {
					markEdit.setEnabled(false);
					markEdit.setText(unmarkedText);
					ifMarked.setChecked(true);
				} else {
					markEdit.setEnabled(true);
					markEdit.setText(Integer.toString(stuMark.getMark()));
					ifMarked.setChecked(false);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				markEdit.setEnabled(false);
				markEdit.setText("N/A");
				markText.setText("/ N/A");
			}
		});

		// Immediately modify student's mark on mark change.
		markEdit.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (markEdit.isEnabled() && s.length() > 0)
					// Only make the move if edit text field is enabled.
					tut.changeMark(sId, Integer.parseInt(s.toString()),
							sp.getSelectedItemPosition());
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		// Set check box event listener
		ifMarked.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				sp.getSelectedItemPosition();
				if (isChecked) { // Set unmarked
					tut.changeMark(sId, -1, sp.getSelectedItemPosition());
					markEdit.setEnabled(false);
					// markText.setText("/" + stuMark.getMaxMark());
				} else {
					if (markEdit.getText().toString().equals(unmarkedText))
						markEdit.setText("");
					markEdit.setEnabled(true);
					int nMark = 0;
					if (markEdit.getText().toString().length() > 0)
						nMark = Integer.parseInt(markEdit.getText().toString());
					tut.changeMark(sId, nMark, sp.getSelectedItemPosition());
					// markText.setText(markSeek.getProgress() / 2f + "%");
				}

			}
		});
	}

	public void saveButton(View view) {
		this.onBackPressed();
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
		getMenuInflater().inflate(R.menu.mod_marks, menu);
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
			this.onBackPressed();
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
