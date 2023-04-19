package com.example.fragmentpractice3

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FragmentActivity : AppCompatActivity(){
    private var isDelete = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var delete: TextView = findViewById(R.id.mainPageDelete)

        delete.setOnClickListener {
            isDelete = !isDelete
            Toast.makeText(this, "제거 : $isDelete", Toast.LENGTH_SHORT).show()
        }
    }

    fun getisDelete(): Boolean{
        return isDelete
    }
}