package be.ugent.oomo.groep12.studgent;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
			return rootView;
		}
	}

}
