package com.example.application.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        indexes = {
                @Index(name = "speed_timestamp", columnList = "timestamp")
        }
)
@Data
public class Speed extends AbstractEntity {
    @ManyToOne
    private Car car;
    @Column
    private Integer speed;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
