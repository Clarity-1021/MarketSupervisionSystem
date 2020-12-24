package fudan.se.hjjjxw.marketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@DynamicInsert(true)
public class CheckReport implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private int unqualifiedCnt;

    private Date checkDate;

    @Transient //@ManyToOne
    //@JoinColumn(name = "productcategory_id")
    private ProductCategory productCategory;

    @Transient //@ManyToOne
    //@JoinColumn(name = "checktask_id")
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

    public CheckReport(int unqualifiedCount, Date checkDate, ProductCategory productCategory, CheckTask checkTask) {
        this.unqualifiedCnt = unqualifiedCount;
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
