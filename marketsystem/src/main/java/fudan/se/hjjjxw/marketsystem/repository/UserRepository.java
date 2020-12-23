package fudan.se.hjjjxw.marketsystem.repository;

import fudan.se.hjjjxw.marketsystem.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findUserById(int id);

    User findUserByName(String name);
}
