package in.dailycrunch.quiztion;

/**
 * Created by Adarsh Sodagudi(025281709).
 * Mansi Koul(018761247)
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Adarsh Sodagudi(025281709).
 */

//<summary>
//This class handles the database requests
//</summary>
public class DataBaseHandler extends SQLiteOpenHelper {

    //Database version
    private static final int VERSION = 1;
    //Database name
    private static final String DATABASE = "quiztion";
    //Table name
    private static final String TABLE = "user";
    //Column names
    private static final String KEY_ID = "userid";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";

    public DataBaseHandler(Context context)
    {
        super(context,DATABASE,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String QUERY="CREATE TABLE "+TABLE+"("+KEY_ID+" TEXT,"+KEY_USERNAME+" TEXT,"+KEY_EMAIL+" TEXT)";
        db.execSQL(QUERY);
    }
    //<summary>
    // onUpgrade is called when the table schema is changed i.e When the Version number is less than number provided in the super class constructor.
    // </summary>
    //<param name="SQLiteDatabase"></param>
    //<param name="oldversion"></param>
    //<param name="newversion"></param>
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE);
        onCreate(db);
    }
    //<summary>
    // Inserts signup form data into the table(credentials)
    //</summary>
    //<param name="username"></param>
    //<param name="password"></param>
    //<param name="email"></param>
    //<param name="phone"></param>
    public void onInsert(String userid,String username,String email)
    {
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_ID,userid);
        values.put(KEY_USERNAME,username);
        values.put(KEY_EMAIL,email);

        db.insert(TABLE,null,values);

        db.close();
    }

    /*<summary>to get the user id from the database</summary>*/
    public String getId()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String QUERY="SELECT userid FROM "+TABLE;
        Cursor cursor=db.rawQuery(QUERY,null);
        if(cursor.getCount()==1)
        {
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        cursor.close();
        db.close();
        return "Failed";
    }

    /*<summary>to get the username from the database</summary>*/
    public String getUsername()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String QUERY="SELECT username FROM "+TABLE;
        Cursor cursor=db.rawQuery(QUERY,null);
        if(cursor.getCount()==1)
        {
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        cursor.close();
        db.close();
        return "Failed";
    }

    /*<summary>to get the email id from the database</summary>*/
    public String getEmail()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        String QUERY="SELECT email FROM "+TABLE;
        Cursor cursor=db.rawQuery(QUERY,null);
        if(cursor.getCount()==1)
        {
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        cursor.close();
        db.close();
        return "Failed";
    }



/*<summary>clears the record from the database</summary>*/
    public void logout()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        String QUERY="DELETE FROM "+TABLE;
        db.execSQL(QUERY);
    }

}
