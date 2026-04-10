<div align="center">

# Houtu Admin

**Enterprise-grade Permission Management System Built on the Houtu Framework**

[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![JDK](https://img.shields.io/badge/JDK-8+-green.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3.x-4FC08D.svg)](https://vuejs.org/)
[![Element Plus](https://img.shields.io/badge/Element%20Plus-latest-409EFF.svg)](https://element-plus.org/)

A full-stack permission management system with separated frontend and backend, providing integrated access control for menus, roles, users, positions, and organizations to meet enterprise-level management platform requirements.

[Features](#-features) | [Screenshots](#-screenshots) | [Architecture](#-architecture) | [Quick Start](#-quick-start) | [Project Structure](#-project-structure) | [Contributing](#-contributing)

English | [中文](README-CN.md)

</div>

---

## Screenshots

### Login Page
![Login Page](docs/images/login.png)

### MFA Two-Factor Authentication
![MFA Authentication](docs/images/login-mfa.png)

### Main Page
![Main Page](docs/images/index.png)

### Profile
![Profile](docs/images/my.png)

---

## Features

### Permission Management
- **User Management** — CRUD operations, status management, password reset
- **Role Management** — Role assignment, menu permission binding, data permission control
- **Menu Management** — Multi-level menu configuration, button-level permission control, dynamic routing
- **Organization Management** — Tree-structured organization hierarchy, department level management
- **Position Management** — Position code and name maintenance, user-position assignment

### System Management
- **Dictionary Management** — System dictionary types and data maintenance with status control
- **Parameter Management** — System parameter configuration and dynamic modification
- **Announcement Management** — System announcement publishing and viewing

### Security & Audit
- **Login Authentication** — Based on Spring Security with CAPTCHA support (Kaptcha)
- **MFA Two-Factor Authentication** — Integrated with Google Authenticator (OTP), enabled via `spring.security.mfa=true` (disabled by default). When enabled, login requires secondary verification with QR code binding support
- **Login Logs** — Records login time, IP, status, and other information
- **Operation Logs** — Critical operation audit trail

### Experience Enhancement
- **Multiple Layout Modes** — Supports normal mode, fullscreen mode, and mobile mode for different usage scenarios
- **Internationalization (i18n)** — Chinese/English bilingual switching based on Vue I18n
- **Font Size Switching** — Supports large, medium, and small font sizes to accommodate different visual preferences
- **Swagger Documentation** — Automatic API documentation generation based on SpringDoc OpenAPI

---

## Architecture

```
┌───────────────────── Frontend (mp-web) ───────────────────────┐
│  Vue 3 + Element Plus + Pinia + Vue Router + Vue I18n + Vite  │
└───────────────────────────────┬────────────────────────────────┘
                                │ RESTful API
┌───────────────────────────────┼──── Backend (mp) ─────────────────────────┐
│                               │                                           │
│  ┌── Security ────────┐  ┌── Data Layer ──────┐  ┌── Infrastructure ──┐  │
│  │ Spring Security    │  │ MyBatis Plus      │  │ Redis (Cache/Sess) │  │
│  │ Session (Redis)    │  │ MySQL             │  │ Caffeine (L2 Cache)│  │
│  │ Kaptcha CAPTCHA    │  │ HikariCP Pool     │  │ Log4j2 Logging     │  │
│  │ Google Auth (MFA)  │  └────────────────────┘  └────────────────────┘  │
│  └────────────────────┘                                                   │
│                                                                           │
│  ┌── Houtu Framework ────────────────────────────────────────────────┐    │
│  │ houtu-web (Unified param parsing / response wrapping / exception) │    │
│  │ houtu-web-swagger (SpringDoc OpenAPI documentation enhancement)    │    │
│  └───────────────────────────────────────────────────────────────────┘    │
└───────────────────────────────────────────────────────────────────────────┘
```

### Frontend Stack

| Technology | Description |
|------------|-------------|
| Vue 3 | Progressive JavaScript framework |
| Element Plus | Vue 3 UI component library |
| Pinia | Vue state management |
| Vue Router | Frontend routing |
| Vue I18n | Internationalization |
| Vite 5 | Next-generation frontend build tool |
| Axios | HTTP client |
| Iconify | Icon solution |

### Backend Stack

| Technology | Description |
|------------|-------------|
| JDK 8 | Java runtime environment |
| Spring Boot 2.7.x | Application framework |
| Spring Security | Security framework |
| Spring Session | Distributed session management |
| MyBatis Plus | Enhanced ORM framework |
| Redis + Lettuce | Cache & session storage |
| MySQL | Relational database |
| [Houtu](https://github.com/lujiafa/houtu-dependencies) | Enterprise-grade Java base framework |

---

## Quick Start

### Prerequisites

| Requirement | Version |
|-------------|---------|
| JDK | 8+ |
| Maven | 3.6+ |
| Node.js | 18+ |
| MySQL | 5.7+ / 8.0+ |
| Redis | 5.0+ |

### 1. Clone the Repository

```bash
git clone -b 2.7.0 https://github.com/lujiafa/houtu-admin.git
cd houtu-admin
```

### 2. Initialize the Database

Create a MySQL database and import the initialization script:

```bash
mysql -u root -p < docs/sql/base.sql
```

### 3. Start the Backend

```bash
cd mp

# Modify database and Redis connection configuration
# Edit src/main/resources/application-dev.yml

# Start
mvn spring-boot:run
```

The backend service starts at `http://localhost:9090` by default.

### 4. Start the Frontend

```bash
cd mp-web

npm install

npm run serve:dev
```

The frontend service starts at `http://localhost:81` by default.

---

## Project Structure

```
houtu-admin
├── mp/                          # Backend (Spring Boot)
│   └── src/main/java/
│       └── com/xx/mp/
│           ├── aspect/          # AOP aspects
│           ├── config/          # Configuration classes
│           │   └── security/    # Spring Security config
│           ├── module/
│           │   ├── base/        # Base module (Login/Menu/Profile/MFA)
│           │   └── sys/         # System management module
│           │       ├── controller/  # Controller layer
│           │       ├── dao/         # Data access layer
│           │       ├── entity/      # Entity classes
│           │       ├── service/     # Business logic layer
│           │       └── vo/          # View objects
│           ├── support/         # Common support
│           └── util/            # Utility classes
├── mp-web/                      # Frontend (Vue 3)
│   └── src/
│       ├── components/          # Shared components
│       ├── layout/              # Layout components
│       ├── locale/              # i18n resources
│       ├── router/              # Route configuration
│       ├── store/               # Pinia state management
│       ├── utils/               # Utility functions
│       └── views/               # Page views
│           ├── UserManage/      # User management
│           ├── RoleManage/      # Role management
│           ├── MenuManage/      # Menu management
│           ├── OrgManage/       # Organization management
│           ├── PostManage/      # Position management
│           ├── DictManage/      # Dictionary management
│           ├── ParamsManage/    # Parameter management
│           ├── Announcement/    # Announcement management
│           ├── LoginLog/        # Login logs
│           ├── OptLog/          # Operation logs
│           └── ...
└── docs/
    └── sql/                     # Database scripts
        └── base.sql
```

---

## Related Projects

| Project | Description |
|---------|-------------|
| [houtu-dependencies](https://github.com/lujiafa/houtu-dependencies) | Houtu Base Framework — Provides web enhancement, caching, security, Spring Cloud extensions and other enterprise-grade infrastructure |

---

## Contributing

Contributions of all kinds are welcome:

- **Report Issues** — Submit bugs or feature requests via [Issues](https://github.com/lujiafa/houtu-admin/issues)
- **Submit Code** — Fork the repository → Create a feature branch → Submit a Pull Request
- **Improve Documentation** — Fix errors, add examples, improve descriptions
- **Test & Feedback** — Test in different environments and report compatibility

---

## License

MIT License
