/*Paquetes*/
package com.example.intellitasks

/*Data Class de "Task"*/
data class Task(
    var id: String = "",           // ID generado por Firestore (se asigna manualmente)
    var name: String = "",         // Nombre de la tarea
    var description: String = "",  // Descripción de la tarea
    var date: String = "",         // Fecha o deadline
    var isCompleted: Boolean = false  // Si la tarea está completada o no
)
