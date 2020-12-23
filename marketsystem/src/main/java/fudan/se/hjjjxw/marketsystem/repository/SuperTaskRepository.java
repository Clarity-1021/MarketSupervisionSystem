package fudan.se.hjjjxw.marketsystem.repository;

import fudan.se.hjjjxw.marketsystem.entity.SuperTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperTaskRepository extends CrudRepository<SuperTask, Integer> {
}
