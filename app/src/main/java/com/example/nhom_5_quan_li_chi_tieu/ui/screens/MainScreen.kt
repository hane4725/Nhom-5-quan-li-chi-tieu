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
import com.example.nhom_5_quan_li_chi_tieu.ui.theme.Black
import com.example.nhom_5_quan_li_chi_tieu.ui.theme.GreenIncome
import com.example.nhom_5_quan_li_chi_tieu.ui.theme.White
import com.example.nhom_5_quan_li_chi_tieu.ui.theme.RedExpense
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.DanhMucUiState
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.GiaoDichUiState
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: BudgetViewModel
) {
    // 1. Quản lý trạng thái nhập liệu & chọn danh mục
    var soTien by rememberSaveable { mutableStateOf("") }
    var ghiChu by rememberSaveable { mutableStateOf("") }
    var isThuNhap by rememberSaveable { mutableStateOf(false) } // False là Chi tiêu, True là Thu nhập

    // Đọc danh mục động từ database
    //val danhSachDanhMuc by viewModel.danhSachDanhMuc.collectAsState()
    var danhMucDuocChon by remember { mutableStateOf<DanhMuc?>(null) }
    var editingGiaoDich by remember { mutableStateOf<GiaoDich?>(null) }
    var giaoDichDangXoa by remember { mutableStateOf<GiaoDich?>(null) }
    var inputError by rememberSaveable { mutableStateOf<String?>(null) }
    //val danhSach by viewModel.danhSachGiaoDich.collectAsState()
    val giaoDichuiState by viewModel.danhSachGiaoDichState.collectAsState()
    val danhSachGiaoDich = if (giaoDichuiState is GiaoDichUiState.Success) {
                (giaoDichuiState as GiaoDichUiState.Success).danhSach //Lấy danh sách ra khỏi hộp
            }else{
                emptyList()
            }
    
    // Trạng thái cuộn của danh sách và Coroutine để kích hoạt cuộn
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val danhMucUiState by viewModel.danhSachDanhMucState.collectAsState()
    val danhSachDanhMuc = if (danhMucUiState is DanhMucUiState.Success) {
        (danhMucUiState as DanhMucUiState.Success).danhSach //Lấy danh sách ra khỏi hộp
    } else {
        emptyList()
    }
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
     val tongThu = danhSachGiaoDich.filter { gd ->
                val dm = danhSachDanhMuc.find { it.id == gd.idDanhMuc }
                dm?.loai == 1
            }.sumOf { it.soTien }

            val tongChi = danhSachGiaoDich.filter { gd ->
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
        when (giaoDichuiState) {
            // TRƯỜNG HỢP A: ĐANG TẢI -> Hiển thị vòng tròn ở giữa màn hình
            is GiaoDichUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
            // TRƯỜNG HỢP B: BỊ LỖI -> Hiển thị chữ báo lỗi màu đỏ
            is GiaoDichUiState.Error -> {
                val thongBaoLoi = (giaoDichuiState as GiaoDichUiState.Error).thongBaoLoi
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues), // ← THÊM DÒNG NÀY để tránh bị TopBar che
                    contentAlignment = Alignment.Center // ← Hiện ra GIỮA màn hình
                ) {
                    Text(
                        text = "⚠️ Lỗi: $thongBaoLoi",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            // TRƯỜNG HỢP C: DỮ LIỆU ĐÃ TẢI XONG -> Hiển thị LazyColumn như cũ
            is GiaoDichUiState.Success -> {

                LazyColumn(
                    state = listState, // Gắn state để có thể điều khiển cuộn
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                     // ================= KHU VỰC 0: Bộ lọc tháng =================
                    item {
                        val thangDuocChon by viewModel.thangDuocChon.collectAsState()
                        
                        // Lấy chuỗi định dạng
                        val chuoiThang = if (thangDuocChon == null) {
                            "Tất cả lịch sử"
                        } else {
                            val sdf = java.text.SimpleDateFormat("MM/yyyy", java.util.Locale.getDefault())
                            "Tháng " + sdf.format(thangDuocChon!!.time)
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Nút Lùi
                            IconButton(
                                onClick = { viewModel.chuyenThang(true) },
                                modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                            ) {
                                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Tháng trước", tint = MaterialTheme.colorScheme.primary)
                            }
                            
                            // Nút hiển thị / Chọn Tất cả
                            TextButton(onClick = { 
                                if (thangDuocChon == null) {
                                    // Chuyển null về tháng hiện tại
                                    viewModel.chuyenThang(false) 
                                } else {
                                    viewModel.xemTatCa()
                                }
                            }) {
                                Text(
                                    text = chuoiThang,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            // Nút Tiến
                            IconButton(
                                onClick = { viewModel.chuyenThang(false) },
                                modifier = Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                            ) {
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Tháng sau", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }


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
                                            colors = listOf(
                                                Color(0xFF1B5E20),
                                                MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    )
                                    .padding(24.dp)
                                    .fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        "Tổng Số Dư Khả Dụng",
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 13.sp
                                    )
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
                                            Text(
                                                "Tổng Thu (▲)",
                                                color = Color.White.copy(alpha = 0.7f),
                                                fontSize = 11.sp
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = String.format("%,.0f đ", tongThu),
                                                color = Color(0xFF81C784),
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                "Tổng Chi (▼)",
                                                color = Color.White.copy(alpha = 0.7f),
                                                fontSize = 11.sp
                                            )
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
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    "Nhập Khoản Giao Dịch Mới",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )

                                // 2.1 Chọn Loại giao dịch (Thu / Chi) dạng Segmented Toggle
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant,
                                            MaterialTheme.shapes.medium
                                        )
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
                                    Text(
                                        inputError!!,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                // Khắc phục lỗi gõ tiếng Việt bằng KeyboardOptions và maxLines = 1
                                OutlinedTextField(
                                    value = ghiChu,
                                    onValueChange = { ghiChu = it },
                                    label = { Text("Ghi chú chi tiết") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3, // Giúp ô nhập liệu phình to ra sẵn (hiển thị 3 dòng)
                                    maxLines = 5, // Cho phép xuống tối đa 5 dòng, gõ thêm nữa thì sẽ có thanh cuộn dọc
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text,
                                        capitalization = KeyboardCapitalization.Sentences
                                    )
                                )

                                // 2.3 Chọn nhanh Danh mục từ DB bằng Chips (Cuộn Ngang)
                                Text(
                                    "Chọn Danh Mục:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                                if (danhSachDanhMuc.isEmpty()) {
                                    Text(
                                        "Đang tải danh mục...",
                                        color = Color.Gray,
                                        fontSize = 13.sp
                                    )
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
                                    val tongTienDaChiCungDanhMuc = danhSachGiaoDich
                                        .filter { it.idDanhMuc == danhMucDuocChon!!.id }
                                        .sumOf { it.soTien }
                                    val soTienSapGhi = soTien.toDoubleOrNull() ?: 0.0

                                    if (tongTienDaChiCungDanhMuc + soTienSapGhi > danhMucDuocChon!!.nganSachToiDa) {
                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(
                                                    0xFFFFF3E0
                                                )
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                text = "⚠️ Vượt ngân sách! Danh mục [${danhMucDuocChon!!.tenDanhMuc}] hạn mức là ${
                                                    String.format(
                                                        "%,.0fđ",
                                                        danhMucDuocChon!!.nganSachToiDa
                                                    )
                                                }. Bạn đã tiêu ${
                                                    String.format(
                                                        "%,.0fđ",
                                                        tongTienDaChiCungDanhMuc
                                                    )
                                                }.",
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
                                                viewModel.luuGiaoDich(
                                                    parseSoTien,
                                                    ghiChu,
                                                    danhMucDuocChon!!.id
                                                )
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
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
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

                    if (danhSachGiaoDich.isEmpty()) {
                        item {
                            Text(
                                "Chưa có giao dịch nào được lưu. Hãy nhập khoản chi đầu tiên!",
                                color = Color.Gray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    } else {
                        items(danhSachGiaoDich) { gd ->
                            val dm = danhSachDanhMuc.find { it.id == gd.idDanhMuc } ?: DanhMuc(id = 99, tenDanhMuc = "Khác", bieuTuong = "🏷️", loai = -1, nganSachToiDa = 0.0)
                            val isIncome = dm.loai == 1
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(24.dp),
                                colors = CardDefaults.cardColors(containerColor = GreenIncome)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                // 1. Ô ICON
                                    Box(
                                        modifier = Modifier
                                            .size(70.dp)
                                            .background(White, RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(dm.bieuTuong, fontSize = 30.sp)
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                // 2. CỘT GIỮA (Tên, Số tiền, Ghi chú)
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                // Ô Tên Danh Mục
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(40.dp)
                                                    .background(White, RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = dm.tenDanhMuc,
                                                    color = Color.DarkGray,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(8.dp))
                                    // Ô Số Tiền
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .height(40.dp)
                                                    .background(White, RoundedCornerShape(8.dp))
                                                    .padding(horizontal = 8.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = (if (isIncome) "+" else "-") + String.format("%,.0fđ", gd.soTien),
                                                    color = if (isIncome) GreenIncome else RedExpense,
                                                    fontSize = 8.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Ô Ghi Chú (Hàng dưới)
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(40.dp)
                                                .background(White, RoundedCornerShape(8.dp))
                                                .padding(horizontal = 12.dp, vertical = 8.dp), // Thêm padding dọc cho đẹp
                                            contentAlignment = Alignment.TopStart // Đẩy chữ lên góc trên
                                        ) {
                                            Text(
                                                text = gd.ghiChu,
                                                fontSize = 13.sp, // Phóng to chữ lên
                                                color = Color.DarkGray,
                                                maxLines = 2,
                                                overflow = TextOverflow.Ellipsis,
                                                lineHeight = 18.sp // Tạo khoảng cách giữa 2 dòng nếu có
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(8.dp))

                                    // 3. CỘT NÚT BẤM (Xóa, Sửa)
                                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    // Nút Xóa
                                        Box(
                                            modifier = Modifier
                                                .size(width = 60.dp, height = 40.dp)
                                                .background(White, RoundedCornerShape(8.dp))
                                                .clickable { giaoDichDangXoa = gd },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Xóa", color = RedExpense, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                        // Nút Sửa
                                        Box(
                                            modifier = Modifier
                                                .size(width = 60.dp, height = 40.dp)
                                                .background(White, RoundedCornerShape(8.dp))
                                                .clickable {
                                                    editingGiaoDich = gd
                                                    soTien = gd.soTien.toLong().toString()
                                                    ghiChu = gd.ghiChu
                                                    isThuNhap = isIncome
                                                    danhMucDuocChon = dm
                                                    inputError = null
                                                    // Cuộn ngược lên form nhập liệu (vị trí số 2)
                                                    coroutineScope.launch {
                                                        listState.animateScrollToItem(2)
                                                    }
                                                },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text("Sửa", color = GreenIncome, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            else->{}
        }
    }

    // ================= DIALOG CẢNH BÁO XÓA GIAO DỊCH =================
    if (giaoDichDangXoa != null) {
        AlertDialog(
            onDismissRequest = { giaoDichDangXoa = null },
            title = { 
                Text(
                    text = "Xác nhận xóa giao dịch", 
                    fontWeight = FontWeight.Bold, 
                    color = MaterialTheme.colorScheme.error
                ) 
            },
            text = { 
                Text(
                    text = "Bạn có chắc chắn muốn xóa giao dịch [${giaoDichDangXoa!!.ghiChu}] với số tiền ${String.format("%,.0fđ", giaoDichDangXoa!!.soTien)} không?",
                    lineHeight = 20.sp
                ) 
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.xoaGiaoDich(giaoDichDangXoa!!)
                        giaoDichDangXoa = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Xóa Ngay")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { giaoDichDangXoa = null }) {
                    Text("Hủy Bỏ")
                }
            }
        )
    }
}
