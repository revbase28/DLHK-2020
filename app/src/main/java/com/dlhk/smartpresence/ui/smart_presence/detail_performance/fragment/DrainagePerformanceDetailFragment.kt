package com.dlhk.smartpresence.ui.smart_presence.detail_performance.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.ui.smart_presence.detail_performance.PerformanceDetailActivity
import com.dlhk.smartpresence.ui.smart_presence.detail_performance.PerformanceDetailViewModel
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.TypefaceManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.fragment_drainage_performance_detail.*


class DrainagePerformanceDetailFragment(
    val employeeId: Long,
    val photoString: String
) : Fragment() {

    lateinit var activity: Activity
    lateinit var viewModel: PerformanceDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drainage_performance_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        constraintLayout.visibility = View.INVISIBLE
        viewModel = (activity as PerformanceDetailActivity).viewModel
        TypefaceManager(activity)

        getDrainagePerformanceDetail(employeeId)
    }

    fun getDrainagePerformanceDetail(id: Long){
        Utility.showLoadingDialog(childFragmentManager, "Loading Performance Detail")

        if(viewModel.drainagePerformanceDetail.value != null) viewModel.drainagePerformanceDetail.postValue(null)

        viewModel.getDrainagePerformanceDetail(id)
        viewModel.drainagePerformanceDetail.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    response.data!!.let {
                        textName.text = it.data.employeeName
                        textEmployeeNumber.text = it.data.employeeNumber
                        disciplinePercentage.text = "${it.data.dicipline} %"
                        CompletenessPercentage.text = "${it.data.completeness} %"
                        cleanlinessPercentage.text = "${it.data.cleanliness} %"
                        sedimentPercentage.text = "${it.data.sediment} %"
                        weedPercentage.text = "${it.data.weed} %"
                        constraintLayout.visibility = View.VISIBLE

                        if(photoString != ""){
                            Glide.with(activity).load(Utility.decodeBase64(photoString)).circleCrop().into(foto)
                        }else{
                            Glide.with(activity).load(activity.getDrawable(R.drawable.ic_person_placeholder)).circleCrop().into(foto)
                        }

                        Utility.dismissLoadingDialog()
                    }
                }

                is Resource.Error -> {
                    Utility.dismissLoadingDialog()
                    Toast.makeText(activity, "Gagal Mendapat Data", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity
    }
}