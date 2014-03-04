package com.blog.travel;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

public class DataStore {

	public static final String FOLD_ID = "fold_id";
	public static final String FOLD_NAME = "fold_name";
	public static final String ENTRY_ID = "entry_id";
	public static final String ENTRY_NAME= "entry_name";
	public static final String ENTRY_TEXT= "entry_text";
	public static final String FILE_ID= "file_id";
	public static final String FILE_NAME= "file_name";
	
	public static final String DATABASE_NAME= "store";
	public static final String FOLDER_TABLE= "folder";
	public static final String ENTRY_TABLE= "entry";
	public static final String IMAGE_TABLE= "image";
	public static final String VIDEO_TABLE= "video";
	public static final String AUDIO_TABLE= "audio";
	public static final int DATABASE_VERSION= 1;
	
	public DbHelper ourHelper;
	public final Context ourContext;
	public SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{
		Context c;
		public DbHelper(Context context){
			super(context, DATABASE_NAME,null, DATABASE_VERSION);
		    c = context;
		}

		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String sql;
			sql = "CREATE TABLE " + FOLDER_TABLE + " ("+ FOLD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " +
					", " + FOLD_NAME +" TEXT NOT NULL);";
			db.execSQL(sql);
			
			sql = "CREATE TABLE " + ENTRY_TABLE + " ("+ FOLD_ID + " INTEGER REFERENCES " + 
			FOLDER_TABLE + "(\""+ FOLD_ID + "\") ON DELETE CASCADE, "+ ENTRY_ID + " INTEGER, " +  ENTRY_TEXT +" TEXT DEFAULT NULL, " +
					ENTRY_NAME +" TEXT NOT NULL, "  + " PRIMARY KEY (" + FOLD_ID + "," + ENTRY_ID + "));";
	
			db.execSQL(sql);
			sql = "CREATE TABLE " + IMAGE_TABLE + " ("+ FOLD_ID + " INTEGER, "+ ENTRY_ID + " INTEGER, " + FILE_NAME +" TEXT NOT NULL, " 
			+ " FOREIGN KEY("+FOLD_ID+","+ENTRY_ID+") REFERENCES  "+ENTRY_TABLE+"("+FOLD_ID+","+ENTRY_ID+") ON DELETE CASCADE, PRIMARY KEY("+FOLD_ID+","+ENTRY_ID+","+FILE_NAME+"));";
			
		
			db.execSQL(sql);
		
			sql = "CREATE TABLE " + VIDEO_TABLE + " ("+ FOLD_ID + " INTEGER, "+ ENTRY_ID + " INTEGER, " + FILE_NAME +" TEXT NOT NULL, " 
			+ " FOREIGN KEY("+FOLD_ID+","+ENTRY_ID+") REFERENCES "+ENTRY_TABLE+"("+FOLD_ID+","+ENTRY_ID+") ON DELETE CASCADE, PRIMARY KEY("+FOLD_ID+","+ENTRY_ID+","+FILE_NAME+"));";
			db.execSQL(sql);
			
			sql = "CREATE TABLE " + AUDIO_TABLE + " ("+ FOLD_ID + " INTEGER, "+ ENTRY_ID + " INTEGER, " + FILE_NAME +" TEXT NOT NULL, " 
			+ " FOREIGN KEY("+FOLD_ID+","+ENTRY_ID+") REFERENCES "+ENTRY_TABLE+"("+FOLD_ID+","+ENTRY_ID+") ON DELETE CASCADE, PRIMARY KEY("+FOLD_ID+","+ENTRY_ID+","+FILE_NAME+"));";
			
			db.execSQL(sql);
		
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+FOLDER_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "+ENTRY_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "+IMAGE_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "+VIDEO_TABLE);
			db.execSQL("DROP TABLE IF EXISTS "+AUDIO_TABLE);
			onCreate(db);
		} 
	}
	
	public DataStore(Context c){
		ourContext = c;
	}
	
	public DataStore open() throws SQLException  {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		ourHelper.close();  
	}

	public long createFolder(String s) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(FOLD_NAME,s);
		return ourDatabase.insert(FOLDER_TABLE, null, cv);
	}

	public long createEntry(int i,int id, String s) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put(FOLD_ID,i);
		cv.put(ENTRY_ID,id);
		cv.put(ENTRY_NAME,s);
		return ourDatabase.insert(ENTRY_TABLE, null, cv);
		
	} 
	
	public void deleteFolder (int lRow1) throws SQLException { 
		
		
		 ourDatabase.delete(IMAGE_TABLE, FOLD_ID+"="+lRow1, null);
		 ourDatabase.delete(VIDEO_TABLE, FOLD_ID+"="+lRow1, null);
		 ourDatabase.delete(AUDIO_TABLE, FOLD_ID+"="+lRow1, null);
		 ourDatabase.delete(ENTRY_TABLE, FOLD_ID+"="+lRow1, null);
		 ourDatabase.delete(FOLDER_TABLE, FOLD_ID + "=" + lRow1 , null);
	}
	
	public void deleteEntry (int folid,int lRow1) throws SQLException { 	
		 
		 ourDatabase.delete(IMAGE_TABLE, ENTRY_ID+"="+lRow1+" and "+ FOLD_ID+"="+folid, null);
		 ourDatabase.delete(VIDEO_TABLE, ENTRY_ID+"="+lRow1+" and "+ FOLD_ID+"="+folid, null);
		 ourDatabase.delete(AUDIO_TABLE, ENTRY_ID+"="+lRow1+" and "+ FOLD_ID+"="+folid, null);
		 ourDatabase.delete(ENTRY_TABLE, ENTRY_ID+"="+lRow1+" and "+ FOLD_ID+"="+folid, null);
	}
	
	
	
	

	public void insertEntry (int i, int j, String string,
			ArrayList<String> audioList, ArrayList<String> videoList,
			ArrayList<String> imageList, Context c) throws SQLException{
		// TODO Auto-generated method stub
		//Dialog d=new Dialog(c);
		  // TextView tv = new TextView(c);
		if (string!=null)
		{	
		  
		  /*d.setTitle("Text");
		
		   tv.setText(string);
		   d.setContentView(tv);
		   d.show();*/
		   ContentValues cv1 = new ContentValues();
		   cv1.put(ENTRY_TEXT,string);
		   int rows=ourDatabase.update(ENTRY_TABLE, cv1, FOLD_ID+"="+i+" and "+ENTRY_ID+"="+j, null);
		}
	
	
		   ContentValues cv2 = new ContentValues();
		   if(!imageList.isEmpty()){
		   for(int l=0;l<imageList.size();l++){
			   cv2.put(FOLD_ID,i);
			   cv2.put(ENTRY_ID,j);
			   cv2.put(FILE_NAME,imageList.get(l));
			   ourDatabase.insert(IMAGE_TABLE, null,cv2 );
			   /*d.setTitle("Image Entry done"+imageList.get(l));
			   d.show();*/
			   cv2.clear();
		   }
		   }
		   ContentValues cv3 = new ContentValues();
		   if(!videoList.isEmpty()){
			   for(int l=0;l<videoList.size();l++){
				   cv3.put(FOLD_ID,i);
				   cv3.put(ENTRY_ID,j);
				   cv3.put(FILE_NAME,videoList.get(l));
				   ourDatabase.insert(VIDEO_TABLE, null,cv3 );
				 /*  d.setTitle("Video Entry done"+videoList.get(l));
				   d.show();*/
				   cv3.clear();
			   }
		 }
		   ContentValues cv4 = new ContentValues();
		   if(!audioList.isEmpty()){
			   
			   for(int l=0;l<audioList.size();l++){
				   cv4.put(FOLD_ID,i);
				   cv4.put(ENTRY_ID,j);
				   cv4.put(FILE_NAME,audioList.get(l));
				   ourDatabase.insert(AUDIO_TABLE, null,cv4 );
				   /*d.setTitle("Audio Entry done"+audioList.get(l));
				   d.show();*/
				   cv4.clear();
			   }
		   }  
	}
	
	
}
