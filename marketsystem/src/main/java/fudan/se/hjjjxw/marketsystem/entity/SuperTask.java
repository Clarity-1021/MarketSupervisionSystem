package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
public class SuperTask implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String description;

    // TODO ignore
    private Expert expert;

    @OneToOne
    private ProductCategory productCategory;

    @OneToMany
    private Set<CheckTask> checkTaskSet;

    private Date deadLine;

    private Date finishDate;

    public SuperTask() {
    }

    public SuperTask(Integer id, String description, Expert expert, ProductCategory productCategory, Set<CheckTask> checkTaskSet, Date deadLine, Date finishDate) {
        this.id = id;
        this.description = description;
        this.expert = expert;
        this.productCategory = productCategory;
        this.checkTaskSet = checkTaskSet;
        this.deadLine = deadLine;
        this.finishDate = finishDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Expert getExpert() {
        return expert;
    }

    public void setExpert(Expert expert) {
        this.expert = expert;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Set<CheckTask> getCheckTaskSet() {
        return checkTaskSet;
    }

    public void setCheckTaskSet(Set<CheckTask> checkTaskSet) {
        this.checkTaskSet = checkTaskSet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }


}
