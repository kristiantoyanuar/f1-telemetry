package com.example.application.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        indexes = {
                @Index(name = "brake_timestamp", columnList = "timestamp")
        }
)
@Data
public class BrakeCondition extends AbstractEntity {

    public enum Condition {
        COLD(0), WARM(1), HOT(2);
        private int value;

        private Condition(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    @ManyToOne
    private Car car;
    @Enumerated(EnumType.ORDINAL)
    private Condition condition;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
