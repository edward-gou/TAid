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

import com.hash.taid.AddAssignmentActivity;
import com.hash.taid.ModAssignmentActivity;
import com.hash.taid.customs.TAidActivity;

public class AssignmentListFragment extends ListFragment {
	int cId, tId, tabId;
	List<String> aArray = new ArrayList<String>();

	public AssignmentListFragment() {
	}

	// Called when this fragment needs to be displayed for the first time.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cId = getArguments().getInt(TAidActivity.modCourseTag);
		tId = getArguments().getInt(TAidActivity.modTutorialTag);
		tabId = getArguments().getInt(TAidActivity.startAtTabTag);

		// Set up adapter for list of students.
		ArrayAdapter<String> arrAdpt = new ArrayAdapter<String>(
				inflater.getContext(), android.R.layout.simple_list_item_1,
				aArray);
		setListAdapter(arrAdpt);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	// Called to update the array adapter used to display this list.
	private void updateArray() {
		aArray.clear();
		aArray.add("add Assignment"); // The button for adding a new student

		for (String aName : TAidActivity.clist.getCourse(cId).getTutorial(tId).getAssignmentNames()) {
			aArray.add(aName);
		} // End of for
	}

	// When user returns back to this fragment, update the student list with
	// possible modifications.
	@Override
	public void onResume() {
		super.onResume();
		this.updateArray();
		getListView().invalidateViews();
	}

	private void addAssignment() {
		Intent intent = new Intent(getActivity(), AddAssignmentActivity.class);
		// User wants to add a student
		intent.putExtra(TAidActivity.modCourseTag, cId);
		intent.putExtra(TAidActivity.modTutorialTag, tId);
		intent.putExtra(TAidActivity.startAtTabTag, tabId);
		startActivity(intent);
	}

	private void modAssignment(int position) {
		Intent intent = new Intent(getActivity(), ModAssignmentActivity.class);
		intent.putExtra(TAidActivity.modCourseTag, cId);
		intent.putExtra(TAidActivity.modTutorialTag, tId);
		intent.putExtra(TAidActivity.modAsmtTag, position-1);
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

				if (position == 0) {
					addAssignment();
				} else {
					if (TAidActivity.clist.getCourse(cId).getTutorial(tId)
							.getNumAssignments() == 0) {
						// No assignments exist. Do not allow modifications.
						Toast.makeText(getActivity(),
								"Cannot modify marks. No assignment exists.",
								Toast.LENGTH_SHORT).show();
					} else {
						modAssignment(position);
					}
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
					modAssignment(position);
					return true;
				}
			}// End of abstract method fill in
		};// onItemClick set

		getListView().setOnItemClickListener(onItemClick);
		getListView().setOnItemLongClickListener(onItemLongClick);
	}
}
