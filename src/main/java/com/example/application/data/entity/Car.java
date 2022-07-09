package com.example.application.data.entity;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(indexes = @Index(name = "name", columnList = "name"))
@Data
public class Car extends AbstractEntity{
    @Column
    @NaturalId
    private String name;
}
