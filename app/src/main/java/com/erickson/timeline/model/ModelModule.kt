package com.erickson.timeline.model

import androidx.lifecycle.MutableLiveData
import com.erickson.timeline.model.livedata.ActiveViewLiveData
import com.erickson.timeline.smithsonian.RequestHandler
import com.erickson.timeline.smithsonian.RequestHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
class ModelModule {

    @Provides
    @ViewModelScoped
    fun provideSelectedIdLiveData(): MutableLiveData<String> = MutableLiveData("")

    @Provides
    @ViewModelScoped
    fun provideRequestHandler(): RequestHandler = RequestHandlerImpl

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

    @ChoiceOneActiveViewLiveData
    @Provides
    @ViewModelScoped
    fun providedChoiceOne(): ActiveViewLiveData = ActiveViewLiveData(RequestHandlerImpl)

    @ChoiceTwoActiveViewLiveData
    @Provides
    @ViewModelScoped
    fun providedChoiceTwo(): ActiveViewLiveData = ActiveViewLiveData(RequestHandlerImpl)
}