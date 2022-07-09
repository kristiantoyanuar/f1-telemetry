package com.example.application.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(
        indexes = {
                @Index(name = "gear_position_timestamp", columnList = "timestamp")
        }
)
@Data
public class GearPosition extends AbstractEntity {

    public enum Position {
        _1("1"), _2("2"), _3("3"), _4("4"), _5("5"), _6("R");
        private String value;
        private Position(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return value;
        }
    }
    @ManyToOne
    private Car car;
    @Enumerated(EnumType.ORDINAL)
    @Column(length = 1)
    private Position position;
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
