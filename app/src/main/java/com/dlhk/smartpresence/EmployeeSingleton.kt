package com.dlhk.smartpresence

import com.dlhk.smartpresence.api.response.data.DataEmployee

class EmployeeSingleton {

    companion object{
        @Volatile
        private var instance: EmployeeSingleton? = null
        private val LOCK = Any()
        var employeeDataList : ArrayList<DataEmployee> = ArrayList()

        operator fun invoke() = instance ?: synchronized(LOCK){
            instance?: EmployeeSingleton().also {
                instance = it
            }
        }

        fun insertEmployeeData(employeeList: List<DataEmployee>){
            if (employeeDataList.size == 0){
                employeeDataList.addAll(employeeList)
            }else{
                employeeDataList.clear()
                employeeDataList.addAll(employeeList)
            }
        }

        fun getEmployeeData(): ArrayList<DataEmployee>{
            return employeeDataList
        }

    }
}