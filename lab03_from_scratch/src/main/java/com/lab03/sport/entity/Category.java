package com.lab03.sport.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("CATEGORY")
public class Category {
    @Id
    private Long catId;
    private String catName;
}
