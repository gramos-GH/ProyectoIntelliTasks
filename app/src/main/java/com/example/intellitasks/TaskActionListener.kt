/*Paquete*/
package com.example.intellitasks

/*Interface*/
interface TaskActionListener {
    fun onTaskDeleted()
    fun onTaskUpdated()
    fun onTaskClicked(task: Task)
}
