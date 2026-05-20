package com.example.nhom_5_quan_li_chi_tieu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nhom_5_quan_li_chi_tieu.data.GiaoDich
import com.example.nhom_5_quan_li_chi_tieu.data.GiaoDichDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
// Kế thừa và Khởi tạo ViewModel của Android
class BudgetViewModel(private val giaoDichDao: GiaoDichDao) : ViewModel() {

    // 1. Dòng chảy dữ liệu (StateFlow) chứa danh sách chi tiêu để giao diện UI hứng lấy và vẽ ra
    //Dữ liệu chảy 1 chiều từ đây xuống UI
    val danhSachGiaoDich: StateFlow<List<GiaoDich>> = giaoDichDao.layLichSuGiaoDich()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList() // Ban đầu chưa có dữ liệu thì trả về danh sách rỗng
        )

    // 2. Hàm xử lý logic khi người dùng nhấn nút "Lưu" trên giao diện
    // Nhận sự kiện từ UI đẩy lên
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

    // 3. Hàm xử lý logic khi người dùng nhấn nút "Xóa" một giao dịch
    fun xoaGiaoDich(giaoDich: GiaoDich){
        // Sử dụng Coroutines (viewModelScope.launch) để chạy lệnh xóa dưới nền,
        // tránh làm đơ giao diện (UI) chính của điện thoại
        viewModelScope.launch{
            giaoDichDao.xoaGiaoDich(giaoDich)
        }
    }
}