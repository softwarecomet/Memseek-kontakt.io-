package com.example.memseekandroid.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.memseekandroid.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<String> mData;
    private List<String> mData1;
    private List<String> mData2;
    private List<String> mData3;
    private List<String>mData4;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    // data is passed into the constructor

    public RecyclerViewAdapter(Context context, List<String> uuid, List<String> major, List<String> minor,List<String> distance,List<String>rssi){
        this.mInflater = LayoutInflater.from(context);
        this.mData = uuid;
        this.mData1 = major;
        this.mData2 = minor;
        this.mData3 = distance;
        this.mData4 = rssi;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.list_item_device_ibeacon, parent, false);
        return new ViewHolder(view);
    }
    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String beaconuuid = mData.get(position);
        String beaconmajor = mData1.get(position);
        String beaconminor = mData2.get(position);
        String beacondistance = mData3.get(position);
        String beaconrssi=mData4.get(position);
        holder.beaconUuid.setText(beaconuuid);
        holder.beaconMajor.setText(beaconmajor);
        holder.beaconMinor.setText(beaconminor);
        holder.beaconDistance.setText(beacondistance);
        holder.beaconRssi.setText(beaconrssi);
    }
    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }
    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView beaconUuid;
        TextView beaconMajor;
        TextView beaconMinor;
        TextView beaconDistance;
        TextView beaconRssi;


        ViewHolder(View itemView) {
            super(itemView);
//            myTextView = itemView.findViewById(R.id.tvAnimalName);
            beaconUuid= itemView.findViewById(R.id.ibeacon_uuid);
            beaconMajor=itemView.findViewById(R.id.ibeacon_major);
            beaconMinor=itemView.findViewById(R.id.ibeacon_minor);
            beaconDistance=itemView.findViewById(R.id.ibeacon_distance);
            beaconRssi=itemView.findViewById(R.id.ibeacon_rssi);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    public String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }


}
