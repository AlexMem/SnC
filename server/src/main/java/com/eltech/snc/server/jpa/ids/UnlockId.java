package com.eltech.snc.server.jpa.ids;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
public class UnlockId implements Serializable {
    Integer id;
    Integer rowNum;
}
