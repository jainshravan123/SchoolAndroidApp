package infotech.jain.app.school.schoolapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.modules.sub_modules.GalleryCatDescription;
import infotech.jain.app.school.schoolapp.activity.nav_drawer.AboutSchool;
import infotech.jain.app.school.schoolapp.bean.GalleryCategoryImages;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;

/**
 * Created by admin on 14/08/16.
 */
public class GalleryCategoryAdapter extends RecyclerView.Adapter<GalleryCategoryAdapter.ViewGalleryCategoryAdapter>
{

    private ArrayList<GalleryCategoryImages> galCats;
    private Context context;


    public GalleryCategoryAdapter(ArrayList<GalleryCategoryImages> c_galCats, Context c_ctx){
        this.galCats = c_galCats;
        this.context = c_ctx;
    }

    @Override
    public ViewGalleryCategoryAdapter onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_cat_layout_item, parent, false);
        ViewGalleryCategoryAdapter avh = new ViewGalleryCategoryAdapter(v, context, galCats);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewGalleryCategoryAdapter holder, int position)
    {
        Log.e("Gal Cat Image URL : ", Web_API_Config.root_image_url+galCats.get(position).getImage_name());
        Picasso.with(context).load(Web_API_Config.root_image_url+galCats.get(position).getImage_name()).into(holder.v_imageGalleryCat);
        holder.v_imgCaption.setText(galCats.get(position).getCat_name());
    }

    @Override
    public int getItemCount()
    {
        return galCats.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewGalleryCategoryAdapter extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public ImageView v_imageGalleryCat;
        public TextView  v_imgCaption;
        public Context   v_ctx;
        public ArrayList<GalleryCategoryImages> v_galCats = new ArrayList<GalleryCategoryImages>();

        public ViewGalleryCategoryAdapter(View itemView, Context c_ctx, ArrayList<GalleryCategoryImages> c_galCats)
        {
            super(itemView);
            v_imageGalleryCat = (ImageView) itemView.findViewById(R.id.imageGalleryCat);
            v_imgCaption      = (TextView)  itemView.findViewById(R.id.imageCaption);

            this.v_ctx     = c_ctx;
            this.v_galCats = c_galCats;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            String galCatId   = v_galCats.get(position).getId();
            String galCatName = v_galCats.get(position).getCat_name();

            Intent intent1 = new Intent(this.v_ctx,  GalleryCatDescription.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("getCatId", galCatId);
            intent1.putExtra("galCatName", galCatName);
            this.v_ctx.startActivity(intent1);


        }

    }
}


