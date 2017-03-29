package infotech.jain.app.school.schoolapp.adapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.roomorama.caldroid.CaldroidFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.modules.sub_modules.LeaveDescriptionActivity;
import infotech.jain.app.school.schoolapp.bean.Attendance;
import infotech.jain.app.school.schoolapp.bean.Holiday;
import infotech.jain.app.school.schoolapp.bean.HolidayDesc;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;

/**
 * Created by admin on 03/09/16.
 */
public class HolidayListAdapter extends RecyclerView.Adapter<HolidayListAdapter.ViewHolidayListAdapter>
{

    private ArrayList<Holiday> holidays;
    private Context context;
    private CaldroidFragment dialogCaldroidFragment;
    String TAG = "HolidayListAdapter";
    FragmentActivity fragmentActivity;



    public HolidayListAdapter(ArrayList<Holiday> c_holidays, Context c_ctx, FragmentActivity c_fragmentActivity){
        this.holidays         = c_holidays;
        this.context          = c_ctx;
        this.fragmentActivity = c_fragmentActivity;
    }

    @Override
    public ViewHolidayListAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_list_item_layout, parent, false);
        ViewHolidayListAdapter avh = new ViewHolidayListAdapter(v, context, holidays);
        return avh;
    }

    @Override
    public void onBindViewHolder(final ViewHolidayListAdapter holder, final int position)
    {

        holder.txt_holiday_month.setText(String.valueOf(holidays.get(position).getMonth_name()));
        holder.txt_holiday_year.setText(String.valueOf(holidays.get(position).getYear()));
        holder.txt_no_of_holidays.setText(String.valueOf(holidays.get(position).getNoOfHolidays()));


        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        TextDrawable txtDrawable = builder.build(holidays.get(position).getMonth(), color1);
        holder.image_view_month_number.setImageDrawable(txtDrawable);


      /*  if(Integer.parseInt(holidays.get(position).getNoOfHolidays()) <= 0){
            holder.btn_list_view.setEnabled(false);
            holder.btn_calendar_view.setEnabled(false);
        }
*/
        holder.btn_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Integer.parseInt(holidays.get(position).getNoOfHolidays()) > 0)
                {
                    showHolidaysInDialog(holidays.get(position));
                }


            }
        });

        holder.btn_calendar_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(Integer.parseInt(holidays.get(position).getNoOfHolidays()) > 0)
                {
                    ArrayList<String> holiday_list_string = new ArrayList<String>();

                    for(HolidayDesc holidayDesc : holidays.get(position).getHolidaysDescList())
                    {
                        holiday_list_string.add(holidayDesc.getNoOfDays());
                    }

                    ColorDrawable blue = new ColorDrawable(context.getResources().getColor(R.color.blue_btn_bg_color));

                    dialogCaldroidFragment = CaldroidFragment.newInstance("Holiday", Integer.parseInt(String.valueOf(holidays.get(position).getMonth())), Integer.parseInt(holidays.get(position).getYear()));


                    for(String holiday_date : holiday_list_string)
                    {
                        Log.e(TAG, "Holiday Date : " + holiday_date);
                        Calendar cal = Calendar.getInstance();
                        cal.set(Integer.parseInt(holidays.get(position).getYear()), Integer.parseInt(holidays.get(position).getMonth()) - 1, Integer.parseInt(holiday_date)); //Year, month and day of month
                        Date date = cal.getTime();
                        dialogCaldroidFragment.setBackgroundDrawableForDate(blue, date);
                        dialogCaldroidFragment.setTextColorForDate(R.color.white_color, date);
                    }

                    dialogCaldroidFragment.show(fragmentActivity.getSupportFragmentManager(), "Holidays");

                }else{

                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return holidays.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewHolidayListAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


        public TextView  txt_holiday_month, txt_holiday_year, txt_no_of_holidays;
        private Button   btn_list_view, btn_calendar_view;
        public ImageView image_view_month_number;
        public Context   v_ctx;
        public ArrayList<Holiday> v_holidays = new ArrayList<Holiday>();

        public ViewHolidayListAdapter(View itemView, Context c_ctx, ArrayList<Holiday> c_holidays) {
            super(itemView);

            txt_holiday_month              = (TextView)  itemView.findViewById(R.id.txt_holiday_month);
            txt_holiday_year               = (TextView)  itemView.findViewById(R.id.txt_holiday_year);
            txt_no_of_holidays             = (TextView)  itemView.findViewById(R.id.txt_no_of_holidays);
            btn_list_view                  = (Button)    itemView.findViewById(R.id.btn_list_view);
            btn_calendar_view              = (Button)    itemView.findViewById(R.id.btn_calendar_view);
            image_view_month_number        = (ImageView) itemView.findViewById(R.id.image_view_month_number);
            this.v_ctx                     = c_ctx;
            this.v_holidays                = c_holidays;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {

            int position = getAdapterPosition();




        }

    }

    public void showHolidaysInDialog(Holiday holiday)
    {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(fragmentActivity)
                .title(String.valueOf(holiday.getMonth_name()) + "," + String.valueOf(holiday.getYear()))
                .titleColor(Color.BLACK)
                .customView(R.layout.holiday_dialog_layout, true)
                .negativeText("Cancel")
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);


        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        RecyclerView holidaysDialogRecycleView    = (RecyclerView) view1.findViewById(R.id.holidaysDialogRecycleView);

        holidaysDialogRecycleView.setHasFixedSize(true);
        StaggeredGridLayoutManager  mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        holidaysDialogRecycleView.setLayoutManager(mStaggeredLayoutManager);

        HolidayDialogAdapter holidayDialogAdapter = new HolidayDialogAdapter(holiday.getHolidaysDescList(), context, fragmentActivity);
        holidaysDialogRecycleView.setAdapter(holidayDialogAdapter);


        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });

    }
}


