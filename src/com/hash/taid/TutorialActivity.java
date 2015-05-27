package com.hash.taid;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.hash.core.Tutorial;
import com.hash.taid.customs.TAidActivity;

public class TutorialActivity extends TAidActivity {

	GridView gv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tutorial);
		// Show the Up button in the action bar.
		setupActionBar();

		Intent intent = getIntent();
		cId = intent.getIntExtra(modCourseTag, 0);

		setTitle("Course: " + clist.getCourse(cId).getCourseName());

		List<String> tArray = new ArrayList<String>();

		tArray.add("add tut"); // The button for adding a new course
		for (Tutorial t : clist.getCourse(cId).getTutorials()) {
			if (t.getName().trim().equalsIgnoreCase("")) {
				tArray.add("No Name");
			} else {
				tArray.add(t.getName());
			}
		}

		gv = (GridView) findViewById(R.id.gridView2);

		ArrayAdapter<String> arrAdpt = new ArrayAdapter<String>(this,
				R.layout.gridview_text, R.id.textView1, tArray);

		gv.setAdapter(arrAdpt);

		OnItemClickListener onItemClick = new OnItemClickListener() {
			// Action to do when a tile is clicked
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (position == 0) {
					Intent intent = new Intent(TutorialActivity.this,
							ModTutsActivity.class);
					// User wants to add new course
					intent.putExtra(modCourseTag, cId);
					intent.putExtra(modTutorialTag, -1);
					startActivity(intent);
				} else {
					Intent intent = new Intent(TutorialActivity.this,
							TabStuActivity.class);
					// User is going into student list of tutorial
					intent.putExtra(modCourseTag, cId);
					intent.putExtra(modTutorialTag, position - 1);
					startActivity(intent);
				}
			}// End of abstract method fill in
		};// onItemClick set

		OnItemLongClickListener onItemLongClick = new OnItemLongClickListener() {
			// Action to do when a tile is clicked
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return true;
				} else {
					Intent intent = new Intent(TutorialActivity.this,
							ModTutsActivity.class);
					intent.putExtra(modCourseTag, cId);
					intent.putExtra(modTutorialTag, position - 1);
					startActivity(intent);
					return true;
				}
			}// End of abstract method fill in
		};// onItemClick set
		gv.setOnItemClickListener(onItemClick);
		gv.setOnItemLongClickListener(onItemLongClick);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tutorial, menu);
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
			finish();
			return true;
		case R.id.action_memo:
			Intent intent = new Intent(this, MemoAct.class);
			intent.putExtra(modCourseTag, cId);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		gv.invalidateViews();
	}

}
