/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

/*Clase LoginViewModel*/
class LoginViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    /*Publisher para el resultado del login*/
    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean> = _loginResult

    /*Publisher para mensajes de error*/
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    /*Requisito: "En cada viewModel deberán crear los publishers para el loader"*/
    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean> = _showLoader

    fun loginUser(email: String, password: String) {
        /*Requisito: "crear los publishers para el loader"*/
        _showLoader.value = true /*Mostrar loader antes de la operación de Firebase*/

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                _showLoader.value = false /*Ocultar loader después de la operación de Firebase*/

                if (task.isSuccessful) {
                    Log.d("LoginViewModel", "Usuario logeado exitosamente.")
                    _loginResult.value = true /*Publicar éxito en el login*/
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido al iniciar sesión."
                    Log.w("LoginViewModel", "Fallo el inicio de sesión: $errorMessage", task.exception)
                    _loginResult.value = false /*Publicar fallo en el login*/
                    _error.value = errorMessage /*Publicar mensaje de error*/
                }
            }
    }
}