package be.ugent.oomo.groep12.studgent.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;

public class POIView extends View{
	
	private PointOfInterest poi;
	private Paint cpaint;
	private Paint tpaint;
	private int screenWidth;
	private int screenHeight;

	public POIView(Context context) {
		super(context);
		init(context);
	}
	
	public POIView(Context context, PointOfInterest poi) {
		super(context);
		init(context);
		
		this.poi = poi;
	}
	
	private void init(Context context) {
		
		// Otherwise no onDraw() call
		this.setWillNotDraw(false);
		
		cpaint = new Paint();
		cpaint.setAntiAlias(true);
		cpaint.setColor(Color.GRAY);
		cpaint.setStrokeWidth(7);
		cpaint.setStyle(Paint.Style.STROKE);
		
		tpaint = new Paint();
		tpaint.setAntiAlias(true);
		tpaint.setColor(Color.GRAY);
		tpaint.setTextSize(18.0f);
		
		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		screenHeight = context.getResources().getDisplayMetrics().heightPixels;
		
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawCircle(screenWidth / 2, screenHeight / 2, 35, cpaint);
		int xPos = (int) ((screenWidth / 2) - (tpaint.measureText(poi.getName()) / 2 ));
		int yPos = (int) ((screenHeight / 2) - ((tpaint.descent() + tpaint.ascent()) / 2)) + 48 ;
		canvas.drawText(poi.getName(), xPos, yPos, tpaint);
	}

	public PointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(PointOfInterest poi) {
		this.poi = poi;
	}

}
