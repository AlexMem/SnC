package com.eltech.snc.server.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.eltech.snc.server.jpa.ids.UnlockId;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
//@IdClass(UnlockId.class)
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "unlock_statistic_1")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnlockEntity implements Serializable {

    @Id
    @Column(name = "uniq_id")
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "unlock")
    @SequenceGenerator(name = "unlock", sequenceName = "unlock_statistic_1_uniq_id_seq", allocationSize = 1)
    Integer uniqId;

//    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "unlock")
//    @SequenceGenerator(name = "unlock", sequenceName = "unlock_statistic_id_seq", allocationSize = 1)
    @Column(name = "id")
    Integer id;

    @Column(name = "row_num")
    Integer rowNum;

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "point_x")
    Double pointX;

    @Column(name = "point_y")
    Double pointY;

    @Column(name = "date_time")
    LocalDateTime date;
}