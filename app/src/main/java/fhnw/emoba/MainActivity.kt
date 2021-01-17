package fhnw.emoba

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.setContent
import androidx.lifecycle.Observer
import fhnw.emoba.yelloapp.YelloApp


class MainActivity : AppCompatActivity() {
    private lateinit var app: EmobaApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = YelloApp
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

