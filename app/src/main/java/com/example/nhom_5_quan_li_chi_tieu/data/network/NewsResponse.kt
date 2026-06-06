package com.example.nhom_5_quan_li_chi_tieu.network

import com.google.gson.annotations.SerializedName

// Khuôn chứa toàn bộ phản hồi từ Server
data class PhanHoiTinTuc(
    @SerializedName("status") val trangThai: String,
    @SerializedName("articles") val danhSachBaiBao: List<BaiBao>
)

// Khuôn chứa chi tiết 1 bài báo
data class BaiBao(
    @SerializedName("title") val tieuDe: String,
    @SerializedName("description") val moTa: String?,
    @SerializedName("urlToImage") val linkAnh: String?,
    @SerializedName("url") val linkBaiViet: String
)
