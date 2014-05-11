package be.ugent.oomo.groep12.studgent.activity;

import java.util.ArrayList;

import android.widget.AdapterView;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.R.layout;
import be.ugent.oomo.groep12.studgent.R.menu;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import be.ugent.oomo.groep12.studgent.adapter.CalenderAdapter;
import be.ugent.oomo.groep12.studgent.adapter.FriendAdapter;
import be.ugent.oomo.groep12.studgent.common.Friend;
import be.ugent.oomo.groep12.studgent.common.ICalendarEvent;
import be.ugent.oomo.groep12.studgent.common.CalendarEvent;
import be.ugent.oomo.groep12.studgent.common.IData;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import be.ugent.oomo.groep12.studgent.data.CalendarEventDataSource;
import be.ugent.oomo.groep12.studgent.data.FriendListDataSource;
import be.ugent.oomo.groep12.studgent.utilities.LoginUtility;

public class FriendListActivity extends Activity implements AdapterView.OnItemClickListener, TextWatcher {
	
	protected ICalendarEvent[] event_data;
	protected FriendAdapter adapter;
	protected ListView friend_list_view;
	protected EditText inputSearch;
	protected View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.overridePendingTransition(R.anim.animation_enter,
				R.anim.animation_leave);
		setContentView(R.layout.activity_friendlist);
		
		friend_list_view = (ListView) findViewById(R.id.friends_list);
		friend_list_view.setOnItemClickListener(this);
		inputSearch = (EditText) findViewById(R.id.searchFriends_EditText);
		inputSearch.addTextChangedListener(this);
		
        /*View header = (View)getLayoutInflater().inflate(R.layout.listview_header_row, null);
        event_list_view.addHeaderView(header);*/
		
		// create adapter with empty list and attach custom item view
        adapter = new FriendAdapter(this, R.layout.friend_list_item, new ArrayList<Friend>());
        
        friend_list_view.setAdapter(adapter);

		if (LoginUtility.getInstance().isLoggedIn()==false){
			Toast.makeText(this, "Log in om de vrienden volgen!", Toast.LENGTH_SHORT).show();
			onBackPressed();
		}else{
	        new AsyncFriendListViewLoader().execute(adapter);
		}
        
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events, menu);
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> parent, View item, int position, long rowID) {
		System.out.println("geklikt op vriend");
		Intent intent = new Intent(this, TrophiesListActivity.class);
		//intent.putExtra("id", adapter.getItem(position).getId());
		startActivity(intent);
	}

	
	private class AsyncFriendListViewLoader extends AsyncTask<FriendAdapter, Void, ArrayList<Friend>> {
	    private final ProgressDialog dialog = new ProgressDialog(FriendListActivity.this);

	    @Override
	    protected void onPostExecute(ArrayList<Friend> result) {            
	        super.onPostExecute(result);
	        
	        dialog.dismiss();
	        //adapter.setItemList(result);
	        adapter.clear();
	        for(Friend friend: result){
	        	adapter.add(friend);
	        }
	        friend_list_view.setAdapter(adapter);
	        adapter.notifyDataSetChanged();
	    }

	    @Override
	    protected void onPreExecute() {        
	        super.onPreExecute();
	        dialog.setMessage("Gebruikers ophalen...");
	        dialog.show();            
	    }

		@Override
		protected ArrayList<Friend> doInBackground(FriendAdapter... params) {
			//adp = params[0];
	        try {
	        	Map<Integer, Friend> friends = FriendListDataSource.getInstance().getLastItems();
	        	return new ArrayList<Friend>(friends.values());
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
	
	public void changeImageButton(boolean addfriend){
		if(addfriend){
			view.setContentDescription(getString(R.string.friend_description));
			view.setBackgroundResource(R.drawable.check);
		}
		else{
			view.setContentDescription(getString(R.string.no_friend_description));
			view.setBackgroundResource(R.drawable.add_friend);
		}
	}
	
	public void change_friend_status(View view){
		this.view = view;
		System.out.println("er is geklikt op het icoon in de vriendenlijst");
		System.out.println("view.getContentDescription() = "+view.getContentDescription());
		System.out.println("R.string.check_discription = "+getString(R.string.friend_description));
		
		//creating alert dialog frame
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); //de this slaat op de ouder
			if(view.getContentDescription().equals(getString(R.string.friend_description))){ // in de adapter moet ik nog bij de niet vrienden die toegevoegd worden de contentDescription veranderen
				//alert dialog opmaken voor verwijderen van vriend
				alertDialogBuilder.setTitle(getString(R.string.remove_friend_title));
				alertDialogBuilder.setMessage(getString(R.string.remove_friend));
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							changeImageButton(false);
							dialog.cancel();
						}
					  });
				alertDialogBuilder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
			}
			else{
				//alert dialog opmaken voor toevoegen van vriend
				alertDialogBuilder.setTitle(getString(R.string.add_friend_title));
				alertDialogBuilder.setMessage(getString(R.string.add_friend));
				alertDialogBuilder.setCancelable(false);
				alertDialogBuilder.setPositiveButton(R.string.yes,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							changeImageButton(true);
							dialog.cancel();
						}
					  });
				alertDialogBuilder.setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						}
					});
			}
 
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
	}

}
