package com.hash.taid.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hash.taid.R;
import com.hash.taid.customs.TAidActivity;
import com.inqbarna.tablefixheaders.TableFixHeaders;

public class MarkTableFragment extends Fragment {
	static int cId, tId;
	TableFixHeaders tableFixHeaders;

	public MarkTableFragment() {
	}

	// Called when this fragment needs to be displayed for the first time.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		cId = getArguments().getInt(TAidActivity.modCourseTag);
		tId = getArguments().getInt(TAidActivity.modTutorialTag);

		View rootView = inflater.inflate(R.layout.fragment_mark_table,
				container, false);

		tableFixHeaders = (TableFixHeaders) rootView
				.findViewById(R.id.mark_table);

		int bodyResource = R.layout.mark_table_body_text;
		int headResource = R.layout.mark_table_header_text;
		int textResource = R.id.text1;

		MarkTableAdapter mkAdpt = new MarkTableAdapter(this.getActivity(),
				TAidActivity.clist.getCourse(cId).getTutorial(tId), bodyResource,
				headResource, headResource, headResource, bodyResource, textResource);

		tableFixHeaders.setAdapter(mkAdpt);

		return rootView;
	}

//	@Override
//	public void onResume() {
//		super.onResume();
//		tableFixHeaders.invalidate();
//		Toast.makeText(this.getActivity(), "invalidated", Toast.LENGTH_SHORT).show();
//	}

}
