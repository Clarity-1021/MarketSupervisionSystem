package fudan.se.hjjjxw.marketsystem.entity;

import java.util.Date;
import java.util.List;

public interface ICheck {

    void checkProductCategory(ProductCategory productCategory, CheckTask checkTask, int unqualifiedCount, Date checkDate);

    List<CheckTask> getUnfinishedCheckTask();

    List<CheckTask> getFinishedCheckTask();

    void addScoreRecord(ScoreRecord scoreRecord);

    int getTotalScore();

    List<CheckTask> getAllCheckTask();

    void setScoreRecordList(List<ScoreRecord> scoreRecordList);
}
