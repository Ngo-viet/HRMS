CREATE TABLE DB_HRMS.HRM_EMPLOYEE (
    id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(150),
    mobile VARCHAR(20),
    department VARCHAR(100),
    gender VARCHAR(10),
    fullAddress VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    country VARCHAR(100),

    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE DB_HRMS.HRM_LEAVES (
    leave_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_id INT,
    leave_reason VARCHAR(255),
    from_date VARCHAR(50),
    to_date VARCHAR(50),
    description VARCHAR(255),
    email VARCHAR(150),

    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_leave_employee
    FOREIGN KEY (emp_id) REFERENCES DB_HRMS.HRM_EMPLOYEE(id)
);



CREATE TABLE DB_HRMS.HRM_SALARY (
    salary_id INT PRIMARY KEY AUTO_INCREMENT,
    emp_id INT,
    month VARCHAR(20),
    total_working_day INT,
    basic_salary VARCHAR(50),
    total_hra VARCHAR(50),
    conveyance_allowance VARCHAR(50),
    total_net_pay VARCHAR(50),
    total_deductions VARCHAR(50),

    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_salary_employee
    FOREIGN KEY (emp_id) REFERENCES DB_HRMS.HRM_EMPLOYEE(id)
);


CREATE TABLE DB_HRMS.HRM_USER (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100),
    email VARCHAR(150),
    mobile VARCHAR(20),
    orgGroup VARCHAR(100),
    address VARCHAR(255),
    password VARCHAR(255),

    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(100) NOT NULL,
    updated_date TIMESTAMP,
    updated_by VARCHAR(100),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
);
commit;


INSERT INTO DB_Hrms.HRM_EMPLOYEE
(id, first_name, last_name, email, mobile, department, gender, fullAddress, city, state, country, created_date, created_by, is_deleted)
VALUES
(1,'An','Nguyen','an@gmail.com','0901','IT','Male','Addr1','HCM','HCM','VN',NOW(),'admin',false),
(2,'Binh','Tran','binh@gmail.com','0902','HR','Male','Addr2','HN','HN','VN',NOW(),'admin',false),
(3,'Chi','Le','chi@gmail.com','0903','Finance','Female','Addr3','DN','DN','VN',NOW(),'admin',false),
(4,'Dung','Pham','dung@gmail.com','0904','IT','Male','Addr4','HCM','HCM','VN',NOW(),'admin',false),
(5,'Em','Vo','em@gmail.com','0905','HR','Female','Addr5','CT','CT','VN',NOW(),'admin',false),
(6,'Fang','Hoang','fang@gmail.com','0906','Finance','Male','Addr6','HN','HN','VN',NOW(),'admin',false),
(7,'Giang','Do','giang@gmail.com','0907','IT','Female','Addr7','HCM','HCM','VN',NOW(),'admin',false),
(8,'Huy','Nguyen','huy@gmail.com','0908','HR','Male','Addr8','DN','DN','VN',NOW(),'admin',false),
(9,'Ivy','Tran','ivy@gmail.com','0909','Finance','Female','Addr9','CT','CT','VN',NOW(),'admin',false),
(10,'Khanh','Le','khanh@gmail.com','0910','IT','Male','Addr10','HN','HN','VN',NOW(),'admin',false);

INSERT INTO DB_Hrms.HRM_LEAVES
(leave_id, emp_id, leave_reason, from_date, to_date, description, email, created_date, created_by, is_deleted)
VALUES
(1,1,'Sick','2026-03-01','2026-03-02','Flu','an@gmail.com',NOW(),'admin',false),
(2,2,'Vacation','2026-03-05','2026-03-07','Travel','binh@gmail.com',NOW(),'admin',false),
(3,3,'Personal','2026-03-03','2026-03-03','Family','chi@gmail.com',NOW(),'admin',false),
(4,4,'Sick','2026-03-08','2026-03-09','Cold','dung@gmail.com',NOW(),'admin',false),
(5,5,'Vacation','2026-03-10','2026-03-12','Trip','em@gmail.com',NOW(),'admin',false),
(6,6,'Personal','2026-03-11','2026-03-11','Event','fang@gmail.com',NOW(),'admin',false),
(7,7,'Sick','2026-03-13','2026-03-14','Headache','giang@gmail.com',NOW(),'admin',false),
(8,8,'Vacation','2026-03-15','2026-03-17','Beach','huy@gmail.com',NOW(),'admin',false),
(9,9,'Personal','2026-03-18','2026-03-18','Family','ivy@gmail.com',NOW(),'admin',false),
(10,10,'Sick','2026-03-19','2026-03-20','Fever','khanh@gmail.com',NOW(),'admin',false);

INSERT INTO DB_Hrms.hrm_SALARY
(salary_id, emp_id, month, total_working_day, basic_salary, total_hra, conveyance_allowance, total_net_pay, total_deductions, created_date, created_by, is_deleted)
VALUES
(1,1,'2026-03',22,'1000','200','100','1200','100',NOW(),'admin',false),
(2,2,'2026-03',22,'1100','200','100','1300','100',NOW(),'admin',false),
(3,3,'2026-03',22,'1200','200','100','1400','100',NOW(),'admin',false),
(4,4,'2026-03',22,'1300','200','100','1500','100',NOW(),'admin',false),
(5,5,'2026-03',22,'1400','200','100','1600','100',NOW(),'admin',false),
(6,6,'2026-03',22,'1500','200','100','1700','100',NOW(),'admin',false),
(7,7,'2026-03',22,'1600','200','100','1800','100',NOW(),'admin',false),
(8,8,'2026-03',22,'1700','200','100','1900','100',NOW(),'admin',false),
(9,9,'2026-03',22,'1800','200','100','2000','100',NOW(),'admin',false),
(10,10,'2026-03',22,'1900','200','100','2100','100',NOW(),'admin',false);

INSERT INTO DB_Hrms.hrm_USER
(user_id, username, email, mobile, orgGroup, address, password, created_date, created_by, is_deleted)
VALUES
(1,'admin','admin@gmail.com','0901','HR','Addr1','123456',NOW(),'system',false),
(2,'user1','user1@gmail.com','0902','IT','Addr2','123456',NOW(),'system',false),
(3,'user2','user2@gmail.com','0903','Finance','Addr3','123456',NOW(),'system',false),
(4,'user3','user3@gmail.com','0904','HR','Addr4','123456',NOW(),'system',false),
(5,'user4','user4@gmail.com','0905','IT','Addr5','123456',NOW(),'system',false),
(6,'user5','user5@gmail.com','0906','Finance','Addr6','123456',NOW(),'system',false),
(7,'user6','user6@gmail.com','0907','HR','Addr7','123456',NOW(),'system',false),
(8,'user7','user7@gmail.com','0908','IT','Addr8','123456',NOW(),'system',false),
(9,'user8','user8@gmail.com','0909','Finance','Addr9','123456',NOW(),'system',false),
(10,'user9','user9@gmail.com','0910','HR','Addr10','123456',NOW(),'system',false),
+-- Roles table
+CREATE TABLE IF NOT EXISTS DB_HRMS.HRM_ROLE (
+    role_id INT PRIMARY KEY AUTO_INCREMENT,
+    role_name VARCHAR(100) UNIQUE NOT NULL,
+    description VARCHAR(255),
+
+    created_date TIMESTAMP NOT NULL,
+    created_by VARCHAR(100) NOT NULL,
+    updated_date TIMESTAMP,
+    updated_by VARCHAR(100),
+    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
+);
+
+-- Join table user-role
+CREATE TABLE IF NOT EXISTS DB_HRMS.HRM_USER_ROLE (
+    user_id INT NOT NULL,
+    role_id INT NOT NULL,
+    PRIMARY KEY (user_id, role_id),
+    CONSTRAINT fk_ur_user FOREIGN KEY (user_id) REFERENCES DB_HRMS.HRM_USER(user_id),
+    CONSTRAINT fk_ur_role FOREIGN KEY (role_id) REFERENCES DB_HRMS.HRM_ROLE(role_id)
+);
+
+-- Features table
+CREATE TABLE IF NOT EXISTS DB_HRMS.HRM_FEATURE (
+    feature_id INT PRIMARY KEY AUTO_INCREMENT,
+    feature_name VARCHAR(150) UNIQUE NOT NULL,
+    endpoint VARCHAR(255),
+    description VARCHAR(255),
+
+    created_date TIMESTAMP NOT NULL,
+    created_by VARCHAR(100) NOT NULL,
+    updated_date TIMESTAMP,
+    updated_by VARCHAR(100),
+    is_deleted BOOLEAN NOT NULL DEFAULT FALSE
+);
+
+-- Permissions table (role - feature mapping with CRUD flags)
+CREATE TABLE IF NOT EXISTS DB_HRMS.HRM_PERMISSION (
+    permission_id INT PRIMARY KEY AUTO_INCREMENT,
+    role_id INT NOT NULL,
+    feature_id INT NOT NULL,
+    can_create BOOLEAN DEFAULT FALSE,
+    can_read BOOLEAN DEFAULT FALSE,
+    can_update BOOLEAN DEFAULT FALSE,
+    can_delete BOOLEAN DEFAULT FALSE,
+
+    created_date TIMESTAMP NOT NULL,
+    created_by VARCHAR(100) NOT NULL,
+    updated_date TIMESTAMP,
+    updated_by VARCHAR(100),
+    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
+
+    CONSTRAINT fk_perm_role FOREIGN KEY (role_id) REFERENCES DB_HRMS.HRM_ROLE(role_id),
+    CONSTRAINT fk_perm_feature FOREIGN KEY (feature_id) REFERENCES DB_HRMS.HRM_FEATURE(feature_id)
+);
+
+-- Role-Feature-Permission join table (alternative denormalized mapping)
+CREATE TABLE IF NOT EXISTS DB_HRMS.HRM_ROLE_FP (
+    id INT PRIMARY KEY AUTO_INCREMENT,
+    role_id INT NOT NULL,
+    feature_id INT NOT NULL,
+    can_create BOOLEAN DEFAULT FALSE,
+    can_read BOOLEAN DEFAULT FALSE,
+    can_update BOOLEAN DEFAULT FALSE,
+    can_delete BOOLEAN DEFAULT FALSE,
+
+    created_date TIMESTAMP NOT NULL,
+    created_by VARCHAR(100) NOT NULL,
+    updated_date TIMESTAMP,
+    updated_by VARCHAR(100),
+    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
+
+    CONSTRAINT fk_rfp_role FOREIGN KEY (role_id) REFERENCES DB_HRMS.HRM_ROLE(role_id),
+    CONSTRAINT fk_rfp_feature FOREIGN KEY (feature_id) REFERENCES DB_HRMS.HRM_FEATURE(feature_id)
+);
