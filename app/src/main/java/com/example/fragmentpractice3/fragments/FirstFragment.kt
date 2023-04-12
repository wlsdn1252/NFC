package com.example.fragmentpractice3.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.fragmentpractice3.*
import kotlinx.android.synthetic.main.fragment_fist.*

class FirstFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_fist,container,false)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        clickGoToMyInfo()
        mainPageEdit()



    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    // 유저프로필 클릭시 내 정보 페이지로 이동
   fun clickGoToMyInfo(){
        mainPageUserInfo.setOnClickListener {
            // 다른 화면으로 이동하기
            // Intent(출발지, 도착지)
            val myIntent = Intent(requireContext(), MyInfoActivity::class.java)
            startActivity(myIntent) // 출발지, 도착지 정보가담긴 myIntent를 넣어준다.
        }
    }

    fun mainPageEdit(){
        mainPageEdit.setOnClickListener {
            // 다른 화면으로 이동하기
            // Intent(출발지, 도착지)
            val myIntent = Intent(requireContext(), MainPageEditActivity::class.java)
            startActivity(myIntent) // 출발지, 도착지 정보가담긴 myIntent를 넣어준다.
        }
    }

    // 알람 편집 후 알람 데이터 들고오가
    fun getData(){
        with(requireContext().getSharedPreferences(USER_TIMEINFO, Context.MODE_PRIVATE)){

            var h = getString(com.example.fragmentpractice3.HOUR, "아직 데이터가 없음")
            var m = getString(com.example.fragmentpractice3.MIN, "노데이터")

            mediTimeText.setText("${h}:${m}")
            if(mediTimeText.text.isNotEmpty()) mediTimeText2.setText("${h}:${m}")



//            // TextView에 값이 있으면 부모 뷰그룹에 추가한다
//            if (mediTimeText.text.isNotEmpty()) {
//                var i = 1
//
//                // 부모 뷰그룹을 가져온다
//                val parentLayout: ViewGroup = mainPageMediLayout
//
//                // TextView를 동적으로 생성한다
//                var textView = TextView(requireContext())
//                textView.setText("${h}:${m}")
//
//                parentLayout.addView(textView)
//            }
        }
    }
}