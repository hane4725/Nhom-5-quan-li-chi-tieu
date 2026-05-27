package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nhom_5_quan_li_chi_tieu.data.local.DanhMuc
import com.example.nhom_5_quan_li_chi_tieu.data.local.GiaoDich
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BudgetViewModel
) {
    // 1. Quản lý trạng thái nhập liệu & chọn danh mục
    var soTien by remember { mutableStateOf("") }
    var ghiChu by remember { mutableStateOf("") }
    var isThuNhap by remember { mutableStateOf(false) } // False là Chi tiêu, True là Thu nhập
    
    // Đọc danh mục động từ database
    val danhSachDanhMuc by viewModel.danhSachDanhMuc.collectAsState()
    var danhMucDuocChon by remember { mutableStateOf<DanhMuc?>(null) }
    var editingGiaoDich by remember { mutableStateOf<GiaoDich?>(null) }
    var inputError by remember { mutableStateOf<String?>(null) }
    val danhSach by viewModel.danhSachGiaoDich.collectAsState()

    // Khởi tạo/Cập nhật danh mục được chọn khi danh sách danh mục load xong
    LaunchedEffect(danhSachDanhMuc) {
        if (danhMucDuocChon == null && danhSachDanhMuc.isNotEmpty()) {
            danhMucDuocChon = danhSachDanhMuc.firstOrNull { it.loai == (if (isThuNhap) 1 else -1) } ?: danhSachDanhMuc.firstOrNull()
        }
    }

    // Tự động chuyển đổi danh mục được chọn khi thay đổi Tab Thu nhập / Chi tiêu
    LaunchedEffect(isThuNhap, danhSachDanhMuc) {
        if (danhSachDanhMuc.isNotEmpty()) {
            danhMucDuocChon = danhSachDanhMuc.firstOrNull { it.loai == (if (isThuNhap) 1 else -1) } ?: danhSachDanhMuc.firstOrNull()
        }
    }

    // 2. Tính toán tổng số dư nhanh hiển thị lên Card dựa trên loại danh mục trong DB
    val tongThu = danhSach.filter { gd ->
        val dm = danhSachDanhMuc.find { it.id == gd.idDanhMuc }
        dm?.loai == 1
    }.sumOf { it.soTien }

    val tongChi = danhSach.filter { gd ->
        val dm = danhSachDanhMuc.find { it.id == gd.idDanhMuc }
        dm?.loai == -1
    }.sumOf { it.soTien }

    val soDu = tongThu - tongChi

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
                        Text("💰", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "BudgetBuddy",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Quản lý chi tiêu thông minh",
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
            
            // ================= KHU VỰC 1: gradient card số dư =================
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(Color(0xFF1B5E20), MaterialTheme.colorScheme.primary)
                                )
                            )
                            .padding(24.dp)
                            .fillMaxWidth()
                    ) {
                        Column {
                            Text("Tổng Số Dư Khả Dụng", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = String.format("%,.0f VNĐ", soDu),
                                color = Color.White,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text("Tổng Thu (▲)", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = String.format("%,.0f đ", tongThu),
                                        color = Color(0xFF81C784),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("Tổng Chi (▼)", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = String.format("%,.0f đ", tongChi),
                                        color = Color(0xFFFF8A80),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
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
                        Text("Nhập Khoản Giao Dịch Mới", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                        // 2.1 Chọn Loại giao dịch (Thu / Chi) dạng Segmented Toggle
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.medium)
                                .padding(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (!isThuNhap) MaterialTheme.colorScheme.error else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { isThuNhap = false }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Khoản Chi (▼)",
                                    color = if (!isThuNhap) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(
                                        if (isThuNhap) MaterialTheme.colorScheme.primary else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { isThuNhap = true }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Khoản Thu (▲)",
                                    color = if (isThuNhap) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
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
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            )
                        )
                        if (inputError != null) {
                            Text(inputError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                        }

                        // Khắc phục lỗi gõ tiếng Việt bằng KeyboardOptions và maxLines = 1
                        OutlinedTextField(
                            value = ghiChu,
                            onValueChange = { ghiChu = it },
                            label = { Text("Ghi chú chi tiết") },
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                capitalization = KeyboardCapitalization.Sentences
                            )
                        )

                        // 2.3 Chọn nhanh Danh mục từ DB bằng Chips (Cuộn Ngang)
                        Text("Chọn Danh Mục:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        if (danhSachDanhMuc.isEmpty()) {
                            Text("Đang tải danh mục...", color = Color.Gray, fontSize = 13.sp)
                        } else {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(danhSachDanhMuc.filter { it.loai == (if (isThuNhap) 1 else -1) }) { dm ->
                                    FilterChip(
                                        selected = danhMucDuocChon?.id == dm.id,
                                        onClick = { danhMucDuocChon = dm },
                                        label = { Text("${dm.bieuTuong} ${dm.tenDanhMuc}") },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    )
                                }
                            }
                        }

                        // 2.4 Cảnh báo ngân sách tức thời
                        if (!isThuNhap && danhMucDuocChon != null) {
                            val tongTienDaChiCungDanhMuc = danhSach
                                .filter { it.idDanhMuc == danhMucDuocChon!!.id }
                                .sumOf { it.soTien }
                            val soTienSapGhi = soTien.toDoubleOrNull() ?: 0.0
                            
                            if (tongTienDaChiCungDanhMuc + soTienSapGhi > danhMucDuocChon!!.nganSachToiDa) {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "⚠️ Vượt ngân sách! Danh mục [${danhMucDuocChon!!.tenDanhMuc}] hạn mức là ${String.format("%,.0fđ", danhMucDuocChon!!.nganSachToiDa)}. Bạn đã tiêu ${String.format("%,.0fđ", tongTienDaChiCungDanhMuc)}.",
                                        color = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.padding(8.dp),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        // 2.5 Nút lưu / Cập nhật
                        if (editingGiaoDich == null) {
                            Button(
                                onClick = {
                                    val parseSoTien = soTien.toDoubleOrNull()
                                    if (parseSoTien == null || parseSoTien <= 0) {
                                        inputError = "Vui lòng nhập số tiền lớn hơn 0"
                                    } else if (ghiChu.isBlank()) {
                                        inputError = "Vui lòng nhập ghi chú"
                                    } else if (danhMucDuocChon == null) {
                                        inputError = "Vui lòng chọn hoặc thêm danh mục"
                                    } else {
                                        viewModel.luuGiaoDich(parseSoTien, ghiChu, danhMucDuocChon!!.id)
                                        soTien = ""
                                        ghiChu = ""
                                        inputError = null
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Save")
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Lưu Giao Dịch", fontWeight = FontWeight.Bold)
                            }
                        } else {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedButton(
                                    onClick = { 
                                        editingGiaoDich = null
                                        soTien = ""
                                        ghiChu = ""
                                        inputError = null
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Text("Hủy")
                                }
                                Button(
                                    onClick = {
                                        val parseSoTien = soTien.toDoubleOrNull()
                                        if (parseSoTien == null || parseSoTien <= 0) {
                                            inputError = "Vui lòng nhập số tiền hợp lệ"
                                        } else if (ghiChu.isBlank()) {
                                            inputError = "Vui lòng nhập ghi chú"
                                        } else if (danhMucDuocChon == null) {
                                            inputError = "Vui lòng chọn danh mục"
                                        } else {
                                            // Tạo bản sao của giao dịch đang sửa và nạp dữ liệu mới vào
                                            val updatedGd = editingGiaoDich!!.copy(
                                                soTien = parseSoTien,
                                                ghiChu = ghiChu,
                                                idDanhMuc = danhMucDuocChon!!.id
                                            )
                                            viewModel.suaGiaoDich(updatedGd)
                                            
                                            // Reset form về trạng thái Thêm Mới
                                            editingGiaoDich = null
                                            soTien = ""
                                            ghiChu = ""
                                            inputError = null
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary), // Màu cam
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Update")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Cập Nhật", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // ================= KHU VỰC 3: danh sách giao dịch gần đây =================
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
                items(danhSach) { gd ->
                    val dm = danhSachDanhMuc.find { it.id == gd.idDanhMuc } ?: DanhMuc(id = 99, tenDanhMuc = "Khác", bieuTuong = "🏷️", loai = -1, nganSachToiDa = 0.0)
                    val isIncome = dm.loai == 1
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            if (isIncome) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                                            RoundedCornerShape(10.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(dm.bieuTuong, fontSize = 20.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = dm.tenDanhMuc, 
                                        fontWeight = FontWeight.Bold, 
                                        fontSize = 15.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = gd.ghiChu, 
                                        color = Color.Gray, 
                                        fontSize = 12.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = (if (isIncome) "+" else "-") + String.format("%,.0fđ", gd.soTien),
                                    color = if (isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                IconButton(
                                    onClick = { 
                                        // Điền ngược dữ liệu lên form nhập liệu ở trên cùng
                                        editingGiaoDich = gd
                                        soTien = gd.soTien.toLong().toString() // Ép sang Long để bỏ số .0 (ví dụ 30000.0 -> 30000)
                                        ghiChu = gd.ghiChu
                                        isThuNhap = isIncome
                                        danhMucDuocChon = dm
                                        inputError = null
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Sửa giao dịch",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                                IconButton(
                                    onClick = { viewModel.xoaGiaoDich(gd) }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Xóa giao dịch",
                                        tint = MaterialTheme.colorScheme.error
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
