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
import kotlinx.android.synthetic.main.fragment_sweeper_perfomance_detail.*

class SweeperPerformanceDetailFragment(
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
        return inflater.inflate(R.layout.fragment_sweeper_perfomance_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TypefaceManager(activity)

        constraintLayout.visibility = View.INVISIBLE
        viewModel = (activity as PerformanceDetailActivity).viewModel
        getSweeperPerformanceDetail(employeeId)
    }

    fun getSweeperPerformanceDetail(id: Long){
        Utility.showLoadingDialog(childFragmentManager, "Loading Performance Detail")

        if(viewModel.sweeperPerformanceDetail.value != null) viewModel.sweeperPerformanceDetail.postValue(null)

        viewModel.getSweeperPerformanceDetail(id)
        viewModel.sweeperPerformanceDetail.observe(viewLifecycleOwner, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    response.data!!.let {
                        textName.text = it.data.employeeName
                        textEmployeeNumber.text = it.data.employeeNumber
                        disciplinePercentage.text = "${it.data.dicipline} %"
                        CompletenessPercentage.text = "${it.data.completeness} %"
                        roadPercentage.text = "${it.data.road} %"
                        sidewalksPercentage.text = "${it.data.sidewalk} %"
                        waterRopePercentage.text = "${it.data.waterRope} %"
                        roadMedianPercentage.text = "${it.data.roadMedian} %"
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