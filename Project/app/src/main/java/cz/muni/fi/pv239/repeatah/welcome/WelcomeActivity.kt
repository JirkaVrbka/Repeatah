package cz.muni.fi.pv239.repeatah.welcome

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.database.DrillRoomDatabase
import cz.muni.fi.pv239.repeatah.main.MainActivity
import cz.muni.fi.pv239.repeatah.web.ConnectionChecker
import java.lang.Exception

/**
 * Launcher Activity.
 * App's Database updates here. Automatic transition to MainActivity upon update.
 */
class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        try {
            //Asynchronous download of Database
            DownloadTask(this).execute()
        }catch (e : Exception){
            Snackbar.make(ConstraintLayout(this) , R.string.downloadErrorMessage, Snackbar.LENGTH_INDEFINITE).show()
        }
    }


    companion object{

        private const val ASYNC_RESULT = 0

        /**
         * Class for asynchronous download of Database from Internet Database
         */
        class DownloadTask(val context : Context) : AsyncTask<Void, Int, Int>() {

            private val activity = context as Activity

            //Welcome Dialog
            private val dialog = AlertDialog.Builder(context).setView(R.layout.dialog_alert_welcome)
                .create()

            //Shows Dialog and ProgressBar before download
            override fun onPreExecute() {
                val progressBar = activity.findViewById<ProgressBar>(R.id.welcome_progress_bar)
                progressBar.visibility = View.VISIBLE
                dialog.show()
            }

            //Downloads Database
            override fun doInBackground(vararg params: Void?): Int {
                //Checks if connected to the Internet
                val connected = ConnectionChecker.checkConnection(context)
                //Skips download if not connected. Try of download deletes current Database
                if (connected){
                    val database = DrillRoomDatabase.getDatabase(context)
                    database.updateDatabase()
                }
                return ASYNC_RESULT
            }

            //Dismiss Dialog, switch to MainActivity and finish WelcomeActivity after download
            override fun onPostExecute(result: Int) {
                dialog.dismiss()
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                activity.finish()
            }
        }
    }

}
