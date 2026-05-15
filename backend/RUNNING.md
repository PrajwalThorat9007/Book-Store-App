# 🚀 Running the Bookstore Backend

## ✅ Current Status: **RUNNING SUCCESSFULLY!**

The boilerplate is fully functional and ready for team collaboration!

## 📋 Prerequisites

- Java 17 or higher
- Maven 3.6+

## 🏃 How to Run

### Option 1: Using Maven
```bash
cd backend
mvn spring-boot:run
```

### Option 2: Using JAR
```bash
cd backend
mvn clean package -DskipTests
java -jar target/bookstore-app-0.0.1-SNAPSHOT.jar
```

## ✅ Verify It's Running

### 1. Check Health Endpoint
```bash
curl http://localhost:8080/api/health
```

**Expected Response:**
```json
{
  "status": "UP",
  "message": "Bookstore Backend is running successfully!",
  "timestamp": "2026-05-15T11:05:13.482863",
  "version": "0.0.1-SNAPSHOT"
}
```

### 2. Check Welcome Endpoint
```bash
curl http://localhost:8080/api/
```

**Expected Response:**
```json
{
  "message": "Welcome to Bookstore API",
  "health": "/api/health",
  "h2Console": "/h2-console",
  "documentation": "See IMPLEMENTATION_GUIDE.md for details"
}
```

### 3. Access Swagger UI (API Documentation)
Open in browser: **http://localhost:8080/swagger-ui.html**

Features:
- Interactive API documentation
- Test endpoints directly from browser
- View request/response schemas
- JWT authentication support

### 4. Access OpenAPI JSON
```bash
curl http://localhost:8080/v3/api-docs
```

### 5. Access H2 Database Console
Open in browser: http://localhost:8080/h2-console

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:bookstore`
- Username: `sa`
- Password: (leave empty)

## 📊 What's Working

✅ **Application Starts Successfully**
- Spring Boot 3.2.0
- Port 8080
- H2 In-Memory Database

✅ **Database Tables Created**
- users
- products
- carts
- cart_items
- wishlists
- wishlist_items
- orders
- order_items
- customer_profiles
- addresses
- feedback

✅ **Security Configured**
- Basic security setup (currently permits all requests)
- BCrypt password encoder ready
- JWT dependencies included

✅ **API Endpoints**
- `/api/` - Welcome message
- `/api/health` - Health check
- `/swagger-ui.html` - Interactive API documentation
- `/v3/api-docs` - OpenAPI JSON specification
- `/h2-console` - Database console

## 🎯 What's Ready for Implementation

All module boilerplates are created with:
- ✅ Proper package structure
- ✅ Entity stubs (compilable)
- ✅ Repository interfaces
- ✅ Service classes with TODO comments
- ✅ Controller classes with endpoint specifications
- ✅ DTO classes with field requirements

## 📝 Next Steps for Team

1. **Pick a Module** from:
   - Cart
   - Wishlist
   - Customer
   - Order
   - Feedback
   - Admin

2. **Implement Following the TODOs**:
   - Complete entity fields and relationships
   - Implement repository methods
   - Add business logic to services
   - Create controller endpoints
   - Define DTO fields with validation

3. **Test Your Work**:
   - Use Postman or curl
   - Access H2 console to verify data
   - Write unit tests

## 🔧 Configuration

### Database
- **Type**: H2 (in-memory)
- **URL**: jdbc:h2:mem:bookstore
- **Console**: http://localhost:8080/h2-console

### Server
- **Port**: 8080
- **Context Path**: /

### JWT
- **Secret**: Configured in application.properties
- **Expiration**: 24 hours (86400000 ms)

## 📚 Documentation

- **Implementation Guide**: `IMPLEMENTATION_GUIDE.md`
- **Boilerplate Summary**: `BOILERPLATE_SUMMARY.md`
- **API Documentation**: Coming soon (Swagger/OpenAPI)

## 🐛 Troubleshooting

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Build Fails
```bash
# Clean and rebuild
mvn clean install -DskipTests
```

### Application Won't Start
- Check Java version: `java -version` (should be 17+)
- Check Maven version: `mvn -version` (should be 3.6+)
- Check logs in console output

## 🎉 Success Indicators

When the application starts successfully, you'll see:

```
==============================================
🚀 Bookstore Application Started Successfully!
📍 Server running at: http://localhost:8080
📊 H2 Console: http://localhost:8080/h2-console
==============================================
```

## 💡 Tips

- Use H2 console to inspect database tables
- Check application logs for debugging
- All endpoints currently permit access (security is minimal for development)
- Implement authentication/authorization as you build modules

---

**The boilerplate is ready! Start building! 🚀**
