package com.task.webchallengetask.ui.modules.activity.adapters;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.task.webchallengetask.R;
import com.task.webchallengetask.data.database.tables.ActionParametersModel;
import com.task.webchallengetask.data.database.tables.ProgramTable;
import com.task.webchallengetask.global.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by klim on 24.03.16.
 */
public class ActivityListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected List<ActionParametersModel> mData = new ArrayList<>();
    private OnItemClickListener mListener;
    private static final int HEADER = 0;
    private static final int ACTIVITY = 1;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        RecyclerView.ViewHolder vh;

        if (viewType == HEADER) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_item_header, parent, false);
            vh = new HeaderHolder(v);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_item, parent, false);
            ActivityItemHolder holder = new ActivityItemHolder(v);
            holder.setExternalListener(mListener);
            vh = holder;
        }
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            ((HeaderHolder) holder).tvTitle.setText(TimeUtil.timeToString(mData.get(position).getDate()));
        } else {
            ((ActivityItemHolder) holder).tvTitle.setText(mData.get(position).getName());

            long duration = mData.get(position).getEndTime() - mData.get(position).getStartTime();
            int seconds = (int) (duration / 1000) % 60;
            int minutes = (int) ((duration / (1000 * 60)) % 60);
            int hours = (int) ((duration / (1000 * 60 * 60)) % 24);

            minutes += hours * 60 + minutes;
            if (minutes > 0) {
                ((ActivityItemHolder) holder).tvSubTitle.setText(minutes + " min");
            } else {
                ((ActivityItemHolder) holder).tvSubTitle.setText(seconds + " sec");
            }

            ((ActivityItemHolder) holder).tvStartDate.setText(TimeUtil.timeToString(mData.get(position).getStartTime()));
            ((ActivityItemHolder) holder).tvEndDate.setText(TimeUtil.timeToString(mData.get(position).getEndTime()));
        }
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class ActivityItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnItemClickListener mExternalListener;
        TextView tvTitle;
        TextView tvSubTitle;
        TextView tvStartDate;
        TextView tvEndDate;

        public ActivityItemHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle_AI);
            tvSubTitle = (TextView) itemView.findViewById(R.id.tvSubTitle_AI);
            tvStartDate = (TextView) itemView.findViewById(R.id.tvStartTime_AI);
            tvEndDate = (TextView) itemView.findViewById(R.id.tvEndTime_AI);
        }

        @Override
        public void onClick(View v) {
            if (mExternalListener != null)
                mExternalListener.onClick(mData.get(getAdapterPosition()));
        }

        public void setExternalListener(OnItemClickListener _listener) {
            mExternalListener = _listener;
        }
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public HeaderHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvHeader_AIH);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getName().equals("header") ? HEADER : ACTIVITY;
    }

    public void onItemClickListener(OnItemClickListener _listener) {
        mListener = _listener;
    }

    public interface OnItemClickListener {
        void onClick(ActionParametersModel _model);
    }

    public void addDataByDay(List<ActionParametersModel> _data) {
        if (!_data.isEmpty()) {
            ActionParametersModel header = new ActionParametersModel();
            header.name = "header";
            header.date = _data.get(0).date;
            int curSize = getItemCount();
            mData.add(header);
            mData.addAll(_data);
            notifyItemRangeInserted(curSize, _data.size() + 1);
        }

    }


}
