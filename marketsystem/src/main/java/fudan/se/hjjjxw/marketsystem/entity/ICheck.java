package fudan.se.hjjjxw.marketsystem.entity;

import java.util.List;

public interface ICheck {
    void checkProductCategory(ProductCategory productCategory, CheckTask checkTask);

    List<CheckTask> checkUnfinishedCheckTask();
}
