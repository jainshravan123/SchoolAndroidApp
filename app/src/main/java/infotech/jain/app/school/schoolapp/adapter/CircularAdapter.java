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
import infotech.jain.app.school.schoolapp.activity.modules.sub_modules.CircularDescription;
import infotech.jain.app.school.schoolapp.bean.Circular;

/**
 * Created by admin on 02/08/16.
 */
public class CircularAdapter extends RecyclerView.Adapter<CircularAdapter.ViewCircularAdapter>
{

    private ArrayList<Circular> circulars;
    private Context context;


    public CircularAdapter(ArrayList<Circular> c_circulars, Context c_ctx){
        this.circulars = c_circulars;
        this.context   = c_ctx;
    }

    @Override
    public ViewCircularAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.circular_list_item, parent, false);
        ViewCircularAdapter avh = new ViewCircularAdapter(v, context, circulars);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewCircularAdapter holder, int position)
    {
       holder.v_txt2.setText(String.valueOf(circulars.get(position).getHeading()));
       holder.v_txt3.setText(String.valueOf(circulars.get(position).getCreatedAt()));
       holder.v_txt4.setText(String.valueOf(circulars.get(position).getUpdatedAt().substring(0, 10)));

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        //TextDrawable txtDrawable = builder.build(String.valueOf(circulars.get(position).getId()), color1);
        TextDrawable txtDrawable = builder.build(String.valueOf(position + 1), color1);
        holder.v_image_view_circular_number.setImageDrawable(txtDrawable);

    }

    @Override
    public int getItemCount()
    {
        return circulars.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewCircularAdapter extends RecyclerView.ViewHolder implements View.OnClickListener
    {


        public ImageView v_image_view_circular_number;
        public TextView  v_txt2, v_txt3, v_txt4;
        public Context  v_ctx;
        public ArrayList<Circular> v_circulars = new ArrayList<Circular>();

        public ViewCircularAdapter(View itemView, Context c_ctx, ArrayList<Circular> c_circulars) {
            super(itemView);

            v_txt2                       = (TextView) itemView.findViewById(R.id.txt_circular_heading);
            v_txt3                       = (TextView) itemView.findViewById(R.id.txt_created_at);
            v_txt4                       = (TextView) itemView.findViewById(R.id.txt_updated_at);
            v_image_view_circular_number = (ImageView) itemView.findViewById(R.id.image_view_circular_number);
            this.v_ctx                   = c_ctx;
            this.v_circulars             = c_circulars;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int    position      = getAdapterPosition();
            int    circular_id   = v_circulars.get(position).getId();
            Intent intent2       = new Intent(this.v_ctx, CircularDescription.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent2.putExtra("circular_id", String.valueOf(circular_id));
            this.v_ctx.startActivity(intent2);


        }

    }
}


