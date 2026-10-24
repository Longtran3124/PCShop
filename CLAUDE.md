# CLAUDE.md — PCShop

Web bán linh kiện/máy tính. Spring Boot + MySQL. Xem tiến độ chi tiết ở `PROGRESS.md`.

## Quy ước làm việc với Claude

- Khi hướng dẫn: **chỉ giảng giải + đưa code mẫu**, KHÔNG tự tạo/sửa file. Chỉ trực tiếp sửa code khi người dùng ra lệnh rõ ràng.
- Giải thích bằng tiếng Việt, kèm insight ngắn về lý do chọn cách làm.

## 🔔 Việc làm sau — cần chủ động NHẮC người dùng khi tới thời điểm

- **Khi nào bắt đầu viết giao diện (frontend):** nhắc người dùng chuyển các câu báo lỗi validation sang `messages.properties` (i18n).
  - Hiện tại: message viết thẳng tiếng Việt trong annotation, ví dụ `@NotBlank(message = "SKU không được để trống")`. Cách này ổn khi đang học/chưa có UI.
  - Khi có UI thật: tách sang `src/main/resources/messages.properties` với dạng `@NotBlank(message = "{product.sku.required}")` để gom thông báo 1 chỗ, dễ sửa và hỗ trợ đa ngôn ngữ.

- **Nâng cấp sinh `slug` sản phẩm:** hiện `generateSlug()` trong `ProductServiceImp` chỉ `toLowerCase` + thay khoảng trắng bằng `-`. Còn thiếu:
  - Chống trùng slug (vì cột `slug` là `unique`): 2 sản phẩm trùng tên sẽ gây lỗi khi save → cần thêm hậu tố (`-2`, hoặc gắn SKU/id).
  - Bỏ dấu tiếng Việt: `"Chuột không dây"` nên ra `chuot-khong-day` thay vì giữ nguyên dấu.
