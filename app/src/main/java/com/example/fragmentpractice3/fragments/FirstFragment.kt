package com.example.fragmentpractice3.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fragmentpractice3.LoginActivity
import com.example.fragmentpractice3.MyInfoActivity
import com.example.fragmentpractice3.R
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

}