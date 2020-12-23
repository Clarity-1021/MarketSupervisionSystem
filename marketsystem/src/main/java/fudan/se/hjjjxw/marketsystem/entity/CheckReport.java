package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class CheckReport implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private int unqualifiedCnt;

    private Date checkDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private ProductCategory productCategory;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private CheckTask checkTask;

    public CheckReport() {
    }

    public CheckReport(int unqualifiedCnt, Date checkDate, ProductCategory productCategory) {
        this.unqualifiedCnt = unqualifiedCnt;
        this.checkDate = checkDate;
        this.productCategory = productCategory;
    }

    public CheckReport(Integer id, int unqualifiedCnt, Date checkDate, ProductCategory productCategory, CheckTask checkTask) {
        this.id = id;
        this.unqualifiedCnt = unqualifiedCnt;
        this.checkDate = checkDate;
        this.productCategory = productCategory;
        this.checkTask = checkTask;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUnqualifiedCnt() {
        return unqualifiedCnt;
    }

    public void setUnqualifiedCnt(int unqualifiedCnt) {
        this.unqualifiedCnt = unqualifiedCnt;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public CheckTask getCheckTask() {
        return checkTask;
    }

    public void setCheckTask(CheckTask checkTask) {
        this.checkTask = checkTask;
    }
}
