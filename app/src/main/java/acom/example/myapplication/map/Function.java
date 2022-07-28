package acom.example.myapplication.map;

public interface Function<T, R> {
    /**
     * 使T类型转化为R
     */
    R apply(T t);
}
