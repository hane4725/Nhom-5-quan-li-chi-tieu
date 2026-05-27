package com.example.nhom_5_quan_li_chi_tieu.data.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Khai báo các bảng xuất hiện trong Database và phiên bản (version) của DB
@Database(entities = [DanhMuc::class, GiaoDich::class], version = 3, exportSchema = false)
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
                )
                .fallbackToDestructiveMigration()
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        // Kiểm tra xem bảng danh mục có rỗng không (phòng trường hợp destructive migration xóa bảng)
                        var isEmpty = true
                        try {
                            val cursor = db.query("SELECT COUNT(*) FROM bang_danh_muc")
                            if (cursor.moveToFirst()) {
                                isEmpty = cursor.getInt(0) == 0
                            }
                            cursor.close()
                        } catch (e: Exception) {
                            Log.e("BTL_DB", "Lỗi kiểm tra bảng danh mục: ${e.message}")
                        }

                        if (isEmpty) {
                            db.execSQL("INSERT INTO bang_danh_muc (tenDanhMuc, bieuTuong, loai, nganSachToiDa) VALUES ('Ăn uống', '🍔', -1, 2000000.0)")
                            db.execSQL("INSERT INTO bang_danh_muc (tenDanhMuc, bieuTuong, loai, nganSachToiDa) VALUES ('Đi lại', '🚗', -1, 500000.0)")
                            db.execSQL("INSERT INTO bang_danh_muc (tenDanhMuc, bieuTuong, loai, nganSachToiDa) VALUES ('Mua sắm', '🛍️', -1, 1500000.0)")
                            db.execSQL("INSERT INTO bang_danh_muc (tenDanhMuc, bieuTuong, loai, nganSachToiDa) VALUES ('Lương', '💵', 1, 0.0)")
                            db.execSQL("INSERT INTO bang_danh_muc (tenDanhMuc, bieuTuong, loai, nganSachToiDa) VALUES ('Khác', '🏷️', -1, 1000000.0)")
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

