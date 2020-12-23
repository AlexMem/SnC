package com.eltech.snc.server.jpa.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users_1")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "user")
    @Column(name = "id")
    @SequenceGenerator(name = "user", sequenceName = "users_id_seq", allocationSize = 1)
    Integer id;
    @Column(name = "name")
    String name;
}
