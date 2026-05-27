package com.example.nhom_5_quan_li_chi_tieu.repository

import com.example.nhom_5_quan_li_chi_tieu.data.local.SettingsDataStore
import kotlinx.coroutines.flow.Flow
class SettingsRepository(
    private val settingsDataStore: SettingsDataStore
) {
    //Lấy dữ liệu
    val tenNguoiDungFlow: Flow<String> = settingsDataStore.tenNguoiDungFlow

    val cheDoToiFlow: Flow<Boolean> = settingsDataStore.cheDoToiFlow

    //Cầu nối cập nhật dữ liệu

    suspend fun luuTenNguoiDung(ten: String){
        settingsDataStore.luuTenNguoiDung(ten)
    }

    suspend fun luuTrangThaiSangToi(isDarkMode: Boolean){
        settingsDataStore.luuTrangThaiSangToi(isDarkMode)
    }

}
