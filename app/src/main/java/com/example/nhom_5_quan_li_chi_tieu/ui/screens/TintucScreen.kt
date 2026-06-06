package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.TintucUiState
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.TintucViewModel
import androidx.compose.ui.draw.shadow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.Brush


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TintucScreen(viewModel: TintucViewModel) {
    // 1. Cắm ống hút vào ViewModel để lấy dòng chảy trạng thái
    val uiState by viewModel.danhSanhTinTucState.collectAsState()
    
    // Lấy context của màn hình hiện tại để có thể mở trình duyệt web
    val context = LocalContext.current

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF1B5E20),
                                MaterialTheme.colorScheme.primary,
                                Color(0xFF00897B)
                            )
                        ),
                        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                    )
                    .padding(top = 40.dp, bottom = 16.dp, start = 20.dp, end = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo icon
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📰", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Tin tức Tài chính",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Cập nhật thông tin tài chính quanh bạn",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        // 2. Chia 3 trường hợp bằng lệnh when
        when (uiState) {
            // Trường hợp A: Đang Tải -> Hiện vòng xoay
            is TintucUiState.DangTai -> {
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            // Trường hợp B: Lỗi rớt mạng -> Hiện chữ đỏ
            is TintucUiState.Loi -> {
                val loi = (uiState as TintucUiState.Loi).thongbaoLoi
                Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text(text = "⚠️ $loi", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }

            // Trường hợp C: Lấy dữ liệu thành công -> Vẽ danh sách bài báo
            is TintucUiState.ThanhCong -> {
                val danhSachBaiBao = (uiState as TintucUiState.ThanhCong).danhSach
                
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(danhSachBaiBao) { baiBao ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    // Khi người dùng bấm vào thẻ, tạo Intent mở trình duyệt
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(baiBao.linkBaiViet))
                                    context.startActivity(intent)
                                },
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = baiBao.tieuDe,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = baiBao.moTa ?: "Không có mô tả", // Nếu mô tả bị null thì hiện chữ này
                                    fontSize = 14.sp,
                                    color = Color.DarkGray,
                                    maxLines = 3, // Giới hạn đọc 3 dòng
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
