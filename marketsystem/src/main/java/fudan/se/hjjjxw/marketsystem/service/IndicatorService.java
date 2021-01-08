package fudan.se.hjjjxw.marketsystem.service;

import fudan.se.hjjjxw.marketsystem.entity.*;
import fudan.se.hjjjxw.marketsystem.repository.CheckTaskRepository;
import fudan.se.hjjjxw.marketsystem.repository.MarketRepository;
import fudan.se.hjjjxw.marketsystem.repository.SuperTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static final String FINISH_ON_TIME = "按时完成";
    public static final String NOT_FINISH_ON_TIME = "未按时完成";
    public static final String NOT_FINISH_OVER_20_DAIES = "超过20天未完成";


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
        List<CheckTask> checkTasks = new ArrayList<>();
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

        List<CheckTask> checkTasks = new ArrayList<>();
        SuperTask superTask = new SuperTask(description, productCategorySet, deadline);
        for(Market market: marketSet){
            CheckTask checkTask = new CheckTask(market, superTask);
            checkTasks.add(checkTask);
        }
        superTask.setCheckTaskSet(checkTasks);
        superTask.setExpert(expert);
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
    public void updateScore(ICheck iCheck, String todayStr) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        Date today = new Date();
        try {
            today = dateFormat.parse(todayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        iCheck.setScoreRecordList(new ArrayList<>());

        List<CheckTask> finished = iCheck.getFinishedCheckTask();
        List<CheckTask> unfinished = iCheck.getUnfinishedCheckTask();

        // 已完成的抽检
        for (CheckTask checkTask : finished) {
            ScoreRecord sr;
            // 按时完成
            if (checkTask.getFinishDate().compareTo(checkTask.getSuperTask().getDeadLine()) <= 0) {
                sr = new ScoreRecord(checkTask.getSuperTask(), 10, FINISH_ON_TIME);
            }
            else {//未按时完成
                sr = new ScoreRecord(checkTask.getSuperTask(), -10, NOT_FINISH_ON_TIME);
            }
            iCheck.addScoreRecord(sr);
        }

        // 未完成抽检
        for (CheckTask checkTask : unfinished) {
            if (today.compareTo(checkTask.getSuperTask().getDeadLine()) > 0) {
                iCheck.addScoreRecord(new ScoreRecord(checkTask.getSuperTask(), -10, NOT_FINISH_ON_TIME));
                // 20天内未完成
                if (differentDays(checkTask.getSuperTask().getDeadLine(), today) > 20) {// 超过20天未完成
                    iCheck.addScoreRecord(new ScoreRecord(checkTask.getSuperTask(), -20, NOT_FINISH_OVER_20_DAIES));
                }
            }
        }
    }


    // 创建监管任务
    SuperTask createSuperTask (String description, Set<Market> markets, Set<ProductCategory> categories, String deadline) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        Date ddl = new Date();
        try {
            ddl = dateFormat.parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return launchCheck(description, markets, categories, ddl);
    }

    // 创建监管任务
    SuperTask createSuperTask (String description, Set<Market> markets, Set<ProductCategory> categories, String deadline, Expert expert) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        Date ddl = new Date();
        try {
            ddl = dateFormat.parse(deadline);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return launchCheck(description, markets, categories, ddl, expert);
    }

    // 输出监管任务信息
    void printSuperTask(SuperTask superTask) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        System.out.println("[监管任务描述]: " + superTask.getDescription());
        if (superTask.getExpert() != null) {
            System.out.println("[专家抽检]");
            System.out.println("[指定专家]: " + superTask.getExpert().getName());
        }
        else {
            System.out.println("[农贸市场抽检]");
        }
        System.out.println("[截止日期]：" + dateFormat.format(superTask.getDeadLine()));
        System.out.println("[指定农贸市场]: ");
        for(CheckTask checkTask : superTask.getCheckTaskSet()){
            System.out.println(checkTask.getMarket().getName());
        }
        System.out.println("[指定产品类别]: ");
        for(ProductCategory productCategory : superTask.getProductCategorySet()){
            System.out.println(productCategory.getName());
        }
    }

    // 市场在checkDate这天检查superTask中的指定分类
    void checkCategoryByMarket(SuperTask superTask, Market market, ProductCategory category, int unqualifiedCount, String checkDate) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        Date cd = new Date();
        try {
            cd = dateFormat.parse(checkDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<CheckTask> unfinished = market.getUnfinishedCheckTask();
        for (CheckTask checkTask : unfinished) {
            if (checkTask.getSuperTask().equals(superTask)) {
                Set<ProductCategory> unFinishedCategories = checkTask.getUnfinishedProductCategories();
                for (ProductCategory categ : unFinishedCategories) {
                    if (categ.equals(category)) {
                        market.checkProductCategory(category, checkTask, unqualifiedCount, cd);
                    }
                }
                return;
            }
        }
    }

    // 专家在checkDate这天检查superTask中的指定分类
    void checkCategoryByExpert(Expert expert, SuperTask superTask, Market market, ProductCategory category, int unqualifiedCount, String checkDate) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        Date cd = new Date();
        try {
            cd = dateFormat.parse(checkDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<CheckTask> unfinished = expert.getUnfinishedCheckTask();
        for (CheckTask checkTask : unfinished) {
            if (checkTask.getSuperTask().equals(superTask) && checkTask.getMarket().equals(market)) {
                Set<ProductCategory> unFinishedCategories = checkTask.getUnfinishedProductCategories();
                for (ProductCategory categ : unFinishedCategories) {
                    if (categ.equals(category)) {
                        expert.checkProductCategory(category, checkTask, unqualifiedCount, cd);
                    }
                }
                return;
            }
        }
    }



    /**
     * 打印 CheckTask 及相应的未完成产品类别
     * @param unfinishedList
     * @return
     */
    public List<CheckTask> printUnfinished(List<CheckTask> unfinishedList){
        for (CheckTask checkTask: unfinishedList){
            System.out.println("抽查任务名：'" + checkTask.getDescription() + "'");
            Set<ProductCategory> unfinishedCategories = checkTask.getUnfinishedProductCategories();
            String unfinishedCategStr = "";
            for(ProductCategory category: unfinishedCategories){
                unfinishedCategStr += category.getName() + ",";
            }
            System.out.println("未完成分类：" + unfinishedCategStr);
        }
        System.out.println();
        return unfinishedList;
    }

    // 打印checkTask所有上报结果
    void printCheckReports(SuperTask superTask, Market market)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        System.out.println("--- \"" + market.getName() + "\"的所有抽检结果 ---");
        for (CheckTask checkTask : superTask.getCheckTaskSet()) {
            if (checkTask.getMarket().equals(market)) {
                for (CheckReport checkReport : checkTask.getCheckReportSet()) {

                    System.out.println("[监管任务描述]：" + superTask.getDescription());
                    System.out.println("[截止日期]：" + dateFormat.format(superTask.getDeadLine()));
                    System.out.println(
                            "[检查日期]：" + dateFormat.format(checkReport.getCheckDate()) +
                                    "\t [检查分类]：" + checkReport.getProductCategory().getName() +
                                    "\t [不合格数]：" + checkReport.getUnqualifiedCnt());
                }
                System.out.println("--- 打印结束 ---");
                return;
            }
        }

    }

    // 打印checkTask所有上报结果
    void printCheckReports(SuperTask superTask, Expert expert) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        System.out.println("--- \"" + expert.getName() + "\"的所有抽检结果 ---");
        for (CheckTask checkTask : superTask.getCheckTaskSet()) {
            if (checkTask.getSuperTask().getExpert().equals(expert)) {
                for (CheckReport checkReport : checkTask.getCheckReportSet()) {

                    System.out.println("[监管任务描述]：" + superTask.getDescription());
                    System.out.println("[截止日期]：" + dateFormat.format(superTask.getDeadLine()));
                    System.out.println(
                            "[检查日期]：" + dateFormat.format(checkReport.getCheckDate()) +
                                    "\t [检查分类]：" + checkReport.getProductCategory().getName() +
                                    "\t [不合格数]：" + checkReport.getUnqualifiedCnt());
                }
                return;
            }
        }
        System.out.println("--- 打印结束 ---");
    }

    int getTotalUnqualifiedCountByCategory(ProductCategory category, String startDate, String endDate, Set<SuperTask> superTasks) {
        int total = 0;
        for (SuperTask superTask : superTasks) {
            total += superTask.getUnqualifiedCountByCategory(category, startDate, endDate);
        }
        return total;
    }

    public static int differentDays(Date date1,Date date2){
        return (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
    }

}
