package com.example.fragmentpractice3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import com.example.fragmentpractice3.fragments.FirstFragment
import kotlinx.android.synthetic.main.activity_main_page_edit.*

class MainPageEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page_edit)

        timePicker.setOnTimeChangedListener(){ timePicker: TimePicker, hour: Int, minute: Int ->
            var hour = hour
            var minute = minute

            if(hour > 12){
                hour -= 12
                tvText.setText("오후 ${hour}시 ${minute}분")
            }else tvText.setText("${hour}시 ${minute}분")

            // 버튼 클릭시 데이터를 저장 후 들고 메인화면으로 간다.
            okButton.setOnClickListener {
                with(getSharedPreferences(USER_TIMEINFO,Context.MODE_PRIVATE).edit()){
                    putString(HOUR,hour.toString())
                    putString(MIN,minute.toString())
                    apply()
                }
                Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}