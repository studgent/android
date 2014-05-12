package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.TrophieAdapter;
import be.ugent.oomo.groep12.studgent.common.Trophie;
import be.ugent.oomo.groep12.studgent.data.TrophieListDataSource;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;

public class TrophiesListActivity  extends Activity {
	
	protected Trophie[] trophie_data;
	protected TrophieAdapter adapter;
	protected ListView trophie_list_view;
	protected EditText inputSearch;
	private int userID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_trophies);
		
		userID = getIntent().getExtras().getInt("userID");

		// hide keyboard on start activity
	    this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	    
		trophie_list_view = (ListView) findViewById(R.id.trophie_list);
		
        /*View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        event_list_view.addHeaderView(header);*/
		
		// create adapter with empty list and attach custom item view
        adapter = new TrophieAdapter(this, R.layout.trophie_list_item, new ArrayList<Trophie>());
        
        trophie_list_view.setAdapter(adapter);
        
        if (LoginUtility.getInstance().isLoggedIn() == false) {
			Toast.makeText(this, "Log in om je trofeën te bekijken!", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}else{
			new AsyncTrophieListViewLoader().execute(adapter);
		}
        
	}


	private class AsyncTrophieListViewLoader extends AsyncTask<TrophieAdapter, Void, ArrayList<Trophie>> {
	    private final ProgressDialog dialog = new ProgressDialog(TrophiesListActivity.this);

	    @Override
	    protected void onPostExecute(ArrayList<Trophie> result) {            
	        super.onPostExecute(result);
	        
	        dialog.dismiss();
	        //adapter.setItemList(result);
	        adapter.clear();
	        for(Trophie trophie: result){
	        	adapter.add(trophie);
	        }
	        trophie_list_view.setAdapter(adapter);
	        adapter.notifyDataSetChanged();
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage(getString(R.string.load_trophielist));
	        dialog.show();            
	    }

		@Override
		protected ArrayList<Trophie> doInBackground(TrophieAdapter... params) {
			//adp = params[0];
	        try {
	        	TrophieListDataSource trofieDataSource = TrophieListDataSource.getInstance();
	        	trofieDataSource.setUserID(userID);
	        	Map<Integer, Trophie> trophies = trofieDataSource.getLastItems();
	        	return new ArrayList<Trophie>(trophies.values());
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        return null;
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

}