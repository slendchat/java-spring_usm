package com.lab03.sport.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("PUBLISHER")
public class Publisher {
    @Id
    private Long pubId;
    private String pubName;
}
