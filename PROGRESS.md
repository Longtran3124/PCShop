# Tiến độ dự án PCShop

> Cập nhật lần cuối: 2026-10-24

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
- [x] **Validation đầu vào:** `@Valid` ở create/update + `@NotBlank`/`@NotNull`/`@Min` trong các request DTO
- [x] **Tách DTO (không lộ entity ra API):** `ProductCreateRequest`, `ProductUpdateRequest`, `ProductFilter`, `ProductResponse` + `ProductMapper`. Toàn bộ controller/service đã trả `ProductResponse`.
- [x] **Phân trang + sắp xếp + lọc:** `GET /api/products/search-filter?page=&size=&sort=&minPrice=&maxPrice=&brandId=&categoryId=&inStock=` — `ProductSpecification` + `Pageable`, repo `extends JpaSpecificationExecutor`, trả `Page<ProductResponse>`. Đã bật `@EnableSpringDataWebSupport(VIA_DTO)`. (verify OK)
- [x] **Dữ liệu test:** đã seed thêm 12 sản phẩm SKU `TEST-*` phủ các ca biên (INACTIVE, DRAFT, hết hàng, xóa mềm ở nhiều mốc thời gian, hàng giá rẻ). Xóa sạch bằng `DELETE FROM products WHERE sku LIKE 'TEST-%'`.

---

## 🚧 Đang làm / Dự kiến tiếp theo

- [ ] **(tiếp theo)** Xử lý lỗi chuẩn (thay `RuntimeException` trong `getEntityById` bằng exception riêng + `@ControllerAdvice` trả JSON lỗi gọn, 404 khi không tìm thấy) — *để sau, dự án còn mới chưa cần*
- [ ] Entity `Category`, `Brand`, `Series` (hiện đang map tạm bằng `categoryId`, `brandId`...)
- [ ] Xác thực / phân quyền (Spring Security) cho các API admin

---

## 🗺️ Roadmap bổ sung API cho Product (làm theo thứ tự ưu tiên)

> Hiện có 9 endpoint (CRUD + search + activate/deactivate) — mới là lõi cơ bản. Module Product thực tế thường 20–30 endpoint. Làm dần theo thứ tự dưới, xong mục nào tích mục đó.

**🔴 Ưu tiên cao**
- [x] Phân trang + sắp xếp + lọc: `GET /products/search-filter?page=&size=&sort=&minPrice=&maxPrice=&brandId=&categoryId=&inStock=` — `ProductFilter` + `ProductSpecification` + `Pageable`, repo `extends JpaSpecificationExecutor`. Trả `Page<ProductResponse>`. Đã bật `@EnableSpringDataWebSupport(VIA_DTO)` cho JSON phân trang ổn định. (verify OK: 42 sản phẩm, 5 trang)
- [ ] `GET /products/slug/{slug}` — lấy sản phẩm theo slug (tận dụng slug đã sinh, cho URL thân thiện)
- [ ] `POST /products/{id}/restore` — khôi phục hàng đã xóa mềm (`deletedAt = null`)
- [ ] `GET /products/trash` — xem danh sách hàng đã xóa mềm (admin)

**🟡 Ưu tiên vừa**
- [ ] Gallery nhiều ảnh: `POST /products/{id}/images`, `DELETE /products/{id}/images/{imageId}` (bảng `product_images`)
- [ ] Thông số kỹ thuật: `PUT /products/{id}/specifications` (CPU/RAM/VGA... — bắt buộc với shop PC)
- [ ] Quản lý tồn kho riêng: `PATCH /products/{id}/stock`
- [ ] Đổi giá riêng + log lịch sử giá: `PATCH /products/{id}/price`
- [ ] `GET /products/low-stock` — cảnh báo sắp hết hàng

**⚪ Ưu tiên thấp (khi hệ thống đã ổn định)**
- [ ] Review/rating: `GET` + `POST /products/{id}/reviews`
- [ ] Khối hiển thị trang chủ: `/featured`, `/best-sellers`, `/newest`, `/{id}/related`
- [ ] Vận hành admin: import/export Excel, sửa/xóa hàng loạt (bulk)
- [ ] Đếm lượt xem, wishlist, so sánh sản phẩm

---

## 📝 Ghi chú / Việc cần dọn

- [ ] `Product.java` dòng 77: comment bị dính chữ thừa `đã xóa mềmcategories` → nên sửa lại thành `đã xóa mềm`
- [ ] Trong `ProductServiceImp` có cả `@Autowired` lẫn constructor injection (`@RequiredArgsConstructor`) — nên bỏ `@Autowired`, dùng constructor injection
- [ ] Xóa import thừa `entity.Product` ở `ProductService.java` và `ProductController.java` (sau khi chuyển hết sang DTO thì 2 chỗ này không còn dùng `Product`)
- [ ] WARN `spring.jpa.open-in-view is enabled by default` khi khởi động — nên set `spring.jpa.open-in-view: false` trong `application.yaml` để tắt (tránh query DB lúc render view). Để sau, chưa ảnh hưởng.

---

## 🔄 Quy trình làm việc luân phiên 2 máy

1. **Bắt đầu:** `Fetch` → `Pull` để lấy code mới nhất
2. **Code xong:** `Commit` → `Push`
3. **Trước khi rời máy:** đảm bảo đã `Push` hết
