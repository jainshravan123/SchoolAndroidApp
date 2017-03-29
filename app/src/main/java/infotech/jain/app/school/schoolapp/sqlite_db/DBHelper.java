package infotech.jain.app.school.schoolapp.sqlite_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import infotech.jain.app.school.schoolapp.bean.User;

/**
 * Created by admin on 01/07/16.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME              = "SQLiteUserDB";
    private static final int DATABASE_VERSION        =  1;
    private static final String TABLE_NAME           = "user_table";
    private static final String KEY_ID               = "ID";
    private static final String KEY_USER_ID          = "USER_ID";
    private static final String KEY_NAME             = "NAME";
    private static final String KEY_ADMISSION_NO     = "ADDMISSION_NUMBER";
    private static final String KEY_USER_PERSONAL_ID = "USER_PERSONAL_ID";
    private static final String KEY_USER_EMAIL       = "USER_EMAIL";
    private static final String KEY_USER_CLASS_ID    = "USER_CLASS_ID";
    private static final String KEY_USER_SEC_ID      = "USER_SEC_ID";



    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME
                                                   + "("
                                                   + KEY_ID + " INTEGER PRIMARY KEY,"
                                                   + KEY_USER_ID + " TEXT,"
                                                   + KEY_NAME +" TEXT,"
                                                   + KEY_ADMISSION_NO +" TEXT,"
                                                   + KEY_USER_PERSONAL_ID +" TEXT,"
                                                   + KEY_USER_EMAIL + " TEXT,"
                                                   + KEY_USER_CLASS_ID + " TEXT,"
                                                   + KEY_USER_SEC_ID + " TEXT"
                                                   + ")";


        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_QUERY  = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_QUERY);

        // Create tables again
        onCreate(db);
    }

    public void addUserData(User user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, String.valueOf(user.getUser_id()));
        values.put(KEY_NAME, String.valueOf(user.getName()));
        values.put(KEY_ADMISSION_NO, String.valueOf(user.getAdmission_number()));
        if(user.getStudent() != null)
        {
        values.put(KEY_USER_PERSONAL_ID, String.valueOf(user.getStudent().getStu_id()));
        }
        if(user.getTeacher() != null)
        {
            values.put(KEY_USER_PERSONAL_ID, String.valueOf(user.getTeacher().getId()));
        }
        values.put(KEY_USER_EMAIL, String.valueOf(user.getUsername()));
        values.put(KEY_USER_CLASS_ID, String.valueOf(user.getStudent().getClass_id()));
        values.put(KEY_USER_SEC_ID, String.valueOf(user.getStudent().getSec_id()));
        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public String getUserID(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_USER_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         = Integer.parseInt(cursor.getString(0));
        String user_id1 =  cursor.getString(1);

        db.close();
        return user_id1;
    }


    public String getName(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_NAME}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String name =  cursor.getString(1);

        db.close();
        return name;
    }

    public String getUsername(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_EMAIL}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String username =  cursor.getString(1);

        db.close();
        return username;
    }

    public String getClassId()
    {
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_USER_CLASS_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String class_id =  cursor.getString(1);


        db.close();
        return class_id;
    }

    public String getSectionId()
    {
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, KEY_USER_SEC_ID}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String sec_id =  cursor.getString(1);


        db.close();
        return sec_id;
    }



    public String getAdmissionNo(){
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_NAME, KEY_ADMISSION_NO}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String user_id1 =  cursor.getString(1);
        String username     =  cursor.getString(2);
        String adm_no       = cursor.getString(3);

        db.close();
        return adm_no;
    }

    public String getPersonalUserId()
    {
        int id = 1;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { KEY_ID, KEY_USER_ID, KEY_NAME, KEY_ADMISSION_NO, KEY_USER_PERSONAL_ID}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        int id1         =  Integer.parseInt(cursor.getString(0));
        String user_id1 =  cursor.getString(1);
        String username     =  cursor.getString(2);
        String adm_no       = cursor.getString(3);
        String personalUserId = cursor.getString(4);

        db.close();
        return personalUserId;
    }

    public void deleteUserTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();

    }

}
