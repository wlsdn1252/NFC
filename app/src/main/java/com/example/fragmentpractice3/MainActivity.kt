package com.example.fragmentpractice3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fragmentpractice3.adapters.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = MainAdapter(supportFragmentManager)

        mainViewPager.adapter = mAdapter

    }
}