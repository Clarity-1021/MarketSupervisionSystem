package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Entity
public class SuperTask implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    // TODO ignore
    private Expert expert;

    @OneToOne
    private ProductCategory productCategory;

    @OneToMany
    private Set<CheckTask> checkTaskSet;


    public SuperTask() {
    }

    public SuperTask(Integer id, Expert expert, ProductCategory productCategory, Set<CheckTask> checkTaskSet) {
        this.id = id;
        this.expert = expert;
        this.productCategory = productCategory;
        this.checkTaskSet = checkTaskSet;
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
}
