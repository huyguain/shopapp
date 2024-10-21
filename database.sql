CREATE DATABASE ShopApp;
USE ShopApp;
-- Khách hàng khi đăng ký sẽ được cấp quyền là user -> table user
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(255) NOT NULL,
    password VARCHAR(100) NOT NULL DEFAULT '',
    create_at DATETIME,
    update_at DATETIME,
    is_active TININT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 1,
    google_account_id INT DEFAULT 1
);

-- Bảng quyền
CREATE TABLE roles (
    id INT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
)

ALTER TABLE users ADD COLUMN role_id INT;
ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles(id);

-- Bảng 
CREATE TABLE tokens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    expiration_date DATETIME,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- hỗ trợ đăng nhập fb bà gg
CREATE TABLE social_accounts (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `provider` VARCHAR(20) NOT NULL COMMENT 'Social network name',
    provider_id VARCHAR(255) NOT NULL,
    email VARCHAR(150) NOT NULL COMMENT 'Email of user',
    name VARCHAR(100) NOT NULL COMMENT 'Name of user',
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

--Bảng danh mục sản phẩm(Category)
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL DEFAULT '' COMMENT 'Tên danh mục',
);

--Bảng sản phẩm(Product)
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(350) NOT NULL DEFAULT '' COMMENT 'Tên sản phẩm',
    price FLOAT NOT NULL CHECK (price >= 0),
    thumbnail VARCHAR(255) DEFAULT '',
    description LONGTEXT DEFAULT '',
    create_at TIMESTAMP,
    update_at TIMESTAMP,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

--Đạt hàng  (Order)
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    fullname VARCHAR(100) DEFAULT '',
    email VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(255) NOT NULL,
    note VARCHAR(255) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    total_money FLOAT CHECK (total_money >= 0),    
    FOREIGN KEY (user_id) REFERENCES users(id)
);

ALTER TABLE orders ADD COLUMN shipping_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_address VARCHAR(200);
ALTER TABLE orders ADD COLUMN payment_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_date DATE;
ALTER TABLE orders ADD COLUMN tracking_number VARCHAR(100);
--xóa đơn hàng -> xóa mềm -> thêm trường active
ALTER TABLE orders ADD COLUMN active TINYINT(1);
--Trạng thái đơn hàng chỉ được phép nhận "một số giá trị cụ thể"
ALTER TABLE orders 
MODIFY COLUMN status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled')
COMMENT 'Trạng thái đơn hàng';

--Chi tiết đơn hàng (OrderDetail)
CREATE TABLE order_details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    product_id INT,
    number_of_products INT CHECK (number_of_products > 0),
    price FLOAT CHECK (price >= 0),
    total_money FLOAT CHECK (total_money >= 0),
    color VARCHAR(50) DEFAULT '',
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
