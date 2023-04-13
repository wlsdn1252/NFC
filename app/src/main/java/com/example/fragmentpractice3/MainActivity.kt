package com.example.fragmentpractice3


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.fragmentpractice3.adapters.MainAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_fist.*

class MainActivity : AppCompatActivity() {

    lateinit var mAdapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mAdapter = MainAdapter(supportFragmentManager)
        mainViewPager.adapter = mAdapter
        mainTabLayout.setupWithViewPager(mainViewPager)
        //getData()

    }

    fun getData(){
        with(getSharedPreferences(USER_TIMEINFO, Context.MODE_PRIVATE)){
            mediText.text = getString(HOUR, "아직 데이터가 없음")
            mediTimeText.text = getString(MIN, "아직 데이터가 없음")
        }
    }



}