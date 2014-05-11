package be.ugent.oomo.groep12.studgent.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import be.ugent.oomo.groep12.studgent.common.IPointOfInterest;

public class POIView extends View {

	private IPointOfInterest poi;
	private Paint cpaint;
	private Paint tpaint;
	private int screenHeight;
	
	private int circleRadius = 35;
	private int minWidth;

	public POIView(Context context) {
		super(context);
		init(context);
	}

	public POIView(Context context, IPointOfInterest poi) {
		super(context);
		init(context);

		this.poi = poi;
	}

	private void init(Context context) {
		// Paint object for POI marker
		cpaint = new Paint();
		cpaint.setAntiAlias(true);
		cpaint.setColor(Color.GRAY);
		cpaint.setStrokeWidth(7);
		cpaint.setStyle(Paint.Style.STROKE);
		
		// Paint object for POI text
		tpaint = new Paint();
		tpaint.setAntiAlias(true);
		tpaint.setColor(Color.GRAY);
		tpaint.setTextSize(18.0f);

		screenHeight = context.getResources().getDisplayMetrics().heightPixels;
		
		// Make invisible until we're on screen
		this.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onDraw(Canvas canvas) {
		int circleWidth = (int) (circleRadius*2 + cpaint.getStrokeWidth()*2);
		int textWidth = (int) (tpaint.measureText(poi.getName()));
		int yPos = (int) ((screenHeight /2) - ((tpaint.descent() + tpaint.ascent()) / 2)) + 48;
		
		if (circleWidth > textWidth) {
			// Circle is biggest
			int xPos = (circleWidth /2) - (textWidth / 2);
			canvas.drawCircle((circleWidth / 2), (screenHeight / 2), circleRadius, cpaint);
			canvas.drawText(poi.getName(), xPos, yPos, tpaint);
		} else {
			// Text is biggest
			int xPos = (textWidth / 2);
			canvas.drawCircle(xPos, (screenHeight / 2), circleRadius, cpaint);
			canvas.drawText(poi.getName(), 0, yPos, tpaint);
		}
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	minWidth = 0;
    	int minHeight = 0;
    	
    	int textWidth = (int) tpaint.measureText(poi.getName());
    	int iconWidth = (int) (circleRadius*2 + cpaint.getStrokeWidth()*2);
    	minWidth = Math.max(textWidth, iconWidth);

    	minHeight = screenHeight;
    	
        setMeasuredDimension(MeasureSpec.makeMeasureSpec(minWidth, MeasureSpec.EXACTLY),
        		MeasureSpec.makeMeasureSpec(minHeight, MeasureSpec.EXACTLY));
    }

	public IPointOfInterest getPoi() {
		return poi;
	}

	public void setPoi(IPointOfInterest poi) {
		this.poi = poi;
	}
	
	public int getMinWidth() {
		return minWidth;
	}

}
