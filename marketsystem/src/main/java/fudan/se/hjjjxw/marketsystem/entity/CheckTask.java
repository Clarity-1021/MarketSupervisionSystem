package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
public class CheckTask implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private SuperTask superTask;

    private boolean isFinished;

    @OneToMany
    private Set<CheckReport> checkReportSet;

    public CheckTask() {
    }

    public CheckTask(Integer id, SuperTask superTask, boolean isFinished, Set<CheckReport> checkReportSet) {
        this.id = id;
        this.superTask = superTask;
        this.isFinished = isFinished;
        this.checkReportSet = checkReportSet;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SuperTask getSuperTask() {
        return superTask;
    }

    public void setSuperTask(SuperTask superTask) {
        this.superTask = superTask;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Set<CheckReport> getCheckReportSet() {
        return checkReportSet;
    }

    public void setCheckReportSet(Set<CheckReport> checkReportSet) {
        this.checkReportSet = checkReportSet;
    }

    // ------------  功能函数  ----------------
    public List<ProductCategory> getUnfinishedProductCategories(){
        return new ArrayList<>();
    }

    public void updateCheckReport(CheckReport checkReport){

    }

    public void getTotalUnqualifiedCount(Date date, Date endDate, ProductCategory productCategory){

    }
}
