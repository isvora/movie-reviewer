package com.isvora.moviereviewer.validation;

import org.springframework.stereotype.Component;

@Component
public abstract class ReviewValidationResponse<T, K> {

    private T t;

    private K k;

    private final boolean isSuccess;

    public ReviewValidationResponse(T t, boolean success) {
        this.t = t;
        this.isSuccess = success;
    }

    public ReviewValidationResponse(K k) {
        this.k = k;
        this.isSuccess = false;
    }

    public T getT() {
        return t;
    }

    public K getK() {
        return k;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
