package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class CheckTask implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    private SuperTask superTask;

    public CheckTask() {
    }

    public CheckTask(Integer id, SuperTask superTask) {
        this.id = id;
        this.superTask = superTask;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SuperTask getSuperTask() {
        return superTask;
    }

    public void setSuperTask(SuperTask superTask) {
        this.superTask = superTask;
    }
}
