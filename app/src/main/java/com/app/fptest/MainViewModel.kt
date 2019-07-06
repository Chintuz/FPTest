package com.app.fptest

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.fptest.dao.DataRepository
import com.app.fptest.utils.Const
import com.app.fptest.utils.Utils
import com.app.shaditest.api.Api
import com.app.shaditest.api.ApiHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel(var app: Application) : AndroidViewModel(app) {

    private var api = ApiHelper().init().create(Api::class.java)

    var responseData = MutableLiveData<List<ListModel>>()

    var netWorkResponseData = MutableLiveData<ResponseData>()

    var errorData = MutableLiveData<ErrorData>()

    private var tag: Int = -1

    private var position: Int = -1

    private var list: ArrayList<ListModel> = ArrayList()

    private var dataRepository: DataRepository = DataRepository(app)

    var dbItemCount = MutableLiveData<Int>()

    private var disposable = CompositeDisposable()

    var showUpdate = ObservableBoolean(false)

    fun getDataOnline() {
        Utils.log("fetch from server")
        disposable.add(api.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { response: Response<ResponseData>? ->
                            if (response != null && response.isSuccessful && response.body() != null) {
                                netWorkResponseData.value = response.body()
                                responseData.value = toList(Date(response.headers()["date"]).time, response.body()!!)
                                insertToDB(responseData.value!!)
                            } else {
                                errorData.value = ErrorData(response?.code()!!, Const.ERROR_MESSAGE)
                            }
                        },
                        { t: Throwable? ->
                            Log.e("error", t?.message)
                            errorData.value = ErrorData(400, Const.ERROR_MESSAGE)
                        }
                )
        )
    }

    private fun insertToDB(list: List<ListModel>) {
        Observable.fromCallable {
            Utils.log("clear and inserting to db")
            dataRepository.clear()
            dataRepository.insertList(list)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable ->
                    clear()
                }
                .subscribe()
    }

    private fun clear() {
        Observable.fromCallable {
            Utils.log("clearing db")
            dataRepository.clear()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable ->
                    Utils.log("error while deleting records")
                }
                .subscribe()
    }

    fun getFromDB() {
        getTimeStamp()
        disposable.add(Observable.fromCallable {
            Utils.log("fetch from db")
            dataRepository.getList()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable ->
                    clear()
                    getDataOnline()
                }
                .subscribe {
                    responseData.value = it
                }
        )
    }

    fun getCountDB() {
        disposable.add(Observable.fromCallable {
            dataRepository.getCount()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable ->
                    clear()
                }
                .subscribe {
                    dbItemCount.value = it
                }
        )
    }

    fun getTimeStamp() {
        disposable.add(Observable.fromCallable {
            dataRepository.getFirst().timeStamp
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable ->
                    clear()
                }
                .subscribe {
                    Utils.log("timestamp $it")
                    if (Utils.isDayExpired(it)) {
                        showUpdate.set(true)
                        getDataOnline()
                    }
                }
        )
    }

    fun dispose() {
        disposable.dispose()
    }

    private fun toList(timeStamp: Long, responseData: ResponseData): ArrayList<ListModel> {

        if (responseData.facilities.isNotEmpty()) {
            for (facility in responseData.facilities) {
                var model = ListModel()
                model.name = facility.name
                position++
                model.position = position
                tag++
                model.tag = tag
                model.timeStamp = timeStamp
                list.add(model)

                if (facility.options.isNotEmpty()) {
                    for (option in facility.options) {
                        var model = ListModel()
                        model.name = option.name
                        model.icon = option.icon
                        model.position = position
                        model.facility_id = facility.facility_id
                        model.object_id = option.id
                        tag++
                        model.tag = tag
                        model.timeStamp = timeStamp
                        model.exclusions = similarList(facility.facility_id, option.id)!!
                        list.add(model)
                    }
                }
            }
        }
        return list
    }

    private fun similarList(selectedFacilityId: String, selectedOptionId: String): List<Exclusion>? {

        var ex = ArrayList<Exclusion>()

        for (exclusion in netWorkResponseData.value?.exclusions!!) {
            for (excl in exclusion) {
                if (excl.facility_id == selectedFacilityId && excl.options_id == selectedOptionId) {
                    for (exclus in exclusion!!) {
                        if (exclus.facility_id != selectedFacilityId) {
                            ex.add(exclus)
                        }
                    }
                }
            }
        }
        return ex
    }

    fun updateSelection(isSelected: Boolean, tag: Int) {
        disposable.add(Observable.fromCallable {
            dataRepository.updateSelection(isSelected, tag)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable ->
                    Log.e("error", t.message)
                }
                .subscribe()
        )
    }

    fun resetSelection(isSelected: Boolean) {
        disposable.add(Observable.fromCallable {
            dataRepository.resetSelection(isSelected)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable ->
                    Log.e("error", t.message)
                }
                .subscribe()
        )
    }

    fun resetEnabled(isEnabled: Boolean) {
        disposable.add(Observable.fromCallable {
            dataRepository.resetEnabled(isEnabled)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable ->
                    Log.e("error", t.message)
                }
                .subscribe()
        )
    }

    fun updateEnabled(isEnabled: Boolean, facility_id: String, object_id: String) {
        disposable.add(Observable.fromCallable {
            dataRepository.updateEnabled(isEnabled, facility_id, object_id)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError { t: Throwable ->
                    Log.e("error", t.message)
                }
                .subscribe()
        )
    }

}