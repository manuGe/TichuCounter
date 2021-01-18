package com.manuel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.setContent
import com.manuel.tichucounter.TichuApp


class MainActivity : AppCompatActivity() {
    private lateinit var app: AppInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = TichuApp
        app.initialize(activity = this, savedInstanceState = savedInstanceState)

        setContent {
            app.CreateAppUI()
        }
    }

    override fun onStop() {
        super.onStop()
        app.onStop(activity = this)
    }
}

