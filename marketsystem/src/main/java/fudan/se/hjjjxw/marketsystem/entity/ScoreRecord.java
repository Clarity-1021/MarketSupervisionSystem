package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ScoreRecord implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private Market market;

    int score;

    public ScoreRecord() {
    }

    public ScoreRecord(Integer id, Market market, int score) {
        this.id = id;
        this.market = market;
        this.score = score;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Market getMarket() {
        return market;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
