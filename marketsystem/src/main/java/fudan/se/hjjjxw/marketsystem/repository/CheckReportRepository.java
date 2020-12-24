package fudan.se.hjjjxw.marketsystem.repository;

import fudan.se.hjjjxw.marketsystem.entity.CheckReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckReportRepository extends CrudRepository<CheckReport, Integer> {
}
