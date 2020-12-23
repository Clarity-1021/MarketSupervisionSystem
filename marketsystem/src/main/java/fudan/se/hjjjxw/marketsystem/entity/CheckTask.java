package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class CheckTask implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "supertask_id")
    private SuperTask superTask;

    private boolean isFinished;

    @ManyToOne
    @JoinColumn(name = "market_id")
    private Market market;

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

    public CheckTask(Market market) {
        this.market = market;
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

    public void addCheckReport(CheckReport checkReport){
        this.checkReportSet.add(checkReport);
    }

    // ------------  功能函数  ----------------
    public Set<ProductCategory> getUnfinishedProductCategories(){
        Set<ProductCategory> unfinishedCategories = this.superTask.getProductCategorySet();
        for(CheckReport report: this.checkReportSet){
            // 有报告说明已完成
            unfinishedCategories.remove(report.getProductCategory());
        }
        return unfinishedCategories;
    }

    public void updateCheckReport(CheckReport checkReport){

    }

    public void getTotalUnqualifiedCount(Date date, Date endDate, ProductCategory productCategory){

    }
}
