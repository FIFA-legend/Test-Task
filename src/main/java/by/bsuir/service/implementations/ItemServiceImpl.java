package by.bsuir.service.implementations;

import by.bsuir.entity.Item;
import by.bsuir.entity.User;
import by.bsuir.repository.ItemRepository;
import by.bsuir.repository.UserFilter;
import by.bsuir.repository.UserRepository;
import by.bsuir.service.CartService;
import by.bsuir.service.ItemService;
import by.bsuir.service.MailService;
import by.bsuir.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequestScope
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    private final UserFilter userFilter;

    private final MailService mailService;

    private final CartService cartService;

    private final UserRepository userRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository itemRepository, UserFilter userFilter, MailService mailService, CartService cartService, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userFilter = userFilter;
        this.mailService = mailService;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @Override
    public Set<Item> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Set<Item> getAllByNameAndDescriptionAndTags(Item example) {
        return userFilter.filter(example);
    }

    @Override
    public Item getItem(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Override
    public Item save(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item update(Long id, Item item) {
        Item originalItem = itemRepository.findById(id).orElse(null);
        if (originalItem == null) return null;
        for (List<Item> itemList : cartService.getAllCarts().values()) {
            for (Item i : itemList) {
                if (compareItems(originalItem, i)) {
                    return null;
                }
            }
        }
        originalItem.setName(item.getName());
        originalItem.setDescription(item.getDescription());
        originalItem.setTags(item.getTags());
        return itemRepository.save(originalItem);
    }

    @Override
    public Item forceUpdate(Long id, Item item) {
        Item originalItem = itemRepository.findById(id).orElse(null);
        if (originalItem == null) return null;
        Item copy = copyItem(originalItem);
        originalItem.setName(item.getName());
        originalItem.setDescription(item.getDescription());
        originalItem.setTags(item.getTags());
        for (Map.Entry<String, List<Item>> entry : cartService.getAllCarts().entrySet()) {
            boolean flag = false;
            for (Item i : entry.getValue()) {
                if (compareItems(copy, i)) {
                    flag = true;
                    entry.getValue().remove(i);
                    String subject = "Changes in your shopping cart";
                    String message = message(originalItem, item);
                    User user = userRepository.findByUsername(entry.getKey());
                    mailService.sendEmail(user.getEmail(), subject, message);
                    break;
                }
            }
            if (flag) {
                entry.getValue().add(originalItem);
            }
        }
        return itemRepository.save(originalItem);
    }

    @Override
    public boolean delete(Long id) {
        itemRepository.deleteById(id);
        return !itemRepository.existsById(id);
    }

    private String message(Item oldItem, Item newItem) {
        return String.format("Instead of %s with description %s in your shopping cart now is %s with description %s",
                oldItem.getName(), oldItem.getDescription(), newItem.getName(), newItem.getDescription());
    }

    private boolean compareItems(Item i1, Item i2) {
        return i1.getName().equals(i2.getName())
                && i1.getDescription().equals(i2.getDescription())
                && i1.getTags().equals(i2.getTags());
    }

    private Item copyItem(Item i) {
        Item copy = new Item();
        copy.setName(i.getName());
        copy.setDescription(i.getDescription());
        copy.setTags(i.getTags());
        return copy;
    }
}
