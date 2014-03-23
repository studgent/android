package be.ugent.oomo.groep12.studgent.view;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import be.ugent.oomo.groep12.studgent.common.PointOfInterest;

import com.google.android.gms.maps.model.LatLng;

public class OverlayView extends View {

	private int screenWidth;
	private int screenHeight;
	private static int fov = 80;
	private Paint paint;

	public OverlayView(Context context) {
		super(context);
		init(context);
	}

	public OverlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public OverlayView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.GRAY);
		paint.setStrokeWidth(7);
		paint.setStyle(Paint.Style.STROKE);

		screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		screenHeight = context.getResources().getDisplayMetrics().heightPixels;
	}

	@Override
	public void onDraw(Canvas canvas) {
		init(getContext());
		canvas.drawCircle(screenWidth / 2, screenHeight / 2, 35, paint);
	}

	public void updateOverlay(int az) {
		// TODO: Do some calculations to determine bearing to device

		// For now, we only display north, so bearing = azimuth
		float offset = calcOffset(az);
		if (offset > screenWidth / 2) {
			// Do not draw on this pass
			//this.setVisibility(View.INVISIBLE);
		} else {
			//this.setVisibility(View.VISIBLE);
			this.setX(-offset);
		}
	}

	private float calcOffset(int az) {
		float offset;
		float angle;

		if (az >= (360 - (fov / 2))) {
			angle = 360 - az;
			angle = -angle;
		} else if (az <= (fov / 2)) {
			angle = az;
		} else {
			angle = 90;
		}
		offset = screenWidth * (angle / fov);

		return offset;
	}
}
