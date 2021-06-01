package by.bsuir.service;

import by.bsuir.entity.Item;

import java.util.List;
import java.util.Map;

public interface CartService {

    Map<String, List<Item>> getAllCarts();

    List<Item> recoverCart();

    boolean putItem(Long id);

    boolean deleteItem(Long id);

    boolean pay();

}
