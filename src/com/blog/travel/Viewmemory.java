package com.blog.travel;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Viewmemory extends Activity implements OnClickListener{
	
	static int[] ids;
	TextView text;
	DataStore store;
	int iFID,iId,iName,iText;
	ArrayList<String> imageList=new ArrayList<String>();
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.trial);
	    text= (TextView)findViewById(R.id.textView1);
        Button edit = (Button)findViewById(R.id.viewEdit);
        edit.setOnClickListener(this);
	    
	    store = new DataStore(this);
	    Bundle getBun;
	    getBun = getIntent().getExtras();
	    ids= getBun.getIntArray("ids");
	    store.open();
	    String[] columns = new String[]{DataStore.FOLD_ID,DataStore.ENTRY_ID,DataStore.ENTRY_NAME,DataStore.ENTRY_TEXT};
	    Cursor c = store.ourDatabase.query(DataStore.ENTRY_TABLE, columns,DataStore.FOLD_ID + "=" + ids[0]+" and "+ DataStore.ENTRY_ID +"="+ids[1] ,null, null,null, null);
		if(c!=null){
			 iFID=c.getColumnIndex(DataStore.FOLD_ID);
			 iId=c.getColumnIndex(DataStore.ENTRY_ID);
			 iName=c.getColumnIndex(DataStore.ENTRY_NAME); 
			 iText = c.getColumnIndex(DataStore.ENTRY_TEXT);
			 c.moveToFirst();
			 String s = c.getString(iText);
			 text.setText(s);
		 
		}
		
		 /*columns[0]=DataStore.FOLD_ID;
		 columns[1]=DataStore.ENTRY_ID;
		 columns[2]=DataStore.FILE_NAME;*/
		String[] columns1 = new String[]{DataStore.FOLD_ID,DataStore.ENTRY_ID,DataStore.FILE_NAME};
		 c=store.ourDatabase.query(DataStore.IMAGE_TABLE,columns1,DataStore.FOLD_ID + "=" + ids[0]+" and "+ DataStore.ENTRY_ID +"="+ids[1] , null, null, null, null);
		 if(c!=null)
		 {
			 iFID=c.getColumnIndex(DataStore.FOLD_ID);
			 iId=c.getColumnIndex(DataStore.ENTRY_ID);
			 iName=c.getColumnIndex(DataStore.FILE_NAME); 
			 
			 for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				 imageList.add(	c.getString(iName));
				// Toast.makeText(Viewmemory.this, c.getString(iName), 6).show();
			}
			 
		 }
		 store.close();
		 Gallery gallery = (Gallery) findViewById(R.id.gallery1);
		  gallery.setAdapter(new ImageAdapter(this,imageList));
		  //Gallery gallery1 = (Gallery) findViewById(R.id.gallery2);
		  gallery.setAdapter(new ImageAdapter(this,imageList));
	}
		
	

private static class ImageAdapter extends BaseAdapter {
    int mGalleryItemBackground;
    private Context mContext;
   
  
     private ArrayList<String>images = new ArrayList<String>(); 
     
     
    public ImageAdapter(Context c, ArrayList<String> image) {
        mContext = c;
        TypedArray attr = mContext.obtainStyledAttributes(R.styleable.Viewmemory);
        mGalleryItemBackground = attr.getResourceId(
                R.styleable.Viewmemory_android_galleryItemBackground, 0);
        attr.recycle();
        images=image;
     }

    public int getCount() {
        //return mImageIds.length;
    	return images.size();
    }

    public Object getItem(int position) {
        return images.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);

        //imageView.setImageResource(mImageIds[position]);
        Uri u = Uri.parse(images.get(position));
        imageView.setImageURI(u);
      
        imageView.setLayoutParams(new Gallery.LayoutParams(120,100));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setBackgroundResource(mGalleryItemBackground);

        return imageView;
    }


	
}



@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
		Bundle b = new Bundle();
 		b.putIntArray("ids", ids);
		Intent i = new Intent(this,TravelBlogActivity.class);
		i.putExtras(b);
		startActivity(i);
		
}

}