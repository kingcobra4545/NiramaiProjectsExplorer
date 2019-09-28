package com.sandeep.prajwal.niramaiprojectsexplorer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sandeep.prajwal.niramaiprojectsexplorer.PreferencesDB;
import com.sandeep.prajwal.niramaiprojectsexplorer.R;
import com.sandeep.prajwal.niramaiprojectsexplorer.Utils;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<String> mItems;
    private ItemListener mListener;
    private String type;
    private Context context;

    public ItemAdapter(Context context, List<String> items, ItemListener listener, String type) {
        this.context = context;
        mItems = items;
        mListener = listener;
        this.type = type;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(type.equals(Utils.SORT)) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bottom_sheet_item_sorting, parent, false));
        }
        else if(type.equals(Utils.FILTER)){
            return new ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bottom_sheet_item_filter, parent, false), Utils.FILTER);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        holder.setData(mItems.get(position));
        if(type.equals(Utils.SORT)) {
            if (mItems.get(position).equals(PreferencesDB.getString(context, Utils.CURRENT_SORT))) {
                holder.radioButton.setChecked(true);
            } else holder.radioButton.setChecked(false);
        }
    }



    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

        RadioButton radioButton;
        TextView textView;
        CheckBox checkBox;
        String item;
        String type;

        ViewHolder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_button);
            radioButton.setOnClickListener(this);
            textView = itemView.findViewById(R.id.text);
        }
        ViewHolder(View itemView, String type) {
            super(itemView);
            this.type = type;
            checkBox = itemView.findViewById(R.id.check_box);
            checkBox.setOnCheckedChangeListener(this);
            textView = itemView.findViewById(R.id.company_text);
        }

        void setData(String item) {
            this.item = item;
            textView.setText(item);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                radioButton.setChecked(false);
                PreferencesDB.putString(context, Utils.CURRENT_SORT, item);
                notifyDataSetChanged();
                mListener.onItemClick(item);
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            String newFilterText = "";
            if (mListener != null) {
                String oldFilterText = PreferencesDB.getString(context, Utils.CURRENT_FILTER);
                if(!oldFilterText.equals(""))  newFilterText = oldFilterText +"," +  item;
                else newFilterText = item;
                PreferencesDB.putString(context, Utils.CURRENT_SORT,  newFilterText);
//                notifyDataSetChanged();
                mListener.onFilterItemClick(newFilterText);
            }
//

        }
    }

    public interface ItemListener {
        void onItemClick(String item);
        void onFilterItemClick(String item);
    }
}