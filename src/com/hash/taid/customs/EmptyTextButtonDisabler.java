package com.hash.taid.customs;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

/**
 * Disables a given button when the attached edit text field is empty.
 * 
 * @author Leeonadoh
 */
public class EmptyTextButtonDisabler implements TextWatcher {

	Button button;
	int id;
	boolean[] fieldsFilled;

	public EmptyTextButtonDisabler(Button s, boolean[] fieldsFilled, int fieldId) {
		button = s;
		this.fieldsFilled = fieldsFilled;
		this.id = fieldId;
	}

	@Override
	public void afterTextChanged(Editable s) {
		fieldsFilled[id] = (s.length() != 0);
		if (allFilled())
			button.setEnabled(true);
		else
			button.setEnabled(false);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	private boolean allFilled() {
		for (boolean b : fieldsFilled)
			if (!b)
				return false;
		return true;
	}

}
