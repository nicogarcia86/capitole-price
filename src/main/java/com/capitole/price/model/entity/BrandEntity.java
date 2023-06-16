package com.capitole.price.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "brand")
public class BrandEntity {
    @Id
    private Integer id;
    private String name;
}
