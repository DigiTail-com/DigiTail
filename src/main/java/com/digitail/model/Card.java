package com.digitail.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Random;

@Getter
@Setter
public class Card {
    private Integer number;
    private String date;
    private Byte cvv;
    private String owner;

    public boolean check(){
        return new Random().nextInt(2) != 0;
    }

    public boolean checkMoney(){
        return new Random().nextInt(2) != 0;
    }
}
