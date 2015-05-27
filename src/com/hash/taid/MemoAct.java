package com.hash.taid;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.hash.taid.customs.TAidActivity;

public class MemoAct extends TAidActivity {

	private EditText textEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_memo);
		textEditor=(EditText)findViewById(R.id.textbox);
		cId = getIntent().getIntExtra(modCourseTag, 0);
		
		//Check course memo to see if null. 
		//Null => the course was newly created. Display nothing on edit text box. (dont do anything)
		//Else, set the edit text field to be what is contained in coruse.
		if (clist.getCourse(cId).getCourseNotes() != null){
			textEditor.setText(clist.getCourse(cId).getCourseNotes());
		}
	}
	
	//Save button clicked.
	public void save_clicked(View view){
		//Do save button action.
		//Get string stored in edit text.
		//Store it into course.
		clist.getCourse(cId).setCourseNotes(textEditor.getText().toString());
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.memo, menu);
		return true;
	}

}
