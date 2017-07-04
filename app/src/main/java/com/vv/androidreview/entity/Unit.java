
package com.vv.androidreview.entity;

import cn.bmob.v3.BmobObject;

/**
 * Author：Laofu 2017/6/24.
 * Mail：471996049@qq.com
 * Description：单元
 */
public class Unit extends BmobObject {

    //单元名称
    private String name ;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
