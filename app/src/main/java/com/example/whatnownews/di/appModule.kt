package com.example.whatnownews.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.whatnownews.data.repository.AuthRepositoryImpl
import com.example.whatnownews.domain.repository.AuthRepository
import com.example.whatnownews.domain.usecase.auth.CheckEmailVerifiedUseCase
import com.example.whatnownews.domain.usecase.auth.ForgotPasswordUseCase
import com.example.whatnownews.domain.usecase.auth.LoginUseCase
import com.example.whatnownews.domain.usecase.auth.SendEmailVerificationUseCase
import com.example.whatnownews.domain.usecase.auth.SignUpUseCase
import com.example.whatnownews.presentation.auth.CheckAuthStatusUseCase
import com.example.whatnownews.presentation.auth.EmailVerificationViewModel
import com.example.whatnownews.presentation.auth.ForgotPasswordViewModel
import com.example.whatnownews.presentation.auth.LoginViewModel
import com.example.whatnownews.presentation.auth.SignUpViewModel
import com.example.whatnownews.presentation.auth.SplashViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://newsapi.org/v2/"
private const val DATABASE_NAME = "what_now_news_db"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val appModule = module {

    // Network
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
    single {
        OkHttpClient.Builder()
            .addInterceptor(get<HttpLoggingInterceptor>())
            .build()
    }
    single {
        GsonBuilder().create()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    // Retrofit Service

    // Firebase
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }


    // DataStore
    single { androidContext().dataStore }

    // Repositories
    single { AuthRepositoryImpl(get()) } bind AuthRepository::class
    //singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }

    // UseCases
    factoryOf(::SignUpUseCase)
    factoryOf(::SendEmailVerificationUseCase)
    factoryOf(::CheckEmailVerifiedUseCase)
    factory { LoginUseCase(get()) }
    factory { ForgotPasswordUseCase(repository = get()) }
    factory { CheckAuthStatusUseCase(repository = get()) }



    // ViewModels
    viewModelOf(::SignUpViewModel)
    viewModelOf(::EmailVerificationViewModel)
    viewModel { LoginViewModel(get()) }
    viewModel { ForgotPasswordViewModel(forgotPasswordUseCase = get()) }
    viewModel { SplashViewModel(checkAuthStatusUseCase = get()) }

}
