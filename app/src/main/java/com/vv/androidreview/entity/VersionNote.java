
package com.vv.androidreview.entity;

/**
 * Author：Laofu .
 * Mail：471996049@qq.com
 * Description：记录更新日志
 */
public class VersionNote {

    private String versionName;
    private String updateTime;
    private String updateNote;

    public VersionNote(String versionName, String updateTime, String updateNote) {
        this.versionName = versionName;
        this.updateTime = updateTime;
        this.updateNote = updateNote;
    }

    public String getUpdateNote() {
        return updateNote;
    }

    public String getVersionName() {
        return versionName;
    }

    public String getUpdateTime() {
        return updateTime;
    }


}
