package com.example.fragmentpractice3

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TimePicker
import android.widget.Toast
import com.example.fragmentpractice3.database.AppDatabase
import com.example.fragmentpractice3.databinding.ActivityMainPageEditBinding
import com.example.fragmentpractice3.datas.ReData
import java.text.DateFormat
import java.util.*

class MainPageEditActivity : AppCompatActivity(), TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityMainPageEditBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //getTimeData()

        // 알람 설정
        binding.editAddTmage.setOnClickListener{
            var timePicker = TimePickerFragment()
            
            //시계호출
            timePicker.show(supportFragmentManager, "Time Picker")
        }

        // 알람 취소 버튼
        binding.alarmCancleButton.setOnClickListener {
            // 알람 취소 함수
            cancleAlarm()
        }

        // 저장
        binding.addButton2.setOnClickListener {
            add()
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

    // 시간설정하면 해당 시간을 화면에 뿌린다.
    private fun updateTimeText(c: Calendar) {
        var curTime = DateFormat.getTimeInstance(DateFormat.SHORT).format(c.time)
        binding.timeText.append(curTime)
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
        binding.timeText.text = "알람 취소"
    }

    // 추가버튼 눌렀을 때
    private fun add(){
        // 액티비티에 적혀있는 값들을 들고온다.
        val text = binding.editTitleText.text.toString()
        val time = binding.timeText.text.toString()
        val word = ReData(text,time)


        // 메인 UI쓰레드랑 겹치면 안된다. 쓰레드를 따로 생성
        Thread{
            AppDatabase.getInstance(this)?.wordDao()?.insert(word)
            // 이건 뭐죠??
            runOnUiThread{
                Toast.makeText(this, "저장 완료", Toast.LENGTH_SHORT).show()
            }
            // 낭비최소화를 위한구문
            // 데이터입력 후 저장버튼을 누르면
            val intent = Intent().putExtra("isUpdate",true)
            setResult(RESULT_OK,intent)
            finish()
        }.start()



    }


}