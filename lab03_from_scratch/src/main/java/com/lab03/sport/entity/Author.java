package com.lab03.sport.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("AUTHOR")
public class Author {
    @Id
    private Long authId;
    private String authName;
}
