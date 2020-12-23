package fudan.se.hjjjxw.marketsystem.service;

import fudan.se.hjjjxw.marketsystem.entity.*;
import fudan.se.hjjjxw.marketsystem.repository.CheckTaskRepository;
import fudan.se.hjjjxw.marketsystem.repository.SuperTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class IndicatorService {

    @Autowired
    SuperTaskRepository superTaskRepository;

    @Autowired
    CheckTaskRepository checkTaskRepository;




    /** 【 测试用例涵盖场景 】
     * 1. 监管局给一组农贸市场发起监管任务，农贸市场查看待完成任务，并完成抽检任务。
     * 2. 监管局给一个专家发起监管任务，专家查看待完成任务，并完成抽检任务。
     * 3. 监管局查看某个农贸产品类别在某个时间范围内的总的不合格数（时间以抽检日期为准）。
     * 4. 验证专家和农贸市场按时完成和未按时完成的抽检的场景，获取评估总得分和评估得/扣分的记录。测试代码中可以模拟定时器调用IndicatorService.update，需要设计控制系统时间的方法。
     */


    /**
     * 发起监管任务（无专家）
     * @param description
     * @param marketList
     * @param categoryList
     */
    public void lauchCheck(String description, Set<Market> marketList, Set<ProductCategory> categoryList, Date deadline){
//        SuperTask superTask = new SuperTask(description,marketList, categoryList,deadline);
        Set<CheckTask> checkTasks = new HashSet<>();
        for(Market market: marketList){
            CheckTask checkTask = new CheckTask(market);
            checkTasks.add(checkTask);
            checkTaskRepository.save(checkTask);
        }
        SuperTask superTask = new SuperTask(description, categoryList, checkTasks, deadline);
        superTaskRepository.save(superTask);
    }

    /**
     * 发起监管任务(专家任务）
     * @param description
     * @param marketList
     * @param categoryList
     * @param deadline
     * @param expert
     */
    public void lauchCheck(String description, Set<Market> marketList, Set<ProductCategory> categoryList, Date deadline, Expert expert){
        Set<CheckTask> checkTasks = new HashSet<>();
        for(Market market: marketList){
            CheckTask checkTask = new CheckTask(market);
            checkTasks.add(checkTask);
            checkTaskRepository.save(checkTask);
        }
        SuperTask superTask = new SuperTask(description, categoryList, checkTasks, deadline);
        superTask.setExpert(expert);
        superTaskRepository.save(superTask);
    }

    /**
     * 管局查看某个农贸产品类别在某个时间范围内的总的不合格数（时间以抽检日期为准）
     * @param startDate
     * @param endDate
     * @param productCategory
     */
    public int getTotalUnqualifiedCount(Date startDate, Date endDate, List<ProductCategory> productCategory){
        return 0;
    }


    public void update(){

    }
}
