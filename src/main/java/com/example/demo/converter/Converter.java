package com.example.demo.converter;

public interface Converter<F, T> {

    T convert(F from);
}
