package com.leo.secondkill.service;

import com.leo.secondkill.entity.SecondKill;
import com.leo.secondkill.entity.SecondProduct;

import java.util.List;

/**
 * @author Liu
 */
public interface SecondService {
    List<SecondProduct> getSecondInfo();

    String secondKill(String uid, String token, Integer num, String kill_id);
}
