package com.eltech.snc.server.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@NoArgsConstructor
@Table(name = "maze")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnlockEntity implements Serializable {
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