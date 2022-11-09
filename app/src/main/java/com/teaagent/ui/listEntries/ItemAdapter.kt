package com.teaagent.ui.listEntries

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.teaagent.R

class ItemAdapter(private val dataSet: ArrayList<String>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() ,View.OnClickListener{
    private var clickListener: ItemClickListener? = null

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) /*, View.OnClickListener */ {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View.
            textView = view.findViewById(R.id.textView)
        }

//        override fun onClick(p0: View?) {
//            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
//        }
    }

    fun setClickListener(itemClickListener: ItemClickListener) {
        this.clickListener = itemClickListener
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_view_design, viewGroup, false)
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView.text = dataSet[position]
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    override fun onClick(v: View?) {
        val position = v!!.tag as Int
        val `object`: Any =dataSet.get(position)
//        val dataModel: DataModel = `object` as DataModel

    }



}
