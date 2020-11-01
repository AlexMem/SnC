package com.eltech.snc.server.jpa.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(name = "maze")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MazeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "maze")
    @SequenceGenerator(name = "maze", sequenceName = "maze_id_seq", allocationSize = 1)
    @Column(name = "id")
    Integer id;

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "result")
    Double result;

    @Column(name = "date_time")
    LocalDateTime date;
}