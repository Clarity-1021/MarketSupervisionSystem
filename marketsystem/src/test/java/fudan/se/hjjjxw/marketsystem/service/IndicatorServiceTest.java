package fudan.se.hjjjxw.marketsystem.service;

import fudan.se.hjjjxw.marketsystem.entity.*;
import fudan.se.hjjjxw.marketsystem.repository.MarketRepository;
import fudan.se.hjjjxw.marketsystem.repository.ProductCategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
class IndicatorServiceTest {

    static List<String> categoryNames = new ArrayList<>(Arrays.asList("水果类","畜禽肉类","蔬菜类","水产品类"));
    static List<String> expertNames = new ArrayList<>(Arrays.asList("专家1","专家2","专家3"));
    static List<String> marketNames = new ArrayList<>(Arrays.asList("市场1","市场2","市场3"));

    @Autowired
    private IndicatorService indicatorService;

    @Autowired
    private CrudService crudService;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    /** 【 测试用例涵盖场景 】
     * 1. 监管局给一组农贸市场发起监管任务，农贸市场查看待完成任务，并完成抽检任务。
     * 2. 监管局给一个专家发起监管任务，专家查看待完成任务，并完成抽检任务。
     * 3. 监管局查看某个农贸产品类别在某个时间范围内的总的不合格数（时间以抽检日期为准）。
     * 4. 验证专家和农贸市场 按时 完成的抽检的场景，获取评估总得分和评估得/扣分的记录。
     * 5. 验证专家和农贸市场 未按时 完成的抽检的场景,获取评估总得分和评估得/扣分的记录。
     */


    /**
     * 数据库 专家、产品分类、市场 记录初始化
     */
    public void initDB(){
        System.out.println("初始化数据");

//        // ----  创建商品类别  --------
        List<String> nameList = new ArrayList<>();
        nameList.add("水果类");
        nameList.add("畜禽肉类");
        nameList.add("蔬菜类");
        nameList.add("水产品类");
//
        for (String name : nameList) {
            ProductCategory category = new ProductCategory(name);
            crudService.saveProductCategory(category);
        }

//        //  ------ 创建专家  ---------
        List<String> expertNames = new ArrayList<>();
        expertNames.add("专家1");
        expertNames.add("专家2");
        expertNames.add("专家3");
        for (String name : expertNames) {
            Expert expert = new Expert(name);
            crudService.saveExpert(expert);
        }
//
//        //  ------ 创建农贸市场  --------
        List<String> marketNames = new ArrayList<>();
        marketNames.add("市场1");
        marketNames.add("市场2");
        marketNames.add("市场3");
        for (String name : marketNames) {
            Market market = new Market(name);
            crudService.saveMarket(market);
        }
    }

    /**
     * 场景1. 监管局给一组农贸市场发起监管任务，农贸市场查看待完成任务，并完成抽检任务。
     */
    @Test
    void initTaskWithoutExpert(){

        System.out.println("【测试场景1】  监管局给一组农贸市场发起监管任务，农贸市场查看待完成任务，并完成抽检任务。");
        // 加载 市场
        Set<Market> marketSet = new HashSet<>();
        Market market1 = crudService.getMarket("市场1");
        Market market2 = crudService.getMarket("市场2");
        Market market3 = crudService.getMarket("市场3");
        marketSet.add(market1);
        marketSet.add(market2);
        marketSet.add(market3);

        // 加载 目录分类
        Set<ProductCategory> productCategorySet = new HashSet<>();
        ProductCategory fruit = crudService.getProductCategory("水果类");
        ProductCategory meat = crudService.getProductCategory("畜禽肉类");
        ProductCategory vegetable = crudService.getProductCategory("蔬菜类");
        productCategorySet.add(fruit);
        productCategorySet.add(meat);
        productCategorySet.add(vegetable);

        // ------- 监管局给一组农贸市场发起监管任务  -----------

        System.out.println("---  【用例1 监管局创建监管任务 】 ---");
        System.out.println("【Step】 监管局 创建 “监管任务1” ");

        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline = new Date();
        try {
            deadline = dateFormat1.parse("2020-12-1");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /* 监管任务 1 */
        SuperTask superTask1 = indicatorService.launchCheck("监管任务1", marketSet, productCategorySet, deadline);
        System.out.println("[指定农贸市场]:");
        for(String marketName:marketNames){
            System.out.println(marketName);
        }
        System.out.println("[指定产品类别]:");
        for(String categoryName:categoryNames){
            System.out.println(categoryName);
        }

        // ------------ 农贸市场查看待完成任务  ------------
        System.out.println("\n--- 【用例2 农贸市场查看待完成任务 】 ---");
        for(Market market: marketSet){
            List<CheckTask> unfinishedList = market.getUnfinishedCheckTask();
            printUnfinished(unfinishedList);
            Assertions.assertEquals(1,unfinishedList.size());
        }

        // 完成抽检任务
        // 市场1 按时完成 监管任务1 的抽检任务
//        Market market1 = crudService.getMarket("市场1");
        List<CheckTask> unfinishedTaskList = market1.getUnfinishedCheckTask();

        Date finishDate1 = new Date();
        try {
            finishDate1 = dateFormat1.parse("2020-12-2");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // market1 完成 监管任务1 的 水果类抽检
        System.out.println("\n【Step】 市场1 完成 监管任务1-水果类 抽检");
        CheckTask maket1task1 = unfinishedTaskList.get(0);
        market1.checkProductCategory(fruit, maket1task1,10, finishDate1 );
        // 抽检任务 未完成
        System.out.println(market1.getName() + " 未完成抽检任务：");
        unfinishedTaskList = market1.getUnfinishedCheckTask();
        printUnfinished(unfinishedTaskList);
        Assertions.assertEquals(false, maket1task1.isFinished());


        // market1 完成 监管任务1 的 剩余未抽检目录
        System.out.println("\n【Step】 市场1 完成 监管任务1-禽畜肉类 抽检  ---");
        System.out.println("【Step】 市场1 完成 监管任务1-蔬菜类 抽检  ---");
        market1.checkProductCategory(meat, maket1task1,10, finishDate1 );
        market1.checkProductCategory(vegetable, maket1task1,10, finishDate1 );
        // 抽检任务 已完成
        System.out.println(market1.getName() + " 未完成抽检任务：");
        unfinishedTaskList = market1.getUnfinishedCheckTask();
        printUnfinished(unfinishedTaskList);
        Assertions.assertEquals(true, maket1task1.isFinished());

        // 测试 市场1 完成 抽检任务1 后的未完成任务数
        Assertions.assertEquals(0,unfinishedTaskList.size());

    }

    /**
     * 2. 监管局给一个专家发起监管任务，专家查看待完成任务，并完成抽检任务。
     */
    @Test
    void initTaskWithExpert(){
        System.out.println("【测试场景2】  监管局给一个专家发起监管任务，农贸市场查看待完成任务，并完成抽检任务。");
        // 加载 市场
        Set<Market> marketSet = new HashSet<>();
        Market market1 = crudService.getMarket("市场1");
        Market market2 = crudService.getMarket("市场2");
        Market market3 = crudService.getMarket("市场3");
        marketSet.add(market1);
        marketSet.add(market2);
        marketSet.add(market3);
        // 加载 目录分类
        Set<ProductCategory> productCategorySet = new HashSet<>();
        ProductCategory fruit = crudService.getProductCategory("水果类");
        ProductCategory meat = crudService.getProductCategory("畜禽肉类");
        ProductCategory vegetable = crudService.getProductCategory("蔬菜类");
        productCategorySet.add(fruit);
        productCategorySet.add(meat);
        productCategorySet.add(vegetable);
        // 加载 专家
        Expert expert1 = crudService.getExpert("专家1");

        // ------- 监管局给一个专家发起监管任务  -----------

        System.out.println("---  【用例1 监管局创建监管任务 】 ---");
        System.out.println("【Step】 监管局 创建 ”监管任务2“ ");

        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline = new Date();
        try {
            deadline = dateFormat1.parse("2020-12-1");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /* 监管任务 2 */
        SuperTask superTask2 = indicatorService.launchCheck("监管任务2", marketSet, productCategorySet, deadline, expert1);
        System.out.println("[指定农贸市场]:");
        for(String marketName:marketNames){
            System.out.println(marketName);
        }
        System.out.println("[指定产品类别]:");
        for(String categoryName:categoryNames){
            System.out.println(categoryName);
        }
        System.out.println("[指定专家]:" + expert1.getName());

        // ------------ 专家查看待完成任务  ------------
        System.out.println("\n--- 【用例2 专家查看待完成任务 】 ---");
        List<CheckTask> unfinishedTaskList = expert1.getUnfinishedCheckTask();
        printUnfinished(unfinishedTaskList);
        Assertions.assertEquals(3,unfinishedTaskList.size());


        Date finishDate1 = new Date();
        try {
            finishDate1 = dateFormat1.parse("2020-12-2");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // expert1 完成 监管任务1 的 水果类抽检
        CheckTask expert1task1 = unfinishedTaskList.get(0);
        System.out.println("\n【Step】 专家1 完成 "+ expert1task1.getDescription() + fruit.getName() +" 抽检");
        market1.checkProductCategory(fruit, expert1task1,10, finishDate1 );
        // 抽检任务 未完成
        Assertions.assertEquals(false, expert1task1.isFinished());


        // expert1 完成 所有未完成抽检
        unfinishedTaskList = expert1.getUnfinishedCheckTask();

        ListIterator<CheckTask> iter = unfinishedTaskList.listIterator();
        for(CheckTask checkTask: unfinishedTaskList){
            checkTask = iter.next();
            Set<ProductCategory> unfinishedCategoriesCopy = new HashSet<>(checkTask.getUnfinishedProductCategories());
            for (ProductCategory productCategory : unfinishedCategoriesCopy) {
                expert1.checkProductCategory(productCategory, checkTask, 10, finishDate1);
                iter.set(checkTask);
                System.out.println("【Step】 专家1 完成 " + checkTask.getDescription() + productCategory.getName() + " 抽检");
            }
        }

//        Set<CheckTask> allCheckTask = superTask2.getCheckTaskSet();
//        for(CheckTask task: allCheckTask){
//            Set<ProductCategory> unfinishedCategoriesCopy = new HashSet<>(task.getUnfinishedProductCategories());
//            for(ProductCategory productCategory: unfinishedCategoriesCopy){
//                expert1.checkProductCategory(productCategory,task,10,finishDate1);
//                System.out.println("【Step】 市场1 完成 "+ task.getDescription() + productCategory.getName() + " 抽检  ---");
//            }
////            expert1.checkProductCategory();
//        }

        // 抽检任务 已完成
        unfinishedTaskList = expert1.getUnfinishedCheckTask();
        printUnfinished(unfinishedTaskList);
        // 测试 市场1 完成 抽检任务1 后的未完成任务数
        Assertions.assertEquals(true, expert1task1.isFinished());
        Assertions.assertEquals(0,unfinishedTaskList.size());


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
        return indicatorService.launchCheck(description, markets, categories, ddl);
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
        return indicatorService.launchCheck(description, markets, categories, ddl, expert);
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

    // 打印checkTask所有上报结果
    void printCheckReports(SuperTask superTask, Market market) {
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


    /**
     * 3. 监管局查看某个农贸产品类别在某个时间范围内的总的不合格数（时间以抽检日期为准）。
     */
    @Test
    void checkUnqualified(){

        // 加载 市场
        Set<Market> marketSet;
//        Market market1 = crudService.getMarket("市场1");
//        Market market2 = crudService.getMarket("市场2");
        Market market1 = new Market("市场1");
        Market market2 = new Market("市场2");
        // 加载 目录分类
        Set<ProductCategory> productCategorySet;
//        ProductCategory fruit = crudService.getProductCategory("水果类");
//        ProductCategory meat = crudService.getProductCategory("畜禽肉类");
        ProductCategory fruit = new ProductCategory("水果类");
        ProductCategory meat = new ProductCategory("畜禽肉类");


        System.out.println("【测试场景3】  监管局查看某个农贸产品类别在某个时间范围内的总的不合格数（时间以抽检日期为准）。");

        /* 创建监管任务 1 */
        // 指定一组市场
        marketSet = new HashSet<>();
        marketSet.add(market1);
        marketSet.add(market2);
        // 指定一组产品分类
        productCategorySet = new HashSet<>();
        productCategorySet.add(fruit);
        productCategorySet.add(meat);
        // 创建监管任务
        SuperTask superTask1 = createSuperTask("监管任务1", marketSet, productCategorySet, "2020-12-9");
        // 打印监管任务信息
        printSuperTask(superTask1);

        // 市场1完成水果类抽检：水果+10
        checkCategoryByMarket(superTask1, market1, fruit, 10, "2020-12-2");
        // 市场1完成畜禽肉类抽检：肉+12
        checkCategoryByMarket(superTask1, market1, meat, 12, "2020-12-3");
        printCheckReports(superTask1, market1);


        /* 创建监管任务 2 */
        // 指定一组市场
        marketSet = new HashSet<>();
        marketSet.add(market1);
        marketSet.add(market2);
        // 指定一组产品分类
        productCategorySet = new HashSet<>();
        productCategorySet.add(fruit);
        productCategorySet.add(meat);
        // 创建监管任务
        SuperTask superTask2 = createSuperTask("监管任务2", marketSet, productCategorySet, "2020-12-8");
        // 打印监管任务信息
        printSuperTask(superTask2);

        // 市场2完成水果类抽检：水果+8
        checkCategoryByMarket(superTask2, market2, fruit, 8, "2020-12-5");
        // 市场2完成畜禽肉类抽检：肉+4
        checkCategoryByMarket(superTask2, market2, meat, 4, "2020-12-6");
        printCheckReports(superTask2, market2);

        Set<SuperTask> superTasks = new HashSet<>();
        superTasks.add(superTask1);
        superTasks.add(superTask2);


//        System.out.println("水果未合格：" + superTask1.getUnqualifiedCountByCategory(fruit, "2020-12-1", "2020-12-5"));
//        System.out.println("肉未合格：" + superTask1.getUnqualifiedCountByCategory(meat, "2020-12-1", "2020-12-5"));


        Assertions.assertEquals(10, getTotalUnqualifiedCountByCategory(fruit, "2020-12-1", "2020-12-4", superTasks));
        Assertions.assertEquals(18, getTotalUnqualifiedCountByCategory(fruit, "2020-12-1", "2020-12-6", superTasks));
        Assertions.assertEquals(16, getTotalUnqualifiedCountByCategory(meat, "2020-12-1", "2020-12-7", superTasks));
        Assertions.assertEquals(4, getTotalUnqualifiedCountByCategory(meat, "2020-12-4", "2020-12-7", superTasks));


    }

    /**
     * 4. 验证专家和农贸市场按时完成抽检的场景，获取评估总得分和评估得/扣分的记录。
     */
    @Test
    void completeInTime(){
        // 加载 市场
        Set<Market> marketSet;
//        Market market = crudService.getMarket("市场1");
        Market market = new Market("市场1");
        // 加载 目录分类
        Set<ProductCategory> productCategorySet;
//        ProductCategory fruit = crudService.getProductCategory("水果类");
//        ProductCategory meat = crudService.getProductCategory("畜禽肉类");
        ProductCategory fruit = new ProductCategory("水果类");
        ProductCategory meat = new ProductCategory("畜禽肉类");


        System.out.println("【测试场景4】  验证专家和农贸市场按时完成抽检的场景，获取评估总得分和评估得/扣分的记录。");

        /* 创建监管任务 1 */
        // 指定一组市场
        marketSet = new HashSet<>();
        marketSet.add(market);
        // 指定一组产品分类
        productCategorySet = new HashSet<>();
        productCategorySet.add(fruit);
        productCategorySet.add(meat);
        // 创建监管任务1
        SuperTask superTask1 = createSuperTask("监管任务1", marketSet, productCategorySet, "2020-12-9");
        // 打印监管任务信息
//        printSuperTask(superTask1);

        // 市场1暗示完成抽检任务
        // 市场1完成水果类抽检：水果+10
        checkCategoryByMarket(superTask1, market, fruit, 10, "2020-12-2");
        // 市场1完成畜禽肉类抽检：肉+12
        checkCategoryByMarket(superTask1, market, meat, 12, "2020-12-3");
//        printCheckReports(superTask1, market);

        updateScore(market);
        Assertions.assertEquals(10, market.getTotalScore());
        Assertions.assertEquals("[监管任务1 2020-12-09][10]：按时完成",
                market.getScoreRecordList().get(0).getScoreReason());

        /* 创建监管任务 2 */
        // 创建监管任务2
        SuperTask superTask2 = createSuperTask("监管任务2", marketSet, productCategorySet, "2020-12-8");
        // 打印监管任务信息
//        printSuperTask(superTask2);

        // 市场1成水果类抽检：水果+8
        checkCategoryByMarket(superTask2, market, fruit, 8, "2020-12-5");
        // 市场1完成畜禽肉类抽检：肉+4
        checkCategoryByMarket(superTask2, market, meat, 4, "2020-12-6");
//        printCheckReports(superTask2, market);

        updateScore(market);
        Assertions.assertEquals(20, market.getTotalScore());
        Assertions.assertEquals("[监管任务1 2020-12-09][10]：按时完成",
                market.getScoreRecordList().get(0).getScoreReason());
        Assertions.assertEquals("[监管任务2 2020-12-08][10]：按时完成",
                market.getScoreRecordList().get(1).getScoreReason());

//        System.out.println(market1.getScoreRecordList().get(0).getScoreReason());
//        System.out.println(market1.getScoreRecordList().get(1).getScoreReason());

        // 加载 专家
//        Expert expert = crudService.getExpert("专家1");
        Expert expert = new Expert("专家1");
        SuperTask superTask3 = createSuperTask("监管任务3", marketSet, productCategorySet, "2020-12-6", expert);
        // 打印监管任务信息
//        printSuperTask(superTask3);

        // 专家1完成市场1水果类抽检：水果+8
        checkCategoryByExpert(expert, superTask3, market, fruit, 8, "2020-12-5");
        // 专家1完成市场1畜禽肉类抽检：肉+4
        checkCategoryByExpert(expert, superTask3, market, meat, 4, "2020-12-4");
//        printCheckReports(superTask3, expert);

        updateScore(expert);
        Assertions.assertEquals(10, expert.getTotalScore());
        Assertions.assertEquals("[监管任务3 2020-12-06][10]：按时完成",
                expert.getScoreRecordList().get(0).getScoreReason());

        SuperTask superTask4 = createSuperTask("监管任务4", marketSet, productCategorySet, "2020-12-8", expert);
        // 打印监管任务信息
//        printSuperTask(superTask4);

        // 专家1完成市场1水果类抽检：水果+6
        checkCategoryByExpert(expert, superTask4, market, fruit, 6, "2020-12-5");
        // 专家1完成市场1畜禽肉类抽检：肉+4
        checkCategoryByExpert(expert, superTask4, market, meat, 4, "2020-12-4");
//        printCheckReports(superTask4, expert);

        updateScore(expert);
        Assertions.assertEquals(20, expert.getTotalScore());
        Assertions.assertEquals("[监管任务3 2020-12-06][10]：按时完成",
                expert.getScoreRecordList().get(0).getScoreReason());
        Assertions.assertEquals("[监管任务4 2020-12-08][10]：按时完成",
                expert.getScoreRecordList().get(1).getScoreReason());

    }

    private static final String FINISH_ON_TIME = "按时完成";
    private static final String NOT_FINISH_ON_TIME = "未按时完成";
    private static final String NOT_FINISH_OVER_20_DAIES = "超过20天未完成";

    public static int differentDays(Date date1,Date date2){
        return (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
    }

    void updateScore(ICheck iCheck) {

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        Date today = new Date();
        try {
            today = dateFormat.parse("2020-12-25");
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
            ScoreRecord sr;
            if (today.compareTo(checkTask.getSuperTask().getDeadLine()) > 0) {
                // 20天内未完成
                if (differentDays(checkTask.getSuperTask().getDeadLine(), today) <= 20) {
                    sr = new ScoreRecord(checkTask.getSuperTask(), -10, NOT_FINISH_ON_TIME);
                }
                else {// 超过20天未完成
                    sr = new ScoreRecord(checkTask.getSuperTask(), -20, NOT_FINISH_OVER_20_DAIES);
                }
                iCheck.addScoreRecord(sr);
            }
        }
    }

    /**
     * 5. 验证专家和农贸市场未按时完成的抽检的场景,获取评估总得分和评估得/扣分的记录。
     */
    @Test
    void completeNotInTime(){
        // 加载 市场
        Set<Market> marketSet;
//        Market market = crudService.getMarket("市场1");
        Market market = new Market("市场1");
        // 加载 目录分类
        Set<ProductCategory> productCategorySet;
//        ProductCategory fruit = crudService.getProductCategory("水果类");
//        ProductCategory meat = crudService.getProductCategory("畜禽肉类");
        ProductCategory fruit = new ProductCategory("水果类");
        ProductCategory meat = new ProductCategory("畜禽肉类");


        System.out.println("【测试场景5】  验证专家和农贸市场未按时完成的抽检的场景,获取评估总得分和评估得/扣分的记录。");

        /* 创建监管任务 1 */
        // 指定一组市场
        marketSet = new HashSet<>();
        marketSet.add(market);
        // 指定一组产品分类
        productCategorySet = new HashSet<>();
        productCategorySet.add(fruit);
        productCategorySet.add(meat);
        // 创建监管任务1
        SuperTask superTask1 = createSuperTask("监管任务1", marketSet, productCategorySet, "2020-12-2");
        // 打印监管任务信息
//        printSuperTask(superTask1);

        // 市场1暗示完成抽检任务
        // 市场1完成水果类抽检：水果+10
        checkCategoryByMarket(superTask1, market, fruit, 10, "2020-12-1");
        // 市场1完成畜禽肉类抽检：肉+12
        checkCategoryByMarket(superTask1, market, meat, 12, "2020-12-10");
//        printCheckReports(superTask1, market);

        updateScore(market);
        Assertions.assertEquals(-10, market.getTotalScore());
        Assertions.assertEquals("[监管任务1 2020-12-02][-10]：未按时完成",
                market.getScoreRecordList().get(0).getScoreReason());

        /* 创建监管任务 2 */
        // 创建监管任务2
        SuperTask superTask2 = createSuperTask("监管任务2", marketSet, productCategorySet, "2020-12-3");
        // 打印监管任务信息
//        printSuperTask(superTask2);

        // 市场1成水果类抽检：水果+8
        checkCategoryByMarket(superTask2, market, fruit, 8, "2020-12-2");

        updateScore(market);
        Assertions.assertEquals(-30, market.getTotalScore());
//        System.out.println(market.getScoreRecordList().get(0).getScoreReason());
//        System.out.println(market.getScoreRecordList().get(1).getScoreReason());
        Assertions.assertEquals("[监管任务1 2020-12-02][-10]：未按时完成",
                market.getScoreRecordList().get(0).getScoreReason());
        Assertions.assertEquals("[监管任务2 2020-12-03][-20]：超过20天未完成",
                market.getScoreRecordList().get(1).getScoreReason());

        SuperTask superTask5 = createSuperTask("监管任务5", marketSet, productCategorySet, "2020-12-9");
        // 打印监管任务信息
//        printSuperTask(superTask1);

        // 市场1暗示完成抽检任务
        // 市场1完成水果类抽检：水果+10
        checkCategoryByMarket(superTask5, market, fruit, 10, "2020-12-2");
        // 市场1完成畜禽肉类抽检：肉+12
        checkCategoryByMarket(superTask5, market, meat, 12, "2020-12-3");
//        printCheckReports(superTask1, market);

        updateScore(market);
        Assertions.assertEquals(-20, market.getTotalScore());
        Assertions.assertEquals("[监管任务1 2020-12-02][-10]：未按时完成",
                market.getScoreRecordList().get(0).getScoreReason());
        Assertions.assertEquals("[监管任务5 2020-12-09][10]：按时完成",
                market.getScoreRecordList().get(1).getScoreReason());
        Assertions.assertEquals("[监管任务2 2020-12-03][-20]：超过20天未完成",
                market.getScoreRecordList().get(2).getScoreReason());

//        System.out.println(market1.getScoreRecordList().get(0).getScoreReason());
//        System.out.println(market1.getScoreRecordList().get(1).getScoreReason());

        // 加载 专家
//        Expert expert = crudService.getExpert("专家1");
        Expert expert = new Expert("专家1");
        SuperTask superTask3 = createSuperTask("监管任务3", marketSet, productCategorySet, "2020-12-3", expert);
        // 打印监管任务信息
//        printSuperTask(superTask3);

        // 专家1完成市场1畜禽肉类抽检：肉+4
        checkCategoryByExpert(expert, superTask3, market, meat, 4, "2020-12-4");
        // 专家1完成市场1水果类抽检：水果+8
        checkCategoryByExpert(expert, superTask3, market, fruit, 8, "2020-12-1");
//        printCheckReports(superTask3, expert);

        updateScore(expert);
        Assertions.assertEquals(-10, expert.getTotalScore());
        Assertions.assertEquals("[监管任务3 2020-12-03][-10]：未按时完成",
                expert.getScoreRecordList().get(0).getScoreReason());

        SuperTask superTask4 = createSuperTask("监管任务4", marketSet, productCategorySet, "2020-12-14", expert);
        // 打印监管任务信息
//        printSuperTask(superTask4);

        // 专家1完成市场1水果类抽检：水果+6
        checkCategoryByExpert(expert, superTask4, market, fruit, 6, "2020-12-5");

        updateScore(expert);
        Assertions.assertEquals(-20, expert.getTotalScore());
        Assertions.assertEquals("[监管任务3 2020-12-03][-10]：未按时完成",
                expert.getScoreRecordList().get(0).getScoreReason());
        Assertions.assertEquals("[监管任务4 2020-12-14][-10]：未按时完成",
                expert.getScoreRecordList().get(1).getScoreReason());

        SuperTask superTask6 = createSuperTask("监管任务6", marketSet, productCategorySet, "2020-12-8", expert);
        // 打印监管任务信息
//        printSuperTask(superTask4);

        // 专家1完成市场1水果类抽检：水果+6
        checkCategoryByExpert(expert, superTask6, market, fruit, 6, "2020-12-5");
        // 专家1完成市场1畜禽肉类抽检：肉+4
        checkCategoryByExpert(expert, superTask6, market, meat, 4, "2020-12-4");
//        printCheckReports(superTask4, expert);

        updateScore(expert);
        Assertions.assertEquals(-10, expert.getTotalScore());
        Assertions.assertEquals("[监管任务3 2020-12-03][-10]：未按时完成",
                expert.getScoreRecordList().get(0).getScoreReason());
        Assertions.assertEquals("[监管任务6 2020-12-08][10]：按时完成",
                expert.getScoreRecordList().get(1).getScoreReason());
        Assertions.assertEquals("[监管任务4 2020-12-14][-10]：未按时完成",
                expert.getScoreRecordList().get(2).getScoreReason());

    }

    /**
     * 打印 CheckTask 及相应的未完成产品类别
     * @param unfinishedList
     * @return
     */
    public List<CheckTask> printUnfinished(List<CheckTask> unfinishedList){
        for (CheckTask checkTask: unfinishedList){
            System.out.println("'" + checkTask.getDescription() + "'");
            Set<ProductCategory> unfinishedCategories = checkTask.getUnfinishedProductCategories();
            String unfinishedCategStr = "";
            for(ProductCategory category: unfinishedCategories){
                unfinishedCategStr += category.getName() + ",";
            }
            System.out.println("未完成分类：" + unfinishedCategStr);
        }
        return unfinishedList;
    }






}