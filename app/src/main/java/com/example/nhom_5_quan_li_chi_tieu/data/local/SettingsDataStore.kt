package com.example.nhom_5_quan_li_chi_tieu.data.local


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//Khởi tạo DataStore tên là "user_setting" (file vật lí)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_setting")


class SettingsDataStore (private val context: Context) {
    //Duy nhất và dung chung cho toàn bộ ứng dụng
    companion object{
        //<kieudulieu>PreferencesKey chỉ cho phép nhận kiểu dữ liệu đó
        val USER_NAME_KEY = stringPreferencesKey("user_name")
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }

    // Flow đọc dữ liệu người dung liên tục
    val tenNguoiDungFlow: Flow<String> = context.dataStore.data.map()
    {dulieu ->
        dulieu[USER_NAME_KEY] ?: "Bạn" // nếu không có dữ liệu tên truyền vào thì sẽ mặc định là "Bạn"
    }

    // Flow đọc dữ liệu chế độ tối
    val cheDoToiFlow: Flow<Boolean> = context.dataStore.data.map()
    {dulieu ->
        dulieu[DARK_MODE_KEY] ?: false // Mặc định là nền sáng (Light mode)
    }

    //Hàm cất tên mới vào kho
    suspend fun luuTenNguoiDung(name: String){
        context.dataStore.edit { dulieu->
            dulieu[USER_NAME_KEY] = name
        }
    }

    //Hàm cất trạng thái sáng tối vào kho
    suspend fun luuTrangThaiSangToi(isDarkMode: Boolean){
        context.dataStore.edit { dulieu ->
            dulieu[DARK_MODE_KEY] =isDarkMode
        }


    }
}