package com.acmenxd.frame.basis;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Debug;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import com.acmenxd.frame.utils.CrashUtils;
import com.acmenxd.frame.utils.DateUtils;
import com.acmenxd.frame.utils.FileUtils;
import com.acmenxd.frame.utils.code.EncodeDecode;
import com.acmenxd.frame.utils.net.Monitor;
import com.acmenxd.logger.LogTag;
import com.acmenxd.logger.Logger;
import com.acmenxd.retrofit.HttpManager;
import com.acmenxd.sptool.SpEncodeDecodeCallback;
import com.acmenxd.sptool.SpManager;
import com.acmenxd.toaster.Toaster;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2016/11/22 14:36
 * @detail Application基类
 */
public abstract class FrameApplication extends Application {
    protected final String TAG = this.getClass().getSimpleName();

    // 单例实例
    private static FrameApplication sInstance = null;
    // 配置详细参数类
    private FrameConfig sFrameConfig = null;

    public FrameApplication() {
        sInstance = this;
    }

    public static synchronized FrameApplication instance() {
        if (sInstance == null) {
            new RuntimeException("FrameApplication == null ?? you should extends FrameApplication in you app");
        }
        return sInstance;
    }

    @CallSuper
    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        // 终止网络监听
        Monitor.release();
    }

    @CallSuper
    @Override
    public void onLowMemory() {
        // 低内存的时候执行
        super.onLowMemory();
    }

    @CallSuper
    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行
        super.onTrimMemory(level);
    }

    @CallSuper
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // 应用配置变更
        super.onConfigurationChanged(newConfig);
    }

    /**
     * 初始化配置
     */
    protected final void initFrameSetting(@NonNull FrameConfig pFrameConfig) {
        if (pFrameConfig == null) {
            throw new NullPointerException("FrameConfig is null");
        }
        if (sFrameConfig != null) {
            return;
        }
        sFrameConfig = pFrameConfig;
        sFrameConfig.init();

        // 基础组件配置
        setFrameConfig();
        // 初始化File配置
        FileUtils.init();
        // 初始化网络监听配置
        Monitor.init();
        // 崩溃异常捕获
        CrashUtils.getInstance(instance());
    }

    /**
     * 基础组件配置
     */
    public final void setFrameConfig() {
        Context context = this.getApplicationContext();
        //------------------------------------Logger配置---------------------------------
        Logger.APP_PKG_NAME = context.getPackageName();
        Logger.LOG_OPEN = sFrameConfig.LOG_OPEN;
        Logger.LOG_LEVEL = sFrameConfig.LOG_LEVEL;
        Logger.LOGFILE_PATH = sFrameConfig.LOG_DIR;
        //------------------------------------Toaster配置---------------------------------
        Toaster.DEBUG = sFrameConfig.TOAST_DEBUG_OPEN;
        Toaster.TOAST_DURATION = sFrameConfig.TOAST_DURATION;
        Toaster.NEED_WAIT = sFrameConfig.TOAST_NEED_WAIT;
        // * 必须设置,否则无法使用
        Toaster.init(context);
        //------------------------------------SpTool配置---------------------------------
        // 设置全局Sp实例,项目启动时创建,并通过getCommonSp拿到,项目中只有一份实例
        SpManager.CommonSp = sFrameConfig.spAll;
        // 加解密回调 - 不设置或null表示不进行加解密处理
        SpManager.setEncodeDecodeCallback(new SpEncodeDecodeCallback() {
            @Override
            public String encode(String pStr) {
                String result = null;
                try {
                    result = EncodeDecode.encode(pStr);
                } catch (IOException pE) {
                    pE.printStackTrace();
                }
                return result;
            }

            @Override
            public String decode(String pStr) {
                String result = null;
                try {
                    result = EncodeDecode.decode(pStr);
                } catch (IOException pE) {
                    pE.printStackTrace();
                } catch (ClassNotFoundException pE) {
                    pE.printStackTrace();
                }
                return result;
            }
        });
        // * 必须设置,否则无法使用
        SpManager.init(context);
        //------------------------------------Retrofit配置---------------------------------
        HttpManager.INSTANCE.net_log_tag = sFrameConfig.NET_LOG_TAG;
        HttpManager.INSTANCE.net_log_details = sFrameConfig.NET_LOG_DETAILS;
        HttpManager.INSTANCE.net_log_details_all = sFrameConfig.NET_LOG_DETAILS_All;
        HttpManager.INSTANCE.noformbody_canaddbody = sFrameConfig.NOFORMBODY_CANADDBODY;
        HttpManager.INSTANCE.net_cache_dir = new File(FrameApplication.instance().getCacheDir(), "NetCache");
        HttpManager.INSTANCE.net_cache_type = sFrameConfig.NET_CACHE_TYPE;
        HttpManager.INSTANCE.net_cache_size = sFrameConfig.NET_CACHE_SIZE;
        HttpManager.INSTANCE.connect_timeout = sFrameConfig.CONNECT_TIMEOUT;
        HttpManager.INSTANCE.read_timeout = sFrameConfig.READ_TIMEOUT;
        HttpManager.INSTANCE.write_timeout = sFrameConfig.WRITE_TIMEOUT;
        // * 必须设置,否则无法使用
        HttpManager.INSTANCE.init(context, sFrameConfig.BASE_URL);
    }

    /**
     * 获取配置详情
     */
    public final <T extends FrameConfig> T getFrameConfig() {
        if (sFrameConfig != null) {
            return (T) sFrameConfig;
        }
        return null;
    }

    /**
     * 程序发生崩溃异常时回调
     */
    @CallSuper
    public void crashException(@NonNull String projectInformation, @NonNull Thread pThread, @NonNull Throwable pE) {
        String fileName = "crash-" + DateUtils.nowDate(DateUtils.yMdHms2) + ".txt";
        StringBuffer sb = new StringBuffer();
        sb.append("项目信息============================================\n");
        sb.append(projectInformation);
        sb.append("\n机型信息============================================\n");
        Field[] fields = Build.class.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                sb.append(field.getName()).append(" = ").append(field.get("").toString()).append("\n");
            }
        } catch (IllegalAccessException pE1) {
            Logger.e("crashException:" + pE1.getMessage());
        }
        Logger.file(LogTag.mk("crashException"), fileName, pE, sb.toString());
        // 内存溢出类型崩溃,生成.hprof文件 - 由于文件较大,会造成app崩溃时卡顿,所以暂时关闭此功能
        // crashOutOfMemory(pE, fileName.replace(".txt", ".hprof"));
    }

    /**
     * 保存内存溢出日志文件
     */
    private final void crashOutOfMemory(@NonNull Throwable pE, @NonNull String fileName) {
        boolean result = false;
        if (OutOfMemoryError.class.equals(pE.getClass())) {
            result = true;
        } else {
            Throwable cause = pE.getCause();
            while (null != cause) {
                if (OutOfMemoryError.class.equals(cause.getClass())) {
                    result = true;
                }
                cause = cause.getCause();
            }
        }
        if (result) {
            try {
                Debug.dumpHprofData(getFrameConfig().LOG_DIR + fileName);
            } catch (IOException pE1) {
                Logger.e("crashException:" + pE1.getMessage());
            }
        }
    }
}
