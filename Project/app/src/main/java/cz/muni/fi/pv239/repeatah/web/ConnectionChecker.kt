package cz.muni.fi.pv239.repeatah.web

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

/**
 * Class for checking devices connection
 */
class ConnectionChecker{

    //Companion object providing public static method to other Classes
    companion object {
        // Function for checking connection
        fun checkConnection(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

            return networkCapabilities != null &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
    }
}