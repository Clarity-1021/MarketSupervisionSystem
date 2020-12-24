package fudan.se.hjjjxw.marketsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Entity
@JsonIgnoreProperties({"handler","hibernateLazyInitializer"})
@DynamicInsert(true)
public class ScoreRecord implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @Transient //@ManyToOne//(fetch=FetchType.EAGER, cascade = CascadeType.MERGE)
    //@JoinColumn(name = "task_id")
    private SuperTask superTask;

    @Transient //@ManyToOne
    //@JoinColumn(name = "market_id")
    private Market market;

    private int score;

    private String description;

    public ScoreRecord() {
    }

    public ScoreRecord(SuperTask superTask, int score, String description) {
        this.superTask = superTask;
        this.score = score;
        this.description = description;
    }

    public String getScoreReason() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");// 日期格式
        return "[" + this.superTask.getDescription() + " " +
                dateFormat.format(superTask.getDeadLine()) + "]" +
                "[" + score + "]：" + description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public SuperTask getSuperTask() {
        return superTask;
    }

    public void setSuperTask(SuperTask superTask) {
        this.superTask = superTask;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }
}
