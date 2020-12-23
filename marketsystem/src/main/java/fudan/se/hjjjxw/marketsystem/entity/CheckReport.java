package fudan.se.hjjjxw.marketsystem.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
public class CheckReport implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private int unqualifiedCnt;

    private Date checkDate;

    public CheckReport() {
    }

    public CheckReport(Integer id, int unqualifiedCnt, Date checkDate) {
        this.id = id;
        this.unqualifiedCnt = unqualifiedCnt;
        this.checkDate = checkDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getUnqualifiedCnt() {
        return unqualifiedCnt;
    }

    public void setUnqualifiedCnt(int unqualifiedCnt) {
        this.unqualifiedCnt = unqualifiedCnt;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
}
