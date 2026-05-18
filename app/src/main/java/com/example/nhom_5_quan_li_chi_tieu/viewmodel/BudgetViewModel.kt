package com.example.nhom_5_quan_li_chi_tieu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nhom_5_quan_li_chi_tieu.data.GiaoDich
import com.example.nhom_5_quan_li_chi_tieu.data.GiaoDichDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BudgetViewModel(private val giaoDichDao: GiaoDichDao) : ViewModel() {

    // 1. Dòng chảy dữ liệu (StateFlow) chứa danh sách chi tiêu để giao diện UI hứng lấy và vẽ ra
    val danhSachGiaoDich: StateFlow<List<GiaoDich>> = giaoDichDao.layLichSuGiaoDich()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // Ban đầu chưa có dữ liệu thì trả về danh sách rỗng
        )

    // 2. Hàm xử lý logic khi người dùng nhấn nút "Lưu" trên giao diện
    fun luuGiaoDich(soTien: Double, ghiChu: String, idDanhMuc: Int) {
        // Chạy ngầm bằng Coroutine (viewModelScope) để không gây đơ giao diện của điện thoại
        viewModelScope.launch {
            val giaoDichMoi = GiaoDich(
                soTien = soTien,
                ghiChu = ghiChu,
                ngayGiaoDich = System.currentTimeMillis(), // Lấy thời gian hiện tại của máy
                idDanhMuc = idDanhMuc
            )
            giaoDichDao.themGiaoDich(giaoDichMoi)
        }
    }
}