package com.example.nhom_5_quan_li_chi_tieu.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nhom_5_quan_li_chi_tieu.data.local.GiaoDich
import com.example.nhom_5_quan_li_chi_tieu.data.local.DanhMuc
import com.example.nhom_5_quan_li_chi_tieu.repository.BudgetRepository
import com.example.nhom_5_quan_li_chi_tieu.repository.SettingsRepository
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.DanhMucScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.delay

// Kế thừa và Khởi tạo ViewModel của Android
class BudgetViewModel(
    private val repositoryBudget: BudgetRepository,
    private val repositorySetting: SettingsRepository,

) : ViewModel() {

    //=========================================== ĐỚI VỚI DATABASE ===================================================================


    // 1. Dòng chảy dữ liệu (StateFlow) chứa danh sách chi tiêu để giao diện UI hứng lấy và vẽ ra
    val danhSachGiaoDichState: StateFlow<GiaoDichUiState> = repositoryBudget.layLichSuGiaoDich()
        .onStart { delay(3000) }
        .map{danhSachTuDatabase->

            GiaoDichUiState.Success(danhSachTuDatabase) as GiaoDichUiState
        }
        .catch{ loi ->
            emit(GiaoDichUiState.Error("Lỗi hệ thống: ${loi.message}"))

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GiaoDichUiState.Loading // Ban đầu chưa có dữ liệu thì trả về danh sách rỗng
        )

    // Dòng chảy dữ liệu lấy toàn bộ danh mục từ database
    val danhSachDanhMucState: StateFlow<DanhMucUiState> = repositoryBudget.layTatCaDanhMuc()
        .onStart { delay(3000) }
        .map{danhSachTuDatabase->

            DanhMucUiState.Success(danhSachTuDatabase) as DanhMucUiState
        }
        .catch{ loi ->
            emit(DanhMucUiState.Error("Lỗi hệ thống: ${loi.message}"))

        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = DanhMucUiState.Loading
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
            repositoryBudget.themGiaoDich(giaoDichMoi)
        }
    }

    // 3. Hàm xử lý logic khi người dùng nhấn nút "Cập nhật" (Sửa giao dịch)
    fun suaGiaoDich(giaoDich: GiaoDich) {
        viewModelScope.launch {
            repositoryBudget.suaGiaoDich(giaoDich) // Cập nhật đè lên dữ liệu cũ trong Database
        }
    }

    // 3. Hàm xử lý logic khi người dùng nhấn nút "Xóa" một giao dịch
    fun xoaGiaoDich(giaoDich: GiaoDich){
        viewModelScope.launch{
            repositoryBudget.xoaGiaoDich(giaoDich)
        }
    }

//    fun xoaToanBoLichSu() {
//        viewModelScope.launch {
//            val currentState = danhSachGiaoDichState.value
//
//            // Nếu Hộp đang chứa danh sách (Success) thì lôi ra xóa từng cái một
//            if (currentState is GiaoDichUiState.Success) {
//                currentState.danhSach.forEach { gd ->
//                    repositoryBudget.xoaGiaoDich(gd)
//                }
//            }
//        }
//    }

    // 4. Các hàm xử lý danh mục
    fun luuDanhMuc(ten: String, bieuTuong: String, loai: Int, nganSach: Double) {
        viewModelScope.launch {
            val dm = DanhMuc(tenDanhMuc = ten, bieuTuong = bieuTuong, loai = loai, nganSachToiDa = nganSach)
            repositoryBudget.themDanhMuc(dm)
        }
    }

    fun capNhatNganSach(danhMuc: DanhMuc, nganSachMoi: Double) {
        viewModelScope.launch {
            val dmCapNhat = danhMuc.copy(nganSachToiDa = nganSachMoi)
            repositoryBudget.suaDanhMuc(dmCapNhat)
        }
    }

    fun xoaDanhMuc(danhMuc: DanhMuc) {
        viewModelScope.launch {
            repositoryBudget.xoaDanhMuc(danhMuc)
        }
    }

//============================================ ĐỐI VỚI DATASTORE ====================================================


    // --- CÁC DÒNG CHẢY DỮ LIỆU TỪ DATASTORE ---
    val tenNguoiDung: StateFlow<String> = repositorySetting.tenNguoiDungFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "Bạn"
        )
    val isDarkMode: StateFlow<Boolean> = repositorySetting.cheDoToiFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    // --- CÁC HÀM XỬ LÝ CÀI ĐẶT (Đây là đối với DataStore) ---
    fun capNhatTen(tenMoi: String) {
        viewModelScope.launch {
            repositorySetting.luuTenNguoiDung(tenMoi)
        }
    }
    fun capNhatGiaoDien(isDark: Boolean) {
        viewModelScope.launch {
            repositorySetting.luuTrangThaiSangToi(isDark)
        }
    }
}

//============================================ Trạng thái Loading, Success và Error) ====================================================

// Trang thái Loading sử dụng object vì đây là Singleton 1 bản sao trong suốt quá trình khởi chạy
// Trang thái Success và Error sử dụng data class vì đây là trạng thái động
sealed class GiaoDichUiState{
    object Loading : GiaoDichUiState()
    data class Success (val danhSach: List<GiaoDich>) : GiaoDichUiState()
    data class Error (val thongBaoLoi: String) : GiaoDichUiState()
}

sealed class DanhMucUiState{
    object Loading : DanhMucUiState()
    data class Success (val danhSach: List<DanhMuc>) : DanhMucUiState()
    data class Error (val thongBaoLoi: String) : DanhMucUiState()
}

