package com.app.shaditest.api

import com.app.fptest.ResponseData
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface Api {

    @GET("iranjith4/ad-assignment/db")
    fun getData(): Observable<Response<ResponseData>>

}