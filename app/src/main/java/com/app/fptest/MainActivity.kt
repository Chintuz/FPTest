package com.app.fptest

import android.app.ProgressDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.fptest.databinding.ActivityMainBinding
import com.app.fptest.utils.Const
import com.app.fptest.utils.NetworkUtils
import com.app.fptest.utils.Utils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        viewModel.getCountDB()

        binding.list.itemAnimator?.changeDuration = 0
        binding.handler = viewModel

        var adapter = MainAdapter(viewModel)
        binding.adapter = adapter

        viewModel.errorData.observe(this, Observer {
            hideProgressDialog()
            showRetry(it.message)
        })

        viewModel.responseData.observe(this, Observer {
            hideProgressDialog()
            adapter.updateList(it)
        })

        viewModel.showUpdate.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (viewModel.showUpdate.get()) {
                    Toast.makeText(this@MainActivity, "Updating data..", Toast.LENGTH_LONG).show()
                }
                viewModel.showUpdate.set(false)
            }

        })

        viewModel.dbItemCount.observe(this, Observer {
            Utils.log("db count $it")
            if (it == 0) {
                getData()
            } else {
                viewModel.getFromDB()
            }
        })
    }


    private fun getData() {
        if (NetworkUtils().isNetworkConnected(this)) {
            showProgressDialog()
            viewModel.getDataOnline()
        } else {
            showRetry(Const.NETWORK_ERROR_MESSAGE)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }

    val progressDialog by lazy {
        ProgressDialog(this)
    }

    private fun showProgressDialog() {
        progressDialog.setMessage("Fetching data..")
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        if (progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

    private fun showRetry(message: String) {
        Snackbar.make(list, message, Snackbar.LENGTH_LONG)
            .setAction("Retry") { getData() }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .show()
    }


}
