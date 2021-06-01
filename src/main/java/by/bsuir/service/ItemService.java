package by.bsuir.service;

import by.bsuir.entity.Item;

import java.util.Set;

public interface ItemService {

    Set<Item> getAllItems();

    Set<Item> getAllByNameAndDescriptionAndTags(Item example);

    Item getItem(Long id);

    Item save(Item item);

    Item update(Long id, Item item);

    Item forceUpdate(Long id, Item item);

    boolean delete(Long id);

}
