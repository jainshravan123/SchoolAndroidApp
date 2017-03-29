package infotech.jain.app.school.schoolapp.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.bean.Syllabus;

/**
 * Created by admin on 20/08/16.
 */
public class SyllabusBySubjectAdapter extends RecyclerView.Adapter<SyllabusBySubjectAdapter.ViewSyllabusBySubjectAdapter>
{

    private ArrayList<Syllabus> syllabus;
    private Context         context;
    ProgressDialog          progressDialog;
    String TAG           = "SyllabusBySubjectAdapter";
    String absolute_path = "";
    Activity activity;


    public SyllabusBySubjectAdapter(ArrayList<Syllabus> c_syllabus, Context c_ctx, Activity c_activity){
        this.syllabus  = c_syllabus;
        this.context   = c_ctx;
        this.activity  = c_activity;

    }

    @Override
    public ViewSyllabusBySubjectAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.syllabus_item_layout, parent, false);
        ViewSyllabusBySubjectAdapter avh = new ViewSyllabusBySubjectAdapter(v, context, syllabus);
        return avh;
    }

    @Override
    public void onBindViewHolder(ViewSyllabusBySubjectAdapter holder, int position)
    {

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        TextDrawable txtDrawable = builder.build(syllabus.get(position).getExam_name().substring(0, 1), color1);
        holder.v_image_view_syllabus_first_char.setImageDrawable(txtDrawable);


        holder.v_txt_syllabus_title.setText(syllabus.get(position).getExam_name());
        holder.v_btn_syllabus_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_LONG).show();
                new DownloadFileFromURL().execute("http://1.bp.blogspot.com/-NFIfiFmg8SA/Ti0xrNqB4OI/AAAAAAAADno/fB_2wYQBiv4/s1600/silence-between-two-people.jpg");
            }
        });


    }

    @Override
    public int getItemCount()
    {
        return syllabus.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    class ViewSyllabusBySubjectAdapter extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView           v_image_view_syllabus_first_char;
        public TextView            v_txt_syllabus_title;
        public AppCompatButton     v_btn_syllabus_download;
        public Context             v_ctx;
        public ArrayList<Syllabus> v_syllabus = new ArrayList<Syllabus>();

        public ViewSyllabusBySubjectAdapter(View itemView, Context c_ctx, ArrayList<Syllabus> c_syllabus) {
            super(itemView);

            v_image_view_syllabus_first_char  = (ImageView) itemView.findViewById(R.id.image_view_circular_number);
            v_txt_syllabus_title              = (TextView) itemView.findViewById(R.id.txt_circular_heading);
            v_btn_syllabus_download           = (AppCompatButton) itemView.findViewById(R.id.btn_syllabus_download);
            this.v_ctx                        = c_ctx;
            this.v_syllabus                   = c_syllabus;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = getAdapterPosition();
        }

    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                //URL url = new URL(f_url[0]);
                URL url   = new URL("http://1.bp.blogspot.com/-NFIfiFmg8SA/Ti0xrNqB4OI/AAAAAAAADno/fB_2wYQBiv4/s1600/silence-between-two-people.jpg");
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String url1 = "http://1.bp.blogspot.com/-NFIfiFmg8SA/Ti0xrNqB4OI/AAAAAAAADno/fB_2wYQBiv4/s1600/silence-between-two-people.jpg";
                String fileName = url1.substring(url1.lastIndexOf('/') + 1);

                // File file = new File(Environment.getExternalStorageDirectory(), "downloadedfile.jpg");
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                // Output stream to write file
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();
                absolute_path = file.getAbsolutePath();
                Log.e(TAG, "Absolute Path : " + file.getAbsolutePath());
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }


        @Override
        protected void onPostExecute(String file_url)
        {

            dismissProgressDialog();
            documentDialogSuccess();

        }

    }



    public void showProgressDialog()
    {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Downloading file. Please wait...");
        progressDialog.setIndeterminate(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    public void dismissProgressDialog()
    {
        progressDialog.dismiss();
    }

    public void documentDialogSuccess()
    {
        SweetAlertDialog apply_leave_success_dialog;
        apply_leave_success_dialog = new SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE);
        apply_leave_success_dialog.setTitleText("Document Downloaded Successfully.");
        apply_leave_success_dialog.setContentText("Doc Path : "+absolute_path);
        apply_leave_success_dialog.show();

    }

}


