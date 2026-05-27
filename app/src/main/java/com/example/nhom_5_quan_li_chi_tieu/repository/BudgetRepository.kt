package com.example.nhom_5_quan_li_chi_tieu.repository

import com.example.nhom_5_quan_li_chi_tieu.data.local.DanhMuc
import com.example.nhom_5_quan_li_chi_tieu.data.local.DanhMucDao
import com.example.nhom_5_quan_li_chi_tieu.data.local.GiaoDich
import com.example.nhom_5_quan_li_chi_tieu.data.local.GiaoDichDao
import kotlinx.coroutines.flow.Flow

class BudgetRepository(
    private val giaoDichDao: GiaoDichDao,
    private val danhMucDao: DanhMucDao
) {
    // ---- PHẦN DỮ LIỆU LOCAL (OFFLINE) ----

    // Giao Dịch
    fun layLichSuGiaoDich(): Flow<List<GiaoDich>> = giaoDichDao.layLichSuGiaoDich()

    suspend fun themGiaoDich(giaoDich: GiaoDich) {
        giaoDichDao.themGiaoDich(giaoDich)
    }

    suspend fun suaGiaoDich(giaoDich: GiaoDich) {
        giaoDichDao.suaGiaoDich(giaoDich)
    }

    suspend fun xoaGiaoDich(giaoDich: GiaoDich) {
        giaoDichDao.xoaGiaoDich(giaoDich)
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

    // ---- PHẦN DỮ LIỆU NETWORK (ONLINE) SẼ THÊM SAU ----
    // Ví dụ: suspend fun layTyGiaNgoaiTe() = apiService.getTyGia()
}
