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

    @ManyToOne
    @JoinColumn(name = "expert_id")
    private Expert expert;

    @Transient
    private Set<ProductCategory> productCategorySet;

    @OneToMany
    private Set<CheckTask> checkTaskSet;

    private Date deadLine;

    private Date finishDate;

    public SuperTask() {
    }

    public SuperTask(Integer id, String description, Expert expert, Set<ProductCategory> productCategorySet, Set<CheckTask> checkTaskSet, Date deadLine, Date finishDate) {
        this.id = id;
        this.description = description;
        this.expert = expert;
        this.productCategorySet = productCategorySet;
        this.checkTaskSet = checkTaskSet;
        this.deadLine = deadLine;
        this.finishDate = finishDate;
    }


    public SuperTask(String description, Set<ProductCategory> productCategorySet, Set<CheckTask> checkTaskSet, Date deadLine) {
        this.description = description;
        this.productCategorySet = productCategorySet;
        this.checkTaskSet = checkTaskSet;
        this.deadLine = deadLine;
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

    public Set<ProductCategory> getProductCategorySet() {
        return productCategorySet;
    }

    public void setProductCategorySet(Set<ProductCategory> productCategorySet) {
        this.productCategorySet = productCategorySet;
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
