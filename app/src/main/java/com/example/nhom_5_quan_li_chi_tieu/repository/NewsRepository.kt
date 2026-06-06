package com.example.nhom_5_quan_li_chi_tieu.repository

import com.example.nhom_5_quan_li_chi_tieu.network.MangTinTuc
import com.example.nhom_5_quan_li_chi_tieu.network.PhanHoiTinTuc
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NewsRepository {
    // Hàm này lấy tin tức từ mạng và bơm dữ liệu vào ống nước (Flow)
    fun layTinTucTuMang(): Flow<PhanHoiTinTuc> = flow {
        // Cố gắng "gọi điện" cho Server thông qua Retrofit
        val duLieu = MangTinTuc.dichVu.layTinTucTaiChinh()

        // Xin được dữ liệu thành công thì đẩy ra ống nước
        emit(duLieu)
    }
}
