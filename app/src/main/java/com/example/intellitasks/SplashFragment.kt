/*Paquetes*/
package com.example.intellitasks

/*Imports*/
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.navigation.Navigation

/*Clase SplashFragment*/
class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*Layout del fragmento de splash*/
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    /*Esperar algunos segundos si se desea hacer alguna lógica al mostrar el splash*/
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*Después de unos segundos, navegar al siguiente fragmento:*/
        view.postDelayed({
            /*Se usa Navigation.findNavController() en lugar de findNavController()*/
            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment)
        }, 2000) /*Espera de 2 segundos antes de navegar*/
    }
}
