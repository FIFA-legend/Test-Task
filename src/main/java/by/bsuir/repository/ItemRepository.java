package by.bsuir.repository;

import by.bsuir.entity.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Transactional
public interface ItemRepository extends CrudRepository<Item, Long> {

    Set<Item> findAll();

}
