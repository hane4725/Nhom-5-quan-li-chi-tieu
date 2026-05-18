package com.example.nhom_5_quan_li_chi_tieu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.nhom_5_quan_li_chi_tieu.data.BudgetDatabase
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.MainScreen
import com.example.nhom_5_quan_li_chi_tieu.ui.theme.Nhom5quanlichitieuTheme
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 1. Khởi tạo Room Database lấy từ tầng DATA
        val db = BudgetDatabase.getDatabase(this)
        val dao = db.giaoDichDao()

        // 2. Khởi tạo ViewModel lấy từ tầng VIEWMODEL
        val viewModel = BudgetViewModel(dao)

        setContent {
            // Chú ý: Tên Theme này phải khớp với tên trong thư mục ui.theme của bạn
            Nhom5quanlichitieuTheme() {
                Surface(color = MaterialTheme.colorScheme.background) {
                    // 3. Đẩy dữ liệu ra màn hình UI
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Nhom5quanlichitieuTheme {
        Greeting("Android")
    }
}