package com.example.zane.bakingapp.recipe;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.example.zane.bakingapp.R;
import com.example.zane.bakingapp.objects.Step;

import java.util.ArrayList;

/**
 * Created by Zane on 04/06/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepViewHolder>{

    private static final String LOG_TAG = StepsAdapter.class.getSimpleName();

    ArrayList<Step> mStepsArrayList;
    Context mContext;
    OnStepClickListener mOnStepClick;

    public StepsAdapter(ArrayList<Step> stepsArrayList, Context context, OnStepClickListener onStepClickListener) {
        this.mStepsArrayList = stepsArrayList;
        this.mContext = context;
        this.mOnStepClick = onStepClickListener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_rv_items, parent, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mStepsArrayList.size();
    }

    class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvStepShortDes;

        public StepViewHolder(View itemView) {
            super(itemView);
            tvStepShortDes = itemView.findViewById(R.id.tv_step_short_description);
            itemView.setOnClickListener(this);
        }

        private void bind(int position){
            tvStepShortDes.setText(mStepsArrayList.get(position).getDescription());
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnStepClick.onStepClicked(position);
        }
    }

    interface OnStepClickListener{
        void onStepClicked(int position);
    }
}
