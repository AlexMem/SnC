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
@Table(name = "stencil")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StencilEntity implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "stencil")
    @SequenceGenerator(name = "stencil", sequenceName = "stencil_id_seq", allocationSize = 1)
    @Column(name = "id")
    Integer id;

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "result")
    Double result;

    @Column(name = "date_time")
    LocalDateTime date;
}