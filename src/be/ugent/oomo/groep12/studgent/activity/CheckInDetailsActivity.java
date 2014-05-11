package be.ugent.oomo.groep12.studgent.activity;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;

public class CheckInDetailsActivity  extends Activity {

	private PointOfInterest poi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		poi = getIntent().getParcelableExtra("poi");
		setContentView(R.layout.activity_checkin_details);

		
		TextView txtTitle =   (TextView)findViewById(R.id.txtTitle);
		TextView txtDetail =  (TextView)findViewById(R.id.txtDetail);
		TextView txtUrl = (TextView)findViewById(R.id.txtURL);
		
		txtTitle.setVisibility(View.INVISIBLE);
		txtDetail.setVisibility(View.INVISIBLE);
		txtUrl.setVisibility(View.INVISIBLE);
		
		if (poi.getName() != null){
			txtTitle.setText(poi.getName());
			txtTitle.setVisibility(View.VISIBLE);
		}
		
		if (poi.getDetails() != null){
			txtDetail.setText(poi.getDetails().replace(';', '\n') );
			txtDetail.setVisibility(View.VISIBLE);
		}
		
		if (poi.getUrl() != null ) {
			txtUrl.setText(poi.getUrl());	
			txtUrl.setVisibility(View.VISIBLE);
		}
	
	    
	}
	
	
	 public void checkIn(View view) {
			//finish checkin. This means storing the key value pare wich remambers the time, location and POIid of the last checkin
		 SharedPreferences sharedPreferences = this.getSharedPreferences("LastCheckin", Context.MODE_PRIVATE);
		 
         SharedPreferences.Editor editor = sharedPreferences.edit();
         editor.putString("lat", String.valueOf(poi.getLocation().latitude));
         editor.putString("lon", String.valueOf(poi.getLocation().longitude));
         Calendar c = Calendar.getInstance();
         Date now = c.getTime();
         editor.putLong("date", now.getTime());
         editor.putString("name", poi.getName());
         editor.commit();
         long def = 1;
         System.out.println("CheckInDetailsActivity: name: "+sharedPreferences.getString("name", "error"));
         System.out.println("CheckInDetailsActivity: date: "+sharedPreferences.getLong("date", def));
         System.out.println("CheckInDetailsActivity: lat: "+sharedPreferences.getString("lat", "error"));
         System.out.println("CheckInDetailsActivity: lon: "+sharedPreferences.getString("lon", "error"));
         //deze checkin moet hier ook nog geregistreerd worden aan de backend
		 this.finish();
	 }
	 
	
	
	

	
}
