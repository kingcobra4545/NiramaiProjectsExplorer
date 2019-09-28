package com.sandeep.prajwal.niramaiprojectsexplorer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sandeep.prajwal.niramaiprojectsexplorer.PreferencesDB;
import com.sandeep.prajwal.niramaiprojectsexplorer.R;
import com.sandeep.prajwal.niramaiprojectsexplorer.Utils;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<String> mItems;
    private ItemListener mListener;
    Context context;

    public ItemAdapter(Context context, List<String> items, ItemListener listener) {
        this.context = context;
        mItems = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bottom_sheet_item, parent, false));
    }


    @Override
    public void onBindViewHolder(ItemAdapter.ViewHolder holder, int position) {
        holder.setData(mItems.get(position));
        if(mItems.get(position).equals(PreferencesDB.getString(context, Utils.CURRENT_SORT))) {
            holder.radioButton.setChecked(true);
        }
        else holder.radioButton.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RadioButton radioButton;
        TextView textView;
        String item;

        ViewHolder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_button);
            radioButton.setOnClickListener(this);
            textView = itemView.findViewById(R.id.text);
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
    }

    public interface ItemListener {
        void onItemClick(String item);
    }
}