# 📝 Blog API

A secure blogging REST API built with **Spring Boot 4**, **Spring Security** and **JWT**. Users write posts, only the author can edit or delete a post, and admins have their own protected endpoints.

![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?logo=springboot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?logo=springsecurity&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white)

## ✨ Features

- **JWT authentication** with a custom filter, plus hashed passwords
- **Role-based access control**: `ROLE_USER` and `ROLE_ADMIN`, enforced with `@PreAuthorize` and in `SecurityConfig`
- **Ownership checks**: only a post's author can update or delete it
- **Public reads**: anyone can list and read posts without logging in
- **Validation** (`@Valid`, `@NotBlank`, `@Email`) and a **global exception handler**
- **DTOs** for responses, so password hashes never leave the API

## 🔌 API endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/auth/register` | Public | Create an account, returns a JWT |
| `POST` | `/auth/login` | Public | Log in, returns a JWT |
| `GET` | `/posts` | Public | List posts |
| `GET` | `/posts/{id}` | Public | Read a post |
| `POST` | `/posts` | Logged in | Create a post |
| `PUT` | `/posts/{id}` | Author only | Update your post |
| `DELETE` | `/posts/{id}` | Author only | Delete your post |
| `GET` | `/admin/users` | Admin only | List all users |

## 🏗️ Architecture

```
src/main/java/com/anjan/blog_api
├── controller/   AuthController, PostController, AdminController
├── service/      AuthService, PostService, UserDetailsServiceImpl
├── security/     SecurityConfig, JwtFilter, JwtUtil
├── repository/   UserRepository, PostRepository
├── entity/       User, Post, Role
├── dto/          Request and response objects
└── exception/    GlobalExceptionHandler
```

## 🚀 Getting started

**Prerequisites:** Java 21 and MySQL 8

```sql
CREATE DATABASE blogdb;
```

Copy the config template and fill in your own values (the real file is git-ignored):

```bash
mkdir -p src/main/resources
cp application.properties.example src/main/resources/application.properties
```

Set your database password, and set `app.jwt.secret` to a long random string (`openssl rand -base64 48`). Then run:

```bash
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

## 🧪 Try it

Ready-made requests are in [`test.http`](test.http). Or use curl:

```bash
TOKEN=$(curl -s -X POST localhost:8080/auth/register -H "Content-Type: application/json" \
  -d '{"username":"anjan","email":"anjan@example.com","password":"secret123"}' | jq -r .token)

curl -X POST localhost:8080/posts -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" -d '{"title":"First post","content":"Hello, world!"}'

curl localhost:8080/posts
```

## 🛣️ Roadmap
- [ ] Comments and tags
- [ ] Pagination and search
- [ ] OpenAPI / Swagger UI
- [ ] Tests for the security rules (`@WithMockUser`)

## 👤 Author
**Anjan Rimal** · [Portfolio](https://anjanrimal.com/) · [LinkedIn](https://www.linkedin.com/in/anjanrimal) · [GitHub](https://github.com/AnjanRimal)
