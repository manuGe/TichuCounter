package com.manuel.tichucounter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import com.manuel.AppInterface
import com.manuel.tichucounter.model.TichuAppModel
import com.manuel.tichucounter.ui.AppUI

object TichuApp : AppInterface {
    lateinit var model: TichuAppModel

    override fun initialize(activity: AppCompatActivity, savedInstanceState: Bundle?) {
        model = TichuAppModel(activity)
    }

    @Composable
    override fun CreateAppUI() {
//        model.darkMode = isSystemInDarkTheme()
        AppUI(model)
    }
}
