package fudan.se.hjjjxw.marketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@DynamicInsert(true)
public class SuperTask implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String description;

//    @Transient
    @Transient //@ManyToOne(optional=true)
    //@JoinColumn(name = "expert_id")
    private Expert expert;

//    @Transient
    @Transient //@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductCategory> productCategorySet = new HashSet<>();

//    @Transient
    @Transient //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "superTask")
    private List<CheckTask> checkTaskSet = new ArrayList<>();

    private Date deadLine;

    private Date finishDate;

    public SuperTask() {
    }

    public SuperTask(Integer id, String description, Expert expert, Set<ProductCategory> productCategorySet, List<CheckTask> checkTaskSet, Date deadLine, Date finishDate) {
        this.id = id;
        this.description = description;
        this.expert = expert;
        this.productCategorySet = productCategorySet;
        this.checkTaskSet = checkTaskSet;
        this.deadLine = deadLine;
        this.finishDate = finishDate;
    }


    public SuperTask(String description, Set<ProductCategory> productCategorySet, List<CheckTask> checkTaskSet, Date deadLine) {
        this.description = description;
        this.productCategorySet = productCategorySet;
        this.checkTaskSet = checkTaskSet;
        this.deadLine = deadLine;
    }

    public SuperTask(String description, Set<ProductCategory> categoryList, Date deadline) {
        this.description = description;
        this.productCategorySet = categoryList;
        this.deadLine = deadline;
    }

    public int getUnqualifiedCountByCategory(ProductCategory category, String startDate, String endDate) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        Date sd = new Date();
        try {
            sd = dateFormat.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date ed = new Date();
        try {
            ed = dateFormat.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int total = 0;
        for (CheckTask checkTask : checkTaskSet) {
            total += checkTask.getUnqualifiedCountByCategory(category, sd, ed);
        }
        return total;
    }

    public boolean equals(SuperTask superTask) {
//        System.out.println("id=" + id);
//        System.out.println("superTaskId=" + superTask.getId());
        return description.equals(superTask.getDescription());
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

    public List<CheckTask> getCheckTaskSet() {
        return checkTaskSet;
    }

    public void setCheckTaskSet(List<CheckTask> checkTaskSet) {
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
