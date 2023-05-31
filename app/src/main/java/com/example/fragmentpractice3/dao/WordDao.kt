package com.example.fragmentpractice3.dao

import androidx.room.*
import com.example.fragmentpractice3.datas.ReData

// DataAccessObject DB와 관련된 Query문 작성을 위한 클래스
@Dao
interface WordDao {
    @Query("SELECT * from hello ORDER BY id DESC")
    fun getAll():List<ReData>

    @Query("SELECT * from hello ORDER BY id DESC LIMIT 1")
    fun getLateStWord(): ReData

    @Insert
    fun insert(word:ReData)

    @Delete
    fun delete(word: ReData)

    @Update
    fun update(word: ReData)
}