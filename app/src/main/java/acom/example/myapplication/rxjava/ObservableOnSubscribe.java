package acom.example.myapplication.rxjava;

/**
 * 使被观察者与事件发射器建立关系
 * 因为事件与被观察者进行解耦
 * 该接口为了打通与事件源之间的关系
 */
public interface ObservableOnSubscribe<T> {

    //建立订阅关系
    //被观察者可以与事件发射器建立关系
    void subscribe(Emitter<T> emitter);
}
