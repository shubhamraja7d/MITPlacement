package com.sr7d.mitplacement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class SkillAdapterShow extends RecyclerView.Adapter<SkillAdapterShow.SkillHolder> {
    Context context;
    List<String> skills;

    public SkillAdapterShow(Context context, List<String> skills) {
        this.context = context;
        this.skills = skills;
    }

    @NonNull
    @Override
    public SkillHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_skill_show, viewGroup, false);
        return new SkillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillHolder skillHolder, int i) {
        skillHolder.tvSkill.setText(skills.get(i));
    }

    @Override
    public int getItemCount() {
        return skills.size();
    }

    public class SkillHolder extends RecyclerView.ViewHolder {
        TextView tvSkill;

        public SkillHolder(@NonNull View itemView) {
            super(itemView);
            tvSkill = itemView.findViewById(R.id.text_view_skill);
        }
    }
}
