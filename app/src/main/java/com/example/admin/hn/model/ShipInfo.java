package com.example.admin.hn.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by duantao
 * 船舶
 * @date on 2017/10/30 17:10
 */
public class ShipInfo {
    public List<Ship> ships;
    public static class Ship implements Serializable {
        public String shipname;
        public String shipid;//船舶ID
        public boolean isSelect;//是否选中
        public Ship(String shipid, String shipname) {
            this.shipid = shipid;
            this.shipname = shipname;
        }
    }
}
