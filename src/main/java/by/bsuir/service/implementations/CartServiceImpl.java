package by.bsuir.service.implementations;

import by.bsuir.entity.Item;
import by.bsuir.entity.User;
import by.bsuir.repository.ItemRepository;
import by.bsuir.repository.UserRepository;
import by.bsuir.service.CartService;
import by.bsuir.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@RequestScope
public class CartServiceImpl implements CartService {

    private static final Map<String, List<Item>> shoppingCarts = new HashMap<>();

    private final String username;

    private final ItemRepository itemRepository;

    private final UserRepository userRepository;

    private final MailService mailService;

    @Autowired
    public CartServiceImpl(ItemRepository itemRepository, UserRepository userRepository, MailService mailService) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        this.username = auth.getName();
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.mailService = mailService;
    }

    @Override
    public Map<String, List<Item>> getAllCarts() {
        return shoppingCarts;
    }

    @Override
    public List<Item> recoverCart() {
        List<Item> items = findShoppingCart();
        if (items.size() == 0) return null;
        return items;
    }

    @Override
    public boolean putItem(Long id) {
        List<Item> items = findShoppingCart();
        Item item = itemRepository.findById(id).orElse(null);
        if (item == null) return false;
        for (Item i : items) {
            if (item.equals(i)) return false;
        }
        items.add(item);
        return true;
    }

    @Override
    public boolean deleteItem(Long id) {
        List<Item> items = findShoppingCart();
        for (Item item : items) {
            if (item.getId().equals(id)) {
                items.remove(item);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean pay() {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            String email = user.getEmail();
            String subject = "Your shop cart";
            mailService.sendEmail(email, subject, formShopCartMessage());
            shoppingCarts.remove(username);
            return true;
        }
        return false;
    }

    private List<Item> findShoppingCart() {
        for (Map.Entry<String, List<Item>> entry : shoppingCarts.entrySet()) {
            if (entry.getKey().equals(username)) {
                return entry.getValue();
            }
        }
        List<Item> newCart = new LinkedList<>();
        shoppingCarts.put(username, newCart);
        return newCart;
    }

    private String formShopCartMessage() {
        List<Item> items = findShoppingCart();
        String messageBody = "";
        int i = 1;
        for (Item item : items) {
            messageBody = messageBody.concat(i + ". " + item.getName() + "\n" + item.getDescription() + "\n");
            i++;
        }
        return messageBody;
    }

}