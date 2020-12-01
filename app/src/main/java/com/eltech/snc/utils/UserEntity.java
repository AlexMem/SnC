package com.eltech.snc.utils;

import java.io.Serializable;

public class UserEntity implements Serializable {
    private Integer id;
    private String name;

    public UserEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
