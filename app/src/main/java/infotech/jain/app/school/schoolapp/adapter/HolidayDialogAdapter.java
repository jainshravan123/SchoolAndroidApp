package infotech.jain.app.school.schoolapp.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.bean.Holiday;
import infotech.jain.app.school.schoolapp.bean.HolidayDesc;

/**
 * Created by admin on 03/09/16.
 */
public class HolidayDialogAdapter extends RecyclerView.Adapter<HolidayDialogAdapter.ViewHolidayDialogAdapter>
{

    private ArrayList<HolidayDesc> holidayDescs;
    private Context context;
    private CaldroidFragment dialogCaldroidFragment;
    String TAG = "HolidayDialogAdapter";
    FragmentActivity fragmentActivity;



    public HolidayDialogAdapter(ArrayList<HolidayDesc> c_holidayDescs, Context c_ctx, FragmentActivity c_fragmentActivity){
        this.holidayDescs     = c_holidayDescs;
        this.context          = c_ctx;
        this.fragmentActivity = c_fragmentActivity;
    }

    @Override
    public ViewHolidayDialogAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_dialog_item_layout, parent, false);
        ViewHolidayDialogAdapter avh = new ViewHolidayDialogAdapter(v, context, holidayDescs);
        return avh;
    }

    @Override
    public void onBindViewHolder(final ViewHolidayDialogAdapter holder, final int position)
    {
        holder.txt_holiday_date.setText(String.valueOf(holidayDescs.get(position).getNoOfDays()+ ", ("+String.valueOf(holidayDescs.get(position).getDaysOfWeek())+")"));
        holder.txt_holiday_details.setText(String.valueOf(holidayDescs.get(position).getDescription()));

    }

    @Override
    public int getItemCount()
    {
        return holidayDescs.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewHolidayDialogAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView txt_holiday_date, txt_holiday_details;
        public Context   v_ctx;
        public ArrayList<HolidayDesc> v_holidayDescs = new ArrayList<HolidayDesc>();

        public ViewHolidayDialogAdapter(View itemView, Context c_ctx, ArrayList<HolidayDesc> c_holidayDescs) {
            super(itemView);

            txt_holiday_date                 = (TextView)  itemView.findViewById(R.id.txt_holiday_date);
            txt_holiday_details              = (TextView)  itemView.findViewById(R.id.txt_holiday_details);
            this.v_ctx                       = c_ctx;
            this.v_holidayDescs              = c_holidayDescs;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

            int position = getAdapterPosition();

        }

    }
}


