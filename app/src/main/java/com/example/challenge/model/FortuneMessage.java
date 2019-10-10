package com.example.challenge.model;

import java.util.List;

public class FortuneMessage {
    private List<String> fortune;

    public void setFortune(List<String> fortune) {
        this.fortune = fortune;
    }

    public List<String> getFortune() {
        return fortune;
    }
}
