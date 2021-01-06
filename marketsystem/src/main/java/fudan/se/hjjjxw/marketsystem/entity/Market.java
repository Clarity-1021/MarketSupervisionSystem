package fudan.se.hjjjxw.marketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@DynamicInsert(true)
public class Market implements Serializable, ICheck {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @Transient //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "market")//(fetch = FetchType.EAGER)
    private List<ScoreRecord> scoreRecordList = new ArrayList<>();

    @Transient //@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE,mappedBy = "market")
    private List<CheckTask> checkTaskSet = new ArrayList<>();

    public List<CheckTask> getCheckTaskSet() {
        return checkTaskSet;
    }

    public Market() {
    }

    public Market(String name){
        this.name = name;
    }

    public Market(Integer id, String name, List<ScoreRecord> scoreRecordList) {
        this.id = id;
        this.name = name;
        this.scoreRecordList = scoreRecordList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScoreRecord> getScoreRecordList() {
        return scoreRecordList;
    }

    public void setScoreRecordList(List<ScoreRecord> scoreRecordList) {
        this.scoreRecordList = scoreRecordList;
    }

    public boolean equals(Market market) {
        return name.equals(market.getName());
    }

    /**
     * 抽检产品分类，并上报抽检结果
     * @param productCategory
     * @param checkTask
     */
    @Override
    public void checkProductCategory(ProductCategory productCategory, CheckTask checkTask, int unqualifiedCount, Date checkDate) {
        CheckReport checkReport = new CheckReport(unqualifiedCount, checkDate, productCategory, checkTask);
        checkTask.addCheckReport(checkReport);
        if(checkTask.getUnfinishedProductCategories().isEmpty()) {
            checkTask.setFinished(true);
            List<CheckReport> checkReports = checkTask.getCheckReportSet();
            Date finishDate = checkReport.getCheckDate();
            for (CheckReport report : checkReports) {
                if (finishDate.compareTo(report.getCheckDate()) < 0) {
                    finishDate = report.getCheckDate();
                }
            }
            checkTask.setFinishDate(finishDate);
        }

    }

    /**
     * 获得未完成的抽检任务
     * @return
     */
    @Override
    public List<CheckTask> getUnfinishedCheckTask() {
        List<CheckTask> unfinishedList = new ArrayList<>();
        for(CheckTask task: this.checkTaskSet){
            if(!task.isFinished()) {
                unfinishedList.add(task);
            }
        }
        return unfinishedList;
    }

    @Override
    public List<CheckTask> getFinishedCheckTask() {
        List<CheckTask> finishedList = new ArrayList<>();
        for(CheckTask task: this.checkTaskSet){
            if(task.isFinished()) {
                finishedList.add(task);
            }
        }
        return finishedList;
    }

    @Override
    public void addScoreRecord(ScoreRecord scoreRecord) {
        this.scoreRecordList.add(scoreRecord);
    }

    @Override
    public int getTotalScore() {
        int total = 0;
        for (ScoreRecord scoreRecord : scoreRecordList) {
            total += scoreRecord.getScore();
        }
        return total;
    }

    @Override
    public List<CheckTask> getAllCheckTask() {
        return checkTaskSet;
    }

    public void addTask(CheckTask checkTask) {
        this.checkTaskSet.add(checkTask);
    }
}
