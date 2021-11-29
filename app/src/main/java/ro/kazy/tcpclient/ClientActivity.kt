package ro.kazy.tcpclient

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import ro.kazy.tcpclient.TcpClient.OnMessageReceived
import java.util.*

/**
 * Description
 *
 * @author Catalin Prata
 * Date: 29/11/2021
 */
class ClientActivity : Activity() {

    private var mList: ListView? = null
    private var arrayList: ArrayList<String>? = null
    private var mAdapter: ClientListAdapter? = null
    private var mTcpClient: TcpClient? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main)
        arrayList = ArrayList()
        val editText = findViewById<View>(R.id.editText) as EditText
        val send = findViewById<View>(R.id.send_button) as Button

        //relate the listView from java to the one created in xml
        mList = findViewById<View>(R.id.list) as ListView
        mAdapter = ClientListAdapter(this, arrayList!!)
        mList!!.adapter = mAdapter
        send.setOnClickListener {
            val message = editText.text.toString()

            //add the text in the arrayList
            arrayList!!.add("c: $message")

            //sends the message to the server
            if (mTcpClient != null) {
                SendMessageTask().execute(message)
            }

            //refresh the list
            mAdapter!!.notifyDataSetChanged()
            editText.setText("")
        }
    }

    override fun onPause() {
        super.onPause()
        if (mTcpClient != null) {
            // disconnect
            DisconnectTask().execute()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (mTcpClient != null) {
            // if the client is connected, enable the connect button and disable the disconnect one
            menu.getItem(1).isEnabled = true
            menu.getItem(0).isEnabled = false
        } else {
            // if the client is disconnected, enable the disconnect button and disable the connect one
            menu.getItem(1).isEnabled = false
            menu.getItem(0).isEnabled = true
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.connect -> {
                val username: String = PreferencesManager.instance.userName
                // check if we have the username saved in the preferences, if not, notify the user to write one down
                if (username != null) {
                    // connect to the server
                    ConnectTask().execute("")
                } else {
                    Toast.makeText(
                        this,
                        "Please got to preferences and set a username first!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                true
            }
            R.id.disconnect -> {
                if (mTcpClient == null) {
                    return true
                }
                DisconnectTask().execute()
                true
            }
            R.id.preferences -> {
                startActivity(Intent(this, PreferencesActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Sends a message using a background task to avoid doing long/network operations on the UI thread
     */
    inner class SendMessageTask : AsyncTask<String?, Void?, Void?>() {

        override fun doInBackground(vararg params: String?): Void? {

            // send the message
            mTcpClient!!.sendMessage(params[0])
            return null
        }

        override fun onPostExecute(nothing: Void?) {
            super.onPostExecute(nothing)
            // clear the data set
            arrayList!!.clear()
            // notify the adapter that the data set has changed.
            mAdapter!!.notifyDataSetChanged()
        }
    }

    /**
     * Disconnects using a background task to avoid doing long/network operations on the UI thread
     */
    inner class DisconnectTask : AsyncTask<Void?, Void?, Void?>() {

        override fun doInBackground(vararg voids: Void?): Void? {

            // disconnect
            mTcpClient!!.stopClient()
            mTcpClient = null
            return null
        }

        override fun onPostExecute(nothing: Void?) {
            super.onPostExecute(nothing)
            // clear the data set
            arrayList!!.clear()
            // notify the adapter that the data set has changed.
            mAdapter!!.notifyDataSetChanged()
        }
    }

    inner class ConnectTask : AsyncTask<String?, String?, TcpClient?>() {
        override fun doInBackground(vararg message: String?): TcpClient? {

            //we create a TCPClient object and
            mTcpClient = TcpClient(object : OnMessageReceived {
                //here the messageReceived method is implemented
                override fun messageReceived(message: String?) {
                    //this method calls the onProgressUpdate
                    publishProgress(message)
                }
            })
            mTcpClient!!.run()
            return null
        }

        override fun onProgressUpdate(vararg values: String?) {
            super.onProgressUpdate(*values)

            //in the arrayList we add the messaged received from server
            arrayList!!.add(values[0] ?: "")
            // notify the adapter that the data set has changed. This means that new message received
            // from server was added to the list
            mAdapter!!.notifyDataSetChanged()
        }
    }
}