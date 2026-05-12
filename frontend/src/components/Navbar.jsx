import { Link } from 'react-router-dom'

const Navbar = () => {
  return (
    <nav style={styles.nav}>
      <div style={styles.container}>
        <Link to="/" style={styles.logo}>BookStore</Link>
        <ul style={styles.navList}>
          <li><Link to="/" style={styles.link}>Home</Link></li>
          <li><Link to="/books" style={styles.link}>Books</Link></li>
          <li><Link to="/cart" style={styles.link}>Cart</Link></li>
          <li><Link to="/orders" style={styles.link}>Orders</Link></li>
          <li><Link to="/login" style={styles.link}>Login</Link></li>
          <li><Link to="/register" style={styles.link}>Register</Link></li>
        </ul>
      </div>
    </nav>
  )
}

const styles = {
  nav: {
    backgroundColor: '#333',
    padding: '1rem 0',
  },
  container: {
    maxWidth: '1200px',
    margin: '0 auto',
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: '0 20px',
  },
  logo: {
    color: 'white',
    fontSize: '1.5rem',
    fontWeight: 'bold',
    textDecoration: 'none',
  },
  navList: {
    display: 'flex',
    listStyle: 'none',
    gap: '20px',
  },
  link: {
    color: 'white',
    textDecoration: 'none',
  }
}

export default Navbar
