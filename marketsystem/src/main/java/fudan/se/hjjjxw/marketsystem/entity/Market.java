package fudan.se.hjjjxw.marketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@DynamicInsert(true)
public class Market implements Serializable, ICheck {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @Transient //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "market")//(fetch = FetchType.EAGER)
    private Set<ScoreRecord> scoreRecordList = new HashSet<>();

    @Transient //@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE,mappedBy = "market")
    private Set<CheckTask> checkTaskSet = new HashSet<>();


    public Market() {
    }

    public Market(String name){
        this.name = name;
    }

    public Market(Integer id, String name, Set<ScoreRecord> scoreRecordList) {
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

    public Set<ScoreRecord> getScoreRecordList() {
        return scoreRecordList;
    }

    public void setScoreRecordList(Set<ScoreRecord> scoreRecordList) {
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
        if(checkTask.getUnfinishedProductCategories().isEmpty())
            checkTask.setFinished(true);
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

    public void addTask(CheckTask checkTask) {
        this.checkTaskSet.add(checkTask);
    }
}
