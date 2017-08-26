package com.corellidev.personalfinance.expenses

import android.content.Context
import com.corellidev.personalfinance.R
import com.google.gson.GsonBuilder
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


/**
 * Created by Kamil on 2017-06-28.
 */
const val GOOGLE_TOKEN_HEADER_NAME: String = "Google-Token"

interface  ExpenseService{
    @GET("/all")
    fun getAllExpenses(@Header(GOOGLE_TOKEN_HEADER_NAME) token: String?): Observable<List<ExpenseModel>>;

    @POST("/add")
    fun addExpense(@Header(GOOGLE_TOKEN_HEADER_NAME) token: String?, @Body expenseModel: ExpenseModel): Observable<ExpenseModel>
}

class ExpenseServiceDelegate(context: Context) {

    var loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var httpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor)

    val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient.build())
            .baseUrl(context.getString(R.string.personal_finance_backend_url))
            .build()

    val expensesService: ExpenseService = retrofit.create(ExpenseService::class.java)

    fun getAllExpenses(token: String?): Observable<List<ExpenseModel>> {
        return expensesService.getAllExpenses(token);
    }

    fun addExpense(token: String?, expenseModel: ExpenseModel): Observable<ExpenseModel> {
        return expensesService.addExpense(token, expenseModel)
    }
}