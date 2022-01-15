package com.digitail.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Table(name = "basket")
@Entity
@Getter
@Setter
public class BasketGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, optional=true)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, optional=true)
    private Product product;
}
