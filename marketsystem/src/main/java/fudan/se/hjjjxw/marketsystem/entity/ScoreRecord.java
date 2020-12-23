package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class ScoreRecord implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private SuperTask superTask;

    private int score;

    private String description;

    public ScoreRecord() {
    }

    public ScoreRecord(Integer id, SuperTask superTask, int score, String description) {
        this.id = id;
        this.superTask = superTask;
        this.score = score;
        this.description = description;
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

}
