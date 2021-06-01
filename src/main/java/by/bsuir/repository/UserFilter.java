package by.bsuir.repository;

import by.bsuir.entity.Item;

import java.util.Set;

public interface UserFilter {

    Set<Item> filter(Item example);

}
