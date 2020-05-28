package com.funnelmik.spreadly

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class ExpandableListAdapter constructor(
    private var context: Context,
    private var menu: Map<String, List<MenuItem>>,
    private var menuHeaders: List<String>
) : BaseExpandableListAdapter() {

    override fun getGroup(groupPosition: Int): Any {
        // TODO: Change this method to return MenuItem when detail is added to the group view methods
        return menuHeaders[groupPosition]
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(
        groupPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // TODO: This is where we customize the menu section header labels view
        val sectionTitle: String = getGroup(groupPosition) as String
        var view: View? = convertView
        if (view == null) {
            val inflator: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflator.inflate(R.layout.menu_group, null)
        }
        val menuHeader: TextView = view!!.findViewById(R.id.menuHeader) as TextView
        // Customize here
        menuHeader.setTypeface(null, Typeface.BOLD)
        menuHeader.text = sectionTitle
        return menuHeader
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return menu[menuHeaders[groupPosition]]?.size ?: 0
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        // TODO: Change this method to return MEnuItem when detail is added to the child view methods
        return menu[menuHeaders[groupPosition]]?.get(childPosition)?.name ?: "Error Getting Item Name"
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        // TODO: This is where we customize the menu item labels view
        val itemName: String = getChild(groupPosition, childPosition) as String
        var view: View? = convertView
        if (view == null) {
            val inflator: LayoutInflater = this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflator.inflate(R.layout.menu_item, null)
        }
        val menuItem: TextView = view!!.findViewById(R.id.menuItemTitle) as TextView
        val menuImage: ImageView = view.findViewById(R.id.menuItemImage) as ImageView
        // Customize here
        menuImage.setImageResource(R.drawable.default_image)
        menuItem.text = itemName
        return view
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return menuHeaders.size
    }
}