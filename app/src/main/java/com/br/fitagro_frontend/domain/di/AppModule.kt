package com.br.fitagro_frontend.domain.di

import com.br.fitagro_frontend.data.remote.ApiServiceFactory
import com.br.fitagro_frontend.data.remote.MainService
import com.br.fitagro_frontend.domain.repository.MainRepository
import com.br.fitagro_frontend.domain.repository.MainRepositoryImpl
import com.br.fitagro_frontend.domain.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideMainRepository(
        service: MainService,
    ): MainRepository {
        return MainRepositoryImpl(service)
    }

    @Provides
    @Singleton
    fun provideTicketService(): MainService {
        return ApiServiceFactory.createService()
    }

    @Provides
    @Singleton
    fun provideTicketViewModel(
        mainRepository: MainRepository,
    ): MainViewModel {
        return MainViewModel(mainRepository)
    }

}