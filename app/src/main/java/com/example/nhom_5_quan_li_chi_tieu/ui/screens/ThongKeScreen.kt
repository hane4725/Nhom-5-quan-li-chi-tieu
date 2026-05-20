package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nhom_5_quan_li_chi_tieu.data.GiaoDich
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThongKeScreen(
    viewModel: BudgetViewModel,
    onBackClick: () -> Unit
) {
    // 1. Lắng nghe danh sách giao dịch từ ViewModel
    val danhSach by viewModel.danhSachGiaoDich.collectAsState()
    
    // Danh mục giả lập tương ứng với MainScreen
    val danhSachDanhMuc = listOf(
        MockDanhMuc(1, "Ăn uống", "🍔", -1, 2000000.0),
        MockDanhMuc(2, "Đi lại", "🚗", -1, 500000.0),
        MockDanhMuc(3, "Mua sắm", "🛍️", -1, 1500000.0),
        MockDanhMuc(4, "Lương", "💵", 1, 0.0),
        MockDanhMuc(5, "Khác", "🏷️", -1, 1000000.0)
    )

    // 2. Tính toán các chỉ số thống kê (Logic Kotlin)
    // Tách riêng danh sách chi tiêu (không bao gồm Lương/Thu nhập)
    val danhSachChiTieu = danhSach.filter { it.idDanhMuc != 4 }
    
    val tongSoBanGhi = danhSachChiTieu.size
    val tongChi = danhSachChiTieu.sumOf { it.soTien }
    
    val giaTriTB = if (tongSoBanGhi > 0) tongChi / tongSoBanGhi else 0.0
    val maxChi = if (tongSoBanGhi > 0) danhSachChiTieu.maxOf { it.soTien } else 0.0
    val minChi = if (tongSoBanGhi > 0) danhSachChiTieu.minOf { it.soTien } else 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thống Kê Chi Tiêu", fontWeight = FontWeight.Bold) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ================= KHU VỰC 1: chỉ số cực trị (Tổng, TB, Max, Min) =================
            item {
                Text(
                    text = "Chỉ Số Chi Tiêu Cốt Lõi",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tổng số lần chi:", color = Color.Gray)
                            Text("$tongSoBanGhi lần", fontWeight = FontWeight.Bold)
                        }
                        
                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Tổng tiền chi tiêu:", color = Color.Gray)
                            Text(
                                text = String.format("%,.0fđ", tongChi),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828)
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Trung bình mỗi lần chi:", color = Color.Gray)
                            Text(
                                text = String.format("%,.0fđ", giaTriTB),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Khoản chi lớn nhất (Max):", color = Color.Gray)
                            Text(
                                text = String.format("%,.0fđ", maxChi),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC62828)
                            )
                        }

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Khoản chi nhỏ nhất (Min):", color = Color.Gray)
                            Text(
                                text = String.format("%,.0fđ", minChi),
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2E7D32)
                            )
                        }
                    }
                }
            }

            // ================= KHU VỰC 2: phần trăm & ngân sách theo danh mục =================
            item {
                Text(
                    text = "Chi Tiêu Theo Danh Mục & Ngân Sách",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            val danhMucChi = danhSachDanhMuc.filter { it.loai == -1 }
            
            if (tongChi == 0.0) {
                item {
                    Text("Chưa có dữ liệu chi tiêu để tính phần trăm.", color = Color.Gray)
                }
            } else {
                items(danhMucChi) { dm ->
                    val tongChiDanhMuc = danhSach.filter { it.idDanhMuc == dm.id }.sumOf { it.soTien }
                    val phanTram = if (tongChi > 0) (tongChiDanhMuc / tongChi) * 100 else 0.0
                    
                    // Kiểm tra xem có vượt quá ngân sách tối đa đặt trước không
                    val biVuotNganSach = tongChiDanhMuc > dm.nganSach
                    val phanTramNganSachDaDung = if (dm.nganSach > 0) (tongChiDanhMuc / dm.nganSach) else 0.0
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (biVuotNganSach) Color(0xFFFFEBEE) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(dm.icon, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = dm.ten,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = if (biVuotNganSach) Color(0xFFC62828) else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = String.format("%,.0fđ", tongChiDanhMuc),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = String.format("%.1f%% tổng chi", phanTram),
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Thanh tiến trình ngân sách
                            LinearProgressIndicator(
                                progress = phanTramNganSachDaDung.toFloat().coerceAtMost(1f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp),
                                color = if (biVuotNganSach) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = String.format("Đã dùng: %.1f%% ngân sách", phanTramNganSachDaDung * 100),
                                    fontSize = 11.sp,
                                    color = if (biVuotNganSach) Color(0xFFC62828) else Color.Gray
                                )
                                Text(
                                    text = String.format("Hạn mức: %,.0fđ", dm.nganSach),
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                            
                            if (biVuotNganSach) {
                                Text(
                                    text = String.format("⚠️ ĐÃ VƯỢT HẠN MỨC NGÂN SÁCH %,.0fđ!", tongChiDanhMuc - dm.nganSach),
                                    color = Color(0xFFD32F2F),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
