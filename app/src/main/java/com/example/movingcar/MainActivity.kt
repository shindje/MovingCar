package com.example.movingcar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider

private lateinit var viewModel: MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val factory = MainViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(MainViewModel::class.java)
    }

    override fun onPause() {
        super.onPause()
        viewModel.setModel(findViewById<CarView>(R.id.car_view).getModel())
    }

    override fun onResume() {
        super.onResume()
        viewModel.getModel()?.apply {
            findViewById<CarView>(R.id.car_view).setModel(this)
        }
    }
}