package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.saveable.rememberSaveable
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts

@Composable
fun GioiThieuScreen(viewModel: BudgetViewModel) {
    // 1. Hứng luồng dữ liệu từ ViewModel
    val tenNguoiDung by viewModel.tenNguoiDung.collectAsState()
    val isDarkMode by viewModel.isDarkMode.collectAsState()

    val context = LocalContext.current
    val linkPdf = "https://drive.google.com/file/d/1VcP72zRg0jF2fMLYouagoCgR00408AI-/view?usp=drive_link"
    // 2. Biến tạm để gõ phím
    var tempName by rememberSaveable { mutableStateOf("") }

    val exportLauncher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("application/json")) { uri ->
        uri?.let {
            viewModel.xuatDuLieuJson(context, it) { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

    val importLauncher = rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        uri?.let {
            viewModel.nhapDuLieuJson(context, it) { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
        }
    }

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
                .verticalScroll(rememberScrollState())//Cuộn dọc
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
            // --- NHÓM 3: Hướng dẫn sử dụng ---
            Button(
                onClick = {
                    // Tạo một thông điệp (Intent) yêu cầu Android mở link này lên
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkPdf))
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text("📖 Đọc Hướng Dẫn Sử Dụng", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // --- NHÓM 4: QUẢN LÝ DỮ LIỆU ---
            Text(
                text = "Quản lý dữ liệu",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.secondary
            )
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { exportLauncher.launch("dulieu_thuchi.json") },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Xuất JSON", color = Color.White)
                }
                
                Button(
                    onClick = { importLauncher.launch(arrayOf("application/json", "*/*")) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Nhập JSON", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            // --- NHÓM 5: GIỚI THIỆU NHÓM & ỨNG DỤNG ---
            Text(
                text = "Thông tin ứng dụng",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                color = MaterialTheme.colorScheme.secondary
            )
            
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Quản Lý Chi Tiêu", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Phiên bản: 1.0.0 (Beta)", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Nhóm phát triển: Nhóm 5", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Ứng dụng giúp bạn dễ dàng ghi chép, theo dõi thu chi cá nhân. Tích hợp thiết lập hạn mức danh mục, phân tích biểu đồ trực quan, và chuẩn kiến trúc MVVM.",
                        fontSize = 13.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}
