package com.lets.app.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.lets.app.viewmodels.*
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
class ViewModelModule {

    @Provides
    fun providesViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @IntoMap
    @ViewModelKey(EventsViewModel::class)
    fun provideEventsViewModel(viewModel: EventsViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelKey(AddEventFragmentViewModel::class)
    fun provideAddEventFragmentViewModel(viewModel: AddEventFragmentViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelKey(EventFragmentViewModel::class)
    fun provideEventFragmentViewModel(viewModel: EventFragmentViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelKey(FiltersFragmentViewModel::class)
    fun provideFiltersFragmentViewModel(viewModel: FiltersFragmentViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    fun provideLoginViewModel(viewModel: LoginViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    fun provideMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelKey(MessagesViewModel::class)
    fun provideMessagesViewModel(viewModel: MessagesViewModel): ViewModel = viewModel

    @Provides
    @IntoMap
    @ViewModelKey(MessagesFragmentViewModel::class)
    fun provideMessagesFragmentViewModel(viewModel: MessagesFragmentViewModel): ViewModel = viewModel

}