package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel

@Composable
fun MainScreen(viewModel: BudgetViewModel) {
    val danhSach by viewModel.danhSachGiaoDich.collectAsState()

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
                if (soTien.isNotEmpty()) {
                    viewModel.luuGiaoDich(soTien.toDouble(), ghiChu, 1)
                    soTien = ""; ghiChu = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
        ) {
            Text("Lưu Khoản Chi")
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "Lịch sử chi tiêu (Từ Room DB)", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(danhSach) { gd ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Số tiền: -${gd.soTien} VNĐ", color = MaterialTheme.colorScheme.error)
                        Text(text = "Ghi chú: ${gd.ghiChu}")
                    }
                }
            }
        }
    }
}