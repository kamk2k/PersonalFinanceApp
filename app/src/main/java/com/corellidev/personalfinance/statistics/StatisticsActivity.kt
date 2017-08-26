package com.corellidev.personalfinance.statistics

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.corellidev.personalfinance.R
import kotlinx.android.synthetic.main.activity_statistics.*

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener({finish()})
    }

}
