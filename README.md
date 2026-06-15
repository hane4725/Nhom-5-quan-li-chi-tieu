# 💰 BudgetBuddy - Ứng Dụng Quản Lý Chi Tiêu Thông Minh

## 📌 Giới Thiệu
BudgetBuddy là một ứng dụng quản lý tài chính cá nhân dành cho nền tảng Android. Ứng dụng giúp người dùng dễ dàng ghi chép các khoản thu/chi, phân loại theo danh mục, theo dõi biểu đồ thống kê trực quan và cập nhật tin tức tài chính theo thời gian thực.

## 🚀 Công Nghệ Sử Dụng
- **Ngôn ngữ:** Kotlin
- **Giao diện:** Jetpack Compose (Material Design 3)
- **Kiến trúc:** MVVM (Model - View - ViewModel) + Repository Pattern
- **Cơ sở dữ liệu Local:** Room Database (SQLite)
- **Lưu trữ cài đặt:** Jetpack DataStore Preferences
- **Gọi mạng (API):** Retrofit & Gson
- **Xử lý bất đồng bộ:** Kotlin Coroutines & Flow
- **Tải ảnh:** Coil

## ⭐ Tính Năng Chính
1. **Quản lý Thu/Chi:** Thêm, sửa, xóa giao dịch tài chính.
2. **Quản lý Danh Mục:** Tùy biến phân loại dòng tiền, cảnh báo giới hạn ngân sách.
3. **Thống Kê:** Tự động tính toán số dư khả dụng, tổng thu chi, hiển thị biểu đồ phần trăm (Pie Chart).
4. **Tin Tức Tài Chính:** Lấy tin tức thị trường tự động từ Internet thông qua NewsAPI.
5. **Cơ chế an toàn:** Tự động dọn dẹp "giao dịch mồ côi" khi xóa danh mục, sao lưu dữ liệu (Backup/Restore) định dạng JSON.
6. **Cá nhân hóa:** Hỗ trợ chế độ Sáng/Tối (Dark Mode).

## 🛠️ Hướng Dẫn Cài Đặt (Dành cho Developer)
1. Clone dự án về máy tính qua Git hoặc tải mã nguồn (ZIP).
2. Mở dự án bằng **Android Studio** (Bản mới nhất có hỗ trợ Jetpack Compose).
3. Đợi Gradle đồng bộ (Sync) và cài đặt các thư viện cần thiết.
4. Bấm **Run (Shift + F10)** để chạy trên máy ảo hoặc thiết bị thật.

## 📱 Build file APK
File cài đặt APK nằm tại đường dẫn: `app/build/outputs/apk/debug/app-debug.apk`

---
*Dự án thực hiện bởi Nhóm 5*
