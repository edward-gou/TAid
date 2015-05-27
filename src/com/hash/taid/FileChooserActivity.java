package com.hash.taid;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import com.hash.core.FileArrayAdapter;
import com.hash.core.Option;
import com.hash.taid.customs.TAidActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class FileChooserActivity extends ListActivity {

	File dir = Environment.getExternalStorageDirectory();
	File f;
	String content;

	private File currentDir;
	private FileArrayAdapter adapter;
	private int cId, tId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cId = getIntent().getIntExtra(TAidActivity.modCourseTag, 0);
		tId = getIntent().getIntExtra(TAidActivity.modTutorialTag, 0);
		currentDir = new File("/sdcard/");
		fill(currentDir);
	}

	private void fill(File f) {
		File[] dirs = f.listFiles();
		this.setTitle("Current Dir: " + f.getName());
		List<Option> dir = new ArrayList<Option>();
		List<Option> fls = new ArrayList<Option>();
		try {
			for (File ff : dirs) {
				if (ff.isDirectory())
					dir.add(new Option(ff.getName(), "Folder", ff
							.getAbsolutePath()));
				else {
					fls.add(new Option(ff.getName(), "File Size: "
							+ ff.length(), ff.getAbsolutePath()));
				}
			}
		} catch (Exception e) {

		}
		Collections.sort(dir);
		Collections.sort(fls);
		dir.addAll(fls);
		if (!f.getName().equalsIgnoreCase("sdcard"))
			dir.add(0, new Option("..", "Parent Directory", f.getParent()));
		adapter = new FileArrayAdapter(FileChooserActivity.this,
				R.layout.file_view, dir);
		this.setListAdapter(adapter);
	}

	Stack<File> dirStack = new Stack<File>();

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Option o = adapter.getItem(position);
		if (o.getData().equalsIgnoreCase("folder")) {
			dirStack.push(currentDir);
			currentDir = new File(o.getPath());
			fill(currentDir);
		} else if (o.getData().equalsIgnoreCase("parent directory")) {
			currentDir = dirStack.pop();
			fill(currentDir);
		} else {
			onFileClick(o);
		}
	}

	@Override
	public void onBackPressed() {
		if (dirStack.size() == 0) {
			finish();
			return;
		}
		currentDir = dirStack.pop();
		fill(currentDir);
	}

	private void onFileClick(Option o) {
		f = new File(o.getPath());
		// content = "test";

		try {
			content = new Scanner(f).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		}
		Intent output = new Intent();
		output.putExtra("com.hash.taid.FileChooserActivity.content", content);
		output.putExtra(TAidActivity.modCourseTag, cId);
		output.putExtra(TAidActivity.modTutorialTag, tId);
		setResult(RESULT_OK, output);
		finish();
	}

}
