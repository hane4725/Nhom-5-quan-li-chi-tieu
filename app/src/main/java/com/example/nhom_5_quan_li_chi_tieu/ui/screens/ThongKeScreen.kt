package com.example.nhom_5_quan_li_chi_tieu.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.nhom_5_quan_li_chi_tieu.data.local.DanhMuc
import com.example.nhom_5_quan_li_chi_tieu.data.local.GiaoDich
import com.example.nhom_5_quan_li_chi_tieu.viewmodel.BudgetViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThongKeScreen(
    viewModel: BudgetViewModel
) {
    // 1. Lắng nghe danh sách giao dịch và danh mục từ ViewModel
    val danhSach by viewModel.danhSachGiaoDich.collectAsState()
    val danhSachDanhMuc by viewModel.danhSachDanhMuc.collectAsState()

    // 2. Tính toán các chỉ số thống kê (Logic Kotlin dựa trên loại danh mục trong DB)
    // Tách riêng danh sách giao dịch chi tiêu (loai == -1)
    val danhSachChiTieu = danhSach.filter { gd ->
        val dm = danhSachDanhMuc.find { it.id == gd.idDanhMuc }
        dm?.loai == -1
    }
    
    val tongSoBanGhi = danhSachChiTieu.size
    val tongChi = danhSachChiTieu.sumOf { it.soTien }
    
    val giaTriTB = if (tongSoBanGhi > 0) tongChi / tongSoBanGhi else 0.0
    val maxChi = if (tongSoBanGhi > 0) danhSachChiTieu.maxOf { it.soTien } else 0.0
    val minChi = if (tongSoBanGhi > 0) danhSachChiTieu.minOf { it.soTien } else 0.0

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
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📊", fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Thống Kê Chi Tiêu",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Phân tích ngân sách của bạn",
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
                                color = MaterialTheme.colorScheme.error
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
                                color = MaterialTheme.colorScheme.error
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
                                color = MaterialTheme.colorScheme.primary
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
                    val biVuotNganSach = dm.nganSachToiDa > 0 && tongChiDanhMuc > dm.nganSachToiDa
                    val phanTramNganSachDaDung = if (dm.nganSachToiDa > 0) (tongChiDanhMuc / dm.nganSachToiDa) else 0.0
                    
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
                                    Text(dm.bieuTuong, fontSize = 22.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = dm.tenDanhMuc,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = if (biVuotNganSach) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
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
                            
                            if (dm.nganSachToiDa > 0) {
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Thanh tiến trình ngân sách
                                LinearProgressIndicator(
                                    progress = phanTramNganSachDaDung.toFloat().coerceAtMost(1f),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp),
                                    color = if (biVuotNganSach) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
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
                                        color = if (biVuotNganSach) MaterialTheme.colorScheme.error else Color.Gray
                                    )
                                    Text(
                                        text = String.format("Hạn mức: %,.0fđ", dm.nganSachToiDa),
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                                
                                if (biVuotNganSach) {
                                    Text(
                                        text = String.format("⚠️ ĐÃ VƯỢT HẠN MỨC NGÂN SÁCH %,.0fđ!", tongChiDanhMuc - dm.nganSachToiDa),
                                        color = MaterialTheme.colorScheme.error,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(top = 8.dp)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Chưa thiết lập hạn mức ngân sách",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
