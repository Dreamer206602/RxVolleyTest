package com.zc.lb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.kymjs.okhttp.OkHttpStack;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.http.RequestQueue;
import com.kymjs.rxvolley.rx.Result;
import com.squareup.okhttp.OkHttpClient;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class Test1Activity extends AppCompatActivity {

    @Bind(R.id.imageView)
    ImageView imageView;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        ButterKnife.bind(this);

        RxVolley.setRequestQueue(RequestQueue.newRequestQueue(RxVolley.CACHE_FOLDER,
                new OkHttpStack(new OkHttpClient())));

        test();

    }

    /**
     * RxJava
     */
    private void test() {
        Observable<Result> observable = new RxVolley.Builder()
                .url("http://kymjs.com/feed.xml")
                .contentType(RxVolley.ContentType.FORM)
                .getResult();
        subscription = observable.filter(new Func1<Result, Boolean>() {
            @Override
            public Boolean call(Result result) {
                return result.data != null;
            }
        })
                .map(new Func1<Result, String>() {
                    @Override
                    public String call(Result result) {
                        return new String(result.data);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("lb", "==========网络请求" + s);
                    }
                });


    }
}
