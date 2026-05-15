# Bookstore Backend - Implementation Guide

## 📋 Overview
This is a boilerplate structure for the Bookstore backend application. All files have been created with proper package declarations and TODO comments indicating what needs to be implemented.

## 🏗️ Project Structure

```
src/main/java/com/bookstore/
├── BookStoreApplication.java          # ✅ Main entry point (already implemented)
│
├── entity/                            # ✅ Common JPA entities (shared across modules)
│   ├── User.java                      # ✅ Implemented
│   ├── Product.java                   # ✅ Implemented
│   ├── Cart.java                      # ⏳ TODO
│   ├── CartItem.java                  # ⏳ TODO
│   ├── Wishlist.java                  # ⏳ TODO
│   ├── WishlistItem.java              # ⏳ TODO
│   ├── Order.java                     # ⏳ TODO
│   ├── OrderItem.java                 # ⏳ TODO
│   ├── CustomerProfile.java           # ⏳ TODO
│   ├── Address.java                   # ⏳ TODO
│   ├── Feedback.java                  # ⏳ TODO
│   └── Category.java                  # ⏳ TODO
│
├── config/                            # Configuration classes
│   ├── SwaggerConfig.java             # ⏳ TODO: OpenAPI/Swagger setup
│   └── CorsConfig.java                # ⏳ TODO: CORS for React frontend
│
├── common/                            # Shared utilities
│   ├── ApiResponse.java               # ⏳ TODO: Generic response wrapper
│   ├── AppConstants.java              # ⏳ TODO: JWT, roles, pagination constants
│   └── OrderStatus.java               # ⏳ TODO: Order status enum
│
├── security/                          # Security infrastructure
│   ├── SecurityConfig.java            # ✅ Implemented
│   ├── JwtUtil.java                   # ✅ Implemented
│   ├── JwtAuthFilter.java             # ✅ Implemented
│   └── UserDetailsServiceImpl.java    # ✅ Implemented
│
├── exception/                         # Global exception handling
│   ├── GlobalExceptionHandler.java    # ✅ Implemented
│   ├── ResourceNotFoundException.java # ✅ Implemented
│   └── BadRequestException.java       # ✅ Implemented
│
└── modules/                           # Feature modules
    │
    ├── user/                          # User & Authentication module
    │   ├── entity/                    # (Use common entity package)
    │   ├── repository/
    │   │   └── UserRepository.java    # ✅ Implemented
    │   ├── dto/
    │   │   ├── RegisterRequest.java   # ✅ Implemented
    │   │   ├── LoginRequest.java      # ✅ Implemented
    │   │   ├── UserResponse.java      # ✅ Implemented
    │   │   └── AuthResponse.java      # ✅ Implemented
    │   ├── service/
    │   │   ├── AuthService.java       # ✅ Implemented
    │   │   └── UserService.java       # ✅ Implemented
    │   └── controller/
    │       ├── AuthController.java    # ✅ Implemented
    │       └── UserController.java    # ✅ Implemented
    │
    ├── product/                       # Product/Book module
    │   ├── repository/
    │   │   ├── ProductRepository.java # ✅ Implemented
    │   │   └── CategoryRepository.java # ⏳ TODO
    │   ├── dto/
    │   │   ├── ProductResponse.java   # ✅ Implemented
    │   │   └── ProductRequest.java    # ⏳ TODO
    │   ├── service/
    │   │   └── ProductService.java    # ✅ Implemented
    │   └── controller/
    │       └── ProductController.java # ✅ Implemented
    │
    ├── cart/                          # Shopping cart module
    │   ├── repository/
    │   │   ├── CartRepository.java    # ⏳ TODO (boilerplate created)
    │   │   └── CartItemRepository.java # ⏳ TODO (boilerplate created)
    │   ├── dto/
    │   │   ├── CartResponse.java      # ⏳ TODO (boilerplate created)
    │   │   ├── CartItemResponse.java  # ⏳ TODO (boilerplate created)
    │   │   ├── AddToCartRequest.java  # ⏳ TODO (boilerplate created)
    │   │   └── UpdateCartItemRequest.java # ⏳ TODO (boilerplate created)
    │   ├── service/
    │   │   └── CartService.java       # ⏳ TODO (boilerplate created)
    │   └── controller/
    │       └── CartController.java    # ⏳ TODO (boilerplate created)
    │
    ├── wishlist/                      # Wishlist module
    │   ├── repository/
    │   │   ├── WishlistRepository.java # ⏳ TODO (boilerplate created)
    │   │   └── WishlistItemRepository.java # ⏳ TODO (boilerplate created)
    │   ├── dto/
    │   │   ├── WishlistResponse.java  # ⏳ TODO (boilerplate created)
    │   │   └── WishlistItemResponse.java # ⏳ TODO (boilerplate created)
    │   ├── service/
    │   │   └── WishlistService.java   # ⏳ TODO (boilerplate created)
    │   └── controller/
    │       └── WishlistController.java # ⏳ TODO (boilerplate created)
    │
    ├── customer/                      # Customer profile & addresses
    │   ├── repository/
    │   │   ├── CustomerProfileRepository.java # ⏳ TODO (boilerplate created)
    │   │   └── AddressRepository.java # ⏳ TODO (boilerplate created)
    │   ├── dto/
    │   │   ├── CustomerDetailsRequest.java # ⏳ TODO (boilerplate created)
    │   │   ├── CustomerDetailsResponse.java # ⏳ TODO (boilerplate created)
    │   │   ├── AddressRequest.java    # ⏳ TODO (boilerplate created)
    │   │   └── AddressResponse.java   # ⏳ TODO (boilerplate created)
    │   ├── service/
    │   │   └── CustomerService.java   # ⏳ TODO (boilerplate created)
    │   └── controller/
    │       └── CustomerController.java # ⏳ TODO (boilerplate created)
    │
    ├── order/                         # Order management module
    │   ├── repository/
    │   │   ├── OrderRepository.java   # ⏳ TODO (boilerplate created)
    │   │   └── OrderItemRepository.java # ⏳ TODO (boilerplate created)
    │   ├── dto/
    │   │   ├── OrderRequest.java      # ⏳ TODO (boilerplate created)
    │   │   ├── OrderResponse.java     # ⏳ TODO (boilerplate created)
    │   │   ├── OrderItemResponse.java # ⏳ TODO (boilerplate created)
    │   │   └── OrderStatusUpdateRequest.java # ⏳ TODO (boilerplate created)
    │   ├── service/
    │   │   └── OrderService.java      # ⏳ TODO (boilerplate created)
    │   └── controller/
    │       └── OrderController.java   # ⏳ TODO (boilerplate created)
    │
    ├── feedback/                      # Product reviews & ratings
    │   ├── repository/
    │   │   └── FeedbackRepository.java # ⏳ TODO (boilerplate created)
    │   ├── dto/
    │   │   ├── FeedbackRequest.java   # ⏳ TODO (boilerplate created)
    │   │   ├── FeedbackResponse.java  # ⏳ TODO (boilerplate created)
    │   │   └── RatingSummary.java     # ⏳ TODO (boilerplate created)
    │   ├── service/
    │   │   └── FeedbackService.java   # ⏳ TODO (boilerplate created)
    │   └── controller/
    │       └── FeedbackController.java # ⏳ TODO (boilerplate created)
    │
    └── admin/                         # Admin dashboard & management
        ├── dto/
        │   ├── DashboardResponse.java # ⏳ TODO (boilerplate created)
        │   └── UserListResponse.java  # ⏳ TODO (boilerplate created)
        ├── service/
        │   └── AdminService.java      # ⏳ TODO (boilerplate created)
        └── controller/
            └── AdminController.java   # ⏳ TODO (boilerplate created)
```

## 🎯 Implementation Priority

### Phase 1: Core Setup (Week 1)
1. ✅ User & Authentication (Already implemented)
2. ⏳ Common utilities (ApiResponse, AppConstants, OrderStatus)
3. ⏳ Config (SwaggerConfig, CorsConfig)
4. ⏳ Product entities and Category

### Phase 2: Shopping Features (Week 2)
5. ⏳ Cart module (entities, repositories, services, controllers)
6. ⏳ Wishlist module
7. ⏳ Customer profile & addresses

### Phase 3: Order Management (Week 3)
8. ⏳ Order module (place order, order history, cancel)
9. ⏳ Order status management (admin)

### Phase 4: Reviews & Admin (Week 4)
10. ⏳ Feedback module (reviews & ratings)
11. ⏳ Admin dashboard & analytics

## 📝 Implementation Guidelines

### For Each Module:

1. **Entities** (in `/entity` package)
   - Add JPA annotations (@Entity, @Table, @Id, etc.)
   - Define relationships (@OneToMany, @ManyToOne, etc.)
   - Add validation annotations
   - Include timestamps (createdAt, updatedAt)

2. **DTOs** (in module-specific `/dto` packages)
   - Add validation annotations (@NotNull, @NotBlank, @Email, etc.)
   - Use records or @Data from Lombok for cleaner code
   - Separate Request and Response DTOs

3. **Repositories** (in module-specific `/repository` packages)
   - Extend JpaRepository<Entity, ID>
   - Add custom query methods
   - Use @Query for complex queries

4. **Services** (in module-specific `/service` packages)
   - Add @Service annotation
   - Inject repositories via constructor
   - Implement business logic
   - Handle exceptions
   - Add @Transactional where needed

5. **Controllers** (in module-specific `/controller` packages)
   - Add @RestController and @RequestMapping
   - Use proper HTTP methods (@GetMapping, @PostMapping, etc.)
   - Add security annotations (@PreAuthorize)
   - Add validation (@Valid)
   - Return proper HTTP status codes
   - Use ApiResponse wrapper

## 🔐 Security Notes

- All endpoints except `/api/auth/**` require authentication
- Use `@PreAuthorize("hasRole('USER')")` for user endpoints
- Use `@PreAuthorize("hasRole('ADMIN')")` for admin endpoints
- Get current user from SecurityContext:
  ```java
  Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  String email = auth.getName();
  ```

## 🗄️ Database Configuration

Update `application.properties`:
```properties
# Database (H2 for dev, PostgreSQL for prod)
spring.datasource.url=jdbc:h2:mem:bookstore
spring.jpa.hibernate.ddl-auto=update

# JWT
jwt.secret=your-secret-key-here
jwt.expiration=86400000
```

## 🧪 Testing

For each module, create:
- Unit tests for services
- Integration tests for controllers
- Repository tests with @DataJpaTest

## 📚 Dependencies to Add

Check `pom.xml` and ensure you have:
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Validation
- JWT libraries (jjwt)
- Lombok (optional, for cleaner code)
- SpringDoc OpenAPI (for Swagger)
- Database driver (H2 for dev, PostgreSQL for prod)

## 🚀 Getting Started

1. Review the TODO comments in each file
2. Start with Phase 1 (common utilities and config)
3. Implement entities in the `/entity` package
4. Work module by module following the priority order
5. Test each module before moving to the next
6. Update this guide as you complete sections

## 💡 Tips

- Use Lombok to reduce boilerplate (@Data, @Builder, @NoArgsConstructor, @AllArgsConstructor)
- Consider using MapStruct for entity-DTO mapping
- Add proper logging with SLF4J
- Write meaningful commit messages
- Keep controllers thin, put business logic in services
- Use DTOs to avoid exposing entities directly

## 📞 Questions?

Each file contains detailed TODO comments explaining:
- What fields/methods to implement
- What dependencies to inject
- What business logic to handle
- What validations to add

Good luck with the implementation! 🎉
