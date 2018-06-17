package com.example.zane.bakingapp.recipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                .inflate(R.layout.rv_item_step, parent, false);
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
            tvStepShortDes.setText(mStepsArrayList.get(position).getShortDescription());
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnStepClick.onStepClicked(position);
        }
    }

    public interface OnStepClickListener{
        void onStepClicked(int position);
    }
}
