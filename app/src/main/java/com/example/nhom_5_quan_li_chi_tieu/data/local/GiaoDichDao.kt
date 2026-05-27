package com.example.nhom_5_quan_li_chi_tieu.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GiaoDichDao {
    @Insert
    suspend fun themGiaoDich(giaoDich: GiaoDich)

    @Update
    suspend fun suaGiaoDich(giaoDich: GiaoDich)

    @Delete
    suspend fun xoaGiaoDich(giaoDich: GiaoDich)

    @Query("SELECT * FROM bang_giao_dich ORDER BY ngayGiaoDich DESC")
    fun layLichSuGiaoDich(): Flow<List<GiaoDich>>

    // Lệnh tính tổng chi theo danh mục (Cảnh báo vượt ngân sách)
    @Query("""
        SELECT dm.tenDanhMuc, SUM(gd.soTien) as tongTien 
        FROM bang_giao_dich gd 
        INNER JOIN bang_danh_muc dm ON gd.idDanhMuc = dm.id 
        WHERE dm.loai = -1 AND gd.ngayGiaoDich BETWEEN :ngayBatDau AND :ngayKetThuc
        GROUP BY gd.idDanhMuc
    """)
    fun layThongKeChiTheoThang(ngayBatDau: Long, ngayKetThuc: Long): Flow<List<BaoCaoDanhMuc>>
}

data class BaoCaoDanhMuc(
    val tenDanhMuc: String,
    val tongTien: Double
)
