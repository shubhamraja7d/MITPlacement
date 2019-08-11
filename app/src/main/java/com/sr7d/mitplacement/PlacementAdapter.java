package com.sr7d.mitplacement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class PlacementAdapter extends RecyclerView.Adapter<PlacementAdapter.PlacementHolder> {
    private Context context;
    private List<PlacementItem> placementItems;

    public PlacementAdapter(Context context, List<PlacementItem> placementItems) {
        this.context = context;
        this.placementItems = placementItems;
    }

    @NonNull
    @Override
    public PlacementHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_placement_item,viewGroup,false);
        return new PlacementHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacementHolder placementHolder, int i) {

        long plcDate = placementItems.get(i).getPlacementDate();
        long cmpDate = plcDate;
        int day = (int)plcDate % 100;
        plcDate = plcDate / 100;
        int month = (int)plcDate % 100;
        plcDate = plcDate / 100;
        int year = (int)plcDate;
        String finalDate = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

        Date date= new Date();
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Asia"));

        long currDate=0;
        try {
            cal.setTime(date);
            int curreYear = cal.get(Calendar.YEAR);
            int currMonth = cal.get(Calendar.MONTH);
            currMonth++;
            int currDay = cal.get(Calendar.DAY_OF_MONTH);
            currDate = curreYear * 10000 + currMonth * 100 + currDay;
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (currDate < cmpDate) {
            placementHolder.imgStatus.setImageResource(R.drawable.rounded_corner);
        } else {
            placementHolder.imgStatus.setImageResource(R.drawable.rounded_corner_red);
        }
        placementHolder.tvDate.setText(finalDate);
        placementHolder.tvName.setText(placementItems.get(i).getPlacementName());
        Date cd = new Date();
        Timestamp ts = new Timestamp(cd.getTime());

    }

    @Override
    public int getItemCount() {
        return placementItems.size();
    }

    public class PlacementHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvName;
        ImageView imgStatus;
        public PlacementHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.text_view_placement_date);
            tvName = itemView.findViewById(R.id.text_view_placement_name);
            imgStatus = itemView.findViewById(R.id.image_placement_status);
        }
    }
}
