package com.siboren.android.foodofchina;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class MyOrientationListener implements SensorEventListener {
    private Context context;
    private SensorManager sensorManager;
    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    private float lastX;

    private OnOrientationListener onOrientationListener ;

    public MyOrientationListener(Context context)
    {
        this.context = context;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 接受方向感应器的类型
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            accelerometerValues = event.values;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            magneticFieldValues = event.values;
        }
        getOrientation();
    }

    public void getOrientation() {
        float[] values = new float[3];
        float[] R = new float[9];
        SensorManager.getRotationMatrix(R, null, accelerometerValues, magneticFieldValues);
        //将角度信息计算后返回到values中
        SensorManager.getOrientation(R, values);
        values[0] = (float) Math.toDegrees(values[0]);
        //当方向的改变大于一度时回调监听
        if( Math.abs(values[0]- lastX) > 1.0 )
        {
            onOrientationListener.onOrientationChanged(values[0]);
        }
        lastX = values[0] ;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void start(){
        // 获得传感器管理器
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null)
        {
            // 初始化加速度传感器
            accelerometer = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            // 初始化地磁场传感器
            magnetic = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        }
        // 注册
        if (accelerometer != null || magnetic!=null)
        {
            sensorManager.registerListener(this,
                    accelerometer, Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, magnetic,
                    Sensor.TYPE_MAGNETIC_FIELD);
        }
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    public void setOnOrientationListener(OnOrientationListener onOrientationListener) {
        this.onOrientationListener = onOrientationListener;
    }

    public interface OnOrientationListener{
        void onOrientationChanged(float x);
    }
}
