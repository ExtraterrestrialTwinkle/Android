package ru.skillbox.dependency_injection.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import ru.skillbox.dependency_injection.data.ImagesRepository
import ru.skillbox.dependency_injection.data.ImagesRepositoryImpl
import ru.skillbox.dependency_injection.app.App

import android.app.Application

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun providesRepository(impl: ImagesRepositoryImpl): ImagesRepository
}