package com.sr7d.mitplacement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class EducationAdapter extends RecyclerView.Adapter<EducationAdapter.EducationHolder> {
    private Context context;
    private List<EducationItem> educationItems;
    private EducationClickListener educationClickListener;

    public EducationAdapter(Context context, List<EducationItem> educationItems, EducationClickListener educationClickListener) {
        this.context = context;
        this.educationItems = educationItems;
        this.educationClickListener = educationClickListener;
    }

    @NonNull
    @Override
    public EducationHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_education_item, viewGroup, false);
        return new EducationHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EducationHolder educationHolder, int i) {
        final int position = i;
        educationHolder.tvDegree.setText(educationItems.get(i).getDegree());
        educationHolder.tvPercentage.setText(educationItems.get(i).getPercentage());
        educationHolder.tvUniversity.setText(educationItems.get(i).getUniversity());
        educationHolder.tvCollege.setText(educationItems.get(i).getCollege());
        educationHolder.tvCompletionYear.setText(educationItems.get(i).getCompletionYear());
        educationHolder.tvDegreeEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                educationClickListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return educationItems.size();
    }

    public class EducationHolder extends RecyclerView.ViewHolder {
        TextView tvDegree, tvCompletionYear, tvCollege, tvUniversity, tvPercentage;
        TextView tvDegreeEdit;

        public EducationHolder(@NonNull View itemView) {
            super(itemView);
            tvDegree = itemView.findViewById(R.id.et_degree);
            tvCompletionYear = itemView.findViewById(R.id.et_complete_year);
            tvCollege = itemView.findViewById(R.id.et_college);
            tvUniversity = itemView.findViewById(R.id.et_univ);
            tvPercentage = itemView.findViewById(R.id.et_percentage);
            tvDegreeEdit = itemView.findViewById(R.id.tv_degree_show);
        }
    }

    public interface EducationClickListener
    {
        public void OnItemClick(int position);
    }
}
