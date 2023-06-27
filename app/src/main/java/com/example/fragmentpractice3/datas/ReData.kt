package com.example.fragmentpractice3.datas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hello")
data class ReData(
    val mediName : String,
    val textVal : String,
    val NFCId : String,
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
)