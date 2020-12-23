package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
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


    public Market() {
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

    @Override
    public void checkProductCategory(ProductCategory productCategory, CheckTask checkTask) {

    }

    @Override
    public List<CheckTask> checkUnfinishedCheckTask() {
        return null;
    }
}
