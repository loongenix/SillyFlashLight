package com.jeremy.sillyflashlight

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.jeremy.sillyflashlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var receiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        FlashLightManager.start(this)
        var intentFilter = IntentFilter()
        intentFilter.addAction(FlashLightManager.ACTION)
        receiver = LightSensorReciver()
        registerReceiver(receiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        FlashLightManager.stop()
    }

    class LightSensorReciver : BroadcastReceiver() {
        private var luxThreshold = 0
        override fun onReceive(p0: Context?, p1: Intent?) {
            var lux = p1!!.getFloatExtra("lux", 0f)
            Log.e("fl", "lux $lux")
            if (lux > 2000) {
                FlashLightManager.turnOnLight()
            } else {
                FlashLightManager.turnOffLight()
            }
        }
    }
}
