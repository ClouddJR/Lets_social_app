package com.lets.app.fragments

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.lets.app.EventsRepository
import com.lets.app.R
import com.lets.app.adapters.RVBigEventAdapter
import com.lets.app.adapters.RVSmallEventAdapter
import com.lets.app.model.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    val TAG = javaClass.simpleName

    private lateinit var disposable: Disposable

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        disposable = EventsRepository().getEventsNearby()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    setRV(it)
                }

    }

    private fun setRV(list: List<Event>) {
        yourEventsRV.adapter = RVSmallEventAdapter(list)
        joinedEventsRV.adapter = RVSmallEventAdapter(list)
        nearbyEventsRV.adapter = RVBigEventAdapter(list)
    }

    override fun onPause() {
        super.onPause()
        if (!disposable.isDisposed) {
          disposable.dispose()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getToolbar(): Toolbar {
        return toolbar
    }

    override fun getToolbarTitle(): String {
        return context?.getString(R.string.title_fragment_home) ?: ""
    }
}