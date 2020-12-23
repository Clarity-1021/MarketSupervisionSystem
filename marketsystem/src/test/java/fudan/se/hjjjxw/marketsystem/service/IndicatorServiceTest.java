package fudan.se.hjjjxw.marketsystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class IndicatorServiceTest {


    @Autowired
    IndicatorService indicatorService;


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