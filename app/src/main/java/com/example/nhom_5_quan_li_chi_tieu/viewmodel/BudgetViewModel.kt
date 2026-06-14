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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import java.util.Calendar
import android.net.Uri
import android.content.Context
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class BackupData(
    val danhMucList: List<DanhMuc>,
    val giaoDichList: List<GiaoDich>
)


// Kế thừa và Khởi tạo ViewModel của Android
class BudgetViewModel(
    private val repositoryBudget: BudgetRepository,
    private val repositorySetting: SettingsRepository,

) : ViewModel() {

    //============================================ Tìm Theo Ngày Tháng ====================================================
// Biến lưu trữ Tháng đang được chọn (Mặc định là tháng hiện tại). Nếu = null tức là "Xem tất cả"
    private val _thangDuocChon =
        kotlinx.coroutines.flow.MutableStateFlow<Calendar?>(Calendar.getInstance())
    val thangDuocChon: StateFlow<Calendar?> = _thangDuocChon.asStateFlow()

    // Hàm chuyển tháng
    fun chuyenThang(thangTruoc: Boolean) {
        val current = _thangDuocChon.value
        if (current == null) {
            _thangDuocChon.value = Calendar.getInstance()
            return
        }
        val cal = current.clone() as Calendar
        if (thangTruoc) {
            cal.add(Calendar.MONTH, -1)
        } else {
            cal.add(Calendar.MONTH, 1)
        }
        _thangDuocChon.value = cal
    }

    // Hàm chọn Xem Tất cả
    fun xemTatCa() {
        _thangDuocChon.value = null
    }

    //=========================================== ĐỚI VỚI DATABASE ===================================================================

    // 1. Dòng chảy dữ liệu (StateFlow) chứa danh sách chi tiêu để giao diện UI hứng lấy và vẽ ra
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    val danhSachGiaoDichState: StateFlow<GiaoDichUiState> = _thangDuocChon
        .flatMapLatest { calendar ->
            if (calendar == null) {
                // Nếu chọn Tất cả -> Gọi hàm cũ lấy toàn bộ
                repositoryBudget.layLichSuGiaoDich()
            } else {
                // Tính toán Milliseconds ngày đầu tiên của tháng
                val start = calendar.clone() as Calendar
                start.set(Calendar.DAY_OF_MONTH, 1)
                start.set(Calendar.HOUR_OF_DAY, 0)
                start.set(Calendar.MINUTE, 0)
                start.set(Calendar.SECOND, 0)
                start.set(Calendar.MILLISECOND, 0)

                // Tính toán Milliseconds ngày cuối cùng của tháng
                val end = calendar.clone() as Calendar
                end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH))
                end.set(Calendar.HOUR_OF_DAY, 23)
                end.set(Calendar.MINUTE, 59)
                end.set(Calendar.SECOND, 59)
                end.set(Calendar.MILLISECOND, 999)

                // Gọi Repository lấy dữ liệu trong khoảng thời gian này
                repositoryBudget.layLichSuGiaoDichTheoThang(start.timeInMillis, end.timeInMillis)
            }
        }
        .onStart { delay(3500) } // Giả lập dữ liệu nặng, tải mất 3.5 giây
        .map { danhSachTuDatabase ->
            GiaoDichUiState.Success(danhSachTuDatabase) as GiaoDichUiState
        }
        .catch { loi ->
            emit(GiaoDichUiState.Error("Lỗi hệ thống: ${loi.message}"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GiaoDichUiState.Loading // Ban đầu chưa có dữ liệu thì trả về danh sách rỗng
        )


    // Dòng chảy dữ liệu lấy toàn bộ danh mục từ database
    val danhSachDanhMucState: StateFlow<DanhMucUiState> = repositoryBudget.layTatCaDanhMuc()
        .onStart { delay(3500) } // Giả lập dữ liệu nặng, tải mất 3.5 giây
        .map { danhSachTuDatabase ->

            DanhMucUiState.Success(danhSachTuDatabase) as DanhMucUiState
        }
        .catch { loi ->
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
            //Lấy thời gian theo tháng đang chọn, nếu đang xem Tất cả thì mới lấy thời gian hiện tại
            val thoiGianGiaoDich = _thangDuocChon.value?.timeInMillis ?: System.currentTimeMillis()

            val giaoDichMoi = GiaoDich(
                soTien = soTien,
                ghiChu = ghiChu,
                ngayGiaoDich = thoiGianGiaoDich, 
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
    fun xoaGiaoDich(giaoDich: GiaoDich) {
        viewModelScope.launch {
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
            val dm = DanhMuc(
                tenDanhMuc = ten,
                bieuTuong = bieuTuong,
                loai = loai,
                nganSachToiDa = nganSach
            )
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
            // Sửa lỗi Orphaned Records: Phải xóa sạch giao dịch thuộc danh mục này trước
            repositoryBudget.xoaGiaoDichTheoDanhMuc(danhMuc.id)
            // Sau đó mới xóa danh mục
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

    fun capNhatGiaoDien(isDarkMode: Boolean) {
        viewModelScope.launch {
            repositorySetting.luuTrangThaiSangToi(isDarkMode)
        }
    }

    // ================== TÍNH NĂNG XUẤT NHẬP DỮ LIỆU JSON ==================

    fun xuatDuLieuJson(context: Context, uri: Uri, onResult: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Lấy toàn bộ dữ liệu hiện tại
                val tatCaDanhMuc = repositoryBudget.layTatCaDanhMucNhanh()
                val tatCaGiaoDich = repositoryBudget.layTatCaGiaoDichNhanh()

                // 2. Gói vào cục BackupData
                val backupData = BackupData(danhMucList = tatCaDanhMuc, giaoDichList = tatCaGiaoDich)

                // 3. Biến thành chuỗi JSON
                val jsonString = Gson().toJson(backupData)

                // 4. Ghi ra file người dùng đã chọn
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(jsonString.toByteArray())
                }

                withContext(Dispatchers.Main) {
                    onResult("Xuất dữ liệu thành công! Đã lưu ${tatCaGiaoDich.size} giao dịch.")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult("Lỗi xuất dữ liệu: ${e.message}")
                }
            }
        }
    }

    fun nhapDuLieuJson(context: Context, uri: Uri, onResult: (String) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Đọc nội dung file JSON
                val jsonString = context.contentResolver.openInputStream(uri)?.bufferedReader().use { it?.readText() }
                
                if (jsonString != null) {
                    // 2. Dịch ngược chuỗi JSON thành object BackupData
                    val backupData = Gson().fromJson(jsonString, BackupData::class.java)

                    if (backupData.danhMucList.isNotEmpty() || backupData.giaoDichList.isNotEmpty()) {
                        // 3. Xóa sạch dữ liệu cũ trong máy
                        repositoryBudget.xoaToanBoGiaoDich()
                        repositoryBudget.xoaToanBoDanhMuc()

                        // 4. Bơm dữ liệu mới vào
                        repositoryBudget.themNhieuDanhMuc(backupData.danhMucList)
                        repositoryBudget.themNhieuGiaoDich(backupData.giaoDichList)

                        withContext(Dispatchers.Main) {
                            onResult("Khôi phục thành công ${backupData.giaoDichList.size} giao dịch!")
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            onResult("File JSON không chứa dữ liệu hợp lệ.")
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult("Lỗi đọc file JSON: file không đúng định dạng.")
                }
            }
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

