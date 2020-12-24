package fudan.se.hjjjxw.marketsystem.repository;

import fudan.se.hjjjxw.marketsystem.entity.Expert;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpertRepository extends CrudRepository<Expert, Integer> {

    Expert findByName(String name);
}
