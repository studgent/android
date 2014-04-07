package be.ugent.oomo.groep12.studgent.activity;

import be.ugent.oomo.groep12.studgent.R;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class POIDetailActivity extends Activity {

	private PointOfInterest poi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		poi = getIntent().getParcelableExtra("poi");
		setContentView(R.layout.activity_poi_detail);

		
		TextView txtTitle =   (TextView)findViewById(R.id.txtTitle);
		TextView txtLocation = (TextView)findViewById(R.id.txtLocation);
		TextView txtDetail =  (TextView)findViewById(R.id.txtDetail);
		TextView txtUrl = (TextView)findViewById(R.id.txtURL);
		
		txtTitle.setVisibility(View.INVISIBLE);
		txtDetail.setVisibility(View.INVISIBLE);
		txtLocation.setVisibility(View.INVISIBLE);
		txtUrl.setVisibility(View.INVISIBLE);
		
		if (poi.getName() != null){
			txtTitle.setText(poi.getName());
			txtTitle.setVisibility(View.VISIBLE);
		}
		
		if (poi.getDetails() != null){
			txtDetail.setText(poi.getDetails().replace(';', '\n') );
			txtDetail.setVisibility(View.VISIBLE);
		}
		
		if (poi.getStreet() != null || poi.getNumber() != null){
			txtLocation.setText(poi.getStreet() + " " + poi.getNumber());
			txtLocation.setVisibility(View.VISIBLE);
		}
		
		if (poi.getUrl() != null ) {
			txtUrl.setText(poi.getUrl());	
			txtUrl.setVisibility(View.VISIBLE);
		}
	
	    
	}
	
	
	
	
	

	
}
