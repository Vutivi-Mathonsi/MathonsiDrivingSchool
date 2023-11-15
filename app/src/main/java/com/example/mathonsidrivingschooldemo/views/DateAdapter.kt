package com.example.mathonsidrivingschooldemo.views

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.example.mathonsidrivingschooldemo.R
import com.example.mathonsidrivingschooldemo.model.DateData
import java.util.Locale

class DateAdapter (val c: Context, val itemList:ArrayList<DateData>): RecyclerView.Adapter<DateAdapter.ItemViewHolder>()
{
    @SuppressLint("CutPasteId")
    inner class ItemViewHolder(private val v: View): RecyclerView.ViewHolder(v){
        var name: TextView
        var mbNum: TextView
        var mMenus: ImageView
        var nitem: LinearLayout

        init {
            name = v.findViewById<TextView>(R.id.mTitle)
            mbNum = v.findViewById<TextView>(R.id.mSubTitle)
            mMenus = v.findViewById(R.id.mMenus)
            mMenus.setOnClickListener { popupMenus(it) }
            nitem = v.findViewById(R.id.item)
            nitem.setOnClickListener { descriptionMenu(it) }
        }
        private fun popupMenus(v: View) {
            val position = itemList[adapterPosition]
            val popupMenus = PopupMenu(c,v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.editText->{
                        val c = Calendar.getInstance()

                        val datePickerDialog = DatePickerDialog(v.context, {DatePicker, year: Int, month: Int, day: Int ->
                            val selectedDate = Calendar.getInstance()
                            selectedDate.set(year,month,day)
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            val formattedDate = dateFormat.format(selectedDate.time)
                            name.text = formattedDate
                        },
                            c.get(Calendar.YEAR),
                            c.get(Calendar.MONTH),
                            c.get(Calendar.DAY_OF_MONTH),
                        )

                        datePickerDialog.show()

                        true
                    }
                    R.id.delete->{
                        /**set delete*/
                        AlertDialog.Builder(c)
                            .setTitle("Delete")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Are you sure delete this Information")
                            .setPositiveButton("Yes"){
                                    dialog,_->
                                itemList.removeAt(adapterPosition)
                                notifyDataSetChanged()
                                Toast.makeText(c,"Deleted this Information", Toast.LENGTH_SHORT).show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else-> true
                }

            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon",Boolean::class.java)
                .invoke(menu,true)
        }


        private fun descriptionMenu(it: View?) {
            val v = LayoutInflater.from(c).inflate(R.layout.activity_item_desc,null)
            var title = v.findViewById<TextView>(R.id.vTitle)
            title.text = "My Text should be here"
            AlertDialog.Builder(c)
                .setView(v)
                .create()
                .show()
        }

    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val newList = itemList[position]
        holder.name.text = newList.dateForm
        holder.mbNum.text = newList.dateString
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v  = inflater.inflate(R.layout.item_list,parent,false)
        return ItemViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  itemList.size
    }
}