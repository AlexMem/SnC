package com.eltech.snc.server.jpa.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "rolling_ball_1")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RollingBallEntity implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "ball")
    @SequenceGenerator(name = "ball", sequenceName = "rolling_ball_id_seq", allocationSize = 1)
    @Column(name = "id")
    Integer id;

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "result")
    Double result;

    @Column(name = "date_time")
    LocalDateTime date;
}