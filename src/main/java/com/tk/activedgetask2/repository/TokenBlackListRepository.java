package com.tk.activedgetask2.repository;

import com.tk.activedgetask2.entity.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {

    TokenBlackList findByToken(String token);

}
