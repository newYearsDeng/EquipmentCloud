package com.northmeter.equipmentcloud.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.northmeter.equipmentcloud.R;
import com.northmeter.equipmentcloud.bean.ProgectBuildListResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dyd on 2019/2/27.
 * 项目内建筑列表的Adapter
 */
public class ProgectBuildListRVAapter extends RecyclerView.Adapter<ProgectBuildListRVAapter.BuildListViewHolder> {

    public interface OnMyClickListener {
        void onItemClick(View view, int position);
    }

    public OnMyClickListener onClickListener;

    public void setOnMyClickListener(OnMyClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public List<ProgectBuildListResponse.PageList> models;

    public ProgectBuildListRVAapter(List<ProgectBuildListResponse.PageList> models) {
        this.models = models;
    }


    @Override
    public BuildListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_progrect_buildlist, parent, false);
        BuildListViewHolder viewHolder = new BuildListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final BuildListViewHolder holder, final int position) {
        holder.tvBuildTotal.setText((position + 1) + "/" + models.size());
        holder.tvBuildName.setText(models.get(position).getBuildingName());
        holder.tvProgrectUnregister.setText(String.valueOf(models.get(position).getEquipmentUnregistCount()));
        holder.tvProgrectUnregister.setTextColor(models.get(position).getEquipmentUnregistCount()==0?
                Color.parseColor("#333333"):Color.parseColor("#DC4F4F"));

        holder.tvProgrectUnactivation.setText(String.valueOf(models.get(position).getEquipmentUnactivateCount()));
        holder.tvProgrectUnactivation.setTextColor(models.get(position).getEquipmentUnactivateCount()==0?
                Color.parseColor("#333333"):Color.parseColor("#DC4F4F"));

        holder.tvProgrectEquipmentCount1.setText("/" + models.get(position).getEquipmentCount() + "个");
        holder.tvProgrectEquipmentCount2.setText("/" + models.get(position).getEquipmentCount() + "个");

        holder.linearBuildlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(holder.linearBuildlist, holder.getLayoutPosition());
            }
        });
        holder.btnTesting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(holder.btnTesting, holder.getLayoutPosition());
            }
        });
        holder.btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onItemClick(holder.btnCheck, holder.getLayoutPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class BuildListViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.linear_buildlist)
        LinearLayout linearBuildlist;
        @BindView(R.id.tv_build_total)
        TextView tvBuildTotal;
        @BindView(R.id.tv_build_name)
        TextView tvBuildName;
        @BindView(R.id.tv_progrect_unregister)
        TextView tvProgrectUnregister;
        @BindView(R.id.tv_progrect_equipment_count_1)
        TextView tvProgrectEquipmentCount1;
        @BindView(R.id.tv_progrect_unactivation)
        TextView tvProgrectUnactivation;
        @BindView(R.id.tv_progrect_equipment_count_2)
        TextView tvProgrectEquipmentCount2;
        @BindView(R.id.btn_testing)
        Button btnTesting;
        @BindView(R.id.btn_check)
        Button btnCheck;

        public BuildListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
