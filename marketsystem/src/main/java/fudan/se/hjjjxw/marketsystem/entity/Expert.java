package fudan.se.hjjjxw.marketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@DynamicInsert(true)
public class Expert implements Serializable, ICheck {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String name;

    @Transient //@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "expert")
    private Set<SuperTask> superTaskList = new HashSet<>();

    public Expert(){}

    public Expert(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    /**
     * 抽检产品分类，并上报抽检结果
     * @param productCategory
     * @param checkTask
     */
    @Override
    public void checkProductCategory(ProductCategory productCategory, CheckTask checkTask, int unqualifiedCount, Date checkDate) {
        CheckReport checkReport = new CheckReport(unqualifiedCount, checkDate, productCategory, checkTask);
        checkTask.addCheckReport(checkReport);
        if(checkTask.getUnfinishedProductCategories().isEmpty())
            checkTask.setFinished(true);
    }

    /**
     * 获得未完成的抽检任务
     * 一个专家可能有多个supertask中的未完成的子任务
     * @return
     */
    @Override
    public List<CheckTask> getUnfinishedCheckTask() {
        List<CheckTask> unfinishedList = new ArrayList<>();
        for (SuperTask superTask : this.superTaskList) {
            for (CheckTask checkTask : superTask.getCheckTaskSet()) {
                if (!checkTask.isFinished()) {
                    unfinishedList.add(checkTask);
                }
            }
        }
        return unfinishedList;
    }

    public void addSuperTask(SuperTask superTask){
        this.superTaskList.add(superTask);
    }


}
