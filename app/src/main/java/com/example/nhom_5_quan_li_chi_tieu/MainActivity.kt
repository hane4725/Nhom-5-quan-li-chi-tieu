package com.example.nhom_5_quan_li_chi_tieu

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nhom_5_quan_li_chi_tieu.data.local.BudgetDatabase
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.DanhMucScreen
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.GioiThieuScreen
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.MainScreen
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.SplashScreen
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.ThongKeScreen
import com.example.nhom_5_quan_li_chi_tieu.ui.screens.TintucScreen
import com.example.nhom_5_quan_li_chi_tieu.repository.BudgetRepository
import com.example.nhom_5_quan_li_chi_tieu.ui.theme.BudgetBuddyTheme
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.TintucViewModel
import com.example.nhom_5_quan_li_chi_tieu.data.local.SettingsDataStore
import com.example.nhom_5_quan_li_chi_tieu.repository.SettingsRepository
data class NavigationItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("BTL_Lifecycle", "0. onCreate: Khởi tạo ứng dụng")
        // 1. Khởi tạo Room Database lấy từ tầng DATA (Local)
        val db = BudgetDatabase.getDatabase(this)
        val giaoDichDao = db.giaoDichDao()
        val danhMucDao = db.danhMucDao()

        // 2. Khởi tạo Repository lấy từ tầng REPOSITORY
        val repositoryBudget = BudgetRepository(giaoDichDao, danhMucDao)
        val settingsDataStore = SettingsDataStore(this)
        val repositorySetting = SettingsRepository(settingsDataStore)

        // 3. Khởi tạo ViewModel lấy từ tầng VIEWMODEL
        val viewModel = BudgetViewModel(repositoryBudget, repositorySetting)
        val tintucViewModel = TintucViewModel()



        setContent {
            val isDarkMode by viewModel.isDarkMode.collectAsState()
            BudgetBuddyTheme(isDarkMode) {
                val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val items = listOf(
                        NavigationItem("main", "Trang Chủ", Icons.Filled.Home, Icons.Outlined.Home),
                        NavigationItem("thongke", "Thống Kê", Icons.Filled.PieChart, Icons.Outlined.PieChart),
                        NavigationItem("danhmuc", "Danh Mục", Icons.Filled.Category, Icons.Outlined.Category),
                        NavigationItem("tintuc", "Tin Tức", Icons.Filled.Article, Icons.Outlined.Article),
                        NavigationItem("about", "Cài đặt", Icons.Filled.Info, Icons.Outlined.Info)
                    )

                    // Ẩn thanh điều hướng khi đang ở màn hình chờ (Splash)
                    val showBottomBar = currentRoute != "splash"

                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) {
                                NavigationBar(

                                    tonalElevation = 8.dp
                                ) {
                                    items.forEach { item ->
                                        val isSelected = currentRoute == item.route
                                        NavigationBarItem(
                                            selected = isSelected,
                                            onClick = {
                                                if (currentRoute != item.route) {
                                                    navController.navigate(item.route) {
                                                        popUpTo(navController.graph.startDestinationId) {
                                                            saveState = true
                                                        }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                    }
                                                }
                                            },
                                            icon = {
                                                Icon(
                                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                                    contentDescription = item.title
                                                )
                                            },
                                            label = { Text(item.title, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) }
                                        )
                                    }
                                }
                            }
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = "splash",
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            // Màn hình chờ 4 giây
                            composable("splash") {
                                SplashScreen(
                                    onTimeout = {
                                        navController.navigate("main") {
                                            popUpTo("splash") { inclusive = true }
                                        }
                                    }
                                )
                            }
                            composable("main") {
                                MainScreen(viewModel = viewModel)
                            }
                            composable("thongke") {
                                ThongKeScreen(viewModel = viewModel)
                            }
                            composable("danhmuc") {
                                DanhMucScreen(viewModel = viewModel)
                            }
                            composable("about") {
                                GioiThieuScreen(viewModel = viewModel)
                            }
                            composable("tintuc") {
                                TintucScreen(viewModel = tintucViewModel)
                            }
                        }
                    }
                }
            }
        }


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
    BudgetBuddyTheme {
    }
}
