package be.ugent.oomo.groep12.studgent.activity;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.R.id;
import be.ugent.oomo.groep12.studgent.R.layout;
import be.ugent.oomo.groep12.studgent.R.string;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Build;

public class AboutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new AboutFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	

	@Override
	public boolean onPrepareOptionsMenu (Menu menu) {

		return MenuUtil.PrepareMenu(this, menu);
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    return MenuUtil.OptionsItemSelected(this, item);
	}
	


	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class AboutFragment extends Fragment {

		public AboutFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_about,
					container, false);
			
			TextView aboutTV = (TextView) rootView.findViewById(R.id.about_textview);
			aboutTV.setText(Html.fromHtml( getString(R.string.about_text) ));
			aboutTV.setMovementMethod(LinkMovementMethod.getInstance());

			ImageView image = (ImageView) rootView.findViewById(R.id.suImage);
			image.setVisibility(android.view.View.VISIBLE);
			return rootView;
		}
	}

}
