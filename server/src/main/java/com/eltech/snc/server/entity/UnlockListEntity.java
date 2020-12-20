package com.eltech.snc.server.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnlockListEntity implements Serializable {

    Integer userId;

    List<Double> pointX;

    List<Double> pointY;

    LocalDateTime date;
}