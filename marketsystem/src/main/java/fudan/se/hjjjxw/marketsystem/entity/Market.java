package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class Market implements Serializable, ICheck {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany
    private Set<ScoreRecord> scoreRecordList;

    @OneToMany
    private Set<CheckTask> checkTaskSet;


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
        CheckReport checkReport = new CheckReport(unqualifiedCount, checkDate, productCategory);
        checkTask.addCheckReport(checkReport);
        if(checkUnfinishedCheckTask().isEmpty())
            checkTask.setFinished(true);
    }

    /**
     * 获得未完成的抽检任务
     * @return
     */
    @Override
    public List<CheckTask> checkUnfinishedCheckTask() {
        List<CheckTask> unfinishedList = new ArrayList<>();
        for(CheckTask task: this.checkTaskSet){
            if(!task.isFinished())
                unfinishedList.add(task);
        }
        return unfinishedList;
    }
}
