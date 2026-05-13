# Pet Service Platform — Complete Project Overview

> This document provides a comprehensive description of the Pet Service Platform project, including all existing modules, newly added modules, technical architecture, database schema, and business logic. It is designed to give any reader (human or AI) a complete understanding of the project.

---

## 1. Project Overview

**Project Name**: Pet Service Platform (宠物服务平台)

**Database Name**: `chongwufuwu` (宠物服务)

**Tech Stack**: Spring Boot 2.7.18 + MyBatis + MySQL 8.0 + Redis + Thymeleaf

**Default Admin Account**: `admin / admin123`

**Project Purpose**: A web-based platform connecting pet owners with pet service providers. Users can browse pet services (grooming, boarding, training, medical), place orders, make payments, communicate with providers, and leave reviews. Service providers can manage their services and view business analytics.

**Three User Roles**:
| Role | ID | Description |
|------|-----|-------------|
| Admin | 1 | System administrator managing all modules |
| User | 2 | Pet owner who browses and purchases services |
| Provider | 3 | Service provider who offers pet services |

---

## 2. System Architecture

### 2.1 Technology Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| Backend Framework | Spring Boot | 2.7.18 |
| ORM | MyBatis | 2.3.1 |
| Template Engine | Thymeleaf | 3.0.x |
| Database | MySQL | 8.0.33 |
| Cache | Redis (Jedis) | 6.x |
| Frontend | Bootstrap + LayUI + Element UI + jQuery + Vue.js | - |
| Testing | JUnit 5 + Mockito + Selenium | 4.11.0 |
| Monitoring | Micrometer + Prometheus | - |
| Deployment | Docker + Docker Compose + Nginx | - |
| CI/CD | GitHub Actions | - |
| Build Tool | Maven | - |
| Java Version | Java 8 | 1.8 |

### 2.2 Application Configuration

- **Server Port**: 7002
- **Database**: `jdbc:mysql://localhost:3306/chongwufuwu`
- **Redis**: `localhost:6379`
- **Session-based authentication** (not JWT)
- **File upload**: Local filesystem storage
- **Password encryption**: MD5 + AES

### 2.3 Architecture Pattern

Classic **MVC (Model-View-Controller)** layered architecture:

```
Browser → Nginx (reverse proxy) → Spring Boot (Tomcat :7002)
                                       │
                                       ├── Controller Layer (REST APIs + Page routing)
                                       ├── Service Layer (Business logic)
                                       ├── DAO/Mapper Layer (MyBatis)
                                       │
                                       ├── MySQL (persistent data)
                                       ├── Redis (cache + session)
                                       └── Local File System (images/uploads)
```

---

## 3. Module Inventory

### 3.1 Existing Modules (14 modules, already implemented)

| # | Module | Package | Status | Description |
|---|--------|---------|--------|-------------|
| 1 | Login | `modules/login` | ✅ Done | User registration, login, logout. Supports 3 roles. Provider registration auto-creates Company record. |
| 2 | Sysuser | `modules/sysuser` | ✅ Done | User CRUD management. Stores user profile, pet info, balance, geolocation (lat/lng). |
| 3 | Company | `modules/company` | ✅ Done | Service provider/company CRUD. Stores company info, average rating, rating count, service area. |
| 4 | Producttype | `modules/producttype` | ✅ Done | Product/service category management. Default categories: pet grooming, boarding, training, medical. |
| 5 | Product | `modules/product` | ✅ Done | Product/service CRUD. Advanced filtering by time, price, rating, type, keyword. Distance-based filtering. |
| 6 | Shopcart | `modules/shopcart` | ✅ Done | Shopping cart management. Users add products, set quantity, calculate total price. |
| 7 | Order | `modules/order` | ✅ Done | Order CRUD + statistics. On order creation: validates user, reads cart, deducts balance, sends notification to provider. |
| 8 | Tmoney | `modules/tmoney` | ✅ Done | Recharge request and approval workflow. Admin approves recharge → user balance increases. |
| 9 | Webnotice | `modules/webnotice` | ✅ Done | Website announcement management. Admin publishes announcements. |
| 10 | Type | `modules/type` | ✅ Done | Generic type/tag management. |
| 11 | Comment | `modules/comment` | ✅ Done | Announcement comments with tree-structured replies (parent_id self-reference). |
| 12 | Provider | `modules/provider` | ✅ Done | Service provider dashboard. Aggregates data from order, evaluation, product tables. No own database table. |
| 13 | Common | `modules/common` | ✅ Done | Common utilities: file upload service. |
| 14 | Web | `modules/web` | ✅ Done | Frontend page routing controller. Maps URLs to Thymeleaf templates. Three sections: admin, user frontend, provider frontend. |

### 3.2 New Modules (4 modules, 3 implemented + 1 pending)

| # | Module | Package | Status | Description |
|---|--------|---------|--------|-------------|
| 1 | Userlike | `modules/userlike` | ✅ Done | Users can favorite/unfavorite products. Favorites list display. |
| 2 | OrderEvalute | `modules/orderEvalute` | ✅ Done | Users can rate and review completed orders. Rating auto-updates provider's average score. |
| 3 | Message | `modules/message` | ✅ Done | Internal messaging between users and providers. Bidirectional communication channel. |
| 4 | AI Pet QA | (pending) | ❌ Pending | AI-powered pet knowledge Q&A. Not yet implemented. |

---

## 4. Core Business Flow

### 4.1 User Journey (End-to-End)

```
1. Register/Login
   User registers with role=2 (or role=3 for provider)
   Provider registration auto-creates a Company record

2. Browse Services
   User views product listing page (/web/userproductlist)
   Can filter by: type, price range, rating, keyword, distance
   Can view product detail page (/web/usersingle)

3. Add to Cart
   User adds products to shopping cart (/web/usercart)

4. Place Order
   System reads cart items → calculates total → deducts user balance → creates order
   Provider receives notification message automatically

5. Recharge (if balance insufficient)
   User submits recharge request → Admin approves → balance increases

6. Rate & Review
   After order completion, user can rate (1-5 stars) and write review
   Rating auto-updates provider's avg_rating and rating_count

7. Communicate
   User and provider can exchange messages through the messaging system

8. Favorite
   User can favorite products for quick access later
```

### 4.2 Provider Journey

```
1. Register as Provider (role=3)
   → Company record auto-created

2. Manage Products
   Add/edit/delete services with pricing, schedule, images

3. View Dashboard
   See order statistics, revenue, ratings, service distribution

4. Receive Messages
   Read and reply to user messages

5. View Reviews
   See ratings and reviews from completed orders
```

### 4.3 Admin Journey

```
1. Manage all users, providers, products, orders
2. Approve recharge requests
3. Publish announcements
4. View system statistics
```

---

## 5. Module Dependency Graph

### 5.1 Cross-Module Dependencies

```
sysuser (core dependency, used by 5+ modules)
  ├── login (user authentication)
  ├── product (provider username lookup)
  ├── order (user validation, balance deduction)
  ├── tmoney (balance update on recharge approval)
  └── provider (aggregated statistics)

company (provider foundation)
  ├── login (auto-create on provider registration)
  ├── product (products belong to a company)
  ├── orderEvalute (update avg_rating on review)
  └── provider (aggregated statistics)

product (service catalog)
  ├── shopcart (products added to cart)
  ├── order (price calculation from cart products)
  ├── userlike (users favorite products)
  └── provider (service count statistics)

order (transaction core, heaviest dependency)
  ├── shopcart (read cart items, mark as ordered)
  ├── product (get price, confirm provider)
  ├── sysuser (validate user, deduct balance)
  └── message (auto-send notification to provider on order creation)
```

### 5.2 Module Layering

```
Layer 1 — Foundation (no business dependencies):
  sysuser, company, producttype, type, common

Layer 2 — Core Business (depends on Layer 1):
  login, product, shopcart, order, tmoney, message

Layer 3 — Extension (depends on Layer 1+2):
  orderEvalute, userlike, comment, webnotice, provider, web
```

---

## 6. Database Schema

### 6.1 Entity-Relationship Overview

```
sysuser ──1:N──> company (createid)
sysuser ──1:N──> shopcart (userid)
sysuser ──1:N──> order (userid)
sysuser ──1:N──> order_evalute (userid)
sysuser ──1:N──> message (senderid, receiveid)
sysuser ──1:N──> tmoney (userid)
sysuser ──1:N──> userlike (userid)
sysuser ──1:N──> comment (userid, to_user_id)

company ──1:N──> product (companyid)
company ──1:N──> order_evalute (companyid)

producttype ──1:N──> product (producttype)

product ──1:N──> shopcart (productid)
product ──1:N──> userlike (productid)

shopcart ──1:N──> order (carid)

order ──1:N──> order_evalute (orderid)

comment ──self-ref──> comment (pid, tree-structured replies)
```

### 6.2 Table Definitions

#### sysuser — System User Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| username | VARCHAR(100) | Display name |
| account | VARCHAR(100) | Login account |
| password | VARCHAR(255) | Password (encrypted) |
| role | VARCHAR(50) | 1=Admin, 2=User, 3=Provider |
| sex | VARCHAR(10) | Gender |
| age | INT | Age |
| phonenumber | VARCHAR(30) | Phone number |
| address | VARCHAR(255) | Address |
| img | VARCHAR(255) | Avatar URL |
| money | DOUBLE DEFAULT 0 | Account balance |
| petname | VARCHAR(100) | Pet name |
| petage | VARCHAR(50) | Pet age |
| pettype | VARCHAR(100) | Pet type |
| petdes | VARCHAR(500) | Pet description |
| province, city, district | VARCHAR(50) | Geographic location |
| longitude, latitude | DOUBLE | GPS coordinates |

#### company — Service Provider Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| companyname | VARCHAR(255) | Company name |
| phonenumber | VARCHAR(30) | Contact phone |
| address | VARCHAR(255) | Address |
| createid | INT | Creator user ID (FK → sysuser) |
| province, city, district | VARCHAR(50) | Location |
| longitude, latitude | DOUBLE | GPS coordinates |
| avg_rating | DOUBLE DEFAULT 0 | Average rating (auto-calculated) |
| rating_count | INT DEFAULT 0 | Total rating count |
| service_area | VARCHAR(255) | Service coverage area |
| status | VARCHAR(50) | Status |

#### product — Product/Service Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| productname | VARCHAR(255) | Service name |
| productdes | VARCHAR(500) | Description |
| img | VARCHAR(255) | Cover image |
| kedanjia | VARCHAR(50) | Unit price |
| companyid | INT | Provider ID (FK → company) |
| producttype | INT | Category ID (FK → producttype) |
| service_start_time | VARCHAR(20) | Service start time |
| service_end_time | VARCHAR(20) | Service end time |
| status | VARCHAR(50) | Status |

#### shopcart — Shopping Cart Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| productid | INT | Product ID (FK → product) |
| userid | INT | User ID (FK → sysuser) |
| number | INT | Quantity |
| totalprice | DECIMAL(10,2) | Total price |

#### order — Order Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| userid | INT | User ID (FK → sysuser) |
| carid | VARCHAR(255) | Cart item IDs (comma-separated) |
| remark | VARCHAR(255) | Order remark |

#### order_evalute — Order Evaluation Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| content | VARCHAR(1000) | Review text |
| rating | DOUBLE | Rating score |
| userid | INT | User ID (FK → sysuser) |
| orderid | INT | Order ID (FK → order) |
| companyid | INT | Provider ID (FK → company) |

#### message — Message Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| content | VARCHAR(2000) | Message content |
| senderid | INT | Sender ID (FK → sysuser) |
| receiveid | INT | Receiver ID (FK → sysuser) |
| status | VARCHAR(50) | Read/unread status |

#### userlike — User Favorites Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| userid | INT | User ID (FK → sysuser) |
| productid | INT | Product ID (FK → product) |

#### tmoney — Recharge Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| money | DECIMAL(10,2) | Recharge amount |
| userid | INT | User ID (FK → sysuser) |
| auditstatus | VARCHAR(50) | Approval status |
| cause | VARCHAR(500) | Recharge reason |

#### webnotice — Website Announcement Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| title | VARCHAR(255) | Announcement title |
| content | TEXT | Announcement content |
| img | VARCHAR(255) | Cover image |
| status | VARCHAR(50) | Status |

#### comment — Comment Table (for announcements)
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| pid | INT | Parent comment ID (self-reference for replies) |
| userid | INT | Commenter ID (FK → sysuser) |
| to_user_id | INT | Reply target user ID |
| content | VARCHAR(2000) | Comment content |

#### producttype — Product Category Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| type_name | VARCHAR(100) | Category name |
| status | VARCHAR(50) | Status |

#### t_type — Generic Type Table
| Column | Type | Description |
|--------|------|-------------|
| id | INT PK AUTO_INCREMENT | Primary key |
| typename | VARCHAR(100) | Type name |
| status | VARCHAR(50) | Status |

### 6.3 Default Data

Default product categories:
- 宠物美容 (Pet Grooming)
- 宠物寄养 (Pet Boarding)
- 宠物训练 (Pet Training)
- 宠物医疗 (Pet Medical)

---

## 7. Key API Endpoints

### 7.1 Authentication
| Method | Path | Description |
|--------|------|-------------|
| POST | `/toLogin` | Login (returns userId + role, stores session) |
| POST | `/toRegister` | Register (role=3 auto-creates Company) |
| GET | `/admin/logout` | Logout (destroys session) |

### 7.2 Product/Service
| Method | Path | Description |
|--------|------|-------------|
| GET | `/product` | Paginated product list |
| GET | `/product/detail` | Product detail (optional userid for favorites/sales) |
| POST | `/product` | Create product (provider only) |
| PUT | `/product` | Update product |
| DELETE | `/product` | Delete product |
| POST/GET | `/product/filterServices` | Advanced filtering (time, price, rating, type, keyword, distance) |

### 7.3 Order
| Method | Path | Description |
|--------|------|-------------|
| GET | `/order` | Paginated order list |
| POST | `/order` | Create order (reads cart, deducts balance, sends notification) |
| GET | `/order/statistics` | Order statistics (pie chart data) |

### 7.4 Other Key APIs
| Method | Path | Description |
|--------|------|-------------|
| GET | `/provider/dashboard` | Provider dashboard aggregated data |
| GET/POST | `/userlike/*` | Favorites CRUD |
| GET/POST | `/comment/*` | Comments with tree-structured replies |
| GET/POST | `/message/*` | Messaging between users and providers |
| GET/POST | `/orderEvalute/*` | Order evaluation and rating |
| GET/POST | `/tmoney/*` | Recharge request and approval |
| GET/POST | `/webnotice/*` | Announcement management |

---

## 8. Frontend Pages

### 8.1 User Frontend (webSite templates)
| Page | URL | Description |
|------|-----|-------------|
| Homepage | `/web/userindex` | Landing page with service categories and featured products |
| Shop | `/web/userproductlist` | Product listing with filters |
| Product Detail | `/web/usersingle` | Single product detail page |
| Cart | `/web/usercart` | Shopping cart |
| Blog | `/web/userblog` | Pet care blog/articles |
| Contact | `/web/usercontact` | Contact page |

### 8.2 Provider Frontend
| Page | URL | Description |
|------|-----|-------------|
| Dashboard | `/web/provider/dashboard` | Business analytics dashboard |

### 8.3 Admin Backend
| Page | URL | Description |
|------|-----|-------------|
| User Management | `/web/user` | Manage all users |
| Product Management | `/web/product` | Manage all products |
| Order Management | `/web/order` | Manage all orders |
| Company Management | `/web/company` | Manage service providers |
| Recharge Management | `/web/tmoney` | Approve recharge requests |
| Announcement | `/web/notice` | Publish announcements |
| Statistics | `/web/sta` | System statistics |
| Message | `/web/message` | Message management |

### 8.4 Standalone Pages
| Page | URL | Description |
|------|-----|-------------|
| Login | `/login` | Login page (no sidebar) |
| Register | `/toRegisterPage` | Registration page (no sidebar) |

---

## 9. New Feature Modules (Incremental Development)

### 9.1 User Favorites (userlike) — ✅ Implemented
- Users can favorite/unfavorite products
- Favorites list display
- Database: `userlike` table (userid + productid)

### 9.2 Order Evaluation (orderEvalute) — ✅ Implemented
- Users can rate (1-5 stars) and review completed orders
- Rating auto-updates provider's `avg_rating` and `rating_count` in company table
- Database: `order_evalute` table (content, rating, userid, orderid, companyid)

### 9.3 Customer-Provider Messaging (message) — ✅ Implemented
- Bidirectional messaging between users and providers
- Auto-notification on order creation (system sends message to provider)
- Database: `message` table (content, senderid, receiveid, status)

### 9.4 AI Pet Knowledge Q&A — ❌ Pending
- AI-powered pet knowledge question answering
- Not yet implemented
- Planned: integrate external AI API, build pet knowledge base, frontend Q&A interface
- Key risks: AI response time (target ≤3s), answer accuracy (target ≥80%)

---

## 10. Deployment & DevOps

### 10.1 Docker Deployment
- `Dockerfile`: Spring Boot application containerization
- `docker-compose.yml`: Orchestrates app + MySQL + Redis + Nginx
- `docker-compose.prod.yml`: Production configuration
- `docker-compose.dev.yml`: Development configuration

### 10.2 Nginx Configuration
- Reverse proxy to Spring Boot (port 7002)
- Static resource caching
- Located at: `config/nginx.conf`

### 10.3 Monitoring
- Prometheus metrics endpoint via Spring Boot Actuator
- Configuration: `config/prometheus.yml`

### 10.4 CI/CD
- GitHub Actions workflows
- `.github/workflows/ci-cd.yml`: Continuous integration
- `.github/workflows/cd-docker.yml`: Docker deployment

---

## 11. Project Directory Structure

```
myJavaProject/
├── src/main/java/com/javaPro/myProject/
│   ├── SchedulingApplication.java          # Spring Boot entry point
│   ├── common/                             # Shared components
│   │   ├── co/                             # Constants
│   │   ├── config/                         # WebMvc configuration
│   │   ├── controller/                     # Base controller
│   │   ├── handle/                         # Login interceptor
│   │   ├── model/                          # AjaxResult, R, ListByPage
│   │   └── util/                           # AES, MD5, Date, Upload utils
│   ├── modules/                            # Business modules (14 existing + new)
│   │   ├── login/                          # Authentication
│   │   ├── sysuser/                        # User management
│   │   ├── company/                        # Provider management
│   │   ├── product/                        # Product/service management
│   │   ├── producttype/                    # Product category
│   │   ├── shopcart/                       # Shopping cart
│   │   ├── order/                          # Order management
│   │   ├── orderEvalute/                   # Order evaluation (NEW)
│   │   ├── tmoney/                         # Recharge management
│   │   ├── message/                        # Messaging (NEW)
│   │   ├── userlike/                       # Favorites (NEW)
│   │   ├── webnotice/                      # Announcements
│   │   ├── comment/                        # Comments
│   │   ├── type/                           # Generic types
│   │   ├── provider/                       # Provider dashboard
│   │   ├── web/                            # Page routing
│   │   └── common/                         # File upload service
│   └── service/                            # Shared services
├── src/main/resources/
│   ├── mapper/                             # MyBatis XML mappers (12 files)
│   ├── templates/                          # Thymeleaf HTML templates
│   │   ├── webSite/                        # User frontend pages
│   │   ├── login/, register/               # Auth pages
│   │   ├── hAdmin/                         # Admin UI framework
│   │   └── [module]/index.html             # Admin module pages
│   ├── static/                             # CSS, JS, images
│   └── application.properties              # Configuration
├── src/test/                               # Test code
├── scripts/init-db.sql                     # Database initialization
├── config/                                 # Nginx, Redis, Prometheus configs
├── Dockerfile                              # Application Docker image
├── docker-compose.yml                      # Docker orchestration
└── pom.xml                                 # Maven dependencies
```

---

## 12. Testing Infrastructure

| Test Type | Tool | Coverage |
|-----------|------|----------|
| Unit Tests | JUnit 5 + Mockito | Core service layer |
| Integration Tests | Spring Boot Test + H2 | API endpoints |
| UI Tests | Selenium + WebDriverManager | Login flow, page interactions |
| Coverage | JaCoCo | Test coverage reports |
| API Testing | REST Assured | API contract verification |
