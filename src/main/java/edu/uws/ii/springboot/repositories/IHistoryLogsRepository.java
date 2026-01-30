package edu.uws.ii.springboot.repositories;

import edu.uws.ii.springboot.models.HistoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface IHistoryLogsRepository extends JpaRepository<HistoryLog,Long> {


}
