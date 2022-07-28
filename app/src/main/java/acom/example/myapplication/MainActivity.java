package acom.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import acom.example.myapplication.map.Function;
import acom.example.myapplication.rxjava.Emitter;
import acom.example.myapplication.rxjava.Observable;
import acom.example.myapplication.rxjava.ObservableOnSubscribe;
import acom.example.myapplication.rxjava.ObservableSource;
import acom.example.myapplication.rxjava.Observer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(Emitter<Object> emitter) {
                Log.d("TAG", "subscribe");
                emitter.onNext("xxx");
                emitter.onNext("ttt");
                emitter.onNext("ooo");
                emitter.onError(new Throwable());
                emitter.onComplete();
            }
        })
//                .map(new Function<Object, Object>() {
//
//                    @Override
//                    public Object apply(Object o) {
//                        return "处理后的" + o;
//                    }
//                })
                .flatMap(new Function<Object, ObservableSource<? extends Object>>() {
                    @Override
                    public ObservableSource<? extends Object> apply(Object o) {
                        return Observable.create(new ObservableOnSubscribe<Object>() {
                            @Override
                            public void subscribe(Emitter<Object> emitter) {
                                emitter.onNext("处理后的" + o);
                            }
                        });
                    }
                })
                .subscribe(new Observer() {
            @Override
            public void onSubscribe() {
                Log.d("TAG", "onSubscribe...");
            }

            @Override
            public void onNext(Object o) {
                Log.d("TAG", "onNext " + o );
            }

            @Override
            public void onComplete() {
                Log.d("TAG", "onComplete");
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d("TAG", "onError " + throwable );
            }
        });
    }
}