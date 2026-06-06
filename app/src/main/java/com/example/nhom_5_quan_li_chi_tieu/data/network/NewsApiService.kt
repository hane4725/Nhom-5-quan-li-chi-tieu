package com.example.nhom_5_quan_li_chi_tieu.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val DUONG_DAN_GOC = "https://newsapi.org/v2/"
private const val CHIA_KHOA_API = "4278473a1bb94ce088eec035d4308576" // Chìa khóa của bạn

// 1. Giao diện chứa các đường dẫn API
interface DanhBaApiService {
    // Gọi API lấy tin tức kinh doanh (business) mới nhất
//    @GET("top-headlines")
//    suspend fun layTinTucTaiChinh(
//        @Query("country") quocGia: String = "us", // Lấy tin quốc tế (Mỹ)
//        @Query("category") theLoai: String = "business",
//        @Query("apiKey") chiaKhoa: String = CHIA_KHOA_API
//    ): PhanHoiTinTuc // Gọi cái Khuôn ở bên file kia
    @GET("everything") // Đổi từ top-headlines thành everything
    suspend fun layTinTucTaiChinh(
        @Query("q") tuKhoa: String = "tài chính", // Tìm chữ "tài chính"
        @Query("language") ngonNgu: String = "vi", // Bắt buộc tiếng Việt
        @Query("apiKey") chiaKhoa: String = CHIA_KHOA_API
    ): PhanHoiTinTuc

}

// 2. Nối máy: Thiết lập Retrofit để nó sẵn sàng làm việc
object MangTinTuc {
    val dichVu: DanhBaApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) // Chỉ định dùng Gson dịch JSON
            .baseUrl(DUONG_DAN_GOC)
            .build()
            .create(DanhBaApiService::class.java)
    }
}