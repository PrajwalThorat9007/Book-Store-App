# 🗄️ Supabase PostgreSQL Setup Guide

## 📋 Overview

This application supports both H2 (development) and Supabase PostgreSQL (production) databases.

## 🔧 Configuration Profiles

### Development (H2)
```bash
# Run with dev profile (default)
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production (Supabase)
```bash
# Run with prod profile
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 🚀 Supabase Setup Steps

### 1. Create Supabase Project

1. Go to [https://supabase.com](https://supabase.com)
2. Sign up / Log in
3. Click "New Project"
4. Fill in:
   - **Name**: bookstore-backend
   - **Database Password**: (save this!)
   - **Region**: Choose closest to you
5. Wait for project to be created (~2 minutes)

### 2. Get Database Connection Details

1. In your Supabase project dashboard
2. Go to **Settings** → **Database**
3. Find **Connection String** section
4. Copy the **URI** (looks like):
   ```
   postgresql://postgres:[YOUR-PASSWORD]@db.xxxxxxxxxxxxx.supabase.co:5432/postgres
   ```

### 3. Configure Application

#### Option A: Using Environment Variables (Recommended)

Set these environment variables:

```bash
export DATABASE_URL="jdbc:postgresql://db.xxxxxxxxxxxxx.supabase.co:5432/postgres"
export DATABASE_USERNAME="postgres"
export DATABASE_PASSWORD="your-password-here"
export JWT_SECRET="your-secure-jwt-secret-minimum-256-bits"
export SPRING_PROFILES_ACTIVE="prod"
```

#### Option B: Update application-prod.properties

Edit `src/main/resources/application-prod.properties`:

```properties
spring.datasource.url=jdbc:postgresql://db.xxxxxxxxxxxxx.supabase.co:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=your-password-here
jwt.secret=your-secure-jwt-secret-minimum-256-bits
```

⚠️ **Warning**: Don't commit passwords to Git!

### 4. Run with Production Profile

```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=prod

# Using JAR
java -jar -Dspring.profiles.active=prod target/bookstore-app-0.0.1-SNAPSHOT.jar
```

---

## 🔐 Security Best Practices

### 1. Use Environment Variables

Never hardcode credentials in properties files!

```bash
# .env file (add to .gitignore)
DATABASE_URL=jdbc:postgresql://...
DATABASE_USERNAME=postgres
DATABASE_PASSWORD=your-password
JWT_SECRET=your-secret-key
```

### 2. Generate Strong JWT Secret

```bash
# Generate a secure random key
openssl rand -base64 64
```

### 3. Enable SSL for Production

Add to `application-prod.properties`:
```properties
spring.datasource.url=jdbc:postgresql://...?sslmode=require
```

---

## 📊 Verify Supabase Connection

### 1. Check Application Logs

Look for:
```
Initialized JPA EntityManagerFactory for persistence unit 'default'
HikariPool-1 - Start completed.
```

### 2. Check Tables in Supabase

1. Go to Supabase Dashboard
2. Click **Table Editor**
3. You should see tables:
   - users
   - products
   - carts
   - orders
   - etc.

### 3. Test API

```bash
curl http://localhost:8080/api/health
```

---

## 🔄 Switching Between Profiles

### Development (H2)
```bash
# In application.properties
spring.profiles.active=dev

# Or via command line
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Production (Supabase)
```bash
# In application.properties
spring.profiles.active=prod

# Or via command line
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## 🐛 Troubleshooting

### Connection Refused
- Check Supabase project is active
- Verify connection string is correct
- Check firewall/network settings

### Authentication Failed
- Verify password is correct
- Check username (usually `postgres`)
- Ensure no special characters are URL-encoded

### SSL Error
- Add `?sslmode=require` to connection URL
- Or disable SSL for testing: `?sslmode=disable`

### Tables Not Created
- Check `spring.jpa.hibernate.ddl-auto=update` is set
- Verify database user has CREATE TABLE permissions
- Check application logs for errors

---

## 📝 Connection String Format

```
jdbc:postgresql://[HOST]:[PORT]/[DATABASE]?[OPTIONS]
```

Example:
```
jdbc:postgresql://db.abcdefghijk.supabase.co:5432/postgres?sslmode=require
```

Components:
- **HOST**: `db.abcdefghijk.supabase.co`
- **PORT**: `5432` (default PostgreSQL port)
- **DATABASE**: `postgres` (default database name)
- **OPTIONS**: `sslmode=require` (SSL configuration)

---

## 🚀 Deployment Options

### 1. Heroku
```bash
heroku config:set DATABASE_URL="jdbc:postgresql://..."
heroku config:set SPRING_PROFILES_ACTIVE="prod"
```

### 2. Railway
Add environment variables in Railway dashboard

### 3. AWS/Azure/GCP
Configure environment variables in your cloud platform

### 4. Docker
```dockerfile
ENV DATABASE_URL="jdbc:postgresql://..."
ENV SPRING_PROFILES_ACTIVE="prod"
```

---

## ✅ Checklist

- [ ] Supabase project created
- [ ] Database connection string obtained
- [ ] Environment variables configured
- [ ] JWT secret generated
- [ ] Application runs with prod profile
- [ ] Tables created in Supabase
- [ ] API endpoints working
- [ ] Credentials not committed to Git

---

## 📚 Additional Resources

- [Supabase Documentation](https://supabase.com/docs)
- [Spring Boot Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- [PostgreSQL JDBC Driver](https://jdbc.postgresql.org/documentation/)

---

**Need Help?** Check the Supabase dashboard logs and Spring Boot application logs for detailed error messages.
