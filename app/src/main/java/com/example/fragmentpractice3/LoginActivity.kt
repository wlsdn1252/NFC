package com.example.fragmentpractice3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.fragmentpractice3.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    // 카카오계정으로 로그인 공통 callback 구성
// 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("로그인? : ", "카카오계정으로 로그인 실패", error)
        } else if (token != null) {
            Log.i("로그인? : ", "카카오계정으로 로그인 성공 ${token.accessToken}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.sinUp.setOnClickListener {
            startActivity(Intent(this, SingInActivity::class.java))
        }

        // Kakao SDK 초기화
        KakaoSdk.init(this, "a680ab6aa00bc1775035020ab328f996")

        // 카카오톡 로그인 버튼 클릭 시
        binding.loginPageKakaoButton.setOnClickListener {
            // 로그인 조합 예제
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e("로그인? : ", "카카오톡으로 로그인 실패", error)

                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i("로그인? : ", "카카오톡으로 로그인 성공 ${token.accessToken}")
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }

        }


        //회원가입 액티비티에서 보낸 이메일과 비밀번호를 받는다.
        var email = intent.getStringExtra("email") ?: return
        var password = intent.getStringExtra("password") ?: return

        // 로그인 번튼 클릭 시
        binding.loginPageLoginButton.setOnClickListener {
            Firebase.auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {  task ->
                    if(task.isSuccessful){
                        //로그인 성공시
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()

                    }else{
                        Log.e("로그인액티비티",task.exception.toString())
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        // 로그인 하지 않고 메인으로 가기
        binding.mainText2.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }



    }

    private fun kakaoLogin(){

    }


    }
