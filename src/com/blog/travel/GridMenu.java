package com.blog.travel;

import java.util.ArrayList;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;



public class GridMenu extends ListActivity implements OnClickListener{


	//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
	ArrayList<String> listItems=new ArrayList<String>();
	ArrayList<String> listItemextra=new ArrayList<String>();
	ArrayList<Integer> listItemIds=new ArrayList<Integer>();
	EditText foldname;
	Button del;
	int i;
	String s;
	//DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
	ArrayAdapter<String> adapter;
	DataStore store= new DataStore(this);
	@Override
	public void onCreate(Bundle icicle) {

	super.onCreate(icicle);
	setContentView(R.layout.grid);
	foldname= (EditText) findViewById(R.id.edfolder);
	del = (Button)findViewById(R.id.delBtn);
	del.setOnClickListener(this);
	//listItems.add("heya");
	store.open();
	String[] columns = new String[]{DataStore.FOLD_ID,DataStore.FOLD_NAME};   
	Cursor c = store.ourDatabase.query(DataStore.FOLDER_TABLE, columns, null,null, null,null, null);
	int iId=c.getColumnIndex(DataStore.FOLD_ID);
	int iName=c.getColumnIndex(DataStore.FOLD_NAME);  
	 i=1;
	for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
		listItems.add(i+") "+c.getString(iName));
		listItemIds.add(c.getInt(iId));
		i++;
	}
	store.close();
	adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
	setListAdapter(adapter);
	
	}

	//METHOD WHICH WILL HANDLE DYNAMIC INSERTION
	public void addItems(View v) {
		
	boolean diditWork=true;	
	if(foldname.getText().toString().compareTo("")!=0){	
	 s=foldname.getText().toString();
	}
	else
	{
		s="New Folder";
	}
	int id=1;
	if(listItemIds.size()!=0){
		
		 id =listItemIds.get(listItemIds.size()-1)+1;
		}
	try{
	 store.open();
	 store.createFolder(s);
	    
	 store.close();
	 listItems.add(i+") "+s);
	 listItemIds.add(id);
	 i++;
	 adapter.notifyDataSetChanged();
	}catch(Exception e){
		
		diditWork = false;
	}finally{
		
		if(diditWork){
			/*Dialog d=new Dialog(this);
			d.setTitle("Hell Yea!!");
			d.show();*/
		}
	}
	

	 
	}

	
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		int val = listItemIds.get((int)id);
		Bundle b = new Bundle();
		b.putInt("folderid",val);
		Intent i = new Intent(this,ChildList.class);
		i.putExtras(b);
		//foldname.setText(String.valueOf(val));
		startActivity(i);
		
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		boolean didthisWork = true;
		try {	
			int del_id=Integer.parseInt(foldname.getText().toString());
			--del_id;
			int val = listItemIds.get(del_id);
			//foldname.setText(String.valueOf(val));
			store.open();
			store.deleteFolder(val);	
			store.close();
			store.open();
			listItems.clear();
			listItemIds.clear();
			String[] columns = new String[]{DataStore.FOLD_ID,DataStore.FOLD_NAME};   
			Cursor c = store.ourDatabase.query(DataStore.FOLDER_TABLE, columns, null,null, null,null, null);
			int iId=c.getColumnIndex(DataStore.FOLD_ID);
			int iName=c.getColumnIndex(DataStore.FOLD_NAME);  
			 i=1;
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				listItems.add(i+") "+c.getString(iName));
				listItemIds.add(c.getInt(iId));
				i++;
			}
			store.close();
			adapter.notifyDataSetChanged();
			//Intent i = new Intent(this,GridMenu.class);
			//startActivity(i);
		}catch(Exception e){
			
			didthisWork = false;
		}finally{
			
			if(didthisWork){
			/*	Dialog d=new Dialog(this);
				d.setTitle("Khatam!!!");
				d.show();*/
			}
		}
	}
	

}


