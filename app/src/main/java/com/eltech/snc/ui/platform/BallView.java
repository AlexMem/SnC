package com.eltech.snc.ui.platform;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class BallView extends View {
 	private PlatformFragment parent;
    private Paint paint;
    private double x;
    private double y;
    private final int radius = 25;
    private final int borderWidth = 15;
    private final int borderWidth_2 = borderWidth/2;
    private int width;
    private int height;

    public BallView(Context context) {
        super(context);
        init();
	}
	
    public BallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BallView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
	}

	void init() {
        paint = new Paint();
        //setFocusableInTouchMode(true);
    }

	public void setParent(PlatformFragment parent) {
		this.parent = parent;	
	}

    public void setCoord(double x, double y) {
		this.x = x;
		this.y = y;
		invalidate();
	}

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        parent.getBall().setArenaSize(this.width-2*radius,this.height-2*radius); // -2*radius to handle border correctly
		parent.getBall().resetPosition();
        super.onSizeChanged(w, h, oldw, oldh);
	}
    
    @Override
    protected void onDraw(Canvas canvas) {
		paint.setColor(Color.WHITE);
		canvas.drawRect(0, 0, width, height, paint);

		paint.setColor(Color.BLACK);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(borderWidth);
		canvas.drawRect(0, 0, width, height, paint);

		paint.setColor(Color.BLUE);
        paint.setStyle(Style.FILL);
		// limiting just to be sure
		int xpos = Math.min(width-radius-borderWidth_2, Math.max(radius,(int)x+radius+borderWidth_2));
		int ypos = Math.min(height-radius-borderWidth_2, Math.max(radius,(int)y+radius+borderWidth_2));
 	    canvas.drawCircle(xpos, ypos, radius, paint);
    }
}
