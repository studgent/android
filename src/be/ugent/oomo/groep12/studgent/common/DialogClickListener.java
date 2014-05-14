package be.ugent.oomo.groep12.studgent.common;

import android.view.View;

public class DialogClickListener {

	@SuppressWarnings("unused")
	private View view;
	@SuppressWarnings("unused")
	private boolean vriendToevoegen;
	
	public DialogClickListener(View v, boolean vriendToevoegen){
		this.view=v;
		this.vriendToevoegen = vriendToevoegen;
	}
}
