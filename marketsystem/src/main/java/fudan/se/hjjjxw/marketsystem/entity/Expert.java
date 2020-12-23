package fudan.se.hjjjxw.marketsystem.entity;

import java.io.Serializable;

public class Expert implements Serializable {

    private String name;

    public Expert(){}

    public Expert(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
