package com.pueblo.software.base.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pueblo.software.base.activites.JobDetailsActivity;
import com.pueblo.software.base.common.CommonMethods;
import com.pueblo.software.base.common.CommonVariables;
import com.pueblo.software.base.dto.JobDTO;

import java.util.List;

import software.pueblo.com.fucha.R;

public class MyJobAdapter extends RecyclerView.Adapter<MyJobAdapter.ViewHolder> {
    Context context;
    List<JobDTO> jobDTOList;

    public MyJobAdapter(Context context, List<JobDTO> jobDTOList){
        this.context=context;
        this.jobDTOList=jobDTOList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_job_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        JobDTO dto = jobDTOList.get(i);

        viewHolder.jobNameTextView.setText(dto.getJobName());
        viewHolder.jobTypeTextView.setText(dto.getJobType().getJobName());
        viewHolder.jobDescriptionTextView.setText(dto.getJobDescription());
        viewHolder.jobImageView.setImageResource(CommonMethods.getImageByJobType(dto.getJobType().getJobEnum()));
    }

    @Override
    public int getItemCount() {
        return jobDTOList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView jobNameTextView;
        public TextView jobDescriptionTextView;
        public TextView jobTypeTextView;
        public ImageView jobImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            jobNameTextView = itemView.findViewById(R.id.job_details_job_name_tv);
            jobTypeTextView = itemView.findViewById(R.id.job_details_job_type_tv);
            jobDescriptionTextView = itemView.findViewById(R.id.job_details_job_description_tv);
            jobImageView = itemView.findViewById(R.id.job_details_image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            JobDTO dto = jobDTOList.get(getAdapterPosition());
            Intent jobDetailsIntent = new Intent(context,JobDetailsActivity.class);
            jobDetailsIntent.putExtra(CommonVariables.DTO, dto);
            jobDetailsIntent.putExtra(CommonVariables.ASSIGN_JOB,false);
            context.startActivity(jobDetailsIntent);
        }
    }
}
