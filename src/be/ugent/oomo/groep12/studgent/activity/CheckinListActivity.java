package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.CheckinAdapter;
import be.ugent.oomo.groep12.studgent.common.Checkin;
import be.ugent.oomo.groep12.studgent.data.CheckinsDataSource;
import be.ugent.oomo.groep12.studgent.utilities.MenuUtil;

public class CheckinListActivity extends Activity{
	
	protected CheckinAdapter adapter;
	protected ListView checkin_list_view;
	protected View view;
	protected boolean checkinOfUser;
	protected int sourceID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_checkin_list);
		
		checkinOfUser = true;
		checkinOfUser = this.getIntent().getExtras().getBoolean("checkinOfUser");
		sourceID = this.getIntent().getExtras().getInt("sourceID");
		
		checkin_list_view = (ListView) findViewById(R.id.checkin_list);
		
		// hide keyboard on start activity
	    this.getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		
		// create adapter with empty list and attach custom item view
        adapter = new CheckinAdapter(this, R.layout.checkin_list_item, new ArrayList<Checkin>());
        
        checkin_list_view.setAdapter(adapter);

        new AsyncCheckinListViewLoader().execute(adapter);

        
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
	


	

	
	private class AsyncCheckinListViewLoader extends AsyncTask<CheckinAdapter, Void, ArrayList<Checkin> > {
	    private final ProgressDialog dialog = new ProgressDialog(CheckinListActivity.this);

	    @Override
	    protected void onPostExecute(ArrayList<Checkin> result) {            
	        super.onPostExecute(result);
	        dialog.dismiss();
	        adapter.clear();
	        if (result != null){
		       
		        for(Checkin checkin: result){
		        	adapter.add(checkin);
		        }
		        checkin_list_view.setAdapter(adapter);
		        adapter.notifyDataSetChanged();
		        
		        if (result.size() > 0){
		        	//show textview 'no data'
			        TextView txt = (TextView) findViewById(R.id.checkin_list_nodata);
			        txt.setVisibility(View.GONE);
		        }else{
		        	 TextView txt = (TextView) findViewById(R.id.checkin_list_nodata);
				     txt.setVisibility(View.VISIBLE);
		        }
	        }
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage("Checkins ophalen...");
	        dialog.show();            
	    }

		@Override
		protected ArrayList<Checkin> doInBackground(CheckinAdapter... params) {
	        try {
	        	CheckinsDataSource checkinDataSource = new CheckinsDataSource(checkinOfUser, sourceID);
	        	Map<Integer, Checkin> items = checkinDataSource.getLastItems();
	        	return new ArrayList<Checkin>(items.values());
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        return null;
		}
	}
	


}
