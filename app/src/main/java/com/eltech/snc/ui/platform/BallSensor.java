package com.eltech.snc.ui.platform;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class BallSensor implements SensorEventListener {
    private PlatformFragment platformFragment;
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private final float[] mAccelerometerReading = new float[3];
    private final float[] mMagnetometerReading = new float[3];
    float rotationMatrix[] = new float[9];
    float orientation[] = new float[3];

    public BallSensor(PlatformFragment platformFragment) {
        this.platformFragment = platformFragment;
        initSensorManager();
    }

    private void initSensorManager() {
        sensorManager = (SensorManager) platformFragment.getActivity().getSystemService(Context.SENSOR_SERVICE);
    }

    void registerListener() {
        getAppropriateSensors();
        if ( hasAppropriateSensor() ) {
            for (Sensor sensor : sensorList) {
                sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
            }
        }
    }

    boolean hasAppropriateSensor() {
        return sensorList != null && !sensorList.isEmpty();
    }

    void unregisterListener() {
        if(sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    private void getAppropriateSensors() {
        if( sensorManager != null ) {
            getRotationSensor();
            //List<Sensor> list = sensorManager.getSensorList(Sensor.TYPE_GAME_ROTATION_VECTOR); API level 18
            //https://developer.android.com/guide/topics/sensors/sensors_position.html#sensors-pos-geomrot API level 19
            if( hasAppropriateSensor() ) {
                Log.d("Ball", "Rotation sensor is available");
            } else {
                Log.d("Ball", "Rotation sensor is NOT available");
            }
            getAcceleroMeterAndMagneticFieldSensorIfNeeded();
        }
    }

    private void getRotationSensor() {
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ROTATION_VECTOR);
    }

    /**
     * If there is no rotation vector sensor then the combination of accelerometer and magnetic field sensor substitutes it
     * https://developer.android.com/guide/topics/sensors/sensors_position.html#sensors-pos-orient
     */
    private void getAcceleroMeterAndMagneticFieldSensorIfNeeded() {
        if(!hasAppropriateSensor()) { // no rotation vector
            List<Sensor> acceleroMeterSensorList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
            List<Sensor> magneticFieldSensorList = sensorManager.getSensorList(Sensor.TYPE_MAGNETIC_FIELD);
            if(acceleroMeterSensorList != null && !acceleroMeterSensorList.isEmpty() &&
                    magneticFieldSensorList != null && !magneticFieldSensorList.isEmpty()) {
                Log.d("Ball", "Accelerometer and magnetic field is available");
                sensorList = new ArrayList<>();
                sensorList.addAll(acceleroMeterSensorList);
                sensorList.addAll(magneticFieldSensorList);
            } else {
                Log.d("Ball", "Accelerometer and magnetic field is NOT available");
            }
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
            SensorManager.getOrientation(rotationMatrix, orientation);
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(sensorEvent.values, 0, mAccelerometerReading,
                    0, mAccelerometerReading.length);
            updateOrientationAngles();
        } else if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(sensorEvent.values, 0, mMagnetometerReading,
                    0, mMagnetometerReading.length);
            updateOrientationAngles();
        }
        platformFragment.setOrientationText(orientation);
    }

    private void updateOrientationAngles() {
        sensorManager.getRotationMatrix(rotationMatrix, null, mAccelerometerReading, mMagnetometerReading);
        sensorManager.getOrientation(rotationMatrix, orientation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
