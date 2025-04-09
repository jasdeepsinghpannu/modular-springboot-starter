# 🚀 Modular Spring Boot Starter

A clean and scalable Spring Boot backend template featuring modular architecture, JWT authentication, and environment-specific configuration using SQLite for development and MySQL for production.

---

## 🎯 Purpose

Designed as a personal boilerplate for backend projects following best practices in enterprise architecture and clean code structure.

---

## 🧱 Tech Stack

- **Java 17**
- **Spring Boot**
- **Spring Security + JWT**
- **Spring Data JPA + Hibernate**
- **SQLite (Dev) / MySQL (Prod)**
- **SLF4J + Logback (Logging)**
- **Layered + Modular Package Structure**

---

## 📁 Project Structure

```
src/main/java/com/space/
├── auth/               # JWT logic, login, registration
├── user/               # Sample user module (CRUD, RBAC)
├── config/             # Security, database, and profile configs
├── common/             # Constants, exceptions, utility helpers
└── SpaceApplication.java
```

---

## 🚀 Getting Started

```bash
git clone https://github.com/jasdeepsinghpannu/modular-springboot-starter.git
cd modular-springboot-starter
./mvnw spring-boot:run
```

By default, the application runs on: [http://localhost:8080](http://localhost:8080)

---

## ✅ Features

- JWT Auth (Register/Login)
- Role-based access control
- DTOs + Entity separation
- Profile-based config (`dev`, `prod`)
- Global exception handling
- Lightweight logging setup

---

## 📄 License

This project is open for educational and personal use.  
Licensed under the [MIT License](LICENSE).