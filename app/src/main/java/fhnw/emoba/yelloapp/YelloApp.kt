package fhnw.emoba.yelloapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import fhnw.emoba.EmobaApp
import fhnw.emoba.yelloapp.model.YelloAppModel
import fhnw.emoba.yelloapp.ui.AppUI


object YelloApp : EmobaApp {
    lateinit var model: YelloAppModel

    override fun initialize(activity: AppCompatActivity, savedInstanceState: Bundle?) {
        model = YelloAppModel(activity)
    }

    @Composable
    override fun CreateAppUI() {
        model.enableDarkMode = isSystemInDarkTheme()
        AppUI(model)
    }

}

