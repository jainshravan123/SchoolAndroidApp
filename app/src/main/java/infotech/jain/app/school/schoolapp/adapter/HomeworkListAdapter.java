package infotech.jain.app.school.schoolapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.modules.sub_modules.HomeworkDescActivity;
import infotech.jain.app.school.schoolapp.bean.Homework;

/**
 * Created by admin on 21/08/16.
 */
public class HomeworkListAdapter extends RecyclerView.Adapter<HomeworkListAdapter.ViewHomeworkListAdapter>
{

    private ArrayList<Homework> homework_list;
    private Context context;


    public HomeworkListAdapter(ArrayList<Homework> c_homework_list, Context c_ctx){
        this.homework_list  = c_homework_list;
        this.context        = c_ctx;
    }

    @Override
    public ViewHomeworkListAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.homwwork_item_layout, parent, false);
        ViewHomeworkListAdapter avh = new ViewHomeworkListAdapter(v, context, homework_list);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewHomeworkListAdapter holder, int position)
    {

        holder.v_txt_homework_heading.setText(homework_list.get(position).getCaption());
        holder.v_txt_creation_date.setText(homework_list.get(position).getCreated_at());
        holder.v_txt_homework_type.setText(homework_list.get(position).getType());

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        TextDrawable txtDrawable = builder.build(homework_list.get(position).getCaption().substring(0, 1), color1);
        holder.v_image_view_homework_number.setImageDrawable(txtDrawable);

        holder.v_txt_creation_date.setTextColor(color1);
    }

    @Override
    public int getItemCount()
    {
        return homework_list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewHomeworkListAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView  v_txt_homework_heading;
        public TextView  v_txt_creation_date;
        public TextView  v_txt_homework_type;
        public ImageView v_image_view_homework_number;
        public Context   v_ctx;
        public ArrayList<Homework> v_homework_list = new ArrayList<Homework>();

        public ViewHomeworkListAdapter(View itemView, Context c_ctx, ArrayList<Homework> c_homework_list) {
            super(itemView);

            v_txt_homework_heading            = (TextView) itemView.findViewById(R.id.txt_circular_heading);
            v_txt_creation_date               = (TextView) itemView.findViewById(R.id.txt_circular_status);
            v_txt_homework_type               = (TextView) itemView.findViewById(R.id.txt_homework_type);
            v_image_view_homework_number      = (ImageView) itemView.findViewById(R.id.image_view_circular_number);
            this.v_ctx                        = c_ctx;
            this.v_homework_list              = c_homework_list;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Intent intent1 = new Intent(this.v_ctx, HomeworkDescActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("homework_id", String.valueOf(homework_list.get(position).getId()));
            this.v_ctx.startActivity(intent1);
        }

    }
}


