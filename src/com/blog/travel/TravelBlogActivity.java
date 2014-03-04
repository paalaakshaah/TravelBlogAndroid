package com.blog.travel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class TravelBlogActivity extends Activity implements View.OnClickListener, OnTouchListener {
	
	ImageView pic;
	VideoView vid;
	EditText edtext;
	ImageButton cam;
	ImageButton video;
	ImageButton playvd;
	ImageButton playaud;
	ImageButton gps;
	ImageButton audio;
	ImageButton discard;
	ImageButton save;
	Toast t;
	Intent i;
	int iFID,iId,iName,iText;
	//static File mediaFile[][]= new File[1][10];
	static File mediaFile;
	static int list=0,entry=0;
	public static File mediaStorageDir;
	int LENGTH_LONG=6;
	private Uri fileUri=null;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	MediaRecorder recorder;
	DataStore store;
	static String imagefile=null;
	static String videofile=null;
	final static int CamData=12;
	final static int VideoData=10; 
	static String Audiofile=null;
	int[] getid;
	ArrayList<String> imageList = new ArrayList<String>();
	ArrayList<String> videoList = new ArrayList<String>();
	ArrayList<String> audioList = new ArrayList<String>();
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        cam=(ImageButton) findViewById(R.id.ibPic);
        edtext=(EditText)findViewById(R.id.editText1);
        video=(ImageButton) findViewById(R.id.ibVideo);
        playvd = (ImageButton) findViewById(R.id.ibPlayvid);
        playaud = (ImageButton) findViewById(R.id.ibPlayaud);
        gps=(ImageButton) findViewById(R.id.ibGPS);
        audio=(ImageButton) findViewById (R.id.ibAudio);
        discard=(ImageButton) findViewById (R.id.ibDiscard);
        save=(ImageButton)findViewById(R.id.ibSave);
        save.setOnClickListener(this);
        cam.setOnClickListener(this);
        video.setOnClickListener(this);
        playvd.setOnClickListener(this);
        playaud.setOnClickListener(this);
        gps.setOnClickListener(this);
        discard.setOnClickListener(this);
        pic=(ImageView)findViewById(R.id.imageView1);
        audio.setOnClickListener(this);
        pic.setOnTouchListener(this);
       // imageList.add("images");
        //videoList.add("videos");
        //audioList.add("audios");
      //  vid=(VideoView)findViewById(R.id.videoView1);
        store = new DataStore(this);
        Bundle getb= getIntent().getExtras();
        getid=getb.getIntArray("ids");
        store.open();
	    String[] columns = new String[]{DataStore.FOLD_ID,DataStore.ENTRY_ID,DataStore.ENTRY_NAME,DataStore.ENTRY_TEXT};
	    Cursor c = store.ourDatabase.query(DataStore.ENTRY_TABLE, columns,DataStore.FOLD_ID + "=" + getid[0]+" and "+ DataStore.ENTRY_ID +"="+getid[1] ,null, null,null, null);
		if(c!=null){
			 iFID=c.getColumnIndex(DataStore.FOLD_ID);
			 iId=c.getColumnIndex(DataStore.ENTRY_ID);
			 iName=c.getColumnIndex(DataStore.ENTRY_NAME); 
			 iText = c.getColumnIndex(DataStore.ENTRY_TEXT);
			 c.moveToFirst();
			 String s = c.getString(iText);
			 edtext.setText(s);
		store.close();
		}

    }
    
    
    
    @Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
    	File f;
    	
    	Intent intent;
    	 t = Toast.makeText(TravelBlogActivity.this," ",Toast.LENGTH_LONG);
		switch(v.getId()){
		case R.id.ibPic:
			i= new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
		    i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
		    startActivityForResult(i,CamData);
			break;
		case R.id.ibVideo:
			i= new Intent(android.provider.MediaStore.ACTION_VIDEO_CAPTURE);
			fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO); // create a file to save the image
		    i.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, fileUri);// set the image file name
		    i.putExtra(android.provider.MediaStore.EXTRA_VIDEO_QUALITY, 1);
		    startActivityForResult(i,VideoData);
		    break;
		case R.id.ibPlayvid:
			t.setText("accepts the button");
			t.show();
			if(videofile!=null){
			 intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);  
			intent.setDataAndType(Uri.parse(videofile),"video/*" );
	        startActivity(intent); 
			}
			else{
				
				t.setText("No files to play!!");
				t.show();
			}
	
	        break;
			
		case R.id.ibPlayaud:
			
			
			if(Audiofile!=null){
		    
			intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);  
			intent.setDataAndType(Uri.parse(Audiofile),"audio/*" );
	        startActivity(intent);
			}
			else{
				
			Toast.makeText(TravelBlogActivity.this, "No audio file to play", LENGTH_LONG).show();
			}
	        break;
	        
     
		case R.id.ibAudio:	
			 recorder = new MediaRecorder();
			 recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			 recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			 recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			 String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		 	 Audiofile = "/sdcard/Sounds/Music_"+ timeStamp + ".3gp";
		 	audioList.add(Audiofile);
			 recorder.setOutputFile(Audiofile);
			 try {
				recorder.prepare();
				recorder.start();
				t.setText("Audio capture started");
				t.show(); 
				
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				t.setText("Problem with prepare");
				t.show();
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				t.setText("Problem with io");
				t.show();
				e.printStackTrace();
			}
			 
			
			
			//for(int i=0;i<100000000;i++);
			 break;	
			 
		case R.id.ibGPS:
			//Toast.makeText(this, "Audio saved to:\n", Toast.LENGTH_LONG).show();
			if(recorder!= null)
			{	
				recorder.stop();
			    t.setText("Audio recording stopped");
			    t.show();
				recorder.release();
				
			} 
			else
			{
				t.setText("No recorder");
				recorder.release();
			}
			 
			break;	 
			
		case R.id.ibDiscard:
				
			Bundle b = new Bundle();
	 		b.putIntArray("ids", getid);
			Intent i = new Intent(this,Viewmemory.class);
			i.putExtras(b);
			startActivity(i);
			break;
		case R.id.ibSave:
			boolean diditWork=true;
			String s=edtext.getText().toString();
			//edtext.setText(String.valueOf(getid[0])+" "+String.valueOf(getid[1])+s);
			
			try{
				/*Dialog d = new Dialog(this);
				d.setTitle("Wakeup");
				d.show();*/
				
				
			store.open();	
			store.insertEntry(getid[0],getid[1],s,audioList,videoList,imageList, this);
			store.close();
			edtext.setText("Hi");
			}catch(Exception e){
				 diditWork=false;
				 Dialog d = new Dialog(this);
					d.setTitle("error in saving entry!!");
				    TextView tv = new TextView(this);
				    tv.setText(e.toString());
				    d.setContentView(tv);
					d.show();
			}finally
			{
				if(diditWork){
					
					Dialog d = new Dialog(this);
					d.setTitle("Entry Saved!!");
					d.show();
				}
			}
		}
	}
    
    
    
    
    
 // To process the image on getting it back once clicked from the camera.	
 	@Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 		// TODO Auto-generated method stub
 		super.onActivityResult(requestCode, resultCode, data);
 		
 		if (requestCode == CamData) {
 	        if (resultCode == RESULT_OK) {
 	            // Image captured and saved to fileUri specified in the Intent
 	          /*  Toast.makeText(this, "Image saved to:\n" +
 	                     data.getData(), Toast.LENGTH_LONG).show();*/
 	        	pic.setImageURI(fileUri);
 	            imageList.add(imagefile);
 	 			
 	        } else if (resultCode == RESULT_CANCELED) {
 	            System.out.println("Capture Cancelled");
 	        } else {
 	            System.out.println("Problem in Capture");
 	        }  
 	        
 		} 
 		
 		if (requestCode == VideoData) {
 	        if (resultCode == RESULT_OK) {
 	            // Video captured and saved to fileUri specified in the Intent
 	        	videoList.add(videofile);

 	            
 	        } else if (resultCode == RESULT_CANCELED) {
 	        	
 	        	Toast.makeText(
 	                    TravelBlogActivity.this,
 	                    "Your Video is saved at "+fileUri,Toast.LENGTH_LONG).show();
 	           
 	        } else {
 	            // Video capture failed, advise user
 	       	Toast.makeText(
	                    TravelBlogActivity.this,
	                    "Your Video is saved but can't be displayed",Toast.LENGTH_LONG).show();
 	        }
 	    }


 	}
 	
 	
 	/** Create a file Uri for saving an image or video */
 	private static Uri getOutputMediaFileUri(int type){
 	      return Uri.fromFile(getOutputMediaFile(type));
 	}

 	/** Create a File for saving an image or video */
 	private static File getOutputMediaFile(int type){
 	    // To be safe, you should check that the SDCard is mounted
 	    // using Environment.getExternalStorageState() before doing this.
        
 	     mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
 	    		"TravelBlogApp");
 	    // This location works best if you want the created images to be shared
 	    // between applications and persist after your app has been uninstalled.

 	    // Create the storage directory if it does not exist
 	    if (! mediaStorageDir.exists()){
 	        if (! mediaStorageDir.mkdirs()){
 	            Log.d("TravelBlogApp", "failed to create directory");
 	            return null;
 	        }
 	    }

 	    // Create a media file name
 	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
 	    if (type == MEDIA_TYPE_IMAGE){
 	    	imagefile=mediaStorageDir.getPath() + File.separator +
 	    	        "IMG_"+ timeStamp + ".jpg";
 	        mediaFile = new File(imagefile);
 	        
 	       
 	    } else if(type == MEDIA_TYPE_VIDEO) {
 	    	videofile=mediaStorageDir.getPath() + File.separator +
 	    	        "VID_"+ timeStamp + ".mp4";
 	        mediaFile = new File(videofile);
 	        
 	    } else {
 	        return null;
 	    }

 	    return mediaFile;
 	}



	@Override
	public boolean onTouch(View v, MotionEvent act) {
		// TODO Auto-generated method stub
		if(act.getAction()==MotionEvent.ACTION_DOWN)
		{	
			Toast.makeText(
                    TravelBlogActivity.this,
                    "Event Occurs",Toast.LENGTH_LONG).show();
		 Intent intent = new Intent();
		 intent.setAction(android.content.Intent.ACTION_VIEW);  
		 intent.setDataAndType(fileUri,"image/*" );
         

         startActivity(intent); 
		}
         return true;
	}



	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		if(recorder!=null)
		{	
		    //t.setText("Audio recording stopped");
		  //  t.show();
			recorder.release();
		} 
		
	}



	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(recorder!=null)
		{	
		   // t.setText("Audio recording stopped");
		    //t.show();
			recorder.release();
		} 
	}
	
 	
 	
 	

   

}
