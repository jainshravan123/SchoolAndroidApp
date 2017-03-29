package infotech.jain.app.school.schoolapp.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.bean.Attendance;

/**
 * Created by admin on 21/08/16.
 */
public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewAttendanceAdapter>
{

    private ArrayList<Attendance> attendances;
    private Context context;
    private CaldroidFragment dialogCaldroidFragment;
    String TAG = "AttendanceAdapter";
    FragmentActivity fragmentActivity;



    public AttendanceAdapter(ArrayList<Attendance> c_attendances, Context c_ctx, FragmentActivity c_fragment_activity){
        this.attendances      = c_attendances;
        this.context          = c_ctx;
        this.fragmentActivity = c_fragment_activity;
    }

    @Override
    public ViewAttendanceAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_item_layout, parent, false);
        ViewAttendanceAdapter avh = new ViewAttendanceAdapter(v, context, attendances);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewAttendanceAdapter holder, int position)
    {

        holder.v_txt_month_name.setText(attendances.get(position).getMonth_name());
        holder.v_txt_working_days.setText(attendances.get(position).getWorking_days());
        holder.v_txt_absents.setText(attendances.get(position).getAbsent());


        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        TextDrawable txtDrawable = builder.build(attendances.get(position).getMonth(), color1);
        holder.v_image_view_month_first_char.setImageDrawable(txtDrawable);

    }

    @Override
    public int getItemCount()
    {
        return attendances.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewAttendanceAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView  v_txt_month_name;
        public TextView  v_txt_working_days;
        public TextView  v_txt_absents;
        public ImageView v_image_view_month_first_char;
        public Context   v_ctx;
        public ArrayList<Attendance> v_attendances = new ArrayList<Attendance>();

        public ViewAttendanceAdapter(View itemView, Context c_ctx, ArrayList<Attendance> c_attendances) {
            super(itemView);

            v_txt_month_name              = (TextView) itemView.findViewById(R.id.txt_circular_heading);
            v_txt_working_days            = (TextView) itemView.findViewById(R.id.txt_working_days);
            v_txt_absents                 = (TextView) itemView.findViewById(R.id.txt_absent);
            v_image_view_month_first_char = (ImageView) itemView.findViewById(R.id.image_view_circular_number);
            this.v_ctx                    = c_ctx;
            this.v_attendances            = c_attendances;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

            int position = getAdapterPosition();
           /* Intent intent1 = new Intent(this.v_ctx, TrialActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("month", attendances.get(position).getMonth());
            this.v_ctx.startActivity(intent1);*/



            if(!(attendances.get(position).getAbsent().equals(String.valueOf("0"))))
            {

                ArrayList<String> absent_dates = new ArrayList<String>();

                absent_dates = attendances.get(position).getAbsented_days_list();

                ColorDrawable red = new ColorDrawable(context.getResources().getColor(R.color.red_btn_bg_color));

                dialogCaldroidFragment = CaldroidFragment.newInstance("Attendance", Integer.parseInt(attendances.get(position).getMonth()), Integer.parseInt(attendances.get(position).getYear()));

                for(String absent_date : absent_dates)
                {
                    Log.e(TAG, "Abset Date : " + absent_date);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(attendances.get(position).getYear()), Integer.parseInt(attendances.get(position).getMonth()) - 1, Integer.parseInt(absent_date)); //Year, month and day of month
                    Date date = cal.getTime();
                    dialogCaldroidFragment.setBackgroundDrawableForDate(red, date);
                    dialogCaldroidFragment.setTextColorForDate(R.color.white_color, date);
                }

                dialogCaldroidFragment.show(fragmentActivity.getSupportFragmentManager(), "Attendance");
            }else{
                dialogCaldroidFragment = CaldroidFragment.newInstance("Attendance", Integer.parseInt(attendances.get(position).getMonth()), Integer.parseInt(attendances.get(position).getYear()));
                dialogCaldroidFragment.show(fragmentActivity.getSupportFragmentManager(), "Attendance");
            }




          /*  Calendar cal = Calendar.getInstance();
            cal.set(Integer.parseInt(attendances.get(position).getYear()),  Integer.parseInt(attendances.get(position).getMonth()) - 1, 10); //Year, month and day of month
            Date date = cal.getTime();


            Calendar cal2 = Calendar.getInstance();
            cal2.set(Integer.parseInt(attendances.get(position).getYear()),  Integer.parseInt(attendances.get(position).getMonth()) - 1, 15); //Year, month and day of month
            Date date2 = cal2.getTime();


            Log.e(TAG, "Date : "+date.toString());

           // ColorDrawable blue = new ColorDrawable(context.getResources().getColor(R.color.red_btn_bg_color));

            dialogCaldroidFragment = CaldroidFragment.newInstance("Attendance", Integer.parseInt(attendances.get(position).getMonth()), Integer.parseInt(attendances.get(position).getYear()));
            dialogCaldroidFragment.setBackgroundDrawableForDate(blue, date);
            dialogCaldroidFragment.setTextColorForDate(R.color.white_color, date);
            dialogCaldroidFragment.setBackgroundDrawableForDate(blue, date2);
            dialogCaldroidFragment.setTextColorForDate(R.color.white_color, date2);
           // dialogCaldroidFragment.setMinDate(date2);
           // dialogCaldroidFragment.setMaxDate(date3);
            dialogCaldroidFragment.show(fragmentActivity.getSupportFragmentManager(), "Attendance");*/

        }

    }
}


