package by.bsuir.controllers;

import by.bsuir.entity.Item;
import by.bsuir.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart/recovery")
    public List<Item> cartRecovery() {
        return cartService.recoverCart();
    }

    @PutMapping("/cart")
    public boolean putToCart(@RequestBody Long id) {
        return cartService.putItem(id);
    }

    @DeleteMapping("/cart")
    public boolean deleteFromCart(@RequestBody Long id) {
        return cartService.deleteItem(id);
    }

    @GetMapping("/cart")
    public boolean payForCart() {
        return cartService.pay();
    }

}
