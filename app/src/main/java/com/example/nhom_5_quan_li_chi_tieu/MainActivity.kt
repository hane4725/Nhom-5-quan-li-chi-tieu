package com.example.nhom_5_quan_li_chi_tieu

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.nhom_5_quan_li_chi_tieu.data.BudgetDatabase
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.MainScreen
import com.example.nhom_5_quan_li_chi_tieu.ui.theme.Nhom5quanlichitieuTheme
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.ThongKeScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val xanhChuDao = Color(0xFF2E7D32)
        val xanhNhat = Color(0xFFE8F5E9)
        val xamNen = Color(0xFFF5F5F5)

        val BangMauApp = lightColorScheme(
            primary = xanhChuDao,
            secondary = xanhNhat,
            background = xamNen,
            surface = Color.LightGray,       // Màu của các thẻ Card
            onPrimary = Color.White,     // Màu chữ nằm trên nền xanh (ví dụ chữ trên Nút bấm)
            onSurface = Color.Black      // Màu chữ nằm trên thẻ Card
        )

        // 1. Khởi tạo Room Database lấy từ tầng DATA
        val db = BudgetDatabase.getDatabase(this)
        val dao = db.giaoDichDao()


        // 2. Khởi tạo ViewModel lấy từ tầng VIEWMODEL
        val viewModel = BudgetViewModel(dao)

        setContent {
            // Chú ý: Tên Theme này phải khớp với tên trong thư mục ui.theme của bạn
            Nhom5quanlichitieuTheme {
                MaterialTheme(
                    colorScheme = BangMauApp
                ) {
                    val navController = rememberNavController()

                    NavHost(navController =navController, startDestination = "main"){
                        composable("main"){
                            MainScreen(viewModel, onNavigateToThongKe =
                                { navController.navigate("thongke") })
                        }
                        composable("thongke"){
                            ThongKeScreen(onBackClick =
                                {navController.popBackStack() } )
                        }
                    }
                    // 3. Đẩy dữ liệu ra màn hình UI

                }
            }
        }
    }

    private fun compossable(string: String, function: () -> Unit) {}

    override fun onStart() {
        super.onStart()
        Log.d("BTL_Lifecycle", "1. App đang hiện lên (onStart)")
    }

    override fun onResume() {
        super.onResume()
        Log.d("BTL_Lifecycle", "2. App đã sẵn sàng cho Hân tương tác (onResume)")
    }

    override fun onPause() {
        super.onPause()
        Log.d("BTL_Lifecycle", "3. App đang bị che khuất hoặc tạm dừng (onPause)")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BTL_Lifecycle", "4. App đã bị tắt hoàn toàn (onDestroy)")
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Nhom5quanlichitieuTheme {
    }
}