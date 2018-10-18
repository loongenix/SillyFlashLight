package com.jeremy.sillyflashlight

import android.content.Context
import android.content.Intent
import android.hardware.*


object LightSensorManager {

    val ACTION = "com.jeremy.light.receiver"
    private var mSensorManager: SensorManager? = null
    private var sensorListener: LightSensorListener? = null
    var m_Camera: Camera? = null
    fun start(context: Context) {
        mSensorManager = context.applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        var lightSensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorListener = LightSensorListener(context);
        mSensorManager!!.registerListener(sensorListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)

    }

    fun stop() {
        if (mSensorManager == null) {
            return
        }
        mSensorManager!!.unregisterListener(sensorListener);
    }


    fun openFlashLight() {
        if(m_Camera!=null){
            return;
        }
        m_Camera = Camera.open()
        val mParameters: Camera.Parameters
        mParameters = m_Camera!!.getParameters()
        mParameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
        m_Camera!!.setParameters(mParameters)
    }

    fun closeFlashLight() {

        if(m_Camera==null){
            return
        }
        val mParameters: Camera.Parameters
        mParameters = m_Camera!!.getParameters()
        mParameters.flashMode = Camera.Parameters.FLASH_MODE_OFF
        m_Camera!!.setParameters(mParameters)
        m_Camera!!.release()
        m_Camera=null
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