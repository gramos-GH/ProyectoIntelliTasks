/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.os.Bundle

/*Interface: FragmentCommunicator*/
interface FragmentCommunicator {
    fun showLoader()
    fun hideLoader()
    fun openUpdateTaskFragment(task: Task, userId: String)
}
