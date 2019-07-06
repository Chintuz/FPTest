package com.app.fptest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.fptest.databinding.HeaderItemBinding
import com.app.fptest.databinding.ListItemBinding


class MainAdapter(var viewModel: MainViewModel) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        setHasStableIds(true)
    }

    private var list: List<ListModel> = ArrayList()

    fun updateList(list: List<ListModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    private var inflater: LayoutInflater? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.context)
        }

        if (viewType == 0) {
            return HeaderViewHolder(HeaderItemBinding.inflate(inflater!!, parent, false))
        }
        return ItemViewHolder(ListItemBinding.inflate(inflater!!, parent, false))
    }

    override fun getItemViewType(position: Int): Int {
        if (list[position].icon.isEmpty())
            return 0
        return 1
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 0) {
            (holder as HeaderViewHolder).mBinding.model = list[position]
        } else if (getItemViewType(position) == 1) {
            (holder as ItemViewHolder).mBinding.model = list[position]

            holder.mBinding.radio.tag = list[position].tag

            holder.mBinding.layer.setOnClickListener {
                if (!list[position].isEnabled) return@setOnClickListener

                val pos = holder.mBinding.radio.tag as Int
                for (model in list) {
                    if (list[position].position == model.position) {
                        model.isSelected = pos == model.tag
                        viewModel.updateSelection(model.isSelected, model.tag)
                    }
                }

                for (model in list) {
                    if (list[position].position == model.position) {
                        for (ex in model.exclusions!!) {
                            for (mod in list) {
                                if (mod.facility_id == ex.facility_id && mod.object_id == ex.options_id) {
                                    mod.isEnabled = true
                                    viewModel.updateEnabled(mod.isEnabled, mod.facility_id, mod.object_id)
                                }
                            }
                        }
                    }
                }

                for (ex in list[position].exclusions!!) {
                    for (model in list) {
                        if (model.facility_id == ex.facility_id && model.object_id == ex.options_id) {
                            model.isEnabled = false
                            viewModel.updateEnabled(model.isEnabled, model.facility_id, model.object_id)
                        }
                    }
                }
                try {
                    notifyDataSetChanged()
                } catch (e: Exception) {
                }
            }

            holder.mBinding.radio.isChecked = list[position].isSelected
            holder.mBinding.radio.isEnabled = list[position].isEnabled
        }
    }

    override fun getItemId(position: Int): Long {
        return list[position].tag.toLong()

    }

    class ItemViewHolder(binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var mBinding = binding

    }

    class HeaderViewHolder(binding: HeaderItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var mBinding = binding
    }
}