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

public class ChildList extends ListActivity implements OnClickListener{
	

		//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
		ArrayList<String> listItems=new ArrayList<String>();
		ArrayList<String> listItemText=new ArrayList<String>();
		ArrayList<Integer> listItemIds=new ArrayList<Integer>();
		EditText entryname;
		Button Delete;
		String s,listid;
		int getid;
	    DataStore store = new DataStore(this);
		//DEFINING STRING ADAPTER WHICH WILL HANDLE DATA OF LISTVIEW
		ArrayAdapter<String> adapter;
		int iFID;
		int iId;
		int iName;
		int iText;
		int fol,ent;
		int i;
		//RECORDING HOW MUCH TIMES BUTTON WAS CLICKED
		int clickCounter=0;
	    
		@Override
		public void onCreate(Bundle icicle) {

		super.onCreate(icicle);
		setContentView(R.layout.childgrid);
	     entryname= (EditText) findViewById(R.id.edentry);
		 Delete=(Button)findViewById(R.id.cdelBtn);
		 Delete.setOnClickListener(this);
		Bundle getb= getIntent().getExtras();
	    getid=getb.getInt("folderid");
	    store.open();
	    String[] columns = new String[]{DataStore.FOLD_ID,DataStore.ENTRY_ID,DataStore.ENTRY_NAME,DataStore.ENTRY_TEXT};   
		Cursor c = store.ourDatabase.query(DataStore.ENTRY_TABLE, columns,DataStore.FOLD_ID + "=" + getid,null, null,null, null);
		if(c!=null){
			 iFID=c.getColumnIndex(DataStore.FOLD_ID);
			 iId=c.getColumnIndex(DataStore.ENTRY_ID);
			 iName=c.getColumnIndex(DataStore.ENTRY_NAME); 
			 iText = c.getColumnIndex(DataStore.ENTRY_TEXT);
			// entryname.setText(String.valueOf(iText));
			 i=1;
			for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
				listItems.add(i+") "+c.getString(iName));
				fol=c.getInt(iFID);
				ent=c.getInt(iId);
				listItemIds.add(c.getInt(iId));
				listItemText.add(c.getString(iText));
				i++;
			}
		}	
		store.close();
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
		setListAdapter(adapter);
	    
		}

		//METHOD WHICH WILL HANDLE DYNAMIC INSERTION
		public void addItems(View v) {
			boolean diditWork=true;	
			if(entryname.getText().toString()!=""){	
			 s=entryname.getText().toString();
			}
			else
			{
				s="New Entry";
			}
			int id=1;
			if(listItemIds.size()!=0){
				
			 id =listItemIds.get(listItemIds.size()-1)+1;
			}
			try{
			 store.open();
			 store.createEntry(getid,id,s);
			    
			 store.close();
			 listItems.add(i+")"+s);
			 listItemIds.add(id);
			 i++;
			 adapter.notifyDataSetChanged();
			}catch(Exception e){
				
				/*diditWork = false;
				Dialog d=new Dialog(this);
				d.setTitle("Error:(!!");
				d.show();*/
				
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
			int a[]=new int[2];
			a[0]=getid;
			a[1]=listItemIds.get((int)id);
			Bundle b = new Bundle();
	 		b.putIntArray("ids", a);
			Intent i = new Intent(this,Viewmemory.class);
			i.putExtras(b);
			//entryname.setText(String.valueOf(getid)+" "+String.valueOf(a[1])+" "+listItemText.get((int)id));
			startActivity(i);
			
		}

		@Override
		public void onClick(View v) {

			// TODO Auto-generated method stub
			boolean didthisWork = true;
			store.open();
			try {	
				int del_id=Integer.parseInt(entryname.getText().toString());
				--del_id;
				int val = listItemIds.get(del_id);
				//entryname.setText(String.valueOf(val)+" " + String.valueOf(getid));
				
				store.deleteEntry(getid,val);	
				store.close();
				listItems.clear();
				listItemIds.clear();
				 store.open();
				    String[] columns = new String[]{DataStore.FOLD_ID,DataStore.ENTRY_ID,DataStore.ENTRY_NAME};   
					Cursor c = store.ourDatabase.query(DataStore.ENTRY_TABLE, columns,DataStore.FOLD_ID + "=" + getid,null, null,null, null);
					if(c!=null){
						 iFID=c.getColumnIndex(DataStore.FOLD_ID);
						 iId=c.getColumnIndex(DataStore.ENTRY_ID);
						 iName=c.getColumnIndex(DataStore.ENTRY_NAME); 
						 i=1;
						for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
							listItems.add(i+") "+c.getString(iName));
							fol=c.getInt(iFID);
							ent=c.getInt(iId);
							listItemIds.add(c.getInt(iId));
							i++;
						}
					}	
					store.close();
				adapter.notifyDataSetChanged();
				//Intent i = new Intent(this,GridMenu.class);
				//startActivity(i);
			}catch(Exception e){
				
				didthisWork = false;
			}finally{
				
				if(didthisWork){
					/*Dialog d=new Dialog(this);
					d.setTitle("Khatam!!!");
					d.show();*/
				}
			}

			
		}

}
