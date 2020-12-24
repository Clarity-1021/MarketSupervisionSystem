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
        System.out.println("【Step】 监管局 创建 ”监管任务1“ ");

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
                System.out.println("【Step】 市场1 完成 " + checkTask.getDescription() + productCategory.getName() + " 抽检");
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

    /**
     * 3. 监管局查看某个农贸产品类别在某个时间范围内的总的不合格数（时间以抽检日期为准）。
     */
    @Test
    void checkUnqualified(){

    }

    /**
     * 4. 验证专家和农贸市场按时完成抽检的场景，获取评估总得分和评估得/扣分的记录。
     */
    @Test
    void completeInTime(){

    }

    /**
     * 5. 验证专家和农贸市场未按时完成的抽检的场景,获取评估总得分和评估得/扣分的记录。
     */
    @Test
    void completeNotInTime(){

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