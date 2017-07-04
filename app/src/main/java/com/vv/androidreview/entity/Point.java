
package com.vv.androidreview.entity;

import cn.bmob.v3.BmobObject;

/**
 * Author：Laofu .
 * Mail：471996049@qq.com
 */
public class Point extends BmobObject{

    //知识点名字
    private String name;

    //方块颜色
    private Integer color;

    private Unit unit;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getColor() {
        return color;
    }

    public void setColor(Integer color) {
        this.color = color;
    }
}
