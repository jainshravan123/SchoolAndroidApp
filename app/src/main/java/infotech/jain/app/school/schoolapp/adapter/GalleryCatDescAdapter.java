package infotech.jain.app.school.schoolapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.bean.GalleryCategoryDesc;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;

/**
 * Created by admin on 14/08/16.
 */
public class GalleryCatDescAdapter extends RecyclerView.Adapter<GalleryCatDescAdapter.ViewGalleryCategoryDescAdapter>
{

    private ArrayList<GalleryCategoryDesc> galCatsDesc;
    private Context context;
    private Activity activity;
    private GalleryCategoryDesc galleryCategoryDesc;
    int v_position;


    public GalleryCatDescAdapter(ArrayList<GalleryCategoryDesc> c_galCatsDesc, Context c_ctx, Activity c_activity){
        this.galCatsDesc = c_galCatsDesc;
        this.context     = c_ctx;
        this.activity    = c_activity;
        galleryCategoryDesc = new GalleryCategoryDesc();
    }

    @Override
    public ViewGalleryCategoryDescAdapter onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_cat_desc_layout_item, parent, false);
        ViewGalleryCategoryDescAdapter avh = new ViewGalleryCategoryDescAdapter(v, context, galCatsDesc);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewGalleryCategoryDescAdapter holder, int position)
    {
        Log.e("Gal Cat Image URL : ", Web_API_Config.root_image_url + galCatsDesc.get(position).getImage_path());
        Picasso.with(context).load(Web_API_Config.root_image_url+galCatsDesc.get(position).getImage_path()).into(holder.v_imageGalleryCat);
        holder.v_imgCaption.setText(galCatsDesc.get(position).getImage_caption());
        galleryCategoryDesc.setImage_path(Web_API_Config.root_image_url + galCatsDesc.get(position).getImage_path());
        galleryCategoryDesc.setImage_caption(galCatsDesc.get(position).getImage_caption());
    }

    @Override
    public int getItemCount()
    {
        return galCatsDesc.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewGalleryCategoryDescAdapter extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public ImageView v_imageGalleryCat;
        public TextView v_imgCaption;
        public Context   v_ctx;
        public ArrayList<GalleryCategoryDesc> v_galCatsDesc = new ArrayList<GalleryCategoryDesc>();

        public ViewGalleryCategoryDescAdapter(View itemView, Context c_ctx, ArrayList<GalleryCategoryDesc> c_galCatsDesc)
        {
            super(itemView);
            v_imageGalleryCat = (ImageView) itemView.findViewById(R.id.imageGalleryCatDesc);
            v_imgCaption      = (TextView)  itemView.findViewById(R.id.imageCaptionDesc);

            this.v_ctx     = c_ctx;
            this.v_galCatsDesc = c_galCatsDesc;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            v_position = getAdapterPosition();
            openGalleryImageDescDialog();
        }
    }
    //Opening Profile Picture Dialog
    public void openGalleryImageDescDialog()
    {

        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity)
                .title(galleryCategoryDesc.getImage_caption())
                .titleColor(Color.BLACK)
                .customView(R.layout.gallery_desc_dialog_layout, true)
                .negativeText("Cancel")
                .positiveColorRes(R.color.black_color)
                .negativeColorRes(R.color.gray_btn_bg_color)
                .canceledOnTouchOutside(false)
                .autoDismiss(false);

        final MaterialDialog materialDialog = builder.build();
        materialDialog.show();

        View view1 = materialDialog.getCustomView();

        ImageView imageView      = (ImageView) view1.findViewById(R.id.profilePicImageView);

        if(galleryCategoryDesc.getImage_path().equals("NA") || galleryCategoryDesc.getImage_path().equals(""))
        {
            imageView.setImageResource(R.drawable.student);
        }
        else
        {
            Picasso.with(context).load(Web_API_Config.root_image_url+galCatsDesc.get(v_position).getImage_path()).into(imageView);

        }


        View negative  = materialDialog.getActionButton(DialogAction.NEGATIVE);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });



    }
}


