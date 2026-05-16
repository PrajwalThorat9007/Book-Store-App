package com.bookstore.modules.cart.repository;

import com.bookstore.entity.Cart;
import com.bookstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(Long userId);

    // Manas: Added findByUser — OrderService calls this to get cart by User object directly
    Optional<Cart> findByUser(User user);
}