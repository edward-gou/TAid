package com.hash.taid;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.hash.core.Course;
import com.hash.core.FileManager;
import com.hash.taid.customs.TAidActivity;

public class MainActivity extends TAidActivity {
	boolean loaded = false;
	GridView gv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Enable first time loading, if this activity has been initialized
		//for the first time. 
		if (!loaded)
		{
			FileManager fm = new FileManager();
			String[] xml = fm.load(getApplicationContext());
			if (xml != null)
			{
				clist.load(xml[1]);
				sbank.load(xml[0]);
				user.load(xml[2]);
			}
		}
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		List<String> cArray = new ArrayList<String> ();
		
		cArray.add("add course"); //The button for adding a new course
		
		//Populate cArray with the names of each course.
		for (Course c : clist.getCourseList()){
			if (c.getCourseName().trim().equalsIgnoreCase("")){
				cArray.add("No Name");
			} else{
				cArray.add(c.getCourseName());
			}
		}
		
		gv = (GridView)findViewById(R.id.gridView1);
		//Initialize array adapter for grid view. 
		ArrayAdapter<String> arrAdpt = new ArrayAdapter<String>(this, 
				R.layout.gridview_text,
				R.id.textView1,
				cArray);
		
		gv.setAdapter(arrAdpt);
		
		//Define action when item is clicked. 
		OnItemClickListener onItemClick = new OnItemClickListener() {
			//Action to do when a tile is clicked
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				//New course button is selected. 
				if (position == 0){
					Intent intent = new Intent(MainActivity.this, ModCoursesActivity.class);
					intent.putExtra(modCourseTag, -1);
					startActivity(intent);
					// finish();
				}
				//Go inside the course to view tutorials. 
				else{
					Intent intent = new Intent(MainActivity.this, TutorialActivity.class);
					//User is going into tutorial list of course.
					intent.putExtra(modCourseTag, position-1);
					startActivity(intent);
					//No need to refresh main display, as courselist is not modified.
				}
			}//End of abstract method fill in
		};//onItemClick set
		
		//Define actions when user long-clicks. 
		OnItemLongClickListener onItemLongClick = new OnItemLongClickListener() {
			//Action to do when a tile is clicked
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				//Do nothing if user long-clicks the add button. 
				if (position == 0){
					return true;
				}
				//Open up editing window for editing entries. 
				else{
					Intent intent = new Intent(MainActivity.this, ModCoursesActivity.class);
					intent.putExtra(modCourseTag, position-1);
					startActivity(intent);
					//finish();
					return true;
				}
			}//End of abstract method fill in
		};//onItemClick set
		gv.setOnItemClickListener(onItemClick);
		gv.setOnItemLongClickListener(onItemLongClick);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override 
	public void onResume(){
		super.onResume();
		gv.invalidateViews(); //Force redraw. 
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, ModUserActivity.class);
			startActivity(intent);
			
			return true;
		}
		return false;
	}
  
}
