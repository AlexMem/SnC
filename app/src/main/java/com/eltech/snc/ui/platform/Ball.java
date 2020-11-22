package com.eltech.snc.ui.platform;

class Ball {
    private final float mass = 0.5f;
    // TODO use renderscript for vector coordinates?
    private double slopeForceX;
    private double slopeForceY;
    public double accelX;
    public double accelY;
    public double speedX;
    public double speedY;
    // position is limited to [0,width], [0,height] properties
    private int width;
    private int height;
    double posX;
    double posY;
    private BallView ballView;

    void calculateForce(float angleX, float angleY) {
        final float g = 12.5f; // gravity
        // slope: m*g*sin(slopeAngle), rolling: 2/7* m*g*sin(slopeAngle), given that torque for sphere (Theta) is 2/5*m*r^2
        //Log.i("Ball","angle:" + angleX + ":" + angleY);
//        slopeForceX = mass * g * Math.sin(angleY) * 5/ 7;
//        slopeForceY = mass * g * Math.sin(angleX) * 5/ 7;
        accelX = mass * g * Math.sin(angleY);// - slopeForceX;
        accelY = mass * g * Math.sin(angleX);// - slopeForceY;
    }

    void calculateAcceleration() {
        // acceleration
//        accelX = slopeForceX/mass;
//        accelY = slopeForceY/mass;
    }

    /**
     *
     * @param executionRate in sec
     */
    void updateVelocity(float executionRate) {
        if (ballView == null) {
            return;
        }
//
//        for (BallView.Obstacle obstacle : ballView.getObstacles()) {
//            if (obstacle.checkCollision(posX, posY, ballView.getRadius())) {
//                speedX = 0;
//                speedY = 0;
//                return;
//            }
//        }

        speedX += accelX * executionRate;
        speedY += accelY * executionRate;
//        Log.i("Ball", "speed:" + speedX + ":" + speedY);
    }

    void setArenaSize(int w, int h) {
        width = w;
        height = h;
    }

    void resetPosition() {
        posX = width/2;
        posY = height/2;
        speedX = 0;
        speedY = 0;
        accelX = 0;
        accelY = 0;
    }

    /**
     *
     * @param executionRate in sec
     */
    void updatePosition(float executionRate) {
        if (ballView.getClippingPoint().checkCollision(posX, posY)) {
            ballView.getPFParent().clippingPointTouched();
            ballView.spawnClippingPoint();
        }

        final int pixelsToMove = 150; // how many pixels to move by force
        if( posX < 0 ) {
            posX = 0;
            speedX = 0;
        }
        if( posX > width ) {
            posX = width;
            speedX = 0;
        }
        if( posY < 0 ) {
            posY = 0;
            speedY = 0;
        }
        if( posY > height ) {
            posY = height;
            speedY = 0;
        }

        posX += pixelsToMove * speedX * executionRate;
        posY += pixelsToMove * speedY * executionRate;
    }

    public void setBallView(final BallView ballView) {
        this.ballView = ballView;
    }
}
