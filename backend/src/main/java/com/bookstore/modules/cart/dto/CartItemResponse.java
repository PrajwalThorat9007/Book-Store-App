package com.bookstore.modules.cart.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartItemResponse {
    private Long cartItemId;
    private Long productId;
    private String title;
    private String author;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}
