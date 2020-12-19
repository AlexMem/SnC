package com.eltech.snc.server.controller;

import com.eltech.snc.server.entity.UnlockListEntity;
import com.eltech.snc.server.jpa.entity.UnlockEntity;
import com.eltech.snc.server.services.UnlockService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UnlockController {
    UnlockService unlockService;

    public UnlockController(UnlockService unlockService) {
        this.unlockService = unlockService;
    }

    @PostMapping(value = "/saveUnlock", consumes = "application/json", produces = "application/json")
    public IdWrapper createUnlock(@RequestBody UnlockListEntity unlock) {
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

        IdWrapper idWrapper = new IdWrapper(unlockService.save(entities));
//        System.out.println(idWrapper);
        log.debug("SOME MSG" + idWrapper);
        return idWrapper;
    }

    @GetMapping(value = "/getUnlockRegs")
    public Integer getUnlockRegs(@RequestParam Integer userId) {
        return unlockService.getUnlockRegs(userId);
    }

    @GetMapping(value = "/setErr")
    public Double setErr(@RequestParam String err) {
        return unlockService.setErr(Double.parseDouble(err));
    }

    public static class IdWrapper implements Serializable {
        private Integer id;

        public IdWrapper(Integer id) {
            this.id = id;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "IdWrapper{" +
                    "id=" + id +
                    '}';
        }
    }
}
