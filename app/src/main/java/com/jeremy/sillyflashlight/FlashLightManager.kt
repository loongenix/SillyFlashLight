package com.jeremy.sillyflashlight

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.os.Build




object FlashLightManager {

    val ACTION = "com.jeremy.light.receiver"
    private var mSensorManager: SensorManager? = null
    private var sensorListener: LightSensorListener? = null
    private var mCameraManager: CameraManager? = null
    private var mCameraId:String?=null
    private var lightIsOn=false

    fun start(context: Context) {
        mSensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var lightSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorListener = LightSensorListener(context)
        mSensorManager!!.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        mCameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        mCameraId=mCameraManager!!.cameraIdList[0]
    }

    fun stop() {
        turnOffLight()
        if (mSensorManager == null) {
            return
        }
        mSensorManager!!.unregisterListener(sensorListener);
        mSensorManager=null
        sensorListener = null
        mCameraManager=null
        mCameraId=null
    }

    fun turnOnLight() {
        if(lightIsOn){
            return
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager!!.setTorchMode(mCameraId, true)
                lightIsOn=true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    fun turnOffLight() {
        if(!lightIsOn){
            return
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                mCameraManager!!.setTorchMode(mCameraId, false)
                lightIsOn=false
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    class LightSensorListener : SensorEventListener {
        private var context: Context? = null;
        var lux = 0f

        constructor(context: Context?) {
            this.context = context
        }


        override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

        }

        override fun onSensorChanged(p0: SensorEvent?) {
            if (p0?.sensor!!.type == Sensor.TYPE_LIGHT) {
                lux = p0?.values[0]
                var i = Intent()
                i.putExtra("lux", lux)
                i.action = ACTION
                context!!.sendBroadcast(i)
            }
        }
    }
}