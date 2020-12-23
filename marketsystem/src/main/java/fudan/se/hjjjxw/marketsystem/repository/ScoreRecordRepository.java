package fudan.se.hjjjxw.marketsystem.repository;

import fudan.se.hjjjxw.marketsystem.entity.ScoreRecord;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScoreRecordRepository extends CrudRepository<ScoreRecord, Integer> {
}
