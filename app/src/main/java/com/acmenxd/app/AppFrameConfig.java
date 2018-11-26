package com.acmenxd.app;

import com.acmenxd.frame.basis.FrameConfig;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/5/24 18:39
 * @detail 初始配置文件
 */
public final class AppFrameConfig extends FrameConfig {

    public String SP_Device = "spDevice";
    public String SP_Config = "spConfig";
    public String SP_User = "spUser";

    protected AppFrameConfig(boolean isDebug) {
        super(isDebug);
    }

    @Override
    protected void init() {
        DB_NAME = "test_db";
        BASE_DIR = SDCARD_DIR + "/TestApp/";
        spAll = new String[]{SP_Cookie, SP_Device, SP_Config, SP_User};
    }
}
