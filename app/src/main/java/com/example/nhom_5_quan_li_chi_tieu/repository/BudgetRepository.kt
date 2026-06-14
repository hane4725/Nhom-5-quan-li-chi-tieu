package com.example.nhom_5_quan_li_chi_tieu.repository

import com.example.nhom_5_quan_li_chi_tieu.data.local.DanhMuc
import com.example.nhom_5_quan_li_chi_tieu.data.local.DanhMucDao
import com.example.nhom_5_quan_li_chi_tieu.data.local.GiaoDich
import com.example.nhom_5_quan_li_chi_tieu.data.local.GiaoDichDao
import kotlinx.coroutines.flow.Flow
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BudgetRepository(
    private val giaoDichDao: GiaoDichDao,
    private val danhMucDao: DanhMucDao
) {
    // Khai báo kết nối với Firebase Firestore
    private val dbCloud = Firebase.firestore

    // ---- PHẦN DỮ LIỆU LOCAL (OFFLINE) ----

    // Giao Dịch
    fun layLichSuGiaoDich(): Flow<List<GiaoDich>> = giaoDichDao.layLichSuGiaoDich()

    fun layLichSuGiaoDichTheoThang(ngayBatDau: Long, ngayKetThuc: Long): Flow<List<GiaoDich>> =
        giaoDichDao.layLichSuGiaoDichTheoThang(ngayBatDau, ngayKetThuc)
    suspend fun themGiaoDich(giaoDich: GiaoDich) {
        giaoDichDao.themGiaoDich(giaoDich)
    }

    suspend fun suaGiaoDich(giaoDich: GiaoDich) {
        giaoDichDao.suaGiaoDich(giaoDich)
    }

    suspend fun xoaGiaoDich(giaoDich: GiaoDich) {
        giaoDichDao.xoaGiaoDich(giaoDich)
    }

    suspend fun xoaGiaoDichTheoDanhMuc(idDanhMuc: Int) {
        giaoDichDao.xoaGiaoDichTheoDanhMuc(idDanhMuc)
    }




    // Danh Mục
    fun layTatCaDanhMuc(): Flow<List<DanhMuc>> = danhMucDao.layTatCaDanhMuc()

    suspend fun themDanhMuc(danhMuc: DanhMuc) {
        danhMucDao.themDanhMuc(danhMuc)
    }

    suspend fun suaDanhMuc(danhMuc: DanhMuc) {
        danhMucDao.suaDanhMuc(danhMuc)
    }

    suspend fun xoaDanhMuc(danhMuc: DanhMuc) {
        danhMucDao.xoaDanhMuc(danhMuc)
    }

    // ---- PHẦN XUẤT NHẬP DỮ LIỆU (JSON) ----
    suspend fun layTatCaGiaoDichNhanh(): List<GiaoDich> = giaoDichDao.layTatCaGiaoDichNhanh()
    suspend fun layTatCaDanhMucNhanh(): List<DanhMuc> = danhMucDao.layTatCaDanhMucNhanh()
    
    suspend fun xoaToanBoGiaoDich() = giaoDichDao.xoaToanBoGiaoDich()
    suspend fun xoaToanBoDanhMuc() = danhMucDao.xoaToanBoDanhMuc()
    
    suspend fun themNhieuGiaoDich(danhSach: List<GiaoDich>) = giaoDichDao.themNhieuGiaoDich(danhSach)
    suspend fun themNhieuDanhMuc(danhSach: List<DanhMuc>) = danhMucDao.themNhieuDanhMuc(danhSach)

    // ---- PHẦN DỮ LIỆU NETWORK (ONLINE) SẼ THÊM SAU ----
    // Ví dụ: suspend fun layTyGiaNgoaiTe() = apiService.getTyGia()
}
