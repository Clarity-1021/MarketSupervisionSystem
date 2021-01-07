package fudan.se.hjjjxw.marketsystem.entity;

import java.util.Date;
import java.util.List;

public interface ICheck {

    /**
     * 抽检产品分类，并上报抽检结果
     * @param productCategory
     * @param checkTask
     */
    void checkProductCategory(ProductCategory productCategory, CheckTask checkTask, int unqualifiedCount, Date checkDate);

    /**
     * 获取未完成的抽检任务
     * @return
     */
    List<CheckTask> getUnfinishedCheckTask();

    /**
     * 获取已完成的抽检任务
     * @return
     */
    List<CheckTask> getFinishedCheckTask();

    /**
     * 增加分数记录
     * @param scoreRecord
     */
    void addScoreRecord(ScoreRecord scoreRecord);

    int getTotalScore();

    List<CheckTask> getAllCheckTask();

    void setScoreRecordList(List<ScoreRecord> scoreRecordList);
}
