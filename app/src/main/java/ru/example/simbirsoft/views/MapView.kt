package ru.example.simbirsoft.views

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import ru.example.simbirsoft.models.User

/**
 * Created by ag on 10.04.18.
 */
interface MapView : MvpView {
    fun returnToPreviousFragment()
    @StateStrategyType(SkipStrategy::class)
    fun showMessage(text: String)
    fun dataLoaded(users: List<User>)
}