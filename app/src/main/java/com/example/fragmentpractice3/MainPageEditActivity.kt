package com.example.fragmentpractice3

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main_page_edit.*
import java.text.DateFormat
import java.util.Calendar

class MainPageEditActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page_edit)
        //getTimeData()

        // 알람 설정
        okButton.setOnClickListener{
            var timePicker = TimePickerFragment()
            
            //시계호출
            timePicker.show(supportFragmentManager, "Time Picker")
        }

        // 알람 취소 버튼
        alarm_cancle_Button.setOnClickListener {
            // 알람 취소 함수
            cancleAlarm()
        }

    }



//    fun getTimeData(){
//        timePicker.setOnTimeChangedListener(){ timePicker: TimePicker, hour: Int, minute: Int ->
//            var hour = hour
//            var minute = minute
//
//            if(hour > 12){
//                hour -= 12
//                tvText.setText("오후 ${hour}시 ${minute}분")
//            }else tvText.setText("${hour}시 ${minute}분")
//
//            // 버튼 클릭시 데이터를 저장 후 들고 메인화면으로 간다.
//            okButton.setOnClickListener {
//                with(getSharedPreferences(USER_TIMEINFO,Context.MODE_PRIVATE).edit()){
//                    putString(HOUR,hour.toString())
//                    putString(MIN,minute.toString())
//                    apply()
//                }
//                Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
//                finish()
//            }
//        }
//    }

    // 시간 정하면 호출되는 함수
    override fun onTimeSet(timePicker: TimePicker?, hourOfDay: Int, min: Int) {
        var c = Calendar.getInstance()

        //시간설정
        c.set(Calendar.HOUR_OF_DAY, hourOfDay) //시
        c.set(Calendar.MINUTE, min) // 분
        c.set(Calendar.SECOND,0)    //초

        //화면에 시간 지정
        updateTimeText(c)

        // 알람설정
        startAlarm(c)
    }

    private fun updateTimeText(c: Calendar) {
        var curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)

        timeText.append("알람 시간: ")
        timeText.append(curTime)
    }

    private fun startAlarm(c: Calendar) {
        // 알람매니저 선언
        var alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(this, AlertReceiver::class.java)

        //사용자가 선택한 알람 시간 데이터 담기
        var curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)
        intent.putExtra("time", curTime)


        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent,PendingIntent.FLAG_MUTABLE)

        //설정 시간이 현재시간 이후라면 설정
        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1)
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.timeInMillis, pendingIntent)
    }

    private fun cancleAlarm(){
        // 알람매니저 선언
        var alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        var intent = Intent(this, AlertReceiver::class.java)
        var pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_MUTABLE)

        alarmManager.cancel(pendingIntent)
        timeText.text = "알람 취소"
    }


}