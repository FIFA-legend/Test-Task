package by.bsuir.repository;

import by.bsuir.entity.Item;
import by.bsuir.entity.Tag;
import by.bsuir.entity.User;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Repository
public class UserFilterImpl implements UserFilter {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public UserFilterImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    @Transactional
    public Set<Item> filter(Item example) {
        EntityManager em = entityManagerFactory.createEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();

        CriteriaQuery<Item> itemCriteria = cb.createQuery(Item.class);
        Root<Item> itemRoot = itemCriteria.from(Item.class);

        List<Predicate> predicates = new LinkedList<>();
        predicates.add(nameFilter(cb, example, itemRoot));
        predicates.add(descriptionFilter(cb, example, itemRoot));

        if (example.getTags() != null && example.getTags().size() > 0) {
            predicates.add(tagFilter(cb, example, itemRoot));
        }

        itemCriteria.select(itemRoot).where(predicates.toArray(new Predicate[0]));
        List<Item> result = em.createQuery(itemCriteria).getResultList();
        em.close();
        return new HashSet<>(result);
    }

    private Predicate nameFilter(CriteriaBuilder cb, Item example, Root<Item> root) {
        if (example.getName() == null || example.getName().isEmpty()) {
            return cb.like(root.get("name"), "%");
        } else {
            return cb.like(root.get("name"), example.getName() + "%");
        }
    }

    private Predicate descriptionFilter(CriteriaBuilder cb, Item example, Root<Item> root) {
        if (example.getDescription() == null || example.getDescription().isEmpty()) {
            return cb.like(root.get("description"), "%");
        } else {
            return cb.like(root.get("description"), example.getDescription() + "%");
        }
    }

    private Predicate tagFilter(CriteriaBuilder cb, Item example, Root<Item> root) {
        Join<Item, Tag> tagJoin = root.join("tags");
        return tagJoin.in(example.getTags());
    }

}
