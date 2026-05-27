package com.example.nhom_5_quan_li_chi_tieu.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DanhMucDao {
    @Insert
    suspend fun themDanhMuc(danhMuc: DanhMuc)

    @Update
    suspend fun suaDanhMuc(danhMuc: DanhMuc)

    @Delete
    suspend fun xoaDanhMuc(danhMuc: DanhMuc)

    @Query("SELECT * FROM bang_danh_muc")
    fun layTatCaDanhMuc(): Flow<List<DanhMuc>>
}
