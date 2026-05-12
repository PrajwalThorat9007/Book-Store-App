# BookStore Frontend

React + Vite application for BookStore team project.

## Setup

```bash
npm install
npm run dev
```

## Project Structure

```
frontend/
├── public/
├── src/
│   ├── assets/
│   ├── components/
│   │   ├── Navbar.jsx
│   │   └── Footer.jsx
│   ├── pages/
│   │   ├── Home.jsx
│   │   ├── Books.jsx
│   │   ├── BookDetails.jsx
│   │   ├── Login.jsx
│   │   ├── Register.jsx
│   │   ├── Cart.jsx
│   │   ├── Checkout.jsx
│   │   ├── Orders.jsx
│   │   └── NotFound.jsx
│   ├── routes/
│   │   └── AppRoutes.jsx
│   ├── layouts/
│   │   └── MainLayout.jsx
│   ├── App.jsx
│   ├── main.jsx
│   └── index.css
├── package.json
└── vite.config.js
```

## Routes

- `/` → Home
- `/books` → Books
- `/books/:id` → BookDetails
- `/login` → Login
- `/register` → Register
- `/cart` → Cart
- `/checkout` → Checkout
- `/orders` → Orders
- `*` → NotFound

## Note

This is a basic skeleton setup. No business logic, API integration, or backend has been implemented yet.
