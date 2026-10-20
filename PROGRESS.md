# Tiến độ dự án PCShop

> Cập nhật lần cuối: 2026-10-20

Dự án web bán linh kiện/máy tính (Spring Boot + MySQL). File này ghi lại tiến độ để tiện theo dõi khi làm luân phiên giữa laptop và PC.

---

## 🧱 Thông tin chung

| Mục | Giá trị |
|-----|---------|
| Ngôn ngữ / Framework | Java + Spring Boot |
| Database | MySQL (`localhost:3306/pcshop`) |
| Cổng chạy app | `8111` |
| GitHub | https://github.com/Longtran3124/PCShop |
| Nhánh chính | `main` |

> ⚠️ File `src/main/resources/application.yaml` chứa mật khẩu DB riêng của từng máy.
> Đã dùng `git update-index --skip-worktree` để git **không theo dõi** thay đổi cục bộ của file này.
> Khi sang máy mới, chạy lại: `git update-index --skip-worktree src/main/resources/application.yaml`

---

## ✅ Đã hoàn thành

### Module Sản phẩm (Product)
- [x] Entity `Product` với đầy đủ field (sku, name, giá, tồn kho, trạng thái, soft-delete, audit...)
- [x] Enum `ProductStatus`: `DRAFT`, `ACTIVE`, `INACTIVE`
- [x] **CRUD sản phẩm:**
  - [x] `GET /api/products/{id}` — lấy chi tiết (đã lọc hàng xóa mềm)
  - [x] `GET /api/products/search?keyword=...` — tìm theo tên
  - [x] `POST /api/products` — tạo mới (trả 201)
  - [x] `PUT /api/products/{id}` — cập nhật (chỉ sửa field client gửi)
  - [x] `DELETE /api/products/{id}` — xóa mềm (`deletedAt = now()`)
- [x] **Danh sách phân quyền:**
  - [x] `GET /api/products` — khách: chỉ hàng còn bán
  - [x] `GET /api/products/all` — admin: xem tất cả kể cả hàng đã xóa
- [x] **Quản lý trạng thái:**
  - [x] `PATCH /api/products/{id}/activate` — mở bán
  - [x] `PATCH /api/products/{id}/deactivate` — ngừng bán
- [x] Repository lọc hàng đã xóa mềm (`deletedAt IS NULL`)

---

## 🚧 Đang làm / Dự kiến tiếp theo

- [ ] Xử lý lỗi chuẩn (thay `RuntimeException` bằng exception + `@ControllerAdvice` trả JSON lỗi gọn)
- [ ] Validation dữ liệu đầu vào (`@Valid`, bắt buộc field, giá > 0...)
- [ ] Dùng DTO thay vì trả thẳng Entity ra API (tách lớp request/response)
- [ ] Entity `Category`, `Brand`, `Series` (hiện đang map tạm bằng `categoryId`, `brandId`...)
- [ ] Phân trang + sắp xếp cho danh sách sản phẩm (`Pageable`)
- [ ] Xác thực / phân quyền (Spring Security) cho các API admin

---

## 📝 Ghi chú / Việc cần dọn

- [ ] `Product.java` dòng 77: comment bị dính chữ thừa `đã xóa mềmcategories` → nên sửa lại thành `đã xóa mềm`
- [ ] Trong `ProductServiceImp` có cả `@Autowired` lẫn constructor injection (`@RequiredArgsConstructor`) — nên thống nhất 1 kiểu

---

## 🔄 Quy trình làm việc luân phiên 2 máy

1. **Bắt đầu:** `Fetch` → `Pull` để lấy code mới nhất
2. **Code xong:** `Commit` → `Push`
3. **Trước khi rời máy:** đảm bảo đã `Push` hết
