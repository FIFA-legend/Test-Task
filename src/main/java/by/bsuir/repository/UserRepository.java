package by.bsuir.repository;

import by.bsuir.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);

}
