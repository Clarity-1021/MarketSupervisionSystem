package fudan.se.hjjjxw.marketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

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

    private Date finishDate;

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    private String description;


    @Transient //@ManyToOne(fetch=FetchType.EAGER)
    //@JoinColumn(name = "market_id")
    private Market market;

    @Transient //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "checkTask")
    private List<CheckReport> checkReportSet = new ArrayList<>();

    public CheckTask() {
    }

    public CheckTask(Market market, SuperTask superTask) {
        this.superTask = superTask;
        this.market = market;
        this.description = superTask.getDescription() + "-" + market.getName();
    }

    public CheckTask(Integer id, SuperTask superTask, boolean isFinished, List<CheckReport> checkReportSet) {
        this.id = id;
        this.superTask = superTask;
        this.isFinished = isFinished;
        this.checkReportSet = checkReportSet;
    }

    public CheckTask(Integer id, SuperTask superTask, boolean isFinished, Market market, List<CheckReport> checkReportSet) {
        this.id = id;
        this.superTask = superTask;
        this.isFinished = isFinished;
        this.market = market;
        this.checkReportSet = checkReportSet;
    }

    public int getUnqualifiedCountByCategory(ProductCategory category, Date startDate, Date endDate) {
        int total = 0;
        for (CheckReport checkReport : checkReportSet) {
            if (checkReport.getProductCategory().equals(category)) {
                Date checkDate = checkReport.getCheckDate();
                if (checkDate.compareTo(startDate)>=0 && checkDate.compareTo(endDate)<=0) {
                    total += checkReport.getUnqualifiedCnt();
                }
            }
        }
        return total;
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

    public List<CheckReport> getCheckReportSet() {
        return checkReportSet;
    }

    public void setCheckReportSet(List<CheckReport> checkReportSet) {
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
