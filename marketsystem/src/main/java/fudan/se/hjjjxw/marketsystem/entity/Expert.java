package fudan.se.hjjjxw.marketsystem.entity;

import java.io.Serializable;
import java.util.List;

public class Expert implements Serializable, ICheck {

    private String name;

    private List<SuperTask> superTaskList;

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

    public List<SuperTask> getSuperTaskList() {
        return superTaskList;
    }

    public void setSuperTaskList(List<SuperTask> superTaskList) {
        this.superTaskList = superTaskList;
    }

    @Override
    public void checkProductCategory(ProductCategory productCategory, CheckTask checkTask) {

    }

    @Override
    public List<CheckTask> checkUnfinishedCheckTask() {
        return null;
    }
}
