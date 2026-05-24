package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import com.example.nhom_5_quan_li_chi_tieu.data.DanhMuc
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DanhMucScreen(
    viewModel: BudgetViewModel
) {
    val danhSachDanhMuc by viewModel.danhSachDanhMuc.collectAsState()

    // Trạng thái cho Dialog sửa ngân sách
    var showEditDialog by remember { mutableStateOf(false) }
    var danhMucDangSua by remember { mutableStateOf<DanhMuc?>(null) }
    var nganSachMoiText by remember { mutableStateOf("") }
    var dialogError by remember { mutableStateOf<String?>(null) }

    // Trạng thái cho Form thêm danh mục mới
    var tenDanhMuc by remember { mutableStateOf("") }
    var bieuTuong by remember { mutableStateOf("") }
    var isThuNhap by remember { mutableStateOf(false) }
    var nganSachToiDa by remember { mutableStateOf("") }
    var formError by remember { mutableStateOf<String?>(null) }

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
                                Color(0xFF2E7D32),
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
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📂", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Quản Lý Danh Mục",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Thêm, sửa và xóa danh mục",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ================= PHẦN 1: FORM THÊM DANH MỤC MỚI =================
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Thêm Danh Mục Mới",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        ) {
                            Button(
                                onClick = { isThuNhap = false },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!isThuNhap) Color(0xFFC62828) else Color.Transparent,
                                    contentColor = if (!isThuNhap) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Text("Danh mục Chi (▼)")
                            }
                            Button(
                                onClick = { isThuNhap = true },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isThuNhap) Color(0xFF2E7D32) else Color.Transparent,
                                    contentColor = if (isThuNhap) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Text("Danh mục Thu (▲)")
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = bieuTuong,
                                onValueChange = { bieuTuong = it.take(2) }, // Giới hạn chỉ nhập 1-2 ký tự emoji
                                label = { Text("Icon Emoji") },
                                placeholder = { Text("🍔") },
                                modifier = Modifier.weight(1.2f),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = tenDanhMuc,
                                onValueChange = { tenDanhMuc = it },
                                label = { Text("Tên danh mục") },
                                modifier = Modifier.weight(2.8f),
                                maxLines = 1,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    capitalization = KeyboardCapitalization.Sentences
                                )
                            )
                        }

                        if (!isThuNhap) {
                            OutlinedTextField(
                                value = nganSachToiDa,
                                onValueChange = { nganSachToiDa = it },
                                label = { Text("Hạn mức ngân sách (VNĐ)") },
                                prefix = { Text("đ ") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        if (formError != null) {
                            Text(
                                text = formError!!,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Button(
                            onClick = {
                                val cleanBieuTuong = bieuTuong.trim()
                                val cleanTen = tenDanhMuc.trim()
                                val limit = if (isThuNhap) 0.0 else (nganSachToiDa.toDoubleOrNull() ?: 0.0)

                                if (cleanTen.isBlank()) {
                                    formError = "Tên danh mục không được để trống"
                                } else if (cleanBieuTuong.isBlank()) {
                                    formError = "Vui lòng nhập 1 biểu tượng emoji"
                                } else if (!isThuNhap && nganSachToiDa.toDoubleOrNull() == null) {
                                    formError = "Hạn mức ngân sách phải là số lớn hơn 0"
                                } else {
                                    viewModel.luuDanhMuc(
                                        ten = cleanTen,
                                        bieuTuong = cleanBieuTuong,
                                        loai = if (isThuNhap) 1 else -1,
                                        nganSach = limit
                                    )
                                    // Reset Form
                                    tenDanhMuc = ""
                                    bieuTuong = ""
                                    nganSachToiDa = ""
                                    formError = null
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Add")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Thêm Mới Danh Mục", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // ================= PHẦN 2: DANH SÁCH DANH MỤC HIỆN CÓ =================
            item {
                Text(
                    text = "Danh Sách Danh Mục Đang Có",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (danhSachDanhMuc.isEmpty()) {
                item {
                    Text(
                        text = "Chưa có danh mục nào. Hãy tạo danh mục đầu tiên!",
                        color = Color.Gray
                    )
                }
            } else {
                items(danhSachDanhMuc) { dm ->
                    val isIncome = dm.loai == 1
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(
                                            if (isIncome) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                            RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(dm.bieuTuong, fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = dm.tenDanhMuc,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    if (isIncome) {
                                        Surface(
                                            color = Color(0xFFE8F5E9),
                                            shape = RoundedCornerShape(4.dp)
                                        ) {
                                            Text(
                                                text = "Thu nhập",
                                                color = Color(0xFF2E7D32),
                                                fontSize = 11.sp,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = "Ngân sách: " + String.format("%,.0fđ", dm.nganSachToiDa),
                                            color = Color.Gray,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                            }

                            Row {
                                if (!isIncome) {
                                    IconButton(
                                        onClick = {
                                            danhMucDangSua = dm
                                            nganSachMoiText = dm.nganSachToiDa.toLong().toString()
                                            dialogError = null
                                            showEditDialog = true
                                        }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Sửa hạn mức",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                IconButton(
                                    onClick = { viewModel.xoaDanhMuc(dm) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Xóa danh mục",
                                        tint = Color(0xFFC62828)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ================= PHẦN 3: DIALOG SỬA NGÂN SÁCH DÀNH CHO CHI TIÊU =================
    if (showEditDialog && danhMucDangSua != null) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Sửa hạn mức ngân sách") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Đặt hạn mức chi tiêu tối đa mới cho danh mục [${danhMucDangSua!!.bieuTuong} ${danhMucDangSua!!.tenDanhMuc}]",
                        fontSize = 14.sp
                    )
                    OutlinedTextField(
                        value = nganSachMoiText,
                        onValueChange = {
                            nganSachMoiText = it
                            dialogError = null
                        },
                        label = { Text("Ngân sách tối đa (VNĐ)") },
                        prefix = { Text("đ ") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (dialogError != null) {
                        Text(
                            text = dialogError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = nganSachMoiText.toDoubleOrNull()
                        if (parsed == null || parsed < 0) {
                            dialogError = "Vui lòng nhập ngân sách hợp lệ (>= 0)"
                        } else {
                            viewModel.capNhatNganSach(danhMucDangSua!!, parsed)
                            showEditDialog = false
                        }
                    }
                ) {
                    Text("Lưu lại")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Hủy bỏ")
                }
            }
        )
    }
}
