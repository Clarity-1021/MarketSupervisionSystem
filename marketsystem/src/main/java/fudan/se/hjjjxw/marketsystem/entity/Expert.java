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
public class Expert implements Serializable, ICheck {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @Transient //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "expert")
    private List<SuperTask> superTaskList = new ArrayList<>();

    public Expert(){}

    public Expert(String name) {
        this.name = name;
    }

    @Transient //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "market")//(fetch = FetchType.EAGER)
    private List<ScoreRecord> scoreRecordList = new ArrayList<>();

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

    // ------------  功能函数  ----------------
    public void calculateTotalScore(){


    }

    public void getScoreRecord(){

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
        List<CheckTask> checkTasks = new ArrayList<>();
        for (SuperTask superTask : superTaskList) {
            checkTasks.addAll(superTask.getCheckTaskSet());
        }
        return checkTasks;
    }

    /**
     * 获得未完成的抽检任务
     * 一个专家可能有多个supertask中的未完成的子任务
     * @return
     */
    @Override
    public List<CheckTask> getUnfinishedCheckTask() {
        List<CheckTask> unfinishedList = new ArrayList<>();
        for (SuperTask superTask : this.superTaskList) {
            for (CheckTask checkTask : superTask.getCheckTaskSet()) {
                if (!checkTask.isFinished()) {
                    unfinishedList.add(checkTask);
                }
            }
        }
        return unfinishedList;
    }

    @Override
    public List<CheckTask> getFinishedCheckTask() {
        List<CheckTask> finishedList = new ArrayList<>();
        for (SuperTask superTask : this.superTaskList) {
            for (CheckTask checkTask : superTask.getCheckTaskSet()) {
                if (checkTask.isFinished()) {
                    finishedList.add(checkTask);
                }
            }
        }
        return finishedList;
    }

    @Override
    public void addScoreRecord(ScoreRecord scoreRecord) {
        this.scoreRecordList.add(scoreRecord);
    }

    public void addSuperTask(SuperTask superTask){
        this.superTaskList.add(superTask);
    }


}
