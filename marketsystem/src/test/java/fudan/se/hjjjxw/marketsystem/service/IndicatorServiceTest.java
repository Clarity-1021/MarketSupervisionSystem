package fudan.se.hjjjxw.marketsystem.service;

import fudan.se.hjjjxw.marketsystem.entity.CheckTask;
import fudan.se.hjjjxw.marketsystem.entity.Expert;
import fudan.se.hjjjxw.marketsystem.entity.Market;
import fudan.se.hjjjxw.marketsystem.entity.ProductCategory;
import fudan.se.hjjjxw.marketsystem.repository.ExpertRepository;
import fudan.se.hjjjxw.marketsystem.repository.MarketRepository;
import fudan.se.hjjjxw.marketsystem.repository.ProductCategoryRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class IndicatorServiceTest {


    @Autowired
    IndicatorService indicatorService;

    @Autowired
    static ProductCategoryRepository productCategoryRepository;
    @Autowired
    static ExpertRepository expertRepository;
    @Autowired
    static MarketRepository marketRepository;

    @BeforeAll
    public static void init() {
        System.out.println("初始化数据");

        // ----  创建商品类别  --------
        List<String> nameList = new ArrayList<>();
        nameList.add("水果类");
        nameList.add("畜禽肉类");
        nameList.add("蔬菜类");
        nameList.add("水产品类");

        for(String name:nameList){
            ProductCategory category = new ProductCategory(name);
            productCategoryRepository.save(category);
        }

        //  ------ 创建专家  ---------
        List<String> expertNames = new ArrayList<>();
        expertNames.add("专家1");
        expertNames.add("专家2");
        expertNames.add("专家3");
        for(String name:expertNames){
            Expert expert = new Expert(name);
            expertRepository.save(expert);
        }

        //  ------ 创建农贸市场  --------
        List<String> marketNames = new ArrayList<>();
        marketNames.add("市场1");
        marketNames.add("市场2");
        marketNames.add("市场3");
        for(String name:marketNames){
            Market market = new Market(name);
            marketRepository.save(market);
        }


    }


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
     * 1. 监管局给一组农贸市场发起监管任务，农贸市场查看待完成任务，并完成抽检任务。
     */
    @Test
    void initTaskWithoutExpert(){
        // 监管局给一组农贸市场发起监管任务
        List<String> marketNames = new ArrayList<>();
        marketNames.add("市场1");
        marketNames.add("市场2");
        marketNames.add("市场3");
        Set<Market> marketList = new HashSet<>();
        for(String name: marketNames){
            Market market = marketRepository.findByName(name);
            marketList.add(market);
        }

        List<String> nameList = new ArrayList<>();
        nameList.add("水果类");
        nameList.add("畜禽肉类");
        nameList.add("蔬菜类");

        Set<ProductCategory> categoryList = new HashSet<>();
        for(String name:nameList){
            ProductCategory category = productCategoryRepository.findByName(name);
            categoryList.add(category);
        }
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline = new Date();
        try {
            deadline = dateFormat1.parse("2020-12-1");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        indicatorService.lauchCheck("监管任务1",marketList, categoryList, deadline);

        // 农贸市场查看待完成任务
        for(String marketName: marketNames){
            Market market = marketRepository.findByName(marketName);
            List<CheckTask> unfinishedList = market.checkUnfinishedCheckTask();
            Assertions.assertEquals(1,unfinishedList.size());
        }

        // 完成抽检任务
        // 市场1 按时完成 监管任务1
        Market market1 = marketRepository.findByName("市场1");
        List<CheckTask> unfinishedList = market1.checkUnfinishedCheckTask();

        Date finishDate1 = new Date();
        try {
            finishDate1 = dateFormat1.parse("2020-12-2");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(CheckTask checkTask: unfinishedList){
            Set<ProductCategory> unfinishedCategories = checkTask.getUnfinishedProductCategories();
            for(ProductCategory category: unfinishedCategories){
                market1.checkProductCategory(category, checkTask, 10, finishDate1);
            }
        }
        // 测试 市场1 完成 抽检任务1 后的未完成任务数
        unfinishedList = market1.checkUnfinishedCheckTask();
        Assertions.assertEquals(0,unfinishedList.size());

    }

    /**
     * 2. 监管局给一个专家发起监管任务，专家查看待完成任务，并完成抽检任务。
     */
    @Test
    void initTaskWithExpert(){

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






}