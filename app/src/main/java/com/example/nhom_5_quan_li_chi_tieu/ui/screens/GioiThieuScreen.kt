package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel

@Composable
fun GioiThieuScreen(viewModel: BudgetViewModel) {
    // 1. Hứng luồng dữ liệu từ ViewModel
    val tenNguoiDung by viewModel.tenNguoiDung.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    // 2. Biến tạm để gõ phím
    var tempName by remember { mutableStateOf("") }

    // Đồng bộ biến tạm khi dữ liệu tenNguoiDung tải lên thành công
    LaunchedEffect(tenNguoiDung) {
        tempName = tenNguoiDung
    }

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
                        Text("⚙\uFE0F", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Cài đặt hệ thống",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Lựa chọn theo cá nhân bạn",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            // --- NHÓM 1: CÁ NHÂN HÓA ---
            Text(
                text = "Cá nhân hóa",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.secondary
            )
            
            OutlinedTextField(
                value = tempName,
                onValueChange = { tempName = it },
                label = { Text("Tên người dùng") },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { 
                        // Gọi hàm capNhatTen() bên BudgetViewModel
                        viewModel.capNhatTen(tempName)
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Lưu", tint = Color(0xFF2E7D32))
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // --- NHÓM 2: GIAO DIỆN ---
            Text(
                text = "Giao diện",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.secondary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Chế độ nền tối (Dark Mode)", fontSize = 16.sp)
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = { isChecked ->
                        // Gọi hàm capNhatGiaoDien() bên BudgetViewModel
                        viewModel.capNhatGiaoDien(isChecked)
                    }
                )
            }
        }
    }
}
