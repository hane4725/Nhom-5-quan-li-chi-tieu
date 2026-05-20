package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GioiThieuScreen(
    onBackClick: () -> Unit,
    onOpenPdfUserGuide: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Giới Thiệu Ứng Dụng", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // ================= 1. LOGO & TÊN APP =================
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("🏦", fontSize = 48.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "BudgetBuddy",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Phiên bản 1.0 (BTL Nhóm 5)",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // ================= 2. THÔNG TIN DỰ ÁN & ĐỀ TÀI =================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Đề tài số 9: Quản lý chi tiêu cá nhân",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Ứng dụng giúp theo dõi các khoản thu/chi hàng ngày, lập ngân sách chi tiêu tối đa cho từng danh mục để tránh vung tay quá trán, và thống kê báo cáo phần trăm trực quan.",
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // ================= 3. THÔNG TIN NHÓM thực hiện =================
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Thành viên thực hiện:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("1. Lê Ngọc Hân (Nhóm Trưởng)", fontWeight = FontWeight.Medium, fontSize = 13.sp)
                        Text("Đóng góp: 40%", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("2. Thành viên 2", fontSize = 13.sp)
                        Text("Đóng góp: 20%", fontSize = 13.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("3. Thành viên 3", fontSize = 13.sp)
                        Text("Đóng góp: 20%", fontSize = 13.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("4. Thành viên 4", fontSize = 13.sp)
                        Text("Đóng góp: 20%", fontSize = 13.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // ================= 4. NÚT MỞ FILE PDF HDSD (Advanced Requirement) =================
            Button(
                onClick = onOpenPdfUserGuide,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = "PDF")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Xem Hướng Dẫn Sử Dụng (PDF)", fontWeight = FontWeight.Bold)
            }
            
            Text(
                text = "Trường Đại học Hạ Long - Khoa CNTT",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}
