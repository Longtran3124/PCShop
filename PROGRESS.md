# Tiến độ dự án PCShop

> Cập nhật lần cuối: 2026-10-26

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

- [ ] **(ĐANG LÀM)** Entity `Category`, `Brand`, `Series` + CRUD (quyết định: làm "chiều rộng" các bảng cốt lõi trước, rồi quay lại hoàn thiện Product sau).
  - Bảng `categories`, `brands`, `series` **đã có sẵn trong DB** nhưng code chưa có entity.
  - Làm theo 4 tầng như Product: Entity → Repository → Service → Controller.
  - [x] **Category XONG** (full CRUD + slug + trash + restore, verify OK). Khác Product: `is_active` (Boolean) thay status enum; KHÔNG có `@Version`; có `parent_id` tự trỏ (tạm map `Long parentId`). API tại `/api/categories`.
  - [x] **Brand XONG** (full CRUD + slug + trash + restore, verify OK). Đơn giản hơn Category: chỉ `name`, `slug`, `logoUrl` + soft-delete + audit (không parent/sortOrder/isActive/version). API tại `/api/brands`.
  - [ ] **Series**.
  - Nâng cấp sau: đổi `categoryId`/`brandId` trong Product thành quan hệ `@ManyToOne` thật.

### 🗺️ Bổ sung API cho Category (làm SAU, khi đã có quan hệ @ManyToOne Product↔Category)
> Category có cấu trúc CÂY (parent_id) nên API đặc trưng là nhóm duyệt cây. Thứ tự ưu tiên:
- [ ] 🔴 `GET /categories/{id}/products` — sản phẩm thuộc danh mục (kèm phân trang). Cần quan hệ Product↔Category trước cho gọn.
- [ ] 🔴 `GET /categories/roots` (danh mục gốc, parentId=null) + `GET /categories/{id}/children` (con trực tiếp) — dựng menu.
- [ ] 🟡 `GET /categories/tree` (cả cây lồng nhau) + `GET /categories/{id}/breadcrumb` (đường dẫn gốc→node).
- [ ] 🟡 Ràng buộc nghiệp vụ: CHẶN xóa danh mục còn chứa sản phẩm/còn con; CHẶN vòng lặp cha-con (A→B→A).
- [ ] ⚪ Tiện ích admin: `PATCH /{id}/move` (đổi cha), `PATCH /reorder` (sắp sortOrder), `PATCH /{id}/toggle-active`, `GET /{id}/product-count`.

### 🗺️ Bổ sung API cho Brand (làm SAU, khi đã có quan hệ @ManyToOne Product↔Brand)
> Brand "phẳng" (không cây) nên ít API đặc thù hơn Category:
- [ ] 🔴 `GET /brands/{id}/products` — sản phẩm của 1 hãng (kèm phân trang). Cần quan hệ Product↔Brand trước.
- [ ] 🟡 Upload logo thật: `POST /brands/{id}/logo` (upload file thay vì chỉ nhận `logoUrl` text) + `DELETE /brands/{id}/logo`. Làm chung đợt với Product gallery.
- [ ] 🟡 `GET /brands?page=&size=&sort=` (phân trang danh sách) + `GET /brands/featured` (hãng nổi bật trang chủ) + `GET /brands/{id}/product-count`.
- [ ] 🟡 Ràng buộc: CHẶN xóa thương hiệu còn chứa sản phẩm.

### 🔔 Để sau (đã bàn, hoãn lại có chủ đích)
- [ ] Xử lý lỗi chuẩn: thay `RuntimeException` (trong `getEntityById`, `getBySlug`, `restore`...) bằng exception riêng + `@ControllerAdvice` trả JSON lỗi gọn, **404** khi không tìm thấy thay vì 500. *Nhỏ, thêm lúc nào cũng được — làm sau khi xong các entity cốt lõi.*
- [ ] Xác thực / phân quyền (Spring Security) cho các API admin.
- [ ] Nhóm 🟡 (gallery ảnh, thông số kỹ thuật, tồn kho/giá riêng) — xem roadmap dưới. Làm SAU khi có entity Category/Brand.

---

## 🗺️ Roadmap bổ sung API cho Product (làm theo thứ tự ưu tiên)

> Hiện có 9 endpoint (CRUD + search + activate/deactivate) — mới là lõi cơ bản. Module Product thực tế thường 20–30 endpoint. Làm dần theo thứ tự dưới, xong mục nào tích mục đó.

**🔴 Ưu tiên cao**
- [x] Phân trang + sắp xếp + lọc: `GET /products/search-filter?page=&size=&sort=&minPrice=&maxPrice=&brandId=&categoryId=&inStock=` — `ProductFilter` + `ProductSpecification` + `Pageable`, repo `extends JpaSpecificationExecutor`. Trả `Page<ProductResponse>`. Đã bật `@EnableSpringDataWebSupport(VIA_DTO)` cho JSON phân trang ổn định. (verify OK: 42 sản phẩm, 5 trang)
- [x] `GET /products/slug/{slug}` — lấy sản phẩm theo slug (repo `findBySlugAndDeletedAtIsNull` + service `getBySlug`). Verify OK bằng PowerShell. *Lưu ý khi test Postman: coi chừng ký tự `%0A` (Enter thừa) dính vào cuối URL → 500 không tìm thấy.*
- [x] `POST /products/{id}/restore` — khôi phục hàng đã xóa mềm (`deletedAt = null`). Dùng `findById` gốc (không phải `getEntityById`), có check `if deletedAt == null` chặn restore hàng đang sống. Verify OK bằng PowerShell.
- [x] `GET /products/trash` — danh sách hàng đã xóa mềm (admin), repo `findByDeletedAtIsNotNull`. Verify OK (kịch bản trash → restore → trash). *Bug đã gặp & sửa: khai báo `@GetMapping("/Trash")` T hoa → gọi `/trash` t thường không khớp, rơi nhầm vào `/{id}` → 400. URL phân biệt hoa-thường, luôn viết thường.*

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
