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
import com.hash.taid.AddGroupActivity;
import com.hash.taid.ModGroupActivity;
import com.hash.taid.ModGroupMarkActivity;
import com.hash.taid.customs.TAidActivity;

public class GroupListFragment extends ListFragment {
	int cId, tId, tabId;
	List<String> gArray = new ArrayList<String>();
	Tutorial tut;

	public GroupListFragment() {
	}

	// Called when this fragment needs to be displayed for the first time.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cId = getArguments().getInt(TAidActivity.modCourseTag);
		tId = getArguments().getInt(TAidActivity.modTutorialTag);
		tut = TAidActivity.clist.getCourse(cId).getTutorial(tId);
		tabId = getArguments().getInt(TAidActivity.startAtTabTag);

		// Set up adapter for list of groups.
		ArrayAdapter<String> arrAdpt = new ArrayAdapter<String>(
				inflater.getContext(), android.R.layout.simple_list_item_1,
				gArray);
		setListAdapter(arrAdpt);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	// Called to update the array adapter used to display this list.
	private void updateArray() {
		gArray.clear();
		gArray.add("add Group"); // The button for adding a new student

		for (String gName : TAidActivity.clist.getCourse(cId).getTutorial(tId)
				.getGroupNames()) {
			gArray.add(gName);
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

	private void addGroup() {
		Intent intent = new Intent(getActivity(), AddGroupActivity.class);
		// User wants to add a student
		intent.putExtra(TAidActivity.modCourseTag, cId);
		intent.putExtra(TAidActivity.modTutorialTag, tId);
		intent.putExtra(TAidActivity.startAtTabTag, tabId);
		startActivity(intent);
	}

	private void modGroup(int position) {
		Intent intent = new Intent(getActivity(), ModGroupActivity.class);
		intent.putExtra(TAidActivity.modCourseTag, cId);
		intent.putExtra(TAidActivity.modTutorialTag, tId);
		intent.putExtra(TAidActivity.modGroupTag, gArray.get(position));
		intent.putExtra(TAidActivity.startAtTabTag, tabId);
		startActivity(intent);
	}

	private void modGroupMark(int position) {
		if (tut.getNumGroupAssignments() == 0) {
			Toast.makeText(
					getActivity(),
					"No group assignments exist. Cannot modify mark for group.",
					Toast.LENGTH_LONG).show();
			return;
		}
		Intent intent = new Intent(getActivity(), ModGroupMarkActivity.class);
		intent.putExtra(TAidActivity.modCourseTag, cId);
		intent.putExtra(TAidActivity.modTutorialTag, tId);
		intent.putExtra(TAidActivity.modGroupTag, gArray.get(position));
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
					addGroup();
				else
					modGroupMark(position);
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
					modGroup(position);
					return true;
				}
			}// End of abstract method fill in
		};// onItemClick set

		getListView().setOnItemClickListener(onItemClick);
		getListView().setOnItemLongClickListener(onItemLongClick);
	}
}
