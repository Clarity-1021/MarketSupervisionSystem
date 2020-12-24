package fudan.se.hjjjxw.marketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@DynamicInsert(true)
public class ProductCategory implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    String name;

    @Transient //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "productCategory")
    private Set<CheckReport> checkReport = new HashSet<>();

    @Transient //@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "productCategorySet")
    private Set<SuperTask> superTaskSet = new HashSet<>();

    public ProductCategory() {
    }

    public ProductCategory(String name) {
        this.name = name;
    }

    public ProductCategory(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public ProductCategory(Integer id, String name, Set<CheckReport> checkReport, Set<SuperTask> superTaskSet) {
        this.id = id;
        this.name = name;
        this.checkReport = checkReport;
        this.superTaskSet = superTaskSet;
    }

    public ProductCategory(Integer id, String name, Set<CheckReport> checkReport) {
        this.id = id;
        this.name = name;
        this.checkReport = checkReport;
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

    public Set<CheckReport> getCheckReport() {
        return checkReport;
    }

    public void setCheckReport(Set<CheckReport> checkReport) {
        this.checkReport = checkReport;
    }

    public Set<SuperTask> getSuperTaskSet() {
        return superTaskSet;
    }

    public void setSuperTaskSet(Set<SuperTask> superTaskSet) {
        this.superTaskSet = superTaskSet;
    }
}
