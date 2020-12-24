package fudan.se.hjjjxw.marketsystem.service;

import fudan.se.hjjjxw.marketsystem.entity.*;
import fudan.se.hjjjxw.marketsystem.repository.CheckTaskRepository;
import fudan.se.hjjjxw.marketsystem.repository.MarketRepository;
import fudan.se.hjjjxw.marketsystem.repository.SuperTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class IndicatorService {

    @Autowired
    SuperTaskRepository superTaskRepository;

    @Autowired
    CheckTaskRepository checkTaskRepository;

    @Autowired
    MarketRepository marketRepository;



    /** 【 测试用例涵盖场景 】
     * 1. 监管局给一组农贸市场发起监管任务，农贸市场查看待完成任务，并完成抽检任务。
     * 2. 监管局给一个专家发起监管任务，专家查看待完成任务，并完成抽检任务。
     * 3. 监管局查看某个农贸产品类别在某个时间范围内的总的不合格数（时间以抽检日期为准）。
     * 4. 验证专家和农贸市场按时完成和未按时完成的抽检的场景，获取评估总得分和评估得/扣分的记录。测试代码中可以模拟定时器调用IndicatorService.update，需要设计控制系统时间的方法。
     */


    /**
     * 发起监管任务（无专家）
     * @param description
     * @param marketSet
     * @param productCategorySet
     */
    public SuperTask launchCheck(String description, Set<Market> marketSet, Set<ProductCategory> productCategorySet, Date deadline){
        Set<CheckTask> checkTasks = new HashSet<>();
        SuperTask superTask = new SuperTask(description, productCategorySet, deadline);
        for(Market market: marketSet){
            CheckTask checkTask = new CheckTask(market, superTask);
            checkTasks.add(checkTask);
            // 将 checkTask 加入 market 的 Task列表
            market.addTask(checkTask);
        }
        superTask.setCheckTaskSet(checkTasks);
        return superTask;
    }

    /**
     * 发起监管任务(专家任务）
     * @param description
     * @param marketSet
     * @param productCategorySet
     * @param deadline
     * @param expert
     */
    public SuperTask launchCheck(String description, Set<Market> marketSet, Set<ProductCategory> productCategorySet, Date deadline, Expert expert){

        Set<CheckTask> checkTasks = new HashSet<>();
        SuperTask superTask = new SuperTask(description, productCategorySet, deadline);
        for(Market market: marketSet){
            CheckTask checkTask = new CheckTask(market, superTask);
            checkTasks.add(checkTask);
        }
        superTask.setCheckTaskSet(checkTasks);
        // 将 superTask 加入专家的 Task 列表
        expert.addSuperTask(superTask);
        return superTask;
    }

    /**
     * 监管局查看某个农贸产品类别在某个时间范围内的总的不合格数（时间以抽检日期为准）
     * @param startDate
     * @param endDate
     * @param productCategory
     */
    public int getTotalUnqualifiedCount(Date startDate, Date endDate, List<ProductCategory> productCategory){
        return 0;
    }

    /**
     * 监管局查看某次监管任务，还未完成抽检的农贸市场
     */
    public Set<Market> getUnfinishedMarkets(SuperTask superTask){
        return new HashSet<>();
    }

    /**
     * 监管局查看农贸市场得分记录
     * @param market
     * @return
     */
    public List<ScoreRecord> getMarketScoreRecords(Market market){
        return new ArrayList<>();
    }


    /**
     * 系统更新得分记录 （？
     */
    public void update(){

    }
}
