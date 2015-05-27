package com.hash.taid.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.hash.core.Tutorial;
import com.hash.taid.AddStuActivity;
import com.hash.taid.ModMarksActivity;
import com.hash.taid.ModStuActivity;
import com.hash.taid.customs.TAidActivity;

public class StuListFragment extends ListFragment {
	int cId, tId, tabId;
	int[] sNumArray;
	List<String> sArray = new ArrayList<String>();
	Tutorial tut;

	public StuListFragment() {
	}

	// Called when this fragment needs to be displayed for the first time.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cId = getArguments().getInt(TAidActivity.modCourseTag);
		tId = getArguments().getInt(TAidActivity.modTutorialTag);
		tabId = getArguments().getInt(TAidActivity.startAtTabTag);
		tut = TAidActivity.clist.getCourse(cId).getTutorial(tId);

		// Set up adapter for list of students.
		ArrayAdapter<String> arrAdpt = new ArrayAdapter<String>(
				inflater.getContext(), android.R.layout.simple_list_item_1,
				sArray);
		setListAdapter(arrAdpt);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	// Called to update the array adapter used to display this list.
	private void updateArray() {
		// Get student number array
		sNumArray = tut.getStudentIds();
		sArray.clear();
		sArray.add("add Student"); // The button for adding a new student

		// Retrieve the student objects from student bank, and add their names
		// to an array where they will be displayed to the user in a list.
		// Note that the names in sArray has a one to one correspondence with
		// Student numbers in sNumArray.
		String s;
		for (int sNum : sNumArray) {
			s = TAidActivity.sbank.getStudentName(sNum);
			if (s.equalsIgnoreCase("")) {
				sArray.add("-");
			} else {
				sArray.add(s);
			}
		} // End of for
	}

	// When user returns back to this fragment, update the student list with
	// possible modifications.
	@Override
	public void onResume() {
		super.onResume();
		this.updateArray();
		getListView().invalidateViews();
		// Toast.makeText(this.getActivity(), "Resumed",
		// Toast.LENGTH_SHORT).show();
	}

	private void addStudent() {
		Intent intent = new Intent(getActivity(), AddStuActivity.class);
		// User wants to add a student
		intent.putExtra(TAidActivity.modCourseTag, cId);
		intent.putExtra(TAidActivity.modTutorialTag, tId);
		intent.putExtra(TAidActivity.startAtTabTag, tabId);
		startActivity(intent);
	}

	private void modMarks(int position) {
		if (tut.getNumAssignments() == 0) {
			Toast.makeText(getActivity(),
					"No assignments exist. Cannot modify mark for student.",
					Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(getActivity(), ModMarksActivity.class);
		intent.putExtra(TAidActivity.modCourseTag, cId);
		intent.putExtra(TAidActivity.modTutorialTag, tId);
		intent.putExtra(TAidActivity.modStuTag, sNumArray[position - 1]);
		intent.putExtra(TAidActivity.startAtTabTag, tabId);
		startActivity(intent);
	}

	private void modStudent(int position) {
		Intent intent = new Intent(getActivity(), ModStuActivity.class);
		intent.putExtra(TAidActivity.modCourseTag, cId);
		intent.putExtra(TAidActivity.modTutorialTag, tId);
		intent.putExtra(TAidActivity.modStuTag, sNumArray[position - 1]);
		intent.putExtra(TAidActivity.startAtTabTag, tabId);
		startActivity(intent);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		OnItemClickListener onItemClick = new OnItemClickListener() {
			// Action to do when a tile is clicked
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0)
					addStudent();
				else
					modMarks(position);
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
					modStudent(position);
					return true;
				}
			}// End of abstract method fill in
		};// onItemClick set

		getListView().setOnItemClickListener(onItemClick);
		getListView().setOnItemLongClickListener(onItemLongClick);
	}

}
