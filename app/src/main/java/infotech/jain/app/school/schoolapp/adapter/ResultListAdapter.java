package infotech.jain.app.school.schoolapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import java.util.ArrayList;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.bean.Exam;

/**
 * Created by admin on 03/09/16.
 */
public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ViewResultAdapter>
{

    private ArrayList<Exam> exams;
    private Context context;
    private CaldroidFragment dialogCaldroidFragment;
    String                   TAG  = "AttendanceAdapter";
    private Activity activity;



    public ResultListAdapter(ArrayList<Exam> c_exams, Context c_ctx, Activity c_activity){
        this.exams            = c_exams;
        this.context          = c_ctx;
        this.activity         = c_activity;
    }

    @Override
    public ViewResultAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_item_layout, parent, false);
        ViewResultAdapter avh = new ViewResultAdapter(v, context, exams);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewResultAdapter holder, int position)
    {
        holder.txt_main_result_number.setText(String.valueOf(position + 1));
        holder.txt_main_subject_name.setText(exams.get(position).getSubjectName());
        holder.txt_main_exam_subject_grade.setText(exams.get(position).getGrade().toUpperCase());
        holder.txt_main_exam_subject_number.setText(exams.get(position).getTotalMarks() + " / "+ exams.get(position).getOutOfTotal());

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


    class ViewResultAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView txt_main_result_number, txt_main_subject_name, txt_main_exam_subject_grade, txt_main_exam_subject_number;

        public Context   v_ctx;
        public ArrayList<Exam> v_exam_list = new ArrayList<Exam>();

        public ViewResultAdapter(View itemView, Context c_ctx, ArrayList<Exam> c_exams)
        {
            super(itemView);

            txt_main_result_number           = (TextView) itemView.findViewById(R.id.txt_main_result_number);
            txt_main_subject_name            = (TextView) itemView.findViewById(R.id.txt_main_subject_name);
            txt_main_exam_subject_grade      = (TextView) itemView.findViewById(R.id.txt_main_exam_subject_grade);
            txt_main_exam_subject_number     = (TextView) itemView.findViewById(R.id.txt_main_exam_subject_number);
            this.v_ctx                       = c_ctx;
            this.v_exam_list                 = c_exams;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {


        }

    }
}


