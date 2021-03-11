package com.geofferyj.jsignin.views.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import com.geofferyj.jsignin.R
import com.geofferyj.jsignin.viewmodels.CameraViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: CameraViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(CameraViewModel::class.java)
        WindowCompat.setDecorFitsSystemWindows(window, false)
    }


}