package com.sr7d.mitplacement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SkillAdapter extends RecyclerView.Adapter<SkillAdapter.SkillHolder> {
    private Context context;
    private List<String> skills;
    private SkillClickListener skillClickListener;

    public SkillAdapter(Context context, List<String> skills, SkillClickListener skillClickListener) {
        this.context = context;
        this.skills = skills;
        this.skillClickListener = skillClickListener;
    }

    @NonNull
    @Override
    public SkillHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_skill_item, viewGroup, false);
        return new SkillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SkillHolder skillHolder, int i) {
        skillHolder.tvSkill.setText(skills.get(i));
        final int position = i;
        skillHolder.imDelSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skillClickListener.OnItemClick(position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return skills.size();
    }

    public class SkillHolder extends RecyclerView.ViewHolder {
        TextView tvSkill;
        ImageView imDelSkill;

        public SkillHolder(@NonNull View itemView) {
            super(itemView);
            tvSkill = itemView.findViewById(R.id.text_view_skill);
            imDelSkill = itemView.findViewById(R.id.image_view_delete_skill);
        }
    }
    public interface SkillClickListener
    {
        void OnItemClick(int position);
    }
}
