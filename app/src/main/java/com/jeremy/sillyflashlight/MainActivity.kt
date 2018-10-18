package com.jeremy.sillyflashlight

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

class MainActivity : AppCompatActivity() {
    private var receiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LightSensorManager.start(this)
        var intentFilter = IntentFilter()
        intentFilter.addAction(LightSensorManager.ACTION)
        receiver = LightSensorReciver()
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        LightSensorManager.stop()
    }

    class LightSensorReciver : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            var lux = p1!!.getFloatExtra("lux",0f)

            Log.e("fl", "lux $lux")
            if (lux > 10) {
                LightSensorManager.openFlashLight()
            }else{
                LightSensorManager.closeFlashLight()
            }
        }
    }
}
