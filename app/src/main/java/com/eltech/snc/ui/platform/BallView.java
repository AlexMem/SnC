package com.eltech.snc.ui.platform;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BallView extends View {
    private PlatformFragment parent;
    private Paint paint;
    private double x;
    private double y;
    private final int radius = 25;
    private final int borderWidth = 15;
    private final int borderWidth_2 = borderWidth / 2;
    private int width;
    private int height;
    private List<Obstacle> obstacles = new ArrayList<>();
    private ClippingPoint clippingPoint;
    private Random random = new Random();

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

    public void spawnObstacles() {
        obstacles.clear();
        int obstacleW = random.nextBoolean() ? 100 : 200;
        int obstacleH = obstacleW == 100 ? 200 : 100;
        obstacles.add(new Obstacle(random.nextInt(width), random.nextInt(height), obstacleW, obstacleH));
    }

    public void spawnClippingPoint() {
        if (width == 0 || height == 0) {
            return;
        }
        clippingPoint = new ClippingPoint(random.nextInt(width), random.nextInt(height));
    }

    public void removeClippingPoint() {
        clippingPoint = null;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public ClippingPoint getClippingPoint() {
        return clippingPoint;
    }

    public int getRadius() {
        return radius;
    }

    public PlatformFragment getPFParent() {
        return parent;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        parent.getBall().setArenaSize(this.width - 2 * radius, this.height - 2 * radius); // -2*radius to handle border correctly
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

        if (clippingPoint != null) {
            paint.setColor(Color.GREEN);
            paint.setAlpha(75);
            paint.setStyle(Style.FILL);
            canvas.drawCircle(clippingPoint.x, clippingPoint.y, clippingPoint.radius, paint);
//            paint.setColor(Color.RED);
//            canvas.drawPoint(clippingPoint.x+clippingPoint.radius, clippingPoint.y, paint);
//            canvas.drawPoint(clippingPoint.x-clippingPoint.radius, clippingPoint.y, paint);
//            canvas.drawPoint(clippingPoint.x, clippingPoint.y+clippingPoint.radius, paint);
//            canvas.drawPoint(clippingPoint.x, clippingPoint.y-clippingPoint.radius, paint);
        }

        paint.setColor(Color.BLUE);
        paint.setStyle(Style.FILL);
        // limiting just to be sure
        int xpos = Math.min(width - radius - borderWidth_2, Math.max(radius, (int) x + radius + borderWidth_2));
        int ypos = Math.min(height - radius - borderWidth_2, Math.max(radius, (int) y + radius + borderWidth_2));
        canvas.drawCircle(xpos, ypos, radius, paint);

// 	    int multiplier = 50;
// 	    paint.setStrokeWidth(5);
//        paint.setColor(Color.RED);
// 	    canvas.drawLine(xpos, ypos, xpos + (float) parent.getBall().accelX * multiplier, ypos + (float) parent.getBall().accelY * multiplier, paint);
//        paint.setColor(Color.YELLOW);
// 	    canvas.drawLine(xpos, ypos, xpos + (float) parent.getBall().speedX * multiplier, ypos + (float) parent.getBall().speedY * multiplier, paint);
    }

    public static class Obstacle {
        private static final double COS_PI_4 = Math.cos(Math.PI / 4);
        private static final double SIN_PI_4 = Math.sin(Math.PI / 4);

        private final int x;
        private final int y;
        private final int width;
        private final int height;

        public Obstacle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public boolean checkCollision(double ballX, double ballY, int ballRadius) {
            return isPointIn(ballX, ballY - ballRadius) ||                                            //   |
                    isPointIn(ballX + ballRadius * COS_PI_4, ballY - ballRadius * SIN_PI_4) ||           //    /
                    isPointIn(ballX + ballRadius, ballY) ||                                           //     -
                    isPointIn(ballX + ballRadius * COS_PI_4, ballY + SIN_PI_4) ||                      //    \
                    isPointIn(ballX, ballY + ballRadius) ||                                           //   |
                    isPointIn(ballX - ballRadius * COS_PI_4, ballY + ballRadius * SIN_PI_4) ||           //  /
                    isPointIn(ballX - ballRadius, ballY) ||                                           // -
                    isPointIn(ballX - ballRadius * COS_PI_4, ballY - ballRadius * SIN_PI_4);             //  \
        }

        private boolean isPointIn(double x, double y) {
            return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
        }
    }

    public static class ClippingPoint {
        private static final int radius = 50;
        private int x;
        private int y;

        public ClippingPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void setCoords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean checkCollision(double ballX, double ballY) {
            return x - radius <= ballX && ballX <= x + radius && y - radius <= ballY && ballY <= y + radius;
        }
    }
}
