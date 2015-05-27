package com.hash.taid.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hash.core.Mark;
import com.hash.core.Tutorial;
import com.hash.taid.R;
import com.hash.taid.customs.TAidActivity;
import com.inqbarna.tablefixheaders.adapters.BaseTableAdapter;

public class MarkTableAdapter extends BaseTableAdapter {

	private final Context context;
	private final LayoutInflater inflater;
	private final Tutorial tut;
	private final int width, height, rowHeadWidth, attenWidth;

	private final int HEADER_COL = 0;
	private final int HEADER_ROW = 1;
	private final int BODY = 2;
	private final int ORIGIN = 3;
	private final int ATTEND_COL = 4;

	private final int RES_BODY, RES_ORIGIN, RES_HEADROW, RES_HEADCOL, RES_ATTDCOL,
			RES_TEXTVIEW;

	public MarkTableAdapter(Context context, Tutorial tut, int body,
			int origin, int headrow, int headcol, int attdcol, int textview) {
		this.context = context;
		this.tut = tut;
		inflater = LayoutInflater.from(context);

		Resources resources = context.getResources();

		width = resources.getDimensionPixelSize(R.dimen.table_width);
		height = resources.getDimensionPixelSize(R.dimen.table_height);
		rowHeadWidth = resources
				.getDimensionPixelSize(R.dimen.table_rowhead_width);
		attenWidth = resources.getDimensionPixelSize(R.dimen.table_attencol_width);

		RES_BODY = body;
		RES_ORIGIN = origin;
		RES_HEADROW = headrow;
		RES_HEADCOL = headcol;
		RES_TEXTVIEW = textview;
		RES_ATTDCOL = attdcol;
	}

	@Override
	public int getRowCount() {
		return tut.getNumStudents();
	}

	@Override
	public int getColumnCount() {
		return tut.getNumAssignments() + 1;
	}

	@Override
	public int getWidth(int column) {
		if (column < 0)
			return this.rowHeadWidth;
		if (column == 0)
			return this.attenWidth;
		//Else
		return this.width;
	}

	@Override
	public int getHeight(int row) {
		return height;
	}

	public String getCellString(int row, int column) {
		int type = this.getItemViewType(row, column);
		if (type == ORIGIN)
			return "Names";
		if (type == HEADER_COL)
			return TAidActivity.sbank.getStudentName(tut.getStudentId(row));
		if (type == HEADER_ROW){
			if (column == 0)
				return "Attn";
			//Else
			return tut.getAssignment(column-1).getName();
		}
		if (type == ATTEND_COL)
			return tut.getAttendanceMark(tut.getStudentId(row)).toString();
		// Type must therefore be BODY.
		Mark stuMark = tut.getStudentMark(tut.getStudentId(row), column-1);
		if (stuMark.getMark() < 0) // Is unmarked.
			return "N/A";
		// is marked.
		return stuMark.toString();
	}

	public int getLayoutResource(int row, int column) {
		switch (getItemViewType(row, column)) {
		case ORIGIN:
			return RES_ORIGIN;
		case BODY:
			return RES_BODY;
		case HEADER_ROW:
			return RES_HEADROW;
		case HEADER_COL:
			return RES_HEADCOL;
		case ATTEND_COL:
			return RES_ATTDCOL;
		default:
			throw new RuntimeException("wtf?");
		}
	}

	@Override
	public int getItemViewType(int row, int column) {
		if (row < 0 && column < 0)
			return ORIGIN;
		if (row < 0)
			return HEADER_ROW;
		if (column < 0)
			return HEADER_COL;
		if (column == 0)
			return ATTEND_COL;
		return BODY;
	}

	@Override
	public int getViewTypeCount() {
		return 5;
	}

	public Context getContext() {
		return context;
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	@Override
	public View getView(int row, int column, View convertView, ViewGroup parent) {
		if (convertView == null)
			convertView = inflater.inflate(getLayoutResource(row, column),
					parent, false);
		((TextView) convertView.findViewById(RES_TEXTVIEW))
				.setText(getCellString(row, column));
		return convertView;
	}

}
