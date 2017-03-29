package infotech.jain.app.school.schoolapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.modules.sub_modules.LeaveDescriptionActivity;
import infotech.jain.app.school.schoolapp.bean.LeaveDescription;

/**
 * Created by admin on 19/08/16.
 */
public class PreviousLeavesAdapter extends RecyclerView.Adapter<PreviousLeavesAdapter.ViewPreviousLeavesAdapter>
{

    private ArrayList<LeaveDescription> leaves;
    private Context context;


    public PreviousLeavesAdapter(ArrayList<LeaveDescription> c_leaves, Context c_ctx){
        this.leaves  = c_leaves;
        this.context = c_ctx;
    }

    @Override
    public ViewPreviousLeavesAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.previous_leave_list_item, parent, false);
        ViewPreviousLeavesAdapter avh = new ViewPreviousLeavesAdapter(v, context, leaves);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewPreviousLeavesAdapter holder, int position)
    {
        holder.v_txt_leave_number.setText(String.valueOf(position + 1));
        holder.v_txt_leave_type.setText(leaves.get(position).getLeave().getLeaveType().getCaption());
        holder.v_txt_leave_status.setText(leaves.get(position).getStatus());

        if(leaves.get(position).getStatus().equals("Approved")){
            holder.v_image_view_status.setImageResource(R.drawable.approved);
        }else if(leaves.get(position).getStatus().equals("Pending")){
            holder.v_image_view_status.setImageResource(R.drawable.pending);
        }else if(leaves.get(position).getStatus().equals("Rejected")){
            holder.v_image_view_status.setImageResource(R.drawable.rejected);
        }


    }

    @Override
    public int getItemCount()
    {
        return leaves.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewPreviousLeavesAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView  v_txt_leave_number;
        public TextView  v_txt_leave_type;
        public TextView  v_txt_leave_status;
        public ImageView v_image_view_status;
        public Context  v_ctx;
        public ArrayList<LeaveDescription> v_leaves = new ArrayList<LeaveDescription>();

        public ViewPreviousLeavesAdapter(View itemView, Context c_ctx, ArrayList<LeaveDescription> c_leaves) {
            super(itemView);

            v_txt_leave_number  = (TextView) itemView.findViewById(R.id.image_view_circular_number);
            v_txt_leave_type    = (TextView) itemView.findViewById(R.id.txt_circular_heading);
            v_txt_leave_status  = (TextView) itemView.findViewById(R.id.txt_circular_status);
            v_image_view_status = (ImageView) itemView.findViewById(R.id.status_image_view);
            this.v_ctx     = c_ctx;
            this.v_leaves  = c_leaves;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Intent intent1 = new Intent(this.v_ctx,  LeaveDescriptionActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("leave_application_number", leaves.get(position).getLeave_application_number());
            this.v_ctx.startActivity(intent1);
        }

    }
}


