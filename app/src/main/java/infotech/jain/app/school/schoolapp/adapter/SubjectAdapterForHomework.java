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
import infotech.jain.app.school.schoolapp.activity.modules.sub_modules.HomeWorkListActivity;
import infotech.jain.app.school.schoolapp.bean.Subject;

/**
 * Created by admin on 21/08/16.
 */
public class SubjectAdapterForHomework extends RecyclerView.Adapter<SubjectAdapterForHomework.ViewSubjectAdapter>
{

    private ArrayList<Subject> subjects;
    private Context context;


    public SubjectAdapterForHomework(ArrayList<Subject> c_subjects, Context c_ctx){
        this.subjects  = c_subjects;
        this.context   = c_ctx;
    }

    @Override
    public ViewSubjectAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item_layout, parent, false);
        ViewSubjectAdapter avh = new ViewSubjectAdapter(v, context, subjects);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewSubjectAdapter holder, int position)
    {

        holder.v_txt_subject_name.setText(subjects.get(position).getName());
        holder.v_txt_subject_type.setText(subjects.get(position).getType());


        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        TextDrawable txtDrawable = builder.build(subjects.get(position).getName().substring(0,1), color1);
        holder.v_image_view_subject_first_char.setImageDrawable(txtDrawable);

    }

    @Override
    public int getItemCount()
    {
        return subjects.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewSubjectAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView v_txt_subject_name;
        public TextView  v_txt_subject_type;
        public ImageView v_image_view_subject_first_char;
        public Context  v_ctx;
        public ArrayList<Subject> v_subjects = new ArrayList<Subject>();

        public ViewSubjectAdapter(View itemView, Context c_ctx, ArrayList<Subject> c_subjects) {
            super(itemView);

            v_txt_subject_name              = (TextView) itemView.findViewById(R.id.txt_circular_heading);
            v_txt_subject_type              = (TextView) itemView.findViewById(R.id.txt_circular_status);
            v_image_view_subject_first_char = (ImageView) itemView.findViewById(R.id.image_view_circular_number);
            this.v_ctx                      = c_ctx;
            this.v_subjects                 = c_subjects;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
            Intent intent1 = new Intent(this.v_ctx, HomeWorkListActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("subject_id", String.valueOf(subjects.get(position).getId()));
            this.v_ctx.startActivity(intent1);
        }

    }
}


