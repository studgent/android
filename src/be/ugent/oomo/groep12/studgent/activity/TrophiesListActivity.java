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
import android.widget.EditText;
import android.widget.ListView;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.adapter.TrophieAdapter;
import be.ugent.oomo.groep12.studgent.common.Trophie;
import be.ugent.oomo.groep12.studgent.data.TrophieListDataSource;

public class TrophiesListActivity  extends Activity implements TextWatcher {
	
	protected Trophie[] trophie_data;
	protected TrophieAdapter adapter;
	protected ListView trophie_list_view;
	protected EditText inputSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_friendlist);
		
		trophie_list_view = (ListView) findViewById(R.id.trophie_list);
		inputSearch = (EditText) findViewById(R.id.searchTrophies_EditText);
		inputSearch.addTextChangedListener(this);
		
        /*View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        event_list_view.addHeaderView(header);*/
		
		// create adapter with empty list and attach custom item view
        adapter = new TrophieAdapter(this, R.layout.trophie_list_item, new ArrayList<Trophie>());
        
        trophie_list_view.setAdapter(adapter);
        new AsyncTrophieListViewLoader().execute(adapter);
        
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
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
	        	Map<Integer, Trophie> events = TrophieListDataSource.getInstance().getLastItems();
	        	return new ArrayList<Trophie>(events.values());
	        }
	        catch(Throwable t) {
	            t.printStackTrace();
	        }
	        return null;
		}
	}


	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		adapter.getFilter().filter(s);
		
	}


	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

}