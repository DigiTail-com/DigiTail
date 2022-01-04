package com.digitail.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Product")
@Getter
@Setter
public class Product {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String name;

    private Status status;

    private String path;

    private Double price;

    private String description;

    private String fileName;

    private Category category;

    private String techDescription;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, optional=true)
    private User user;

}
