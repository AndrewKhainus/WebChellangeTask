package com.task.webchallengetask.global.programs;

import com.task.webchallengetask.global.programs.difficults.Difficult;

import java.util.List;

/**
 * Created by klim on 23.03.16.
 */
public class Program {

    protected String name;
    protected String description;
    protected List<Difficult> difficult;

    public Program(String name, String description, List<Difficult> difficult) {
        this.name = name;
        this.description = description;
        this.difficult = difficult;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Difficult> getDifficult() {
        return difficult;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
