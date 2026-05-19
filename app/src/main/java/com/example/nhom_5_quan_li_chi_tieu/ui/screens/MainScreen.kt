package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import android.R
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel

@Composable
fun MainScreen(viewModel: BudgetViewModel,
               onNavigateToThongKe: () -> Unit
) {
    //Dùng State để UI tự cập nhật (collectAsState)
    val danhSach by viewModel.danhSachGiaoDich.collectAsState()
    val context = LocalContext.current
    //Dùng State để quản lý ô nhập liệu
    var soTien by remember { mutableStateOf("") }
    var ghiChu by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "BudgetBuddy - Thử nghiệm", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = soTien,
            onValueChange = { soTien = it },
            label = { Text("Nhập số tiền chi tiêu") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ghiChu,
            onValueChange = { ghiChu = it },
            label = { Text("Nhập ghi chú") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(

            onClick = {

                // Dùng toDoubleOrNull(): Thử ép thành số, nếu là chữ "abc" thì nó trả về null
                val soTienHopLe = soTien.toDoubleOrNull()

                // Kiểm tra kép: Vừa không được rỗng chuỗi ghi chú, vừa phải là SỐ hợp lệ
                if (soTienHopLe != null && ghiChu.isNotBlank()) {
                    viewModel.luuGiaoDich(soTienHopLe, ghiChu, 1)
                    soTien = ""
                    ghiChu = ""
                } else {
                    // 2. Hiện thông báo LỖI nếu nhập sai hoặc để trống
                    Toast.makeText(context, "Lỗi: Vui lòng nhập đúng số tiền và không để trống!", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
        ) {
            Text("Lưu Khoản Chi")
        }

        Button(
            onClick = {
                // Khi bấm nút, nó sẽ phát tín hiệu "Đi sang Thống kê"
                onNavigateToThongKe()
            },
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
        ) {
            Text("Xem Thống kê")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "Lịch sử chi tiêu (Từ Room DB)", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(danhSach) { gd ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Số tiền: -${gd.soTien} VNĐ", color = MaterialTheme.colorScheme.onSurface)
                        Text(text = "Ghi chú: ${gd.ghiChu}")
                    }
                }
            }
        }
    }
}