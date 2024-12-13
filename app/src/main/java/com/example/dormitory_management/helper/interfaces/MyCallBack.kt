package com.example.dormitory_management.helper.interfaces

interface MyCallBack<A, B> {
    fun success( param: A)
    fun fail( param: B)
}