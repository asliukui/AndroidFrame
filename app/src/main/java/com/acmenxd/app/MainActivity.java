package com.acmenxd.app;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.acmenxd.frame.basis.FrameActivity;
import com.acmenxd.frame.utils.RxUtils;
import com.acmenxd.logger.Logger;

import rx.Subscription;
import rx.functions.Func1;

/**
 * @author AcmenXD
 * @version v1.0
 * @github https://github.com/AcmenXD
 * @date 2017/2/6 17:35
 * @detail
 */
public class MainActivity extends FrameActivity {
    @Override
    public void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Frame框架");

        Subscription subscription = request().postRx("param...")
                .compose(RxUtils.<TestEntity>applySchedulers())
                //上面一行代码,等同于下面的这两行代码
                //.subscribeOn(Schedulers.io())
                //.observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<TestEntity, TestEntity>() {
                    @Override
                    public TestEntity call(TestEntity pData) {
                        Logger.e(System.currentTimeMillis());
                        Logger.e("subscription:map");
                        return pData;
                    }
                })
                // 这里的true表示请求期间对数据进行过处理,这样Retrofit无法识别处理后的数据,所以需要开发者手动处理错误异常
                .subscribe(new FrameActivity.BindSubscriber<TestEntity>() {
                    @Override
                    public void succeed(final @NonNull TestEntity pData) {
                        Logger.e("succeed");
                    }

                    @Override
                    public void finished() {
                        Logger.e("finished");
                    }
                });
        addSubscriptions(subscription);
        getView(R.id.tv).postDelayed(new Runnable() {
            @Override
            public void run() {
                //getCompositeSubscription().unsubscribe();
                Logger.e("cancel");
            }
        }, 200);
    }

    public IRequest request() {
        return request(IRequest.class);
    }
}
