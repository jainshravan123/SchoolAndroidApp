package infotech.jain.app.school.schoolapp.adapter;

import android.app.Activity;
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
import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.modules.sub_modules.ResultListActivity;
import infotech.jain.app.school.schoolapp.bean.Exam;

/**
 * Created by admin on 02/09/16.
 */
public class ExamsListAdapter extends RecyclerView.Adapter<ExamsListAdapter.ViewExamAdapter>
{

    private ArrayList<Exam>  exams;
    private Context          context;
    private CaldroidFragment dialogCaldroidFragment;
    String                   TAG  = "AttendanceAdapter";
    private Activity         activity;



    public ExamsListAdapter(ArrayList<Exam> c_exams, Context c_ctx, Activity c_activity){
        this.exams            = c_exams;
        this.context          = c_ctx;
        this.activity         = c_activity;
    }

    @Override
    public ViewExamAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_name_item, parent, false);
        ViewExamAdapter avh = new ViewExamAdapter(v, context, exams);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewExamAdapter holder, int position)
    {

        holder.v_txt_exam_name.setText(exams.get(position).getExamTermName());

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        TextDrawable txtDrawable = builder.build(exams.get(position).getExamTermName().substring(0, 1), color1);
        holder.v_image_view_exam_first_char.setImageDrawable(txtDrawable);

    }

    @Override
    public int getItemCount()
    {
        return exams.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewExamAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView  v_txt_exam_name;
        public ImageView v_image_view_exam_first_char;
        public Context   v_ctx;
        public ArrayList<Exam> v_exam_list = new ArrayList<Exam>();

        public ViewExamAdapter(View itemView, Context c_ctx, ArrayList<Exam> c_exams)
        {
            super(itemView);

            v_txt_exam_name               = (TextView) itemView.findViewById(R.id.txt_circular_heading);
            v_image_view_exam_first_char  = (ImageView) itemView.findViewById(R.id.image_view_circular_number);
            this.v_ctx                    = c_ctx;
            this.v_exam_list              = c_exams;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

            int position = getAdapterPosition();
            Intent intent1 = new Intent(this.v_ctx, ResultListActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("exam_term_id", String.valueOf(exams.get(position).getExamTermId()));
            intent1.putExtra("exam_term_name",String.valueOf(exams.get(position).getExamTermName()));
            this.v_ctx.startActivity(intent1);

        }

    }
}


