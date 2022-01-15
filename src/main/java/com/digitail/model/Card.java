package com.digitail.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Random;

@Table(name = "card")
@Entity
@Getter
@Setter
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, optional=true)
    private User user;

    private Long number;
    private String year;
    private String month;
    private Byte cvv;
    private String owner;

    public boolean check(){
        return new Random().nextInt(2) != 0;
    }

    public boolean checkMoney(){
        return new Random().nextInt(2) != 0;
    }
}
