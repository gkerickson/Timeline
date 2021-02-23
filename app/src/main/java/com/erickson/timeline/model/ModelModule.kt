package com.erickson.timeline.model

import com.erickson.timeline.model.livedata.ActiveViewLiveData
import com.erickson.timeline.smithsonian.RequestHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class ModelModule {
    @Provides
    @ViewModelScoped
    fun provideTimelineViewData(): List<ActiveViewLiveData> {
        return listOf(
            ActiveViewLiveData(RequestHandlerImpl),
            ActiveViewLiveData(RequestHandlerImpl),
            ActiveViewLiveData(RequestHandlerImpl),
            ActiveViewLiveData(RequestHandlerImpl),
        )
    }
}