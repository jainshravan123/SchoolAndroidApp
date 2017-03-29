package infotech.jain.app.school.schoolapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.modules.CircularsModule;
import infotech.jain.app.school.schoolapp.bean.Module;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.network.ImageLoader;
import infotech.jain.app.school.schoolapp.utility.ModuleSelector;
import infotech.jain.app.school.schoolapp.activity.modules.*;

/**
 * Created by admin on 02/07/16.
 */
public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewModuleAdapter>
{

    private ArrayList<Module> modules;
    private Context context;


    public ModuleAdapter(ArrayList<Module> c_modules, Context c_ctx){
        this.modules = c_modules;
        this.context = c_ctx;
    }

    @Override
    public ViewModuleAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.module_item_layout, parent, false);
        ViewModuleAdapter avh = new ViewModuleAdapter(v, context, modules);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewModuleAdapter holder, int position) {
            // holder.v_txtView.setText(modules.get(position).getName());
             //holder.v_imageView.setImageResource(R.drawable.transport);
       /*      String uri = "@drawable/transport";
             String uri = "@drawable/"+modules.get(position).getImage().toString().toLowerCase();
             int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
             Log.e("Image Name : ", uri);
             Drawable res = context.getResources().getDrawable(imageResource);
             holder.v_imageView.setImageDrawable(res);


         */

        Log.e("Module Image Path : ", modules.get(position).getImage());
       // Picasso.with(context).load(Web_API_Config.root_image_url + modules.get(position).getImage()).error(R.drawable.image_loading_error).into(holder.v_imageView);

        //Picasso.with(context).load("http://"+modules.get(position).getImage()).into(holder.v_imageView);
        //holder.v_imageView.setImageResource(modules.get(position).getImageId());
        Picasso.with(context).load(modules.get(position).getImageId()).into(holder.v_imageView);
    }

    @Override
    public int getItemCount() {
        return modules.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewModuleAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{


      //  public TextView v_txtView;
        public ImageView v_imageView;
        public Context  v_ctx;
        public ArrayList<Module> v_modules = new ArrayList<Module>();

        public ViewModuleAdapter(View itemView, Context c_ctx, ArrayList<Module> c_modules) {
            super(itemView);
           // v_txtView = (TextView) itemView.findViewById(R.id.moduleTxtView);
            v_imageView = (ImageView) itemView.findViewById(R.id.imageView);


            this.v_ctx     = c_ctx;
            this.v_modules = c_modules;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();

            int    module_id   = v_modules.get(position).getId();
            String module_name = v_modules.get(position).getName();


            ModuleSelector moduleSelector = new ModuleSelector();
            String pack_name          =  "infotech.jain.app.school.schoolapp.activity.modules.";
            String main_module_name   =  moduleSelector.getModuleNameByModuleId(module_id);

            String cmplt_module_name      = pack_name.concat(main_module_name);


            Intent intent = null;
            try {
                intent = new Intent(this.v_ctx, Class.forName(cmplt_module_name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.v_ctx.startActivity(intent);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }



        }

    }
}


