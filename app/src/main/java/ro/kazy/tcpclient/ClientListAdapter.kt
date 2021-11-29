package ro.kazy.tcpclient

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.*

/**
 * Description
 *
 * @author Catalin Prata
 * Date: 29/11/2021
 */
class ClientListAdapter(context: Context, private val mListItems: ArrayList<String>) :
    BaseAdapter() {
    private val mLayoutInflater: LayoutInflater
    override fun getCount(): Int {
        //getCount() represents how many items are in the list
        return mListItems.size
    }

    //get the data of an item from a specific position
    //i represents the position of the item in the list
    override fun getItem(i: Int): Any {
        return mListItems[i]
    }

    //get the position id of the item from the list
    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, view: View, viewGroup: ViewGroup): View {

        //check to see if the reused view is null or not, if is not null then reuse it
        var view = view
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.list_item, null)
        }

        //get the string item from the position "position" from array list to put it on the TextView
        val stringItem = mListItems[position]
        if (stringItem != null) {
            val itemName = view.findViewById<View>(R.id.list_item_text_view) as TextView
            if (itemName != null) {
                //set the item name on the TextView
                itemName.text = stringItem
            }
        }

        //this method must return the view corresponding to the data at the specified position.
        return view
    }

    init {

        //get the layout inflater
        mLayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }
}