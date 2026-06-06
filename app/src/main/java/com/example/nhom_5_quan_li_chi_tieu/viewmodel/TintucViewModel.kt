package com.example.nhom_5_quan_li_chi_tieu.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nhom_5_quan_li_chi_tieu.network.BaiBao
import com.example.nhom_5_quan_li_chi_tieu.repository.NewsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class TintucUiState{
    object DangTai : TintucUiState() // Trang thai Loading
    data class ThanhCong (val danhSach: List<BaiBao>): TintucUiState() //Success
    data class  Loi (val thongbaoLoi: String) : TintucUiState() //Error
}

class TintucViewModel: ViewModel(){
    private  val repository = NewsRepository()
    // Dòng chảy dữ liệu trực tiếp đưa lên Màn hình
    val danhSanhTinTucState: StateFlow<TintucUiState> = repository.layTinTucTuMang()
        .map{phanhoi->
            //Khi lay duoc du lieu nhet vao hop
            TintucUiState.ThanhCong(phanhoi.danhSachBaiBao) as TintucUiState

        }
        .catch { loi->
            //Nếu xảy ra lõi API thì nhét vào hộp lỗi
            emit(TintucUiState.Loi("Không tải được tin tức: ${loi.message}"))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TintucUiState.DangTai //Mac dinh ban dau la Dang Tai
        )
}