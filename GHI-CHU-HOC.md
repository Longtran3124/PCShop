# Ghi chú học - Kiến trúc & luồng dữ liệu PCShop

> File này ghi lại các khái niệm đã học để xem lại. Không phải tài liệu kỹ thuật chính thức.

---

## 1. Luồng dữ liệu tổng thể

```
Người dùng  ⇄  Controller  ⇄  Service  ⇄  Database
                   │            │
              (DTO ở đây)   ProductMapper: DTO ⇄ Entity
                                 │
                            (Entity ở đây)
```

- **Controller** = người điều phối: nhận request, cầm DTO đưa cho Service, rồi gửi kết quả về cho người dùng.
- **Service** = nơi xử lý nghiệp vụ (lưu DB, kiểm tra điều kiện...).
- **DTO** = hộp chứa dữ liệu (bị động, không tự đi đâu, không chứa logic).
- **Entity** = ảnh chiếu của bảng trong DB.
- **Mapper** = thợ chép dữ liệu, chuyển qua lại giữa DTO và Entity.

**Luồng 1 request (ví dụ tạo sản phẩm):**
1. Người dùng gửi request.
2. Controller nhận → Spring đổ dữ liệu vào **request DTO** (`ProductCreateRequest`).
3. Controller cầm request DTO, gọi Service.
4. Service dùng Mapper đổi DTO → Entity, lưu DB, rồi đổi Entity → **response DTO**.
5. Service trả response DTO về Controller.
6. Controller gửi response DTO về cho người dùng.

---

## 2. DTO (Data Transfer Object)

- Chữ T = **Transfer** (vận chuyển). DTO = hộp đựng dữ liệu để chở giữa các tầng.
- Đi **2 chiều**:
  - **Request DTO** (dữ liệu VÀO): `ProductCreateRequest`, `ProductUpdateRequest`, `ProductFilter` — như tờ form khách điền.
  - **Response DTO** (dữ liệu RA): `ProductResponse` — như phiếu kết quả server trả về.
- Dấu hiệu DTO là "đồ bị động": mở class ra chỉ thấy **field + getter/setter**, KHÔNG có logic.
- Vì sao cần DTO thay vì trả thẳng Entity:
  - Request DTO: chỉ nhận field khách được phép gửi → chặn **mass-assignment** (khách không set được `id`, `status`, `version`...).
  - Response DTO: giấu field nội bộ (`version`, `createdBy`, `deletedAt`).

---

## 3. ProductFilter + ProductSpecification (lọc động)

- **ProductFilter**: hộp phía server dùng để HỨNG dữ liệu lọc khách gửi qua URL.
  - Spring tự "đổ" query param vào field cùng tên (data binding). Param không gửi → field = null.
  - Ví dụ: `?brandId=1&minPrice=20000000` → `filter.brandId=1`, `filter.minPrice=20000000`.
- **ProductSpecification**: đọc ProductFilter, dựng câu WHERE động.
  - Bản chất = phiên bản an toàn của việc "nối câu WHERE bằng if":
    ```
    if (brandId != null)  → thêm điều kiện brand_id = ...
    if (minPrice != null) → thêm điều kiện sale_price >= ...
    ```
  - Field nào khách không điền (null) → bỏ qua, không thêm điều kiện.
  - Điều kiện `deleted_at IS NULL` LUÔN có (không nằm trong if) → luôn ẩn hàng đã xóa mềm.
  - `root` = bảng products (để chỉ vào cột), `cb` = công cụ tạo điều kiện an toàn (chống SQL injection), `Predicate` = 1 mảnh điều kiện.
- Specification chỉ **mô tả** điều kiện; **DB** mới là nơi thực sự lọc từng dòng (nhanh vì có index).

---

## 4. Pageable + Page (phân trang)

- **Pageable**: lo phần `ORDER BY` (sort) + `LIMIT` (page, size). Không nằm trong WHERE.
  - `sort=salePrice,asc` → `ORDER BY sale_price ASC` (dùng TÊN FIELD java, không phải tên cột).
  - `page=0, size=5` → `LIMIT 0, 5`.
- **Page<T>**: kết quả 1 trang = danh sách + metadata (`totalElements`, `totalPages`...).
  - Phân trang chạy 2 câu SQL: 1 câu lấy dữ liệu (LIMIT) + 1 câu COUNT(*) để biết tổng.
- `.map(ProductMapper::toResponse)` trên Page → đổi từng Product thành ProductResponse mà vẫn giữ metadata.

---

## 5. ProductMapper (thợ chép dữ liệu)

Bản chất: **copy giá trị từ hộp này sang hộp kia, từng field một.** Không có phép thuật.

### toEntity (DTO -> Entity, lúc tạo mới)
```java
public static Product toEntity(ProductCreateRequest req) {
    Product p = new Product();        // ① tạo hộp Product rỗng
    p.setSku(req.getSku());           // ② đọc sku TỪ req, ghi VÀO p
    p.setName(req.getName());         //    ... lặp lại cho từng field
    return p;                         // ③ trả về hộp đã đầy
}
```
- Đọc dòng `p.setSku(req.getSku())` theo chiều phải→trái: "lấy sku TỪ req → ĐẶT vào p".
- KHÔNG copy hết: bỏ qua `id`, `soldCount`, `status`, `slug`... (field hệ thống tự quản).

### toResponse (Entity -> DTO, lúc trả ra)
```java
return ProductResponse.builder()      // dựng response mới
        .id(p.getId())                // đọc id từ p, đặt vào response
        .sku(p.getSku())
        ...
        .build();                     // hoàn tất
```
- `.builder()...build()` chỉ là cách viết gọn (Lombok `@Builder`) để tạo + điền object.
  Tương đương: `new ProductResponse(); r.setId(...); ... return r;`
- KHÔNG trả hết: bỏ `version`, `createdBy`, `deletedAt`.

### Vì sao cần 2 method?
- 2 chiều khác nhau: `toEntity` = dữ liệu VÀO (DTO→Entity để lưu DB), `toResponse` = dữ liệu RA (Entity→DTO để trả khách).
- Như máy phiên dịch 2 chiều.

### Vì sao tách Mapper ra class riêng?
- Gom chỗ chuyển đổi về 1 nơi: sau này thêm/bớt field chỉ sửa ở `ProductMapper`, không lục khắp Service.
- Mapper cũng là chỗ kiểm soát field nào được đi qua giữa 2 thế giới (DTO ngoài ↔ Entity trong).

---

## 6. Interface vs Implementation (ProductService / ProductServiceImp)

Service có 2 file:
- **`ProductService` (interface)** = bản hợp đồng: khai báo CÓ method gì, KHÔNG viết chi tiết.
  ```java
  public interface ProductService {
      ProductResponse getById(Long id);   // chỉ khai báo, không có thân hàm {}
      ProductResponse restore(Long id);
  }
  ```
- **`ProductServiceImp` (class)** = thực hiện: viết chi tiết mỗi method LÀM SAO.
  ```java
  @Service
  public class ProductServiceImp implements ProductService {
      @Override
      public ProductResponse restore(Long id) { ... code thật ... }
  }
  ```
- `implements ProductService` = cam kết viết đủ mọi method trong hợp đồng; thiếu 1 cái là compile lỗi.
- `@Override` = bằng chứng method đang thực hiện 1 lời hứa trong interface (gõ sai tên/tham số -> báo lỗi).

### Vì sao tách 2 file?
- **Thay ruột không ảnh hưởng bên ngoài**: Controller chỉ biết `ProductService` (hợp đồng). Viết Imp mới bên trong -> Controller không cần sửa.
- **Dễ test**: có thể mock `ProductService` khi test Controller, không cần DB thật.
- **Spring tự ghép**: Controller cần `ProductService`, Spring tự tìm class `implements` nó và tiêm vào.

> Nguyên tắc: lập trình hướng GIAO DIỆN (interface), không hướng CÀI ĐẶT (class imp).
> Giống cắm phích vào ổ cắm chuẩn — không quan tâm sau tường dây đi thế nào.
