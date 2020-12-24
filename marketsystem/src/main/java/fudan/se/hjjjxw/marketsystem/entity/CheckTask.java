package fudan.se.hjjjxw.marketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@DynamicInsert(true)
public class CheckTask implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Transient //@ManyToOne(fetch=FetchType.EAGER)
    //@JoinColumn(name = "supertask_id")
    private SuperTask superTask;

    private boolean isFinished;

    private String description;


    @Transient //@ManyToOne(fetch=FetchType.EAGER)
    //@JoinColumn(name = "market_id")
    private Market market;

    @Transient //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "checkTask")
    private Set<CheckReport> checkReportSet = new HashSet<>();

    public CheckTask() {
    }

    public CheckTask(Market market, SuperTask superTask) {
        this.superTask = superTask;
        this.market = market;
        this.description = superTask.getDescription() + "-" + market.getName();
    }

    public CheckTask(Integer id, SuperTask superTask, boolean isFinished, Set<CheckReport> checkReportSet) {
        this.id = id;
        this.superTask = superTask;
        this.isFinished = isFinished;
        this.checkReportSet = checkReportSet;
    }

    public CheckTask(Integer id, SuperTask superTask, boolean isFinished, Market market, Set<CheckReport> checkReportSet) {
        this.id = id;
        this.superTask = superTask;
        this.isFinished = isFinished;
        this.market = market;
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

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    // ------------  功能函数  ----------------
    public Set<ProductCategory> getUnfinishedProductCategories(){
        Set<ProductCategory> unfinishedCategories = new HashSet<>(this.superTask.getProductCategorySet());
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
