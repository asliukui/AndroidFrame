package com.acmenxd.app;

import com.acmenxd.frame.basis.FrameApplication;
import com.acmenxd.logger.Logger;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail 顶级Application
 */
public final class BaseApplication extends FrameApplication {
    // 初始化状态 -> 默认false,初始化完成为true
    public boolean isInitFinish = false;
    // 记录启动时间
    public long startTime = 0;

    public static synchronized BaseApplication instance() {
        return (BaseApplication) FrameApplication.instance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.w("App已启动!");
        startTime = System.currentTimeMillis();

        // 配置框架设置
        // initFrameSetting(AppFrameConfig.class, AppConfig.DEBUG);

        // 初始化完毕
        isInitFinish = true;
        Logger.w("App初始化完成!");
    }

}
