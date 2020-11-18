package com.eltech.snc.server.controller;

import com.eltech.snc.server.jpa.entity.UnlockEntity;
import com.eltech.snc.server.entity.UnlockListEntity;
import com.eltech.snc.server.services.UnlockService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UnlockController {
    UnlockService unlockService;

    public UnlockController(UnlockService unlockService) {
        this.unlockService = unlockService;
    }

    @PostMapping(value = "/saveUnlock", consumes = "application/json", produces = "application/json")
    public Integer createUser(@RequestBody UnlockListEntity unlock) {
        List<UnlockEntity> entities = new ArrayList<>();
        for (int i = 0; i < unlock.getPointX().size(); i++) {
            entities.add(UnlockEntity.builder()
                    .userId(null)
                    .rowNum(i)
                    .userId(unlock.getUserId())
                    .pointX(unlock.getPointX().get(i))
                    .pointY(unlock.getPointY().get(i))
                    .date(unlock.getDate())
                    .build());
        }
        return unlockService.save(entities);
    }
}
