# 🎵 Hãng Đĩa Thời Đại 🎶

Chào mừng bạn đến với **Hãng Đĩa Thời Đại**, một nền tảng thương mại điện tử mã nguồn mở chuyên bán các loại đĩa nhạc—CD, băng cát-sét, và đĩa vinyl! Dù bạn là nhà sưu tầm hay chỉ đơn giản muốn tận hưởng những giai điệu yêu thích, nền tảng này sẽ mang đến cho bạn trải nghiệm tiện lợi nhất.

---

## 🌟 Tính năng nổi bật

- **Chức năng theo vai trò**
    - **Admin - Quản trị viên**:
        - Quản lý danh sách sản phẩm, danh sách người dùng (thêm, cập nhật, xóa).
        - Điều hành các phiếu giảm giá và chương trình khuyến mãi.
        - Theo dõi trạng thái sản phẩm (đang chờ xử lý, đã giao hàng, v.v.).
    - **User - Người dùng**:
        - Xem sản phẩm, xem chi tiết sản phẩm, thích sản phẩm.
        - Duyệt và mua sản phẩm.
        - Quản lý đơn hàng cá nhân.
        - Thanh toán, đánh giá sản phẩm, yêu cầu hoàn tiền - trả hàng.
    - **Seller - Người bán**:
        - Có tất cả các quyền của User.
        - Theo dõi doanh thu.
        - Tạo phiếu giảm giá và quản lý trạng thái sản phẩm.
        - Theo dõi trạng thái sản phẩm (đang chờ xử lý, đã giao hàng, v.v.).
    - **Shipper - Người giao hàng**:
        - Cập nhật trạng thái vận chuyển sản phẩm.
        - Quản lý đơn hàng được phân công, thống kê đơn hàng được phân giao.

- **Danh mục sản phẩm đa dạng**
    - Cung cấp nhiều thể loại nhạc với các định dạng khác nhau (CD, băng cát-sét, đĩa vinyl).

- **Hệ thống phiếu giảm giá**
    - Hỗ trợ tích hợp các chương trình giảm giá và khuyến mãi.

- **Theo dõi đơn hàng**
    - Cập nhật trạng thái đơn hàng theo thời gian thực.

- **Hỗ trợ cơ sở dữ liệu**
    - Tương thích với cả **SQL Server** và **MySQL**.

---

## 🚀 Công nghệ sử dụng

- **Backend**: [Spring MVC](https://spring.io/projects/spring-framework)
- **Frontend**: [Bootstrap](https://getbootstrap.com/) để tạo giao diện thân thiện và phản hồi tốt.
- **Cơ sở dữ liệu**:
    - **Chính**: SQL Server
    - **Phụ**: MySQL

---

## 📦 Hướng dẫn cài đặt

1. **Clone kho lưu trữ**
   ```bash  
   git clone git@github.com:daley12306/HangDiaThoiDai.git  
   cd HangDiaThoiDai
   ```
2. **Cấu hình cơ sở dữ liệu**
- Cài đặt cơ sở dữ liệu SQL Server hoặc MySQL.
- Cập nhật thông tin kết nối cơ sở dữ liệu trong file `application.properties`.

3. **Xây dựng dự án**
- Đảm bảo bạn đã cài đặt **Java** và **Maven**

4. **Chạy ứng dụng**
```bash
mvn spring-boot:run
```

5. **Truy cập ứng dụng**
   Mở trình duyệt và truy cập:
```
http://localhost:8081
```

---

## 🛠 Hướng dẫn sử dụng

### Admin - Quản trị viên
- Đăng nhập bằng tài khoản quản trị.
- Truy cập bảng điều khiển để quản lý sản phẩm, phiếu giảm giá và đơn hàng.

### User - Người dùng
- Duyệt danh mục sản phẩm và thêm đĩa nhạc vào giỏ hàng.
- Thanh toán và theo dõi trạng thái đơn hàng.

### Seller - Người bán
- Quản lý doanh thu, phiếu giảm giá và trạng thái sản phẩm.

### Shipper - Người giao hàng
- Cập nhật thông tin vận chuyển cho đơn hàng.

---

## 🌍 Đóng góp

Chúng tôi hoan nghênh các đóng góp để làm cho Hãng Đĩa Thời Đại ngày càng tốt hơn! Để đóng góp:

1. Fork repository này.
2. Tạo một nhánh mới cho tính năng hoặc bản sửa lỗi của bạn.
3. Commit thay đổi và đẩy chúng lên nhánh của bạn.
4. Tạo một pull request với mô tả chi tiết về thay đổi.

---

## 🙏 Lời cảm ơn

- **Spring Framework** là một kiến trúc backend mạnh mẽ.
- **Bootstrap** giúp giao diện nền tảng đẹp mắt và dễ sử dụng.
- Cộng đồng mã nguồn mở vì những đóng góp và nguồn cảm hứng!

---

Chúc bạn code vui vẻ! 💻🎧
```
Hãy tự nhiên phát triển dự án này trong tương lai! 😊