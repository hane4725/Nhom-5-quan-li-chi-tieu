package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nhom_5_quan_li_chi_tieu.data.GiaoDich
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel
import androidx.compose.material.icons.filled.Delete

// ĐỊNH NGHĨA DANH MỤC THỬ NGHIỆM ĐỂ HIỂN THỊ TRÊN GIAO DIỆN CHIPS
data class MockDanhMuc(val id: Int, val ten: String, val icon: String, val loai: Int, val nganSach: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BudgetViewModel,
    onNavigateToThongKe: () -> Unit,
    onNavigateToAbout: () -> Unit
) {
    // 1. Quản lý trạng thái nhập liệu & chọn danh mục
    var soTien by remember { mutableStateOf("") }
    var ghiChu by remember { mutableStateOf("") }
    var isThuNhap by remember { mutableStateOf(false) } // False là Chi tiêu, True là Thu nhập
    
    // Danh sách danh mục giả lập để vẽ UI chọn nhanh
    val danhSachDanhMuc = listOf(
        MockDanhMuc(1, "Ăn uống", "🍔", -1, 2000000.0),
        MockDanhMuc(2, "Đi lại", "🚗", -1, 500000.0),
        MockDanhMuc(3, "Mua sắm", "🛍️", -1, 1500000.0),
        MockDanhMuc(4, "Lương", "💵", 1, 0.0),
        MockDanhMuc(5, "Khác", "🏷️", -1, 1000000.0)
    )
    
    var danhMucDuocChon by remember { mutableStateOf(danhSachDanhMuc[0]) }
    var inputError by remember { mutableStateOf<String?>(null) }
    val danhSach by viewModel.danhSachGiaoDich.collectAsState()

    // 2. Tính toán tổng số dư nhanh hiển thị lên Card
    val tongThu = danhSach.filter { gd -> gd.idDanhMuc == 4 }.sumOf { it.soTien }
    val tongChi = danhSach.filter { gd -> gd.idDanhMuc != 4 }.sumOf { it.soTien }
    val soDu = tongThu - tongChi

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("BudgetBuddy", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onNavigateToAbout) {
                        Icon(Icons.Default.Info, contentDescription = "About Project")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            
            // ================= KHU VỰC 1: gradient card số dư =================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
                                )
                            )
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text("Tổng Số Dư", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                            Text(
                                text = String.format("%,.0f VNĐ", soDu),
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Tổng Thu (▲)", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                                    Text(
                                        text = String.format("%,.0f đ", tongThu),
                                        color = Color(0xFF81C784),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Column {
                                    Text("Tổng Chi (▼)", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                                    Text(
                                        text = String.format("%,.0f đ", tongChi),
                                        color = Color(0xFFFF8A80),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ================= KHU VỰC 2: form nhập giao dịch =================
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Nhập Khoản Chi Tiêu Mới", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                        // 2.1 Chọn Loại giao dịch (Thu / Chi)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        ) {
                            Button(
                                onClick = { 
                                    isThuNhap = false
                                    danhMucDuocChon = danhSachDanhMuc.first { it.loai == -1 }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (!isThuNhap) Color(0xFFC62828) else Color.Transparent,
                                    contentColor = if (!isThuNhap) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Text("Khoản Chi (▼)")
                            }
                            Button(
                                onClick = { 
                                    isThuNhap = true
                                    danhMucDuocChon = danhSachDanhMuc.first { it.loai == 1 }
                                },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isThuNhap) Color(0xFF2E7D32) else Color.Transparent,
                                    contentColor = if (isThuNhap) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                Text("Khoản Thu (▲)")
                            }
                        }

                        // 2.2 Nhập Số tiền & Ghi chú
                        OutlinedTextField(
                            value = soTien,
                            onValueChange = { 
                                soTien = it
                                inputError = null 
                            },
                            label = { Text("Nhập số tiền (VNĐ)") },
                            prefix = { Text("đ ") },
                            isError = inputError != null,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                        if (inputError != null) {
                            Text(inputError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }

                        OutlinedTextField(
                            value = ghiChu,
                            onValueChange = { ghiChu = it },
                            label = { Text("Ghi chú chi tiết") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        // 2.3 Chọn nhanh Danh mục bằng Chips (Cuộn Ngang)
                        Text("Chọn Danh Mục:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(danhSachDanhMuc.filter { it.loai == (if (isThuNhap) 1 else -1) }) { dm ->
                                FilterChip(
                                    selected = danhMucDuocChon.id == dm.id,
                                    onClick = { danhMucDuocChon = dm },
                                    label = { Text("${dm.icon} ${dm.ten}") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }
                        }

                        // 2.4 Cảnh báo ngân sách tức thì
                        if (!isThuNhap) {
                            val tongTienDaChiCungDanhMuc = danhSach
                                .filter { it.idDanhMuc == danhMucDuocChon.id }
                                .sumOf { it.soTien }
                            val soTienSapGhi = soTien.toDoubleOrNull() ?: 0.0
                            
                            if (tongTienDaChiCungDanhMuc + soTienSapGhi > danhMucDuocChon.nganSach) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "⚠️ Vượt ngân sách! Danh mục [${danhMucDuocChon.ten}] hạn mức là ${String.format("%,.0fđ", danhMucDuocChon.nganSach)}. Bạn đã tiêu ${String.format("%,.0fđ", tongTienDaChiCungDanhMuc)}.",
                                        color = Color(0xFFE65100),
                                        modifier = Modifier.padding(8.dp),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        // 2.5 Nút lưu
                        Button(
                            onClick = {
                                val parseSoTien = soTien.toDoubleOrNull()
                                if (parseSoTien == null || parseSoTien <= 0) {
                                    inputError = "Vui lòng nhập số tiền lớn hơn 0"
                                } else if (ghiChu.isBlank()) {
                                    inputError = "Vui lòng nhập ghi chú chi tiêu"
                                } else {
                                    viewModel.luuGiaoDich(parseSoTien, ghiChu, danhMucDuocChon.id)
                                    soTien = ""
                                    ghiChu = ""
                                    inputError = null
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Save")
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Lưu Giao Dịch", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // ================= KHU VỰC 3: thống kê nhanh & điều hướng =================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onNavigateToThongKe,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer, contentColor = MaterialTheme.colorScheme.onSecondaryContainer)
                    ) {
                        Text("Xem Thống Kê Chi Tiết", fontSize = 13.sp)
                    }
                }
            }

            // ================= KHU VỰC 4: danh sách giao dịch gần đây =================
            item {
                Text(
                    text = "Lịch Sử Giao Dịch Gần Đây",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (danhSach.isEmpty()) {
                item {
                    Text(
                        "Chưa có giao dịch nào được lưu. Hãy nhập khoản chi đầu tiên!",
                        color = Color.Gray,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            } else {
                //Vòng lặp duyệt qua danh sách giao dịch để hiển thị ra UI
                items(danhSach) { gd ->
                    val dm = danhSachDanhMuc.find { it.id == gd.idDanhMuc } ?: MockDanhMuc(99, "Khác", "🏷️", -1, 0.0)
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(dm.icon, fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = dm.ten, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(text = gd.ghiChu, color = Color.Gray, fontSize = 12.sp)
                                }
                            }
                            
                            val isIncome = gd.idDanhMuc == 4
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                //"%,.0fđ" Chỉ định lấy số 0 sau số thập phân
                                Text(
                                    text = (if (isIncome) "+" else "-") + String.format("%,.0fđ", gd.soTien),
                                    color = if (isIncome) Color(0xFF2E7D32) else Color(0xFFC62828),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))

                                IconButton(
                                    //Khi bấm sẽ gọi hàm xóa giao dich trong viewmodel
                                    onClick = { viewModel.xoaGiaoDich(gd) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Xóa giao dịch",
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
}