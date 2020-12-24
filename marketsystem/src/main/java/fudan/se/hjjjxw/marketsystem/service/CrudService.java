package fudan.se.hjjjxw.marketsystem.service;

import fudan.se.hjjjxw.marketsystem.entity.*;
import fudan.se.hjjjxw.marketsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RestController
public class CrudService {

    @Autowired
    private CheckReportRepository checkReportRepository;

    @Autowired
    private CheckTaskRepository checkTaskRepository;

    @Autowired
    private ExpertRepository expertRepository;

    @Autowired
    private MarketRepository marketRepository;

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Autowired
    private ScoreRecordRepository scoreRecordRepository;

    @Autowired
    private SuperTaskRepository superTaskRepository;

    @RequestMapping(value = "/test_save")
    public void saveMarket(Market market) {
        marketRepository.save(market);
//        Market market1 = new Market(market);
//        marketRepository.save(market1);

    }

    @RequestMapping(value = "/test_get")
    public Market getMarket(String name) {
        return marketRepository.findByName(name);
    }

    public void saveProductCategory(ProductCategory productCategory) {
        productCategoryRepository.save(productCategory);
    }

    public ProductCategory getProductCategory(String name) {
        return productCategoryRepository.findByName(name);
    }


    public void saveExpert(Expert expert) {
        expertRepository.save(expert);
    }

    public Expert getExpert(String name) {
        return expertRepository.findByName(name);
    }


    public void initDB(){
        //
//        System.out.println("初始化数据");
//
//        // ----  创建商品类别  --------
        List<String> nameList = new ArrayList<>();
        nameList.add("水果类");
        nameList.add("畜禽肉类");
        nameList.add("蔬菜类");
        nameList.add("水产品类");
//
        for (String name : nameList) {
            ProductCategory category = new ProductCategory(name);
            saveProductCategory(category);
        }

//        //  ------ 创建专家  ---------
        List<String> expertNames = new ArrayList<>();
        expertNames.add("专家1");
        expertNames.add("专家2");
        expertNames.add("专家3");
        for (String name : expertNames) {
            Expert expert = new Expert(name);
            saveExpert(expert);
        }
//
//        //  ------ 创建农贸市场  --------
        List<String> marketNames = new ArrayList<>();
        marketNames.add("市场1");
        marketNames.add("市场2");
        marketNames.add("市场3");
        for (String name : marketNames) {
            Market market = new Market(name);
            saveMarket(market);
        }
    }

    @RequestMapping(value = "/test")
    public void Test() {

//        initDB();

        List<String> nameList = new ArrayList<>();
        nameList.add("水果类");
        nameList.add("畜禽肉类");
        nameList.add("蔬菜类");

        List<String> expertNames = new ArrayList<>();
        expertNames.add("专家1");
        expertNames.add("专家2");
        expertNames.add("专家3");

        List<String> marketNames = new ArrayList<>();
        marketNames.add("市场1");
        marketNames.add("市场2");
        marketNames.add("市场3");
        // 监管局给一组农贸市场发起监管任务
//        List<String> marketNames = new ArrayList<>();
//        marketNames.add("市场1");
//        marketNames.add("市场2");
//        marketNames.add("市场3");
        Set<Market> marketList = new HashSet<>();
        for (String name : marketNames) {
            Market market = getMarket(name);
            marketList.add(market);
        }

        Set<ProductCategory> categoryList = new HashSet<>();
        for (String name : nameList) {
            ProductCategory category = getProductCategory(name);
            categoryList.add(category);
        }
        DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date deadline = new Date();
        try {
            deadline = dateFormat1.parse("2020-12-1");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        IndicatorService indicatorService = new IndicatorService();
        SuperTask superTask1 = indicatorService.launchCheck("监管任务1", marketList, categoryList, deadline);

        // 农贸市场查看待完成任务
        for (String marketName : marketNames) {
            Market market = getMarket(marketName);
            List<CheckTask> unfinishedList = market.getUnfinishedCheckTask();
            System.out.print(unfinishedList.size());
        }
    }
}
