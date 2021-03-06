package com.eltech.snc.server.services;

import com.eltech.snc.server.jpa.entity.UserEntity;
import com.eltech.snc.server.jpa.repo.UserRepo;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true)
public class UserService {
    UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Integer createUser(UserEntity user) {
        UserEntity save = userRepo.save(user);
        return save.getId();
    }

    public Integer findUser(String name) {
        UserEntity user = userRepo.findByName(name);
        if (user == null) {
            user = new UserEntity();
            user.setId(-1);
        }
        return user.getId();
    }
}
