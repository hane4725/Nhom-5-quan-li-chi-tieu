package com.example.nhom_5_quan_li_chi_tieu.data
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "bang_giao_dich")
data class GiaoDich(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val soTien: Double,
    val ghiChu: String,
    val ngayGiaoDich: Long,
    val idDanhMuc: Int
)
