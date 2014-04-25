package be.ugent.oomo.groep12.studgent.common;

import android.view.View;

public class DialogClickListener {

	private View view;
	private boolean vriendToevoegen;
	
	public DialogClickListener(View v, boolean vriendToevoegen){
		this.view=v;
		this.vriendToevoegen = vriendToevoegen;
	}
}
