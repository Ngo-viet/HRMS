# HRMS - API Documentation

Tài liệu này mô tả các API hiện có trong project HRMS (endpoints, phương thức, dữ liệu vào/ra, và yêu cầu xác thực).

---

## Tổng quan xác thực
- Ứng dụng dùng Keycloak (OIDC) làm provider. Các endpoint liên quan tới đăng nhập/logout Keycloak được công khai.
- Các API tài nguyên (như employee, leaves, salary) mặc định yêu cầu xác thực (theo SecurityConfig). Một số endpoint công khai: `/perform-login`, `/perform-logout`, `/check-user`, `/error`, `/public/**`.
- Xác thực có thể thực hiện bằng cách:
  - Người dùng truy cập `/perform-login` để được redirect tới Keycloak và sau đó quay lại với session cookie.
  - Hoặc gửi Bearer token JWT (Authorization: Bearer <token>) cho các API nếu dùng resource-server JWT validation.

---

## Models (tóm tắt các trường chính)
- BaseEntity (các model kế thừa):
  - createdDate (LocalDateTime)
  - createdBy (String)
  - updatedDate (LocalDateTime)
  - updatedBy (String)
  - isDeleted (Boolean)

- Employee
  - id (int)
  - firstName, lastName, email, mobile, department, gender, fullAddress, city, state, country

- Leaves
  - leaveId (int)
  - employeeId (int)
  - leaveReason, fromDate, toDate, description, email

- Salary
  - salaryId (int)
  - eid (employee id), month (String), totalWorkingDay (int)
  - basic (String), hra (String), ca (String), pay (String), deduction (String)

- User (HR users)
  - userId (int), username, email, mobile, orgGroup, address, password

---

## Endpoints
Tất cả request/response dùng `Content-Type: application/json` khi có body.

### EmployeeController
- GET /employeereport
  - Mô tả: Lấy danh sách tất cả nhân viên
  - Auth: Yêu cầu xác thực
  - Response: JSON array của `Employee`

- POST /addemployee
  - Mô tả: Tạo user/employee mới
  - Body: `Employee` JSON (ít nhất cần `email`)
  - Response: `Employee` vừa tạo
  - Notes: Nếu email đã tồn tại sẽ throw Exception

- GET /editemployee/{id}
  - Mô tả: Lấy employee theo id
  - Path param: `id` (int)
  - Response: `Employee`

- POST /editemployee
  - Mô tả: Cập nhật employee
  - Body: `Employee` JSON (phải kèm id để cập nhật)
  - Response: `Employee` đã cập nhật

- GET /deleteemployee/{id}
  - Mô tả: Xóa employee theo id (soft/hard tùy implement)
  - Path param: `id` (int)
  - Response: 200 OK (void)

- GET /searchemail/{email}
  - Mô tả: Tìm employee theo email
  - Path param: `email` (String)
  - Response: `Employee` (nếu tìm thấy)

---

### LeaveController
- GET /leavesreport
  - Mô tả: Lấy danh sách các đơn nghỉ phép
  - Auth: Yêu cầu xác thực
  - Response: JSON array của `Leaves`

- POST /addleaves
  - Mô tả: Thêm đơn nghỉ phép cho 1 employee
  - Body: `Leaves` JSON (cần `employeeId`)
  - Behavior: service sẽ tìm `Employee` theo `employeeId` để lấy `email` và gán vào đơn; nếu employee không tồn tại sẽ throw Exception
  - Response: `Leaves` vừa tạo

- GET /editleaves/{id}
  - Mô tả: Lấy 1 đơn nghỉ phép theo id
  - Path param: `id` (int)
  - Response: `Leaves`

- POST /editleaves
  - Mô tả: Cập nhật đơn nghỉ phép
  - Body: `Leaves` JSON
  - Response: `Leaves` đã cập nhật

- GET /deleteleaves/{id}
  - Mô tả: Xóa đơn nghỉ phép theo id
  - Path param: `id` (int)
  - Response: 200 OK (void)

---

### SalaryController
- GET /salaryreport
  - Mô tả: Lấy danh sách tất cả bản ghi lương
  - Auth: Yêu cầu xác thực
  - Response: JSON array của `Salary`

- POST /addsalary
  - Mô tả: Thêm bản ghi lương
  - Body: `Salary` JSON
  - Response: `Salary` vừa tạo

- GET /editsalary/{sid}
  - Mô tả: Lấy bản ghi lương theo id
  - Path param: `sid` (int)
  - Response: `Salary`

- POST /editsalary
  - Mô tả: Cập nhật bản ghi lương
  - Body: `Salary` JSON
  - Response: `Salary` đã cập nhật

- GET /deletesalary/{sid}
  - Mô tả: Xóa bản ghi lương theo id
  - Path param: `sid` (int)
  - Response: 200 OK (void)

---

### UserController (HR users)
- POST /loginuser
  - Mô tả: Đăng nhập cho HR user (local auth - DB)
  - Body: `User` JSON (cần `email` và `password`)
  - Response: `User` khi credentials đúng
  - Notes: Nếu sai dữ liệu sẽ throw Exception

- POST /addhr
  - Mô tả: Tạo tài khoản HR mới
  - Body: `User` JSON
  - Response: `User` vừa tạo
  - Notes: Kiểm tra trùng email trước khi tạo

---

### KeycloakController (Keycloak integration)
- GET /perform-login
  - Mô tả: Redirect user sang Keycloak login URL (xây URL từ cấu hình `keycloak.login-url`, `client_id`, `redirect_uri`)
  - Auth: Public (permitAll)
  - Response: Redirect (302) tới Keycloak

- GET /check-user?usernameOrEmail=...
  - Mô tả: Kiểm tra user có tồn tại trong Keycloak hay không (sử dụng Keycloak Admin API)
  - Query param: `usernameOrEmail` (string)
  - Auth: Public (sử dụng admin credentials nội bộ để gọi API Keycloak)
  - Response: JSON: { exists: boolean, ... }

- GET /me
  - Mô tả: Trả thông tin user đã xác thực (thu thập từ OidcUser sau khi login OIDC)
  - Auth: Yêu cầu xác thực (OIDC)
  - Response: JSON info (subject, username, email, fullName, verified, roles, idToken)

- GET /perform-logout
  - Mô tả: Xóa local session (SecurityContext) và redirect sang Keycloak logout URL kèm id_token_hint và post_logout_redirect_uri
  - Auth: Public (endpoint thực hiện local logout + redirect)
  - Response: Redirect (302) tới Keycloak logout

---

## Role, Feature, Permission & User-Role APIs
Dưới đây là các endpoint mới được thêm để quản lý chức năng (feature), quyền (permission) và gán vai trò cho user.

### Role endpoints (đã thêm trước đó)
- GET /roles — Lấy danh sách vai trò
- POST /roles — Tạo vai trò mới (body: Role JSON: roleName, description)
- GET /roles/{id} — Lấy vai trò theo id
- PUT /roles/{id} — Cập nhật vai trò
- DELETE /roles/{id} — Xóa vai trò

### User-Role endpoints
- GET /users/{userId}/roles
  - Mô tả: Lấy danh sách role mà user đang có
  - Response: array Role

- POST /users/{userId}/roles/{roleId}
  - Mô tả: Gán role cho user
  - Response: 200 OK

- DELETE /users/{userId}/roles/{roleId}
  - Mô tả: Bỏ role khỏi user
  - Response: 204 No Content

- GET /roles/{roleId}/users
  - Mô tả: Lấy danh sách user có role đó
  - Response: array User

### Feature endpoints
- GET /features — Lấy danh sách feature (điểm cuối/permissionable feature)
- POST /features — Tạo feature (body: Feature JSON: featureName, endpoint, description)
- GET /features/{id} — Lấy feature theo id
- PUT /features/{id} — Cập nhật feature
- DELETE /features/{id} — Xóa feature

### Permission endpoints
- GET /permissions — Lấy tất cả permission
- POST /permissions — Tạo permission (body: Permission JSON must reference role and feature ids, and flags canCreate/canRead/canUpdate/canDelete)
- GET /permissions/{id} — Lấy permission theo id
- PUT /permissions/{id} — Cập nhật flags của permission
- DELETE /permissions/{id} — Xóa permission

- GET /roles/{roleId}/permissions — Lấy permission theo role
- GET /features/{featureId}/permissions — Lấy permission theo feature

### New endpoints for assigning permissions / features
- POST /roles/{roleId}/features/{featureId}/permissions
  - Mô tả: Gán permission (flags) cho cặp role+feature; nếu permission đã tồn tại thì cập nhật các flag.
  - Body: JSON chứa các flag (ví dụ canRead, canCreate, canUpdate, canDelete).
  - Ví dụ:

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"canRead":true,"canCreate":false,"canUpdate":false,"canDelete":false}' \
  http://localhost:8081/roles/1/features/2/permissions
```

- DELETE /roles/{roleId}/features/{featureId}/permissions
  - Mô tả: Xóa permission (role+feature) nếu tồn tại.
  - Ví dụ:

```bash
curl -X DELETE http://localhost:8081/roles/1/features/2/permissions
```

- POST /roles/{roleId}/features/{featureId}/rfp
  - Mô tả: Gán feature cho role bằng bản ghi RFP (RoleFeaturePermission); nếu đã tồn tại thì cập nhật flags, nếu chưa thì tạo mới.
  - Body: JSON RoleFeaturePermission chứa các flag (canCreate, canRead, ...).
  - Ví dụ:

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"canRead":true,"canCreate":true}' \
  http://localhost:8081/roles/1/features/2/rfp
```

- DELETE /roles/{roleId}/features/{featureId}/rfp
  - Mô tả: Xóa bản ghi RFP cho cặp role+feature.
  - Ví dụ:

```bash
curl -X DELETE http://localhost:8081/roles/1/features/2/rfp
```

### Notes & usage
- `Feature` đại diện cho một chức năng hoặc endpoint trong hệ thống (ví dụ `Employee:GET:/employeereport`).
- `Permission` liên kết `Role` và `Feature` kèm các flag CRUD. Ứng dụng có thể kiểm tra permission trước khi thực hiện một hành động trên một resource.
- Hiện các API tạo/ghi đọc/ghi xóa permission và mapping user-role được triển khai trong các controller:
  - `src/main/java/com/hrms/controller/FeatureController.java`
  - `src/main/java/com/hrms/controller/PermissionController.java`
  - `src/main/java/com/hrms/controller/UserRoleController.java`

### Examples (curl)
- Tạo feature:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"featureName":"Manage Employees","endpoint":"/employeereport","description":"View employee list"}' http://localhost:8081/features
```

- Tạo permission (ví dụ roleId=1, featureId=2):

```bash
curl -X POST -H "Content-Type: application/json" -d '{"role": {"roleId":1}, "feature": {"featureId":2}, "canRead": true, "canCreate": false, "canUpdate": false, "canDelete": false}' http://localhost:8081/permissions
```

- Gán role cho user (userId=3, roleId=2):

```bash
curl -X POST http://localhost:8081/users/3/roles/2
```

---

## Lưu ý & Gợi ý
- Các endpoint GET dùng để xóa (`/deleteemployee/{id}`, `/deleteleaves/{id}`, `/deletesalary/{sid}`) thực tế nên dùng HTTP DELETE theo chuẩn REST; tuy nhiên hiện code dùng GET — nếu muốn an toàn/RESTful, hãy đổi sang @DeleteMapping.
- Các endpoint cập nhật đang dùng POST (`/editemployee`, `/editleaves`, `/editsalary`) có thể chuyển sang PUT nếu cần.
- Input validation/exception handling hiện tại ném Exception thô; nên cải thiện bằng ResponseEntity với mã lỗi rõ ràng (400/404/409) và dùng @ControllerAdvice để xử lý lỗi chung.
- Khi chạy trong Docker, các endpoint bảo mật yêu cầu cấu hình Keycloak đúng (issuer-uri, client-id/secret). Xem `application.properties` để map biến môi trường.

---

## Ví dụ nhanh (curl)
- Lấy danh sách employees (yêu cầu token):

```bash
curl -H "Authorization: Bearer <TOKEN>" http://localhost:8081/employeereport
```

- Thêm employee:

```bash
curl -X POST -H "Content-Type: application/json" -d '{"firstName":"Nguyen","lastName":"A","email":"a@example.com"}' http://localhost:8081/addemployee
```

---

Nếu bạn muốn tôi: 
- xuất file này sang `.txt` thay vì `.md`, hoặc
- thêm ví dụ request/response chi tiết cho từng endpoint, hoặc
- tự động generate Postman collection từ các endpoint hiện có,
hãy cho biết lựa chọn.

### Attendance (Chấm công)
- POST /attendance
  - Mô tả: Ghi nhận chấm công cho nhân viên.
  - Body: Attendance JSON: { employeeId, date(YYYY-MM-DD), hoursWorked, status: PRESENT|ABSENT|LEAVE, leaveId (optional) }

- GET /attendance/{id}
  - Mô tả: Lấy chi tiết bản ghi chấm công theo id.

- GET /attendance/employee/{empId}
  - Mô tả: Lấy tất cả bản ghi chấm công của 1 nhân viên.

- GET /attendance/employee/{empId}/period?from=yyyy-MM-dd&to=yyyy-MM-dd
  - Mô tả: Lấy bản ghi chấm công trong khoảng thời gian.

- GET /attendance/employee/{empId}/summary?from=yyyy-MM-dd&to=yyyy-MM-dd
  - Mô tả: Trả về tổng hợp chấm công: totalPresentDays, totalHours, totalLeaves, totalAbsent
  - Ví dụ:

```bash
curl "http://localhost:8081/attendance/employee/1/summary?from=2026-03-01&to=2026-03-31"
```

- POST /attendance/employee/{empId}/generate-salary?month=YYYY-MM
  - Mô tả: Tạo bản ghi lương cho employee dựa trên dữ liệu chấm công trong tháng.
  - Body: JSON có thể gồm các trường `basic`, `hra`, `ca`, `deduction`.
  - Ví dụ:

```bash
curl -X POST -H "Content-Type: application/json" \
  -d '{"basic":"1500","hra":"200","ca":"100","deduction":"50"}' \
  "http://localhost:8081/attendance/employee/1/generate-salary?month=2026-03"
```

### Leave approval endpoints
- POST /leaves/{id}/approve?approver=NAME
  - Mô tả: Duyệt đơn nghỉ phép, đổi trạng thái thành APPROVED, lưu người duyệt và ngày duyệt.
  - Ví dụ:

```bash
curl -X POST "http://localhost:8081/leaves/5/approve?approver=manager1"
```

- POST /leaves/{id}/reject?approver=NAME
  - Mô tả: Từ chối đơn nghỉ phép, đổi trạng thái thành REJECTED.

```bash
curl -X POST "http://localhost:8081/leaves/5/reject?approver=manager1"
```

### Overtime (OT) endpoints
- POST /ot
  - Tạo bản ghi OT (Body: Overtime JSON: employeeId, date, startTime, endTime, hours, reason)

- GET /ot/{id}
  - Lấy chi tiết OT

- GET /ot/employee/{empId}
  - Lấy danh sách OT của employee

- GET /ot/employee/{empId}/period?from=yyyy-MM-dd&to=yyyy-MM-dd
  - Lấy OT theo khoảng thời gian

- PUT /ot/{id}
  - Cập nhật OT

- DELETE /ot/{id}
  - Xóa OT

- POST /ot/{id}/approve?approver=NAME
  - Duyệt OT (status -> APPROVED)

- POST /ot/{id}/reject?approver=NAME
  - Từ chối OT (status -> REJECTED)

### Performance (KPI) endpoints
- GET /performance
  - Mô tả: Lấy tất cả bản ghi performance (kpi) (có thể paged sau).

- GET /performance/{id}
  - Mô tả: Lấy chi tiết performance theo id.

- GET /performance/employee/{empId}
  - Mô tả: Lấy tất cả performance của 1 nhân viên.

- GET /performance/period/{period}
  - Mô tả: Lấy performance theo kỳ (ví dụ 2026-03).

- POST /performance
  - Mô tả: Tạo bản ghi performance (Body: Performance JSON: employeeId, period, kpiScore, rating, comments, reviewDate)

- PUT /performance/{id}
  - Mô tả: Cập nhật bản ghi performance.

- DELETE /performance/{id}
  - Mô tả: Xóa bản ghi performance.

### Contract endpoints
- GET /contracts
  - Mô tả: Lấy danh sách tất cả hợp đồng

- GET /contracts/{id}
  - Mô tả: Lấy chi tiết hợp đồng theo id

- GET /contracts/employee/{empId}
  - Mô tả: Lấy hợp đồng theo employee id

- POST /contracts
  - Mô tả: Tạo hợp đồng mới (Body: Contract JSON: employeeId, startDate, endDate, contractType, contractSalary, status, notes)

- PUT /contracts/{id}
  - Mô tả: Cập nhật hợp đồng theo id

- DELETE /contracts/{id}
  - Mô tả: Xóa hợp đồng
