package com.example.nhom_5_quan_li_chi_tieu.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Khai báo các bảng xuất hiện trong Database và phiên bản (version) của DB
@Database(entities = [DanhMuc::class, GiaoDich::class], version = 1, exportSchema = false)
abstract class BudgetDatabase : RoomDatabase() {

    // Khai báo các hàm để ViewModel gọi đến các câu lệnh truy vấn SQL
    abstract fun danhMucDao(): DanhMucDao
    abstract fun giaoDichDao(): GiaoDichDao

    companion object {
        @Volatile
        private var INSTANCE: BudgetDatabase? = null

        // Hàm khởi tạo Database theo dạng Singleton (chỉ tạo 1 lần duy nhất để tránh tốn RAM)
        fun getDatabase(context: Context): BudgetDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BudgetDatabase::class.java,
                    "budget_buddy_database" // Tên file database lưu trong điện thoại
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

