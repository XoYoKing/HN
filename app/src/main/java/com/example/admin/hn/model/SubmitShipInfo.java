package com.example.admin.hn.model;

import java.util.List;

/**
 * Created by duantao
 *
 * @date on 2017/10/30 17:10
 * 选择船舶的提交参数
 */
public class SubmitShipInfo {
    public String userId;
    public List<String> ships;
    public SubmitShipInfo(String userId, List<String> ships) {
        this.userId = userId;
        this.ships = ships;
    }
}
