package com.csci448.kate_lundberg.citycircle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.csci448.kate_lundberg.citycircle.ui.navigation.CityCircleNavGraph

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CityCircleNavGraph()
        }
    }
}
