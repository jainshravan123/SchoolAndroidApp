package infotech.jain.app.school.schoolapp.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import cn.pedant.SweetAlert.SweetAlertDialog;
import infotech.jain.app.school.schoolapp.R;
import infotech.jain.app.school.schoolapp.activity.modules.LeaveModule;
import infotech.jain.app.school.schoolapp.bean.Leave;
import infotech.jain.app.school.schoolapp.bean.LeaveType;
import infotech.jain.app.school.schoolapp.bean.User;
import infotech.jain.app.school.schoolapp.bean.Web_API_Config;
import infotech.jain.app.school.schoolapp.sqlite_db.DBHelper;

/**
 * Created by admin on 17/08/16.
 */
public class ApplyLeave extends Fragment
{

    EditText edtDetails, edtAttachDoc;
    static EditText edtFromDate, edtTillDate;
    MaterialBetterSpinner materialDesignSpinner;
    AppCompatButton       btn_submit;
    String TAG = "ApplyLeave";
    int PICK_IMAGE_REQUEST = 101;
    private ArrayList<LeaveType> leave_type_list;
    String imagePath, applyLeaveResult;
    Uri image_uri;
    Bitmap bitmap;
    SweetAlertDialog sAlertDialog;

    public ApplyLeave(){}

    public ApplyLeave(ArrayList<LeaveType> c_leave_type_list)
    {
        this.leave_type_list = c_leave_type_list;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.apply_leave_fragment_layout, container, false);

        edtDetails    = (EditText) rootView.findViewById(R.id.input_apply_leave_details);
        edtFromDate   = (EditText) rootView.findViewById(R.id.input_leave_from);
        edtTillDate   = (EditText) rootView.findViewById(R.id.input_leave_till);
        edtAttachDoc  = (EditText) rootView.findViewById(R.id.input_attach_doc_1);
        btn_submit    = (AppCompatButton) rootView.findViewById(R.id.btn_submit);
        imagePath     = "";
        applyLeaveResult = "";
        ArrayList<String> leave_type_list_adp = new ArrayList<String>();

        for(LeaveType leaveType : leave_type_list)
        {
            leave_type_list_adp.add(leaveType.getCaption());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, leave_type_list_adp);
        materialDesignSpinner             = (MaterialBetterSpinner) rootView.findViewById(R.id.spinner_type_of_leave);
        materialDesignSpinner.setAdapter(arrayAdapter);


        edtFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new FromDatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "From Date");
            }
        });

        edtTillDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TillDatePickerFragment();
                newFragment.show(getActivity().getSupportFragmentManager(), "Till Date");
            }
        });

        edtAttachDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(getActivity(), "Selected Item Position : "+materialDesignSpinner.getText(), Toast.LENGTH_LONG).show();
                Leave leave =  collectingLeaveData();
                if (checkApplyLeaveFields(leave))
                {
                    new ApplyingLeave().execute();
                }
                else {

                    String msg = checkApplyLeaveFieldsErrorMsg(leave);
                    new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText(msg)
                            .show();
                }

            }
        });



        return rootView;
    }


    public static class FromDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {

            //txt1.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(monthOfYear)+"/"+String.valueOf(year) );
            edtFromDate.setText(String.valueOf(dayOfMonth) + " - " + String.valueOf(monthOfYear +1)+" - "+String.valueOf(year) );

        }
    }

    public static class TillDatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
        {

            //txt1.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(monthOfYear)+"/"+String.valueOf(year) );
            edtTillDate.setText(String.valueOf(dayOfMonth) + " - " + String.valueOf(monthOfYear+1)+" - "+String.valueOf(year) );
        }
    }

    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    // Get Result Back
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "requestCode = " + requestCode);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {

           image_uri = data.getData();
           getReadPermissison();
        }
    }

    public void getReadPermissison()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if (!Settings.System.canWrite(getActivity())) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            }
        }else
        {
            final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

            if(isKitKat)
            {
                imagePath = getPath2(image_uri);
                Log.e(TAG, "image Path :" + imagePath);
            }else
            {

                imagePath = getRealPathFromURI(getActivity(), image_uri);
                Log.e(TAG, "image Path :" + imagePath);
            }

            try
            {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);


                setImageNameAndUploadImage();


            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public String getRealPathFromURI(Context context, Uri contentUri)
    {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getPath2(Uri selectedImage)
    {

        String filePath = "";
        String wholeID = DocumentsContract.getDocumentId(selectedImage);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getActivity().getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id }, null);



        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();

        return filePath;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 2909: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Log.e("Permission", "Granted");

                    imagePath = getPath2(image_uri);
                    Log.e(TAG, "image Path :" + imagePath);

                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image_uri);
                        // Log.d(TAG, String.valueOf(bitmap));
                        //openUpdatedProfilePivDialog();
                        //updatedImageView.setImageBitmap(bitmap);


                        setImageNameAndUploadImage();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Permission", "Denied");
                }
                return;
            }
        }
    }

    public void setImageNameAndUploadImage()
    {

        File f = new File(imagePath);
        edtAttachDoc.setText(String.valueOf(f.getName()));
     //   Toast.makeText(getActivity(), "Path : "+imagePath, Toast.LENGTH_LONG).show();
    }



    public class ApplyingLeave extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute()
        {
            applyingLeaveDialogVisible();
        }

        @Override
        protected Void doInBackground(Void... params) {


             Leave leave = collectingLeaveData();

            applyLeaveProcess(leave);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {


            try {

                JSONObject jsonObject = new JSONObject(applyLeaveResult);
                String status      = jsonObject.getString("Status");
                String msg         = jsonObject.getString("Msg");

                if(status.equals("1"))
                {

                    applyLeaveSuccessDialog();
                }else{
                    applyLeaveFailureDialog();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            applyingLeaveDialogInVisible();
        }
    }

    private void applyLeaveProcess(Leave p_leave) {
        try
        {
            HttpClient client = new DefaultHttpClient();
            File file = new File(imagePath);
            HttpPost post = new HttpPost(Web_API_Config.apply_leave_API);

            MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
            entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            entityBuilder.addPart("SId", new StringBody(p_leave.getUser().getUser_id()));
            entityBuilder.addPart("Detail", new StringBody(p_leave.getDetails()));
            entityBuilder.addPart("Leave_Type", new StringBody(String.valueOf(p_leave.getLeaveType().getId())));
            entityBuilder.addPart("Leave_From", new StringBody(p_leave.getLeaveFrom()));
            entityBuilder.addPart("Leave_Till", new StringBody(p_leave.getLeaveTill()));
            entityBuilder.addBinaryBody("Attached_Document1", file);

            HttpEntity entity = entityBuilder.build();
            post.setEntity(entity);

            HttpResponse response = client.execute(post);
            HttpEntity httpEntity = response.getEntity();

            //Log.d("result", EntityUtils.toString(httpEntity));
            applyLeaveResult = EntityUtils.toString(httpEntity);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void applyingLeaveDialogVisible()
    {
        sAlertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        sAlertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        sAlertDialog.setTitleText("Applying..");
        sAlertDialog.setCancelable(false);
        sAlertDialog.show();
    }

    public void applyingLeaveDialogInVisible()
    {
       sAlertDialog.dismiss();
    }

    public void applyLeaveSuccessDialog()
    {
        SweetAlertDialog apply_leave_success_dialog;
        apply_leave_success_dialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE);
        apply_leave_success_dialog.setTitleText("Leave applied successfully.");
        apply_leave_success_dialog.setContentText("Check your leave status in leave tab");
        apply_leave_success_dialog.show();
        apply_leave_success_dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent intent1 = new Intent(getActivity(), LeaveModule.class);
                getActivity().startActivity(intent1);
                getActivity().finish();

            }
        });
    }

    public void applyLeaveFailureDialog()
    {
        String errMsg = "Error Occurred";
        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(errMsg)
                .setContentText("Try Again")
                .show();
    }

    public Leave collectingLeaveData()
    {
        User user = new User();

        DBHelper dbHelper1 = new DBHelper(getActivity());
        user.setUser_id(dbHelper1.getPersonalUserId());

        LeaveType leaveType = new LeaveType();

        for(LeaveType leaveType1 : leave_type_list){
            if(leaveType1.getCaption().equals(materialDesignSpinner.getText().toString())){
                leaveType.setId(leaveType1.getId());
            }
        }

        Leave leave = new Leave();
        leave.setUser(user);
        leave.setDetails(edtDetails.getText().toString());
        leave.setLeaveType(leaveType);
        leave.setLeaveFrom(edtFromDate.getText().toString());
        leave.setLeaveTill(edtTillDate.getText().toString());
        leave.setAttached_doc_path(imagePath);
        return leave;
    }

    //Checking Empty Fields Error Message in Sign Up Activity
    public String checkApplyLeaveFieldsErrorMsg(Leave leave){

        String msg = "";

        if(leave.getDetails().equals("") && leave.getLeaveType() == null && leave.getLeaveFrom().equals("") && leave.getLeaveTill().equals("") && leave.getAttached_doc_path().equals("")){
            msg = "Please fill all of the fields";
        }
        else if(leave.getUser() == null)
        {
            msg = "Something Going Wrong";
        }else if(leave.getDetails().equals("") || leave.getDetails() == null){
            msg = "Please fill the details.";
        }
        else if(leave.getLeaveType() == null){
            msg = "Please select Type of Leave";
        }
        else if(leave.getLeaveFrom().equals("") ||leave.getLeaveFrom() == null){
            msg = "Please fill Leave Start Date";
        }
        else if(leave.getLeaveTill().equals("") || leave.getLeaveTill() == null){
            msg = "Please fill Leave End Date";
        }
        else if(leave.getAttached_doc_path().equals("") || leave.getAttached_doc_path() == null){
            msg = "Please Attach atleast one Document";
        }
        return msg;
    }

    //Checking Whether the SignUpUser fields are empty or not
    public boolean checkApplyLeaveFields(Leave leave)
    {
        if(leave.getUser() == null || leave.getDetails().equals("") || leave.getLeaveType() == null || leave.getLeaveFrom().equals("") || leave.getLeaveTill().equals("") || leave.getAttached_doc_path().equals("")){
            return false;
        }
        return true;
    }

}
