package com.task.webchallengetask.global.programs.difficults;

/**
 * Created by klim on 23.03.16.
 */
public class Difficult {
    String name;
    String target;
    String unit;

    public Difficult(String name, String target, String unit) {
        this.name = name;
        this.target = target;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
