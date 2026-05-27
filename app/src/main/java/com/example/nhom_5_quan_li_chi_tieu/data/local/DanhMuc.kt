package com.example.nhom_5_quan_li_chi_tieu.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bang_danh_muc")
data class DanhMuc(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val tenDanhMuc: String, // Ví dụ: Ăn uống, Học tập, Lương...

    val bieuTuong: String, // Ví dụ: "🍔", "🚗", "🛍️"...

    val loai: Int, // Phân loại: 1 là Thu, -1 là Chi

    val nganSachToiDa: Double // Dùng để cảnh báo khi tiêu lố tiền
)
