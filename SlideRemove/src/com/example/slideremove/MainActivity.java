package com.example.slideremove;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	private ListView listView;
	private List<String> mDatas = new ArrayList<String>();
	private MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initView();
		
		initData();
		
		
	}

	private void initData() {
		
		for(int i=0;i<10;i++){
			mDatas.add("andy" + i);
		}
		
	}

	private void initView() {
		
		listView = getListView();
		myAdapter = new MyAdapter();
		listView.setAdapter(myAdapter);
		
	}
	
	private class ViewHolder{
		SlideRemoveLib sl_test ;
		TextView content;
		TextView delete;
	}
	
	private class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mDatas.size();
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			 ViewHolder vHolder = null;
			
			if(convertView == null){
				convertView = View.inflate(getApplicationContext(), R.layout.activity_main, null);
				vHolder  = new ViewHolder();
				
				vHolder.sl_test = (SlideRemoveLib) convertView.findViewById(R.id.srl_remove);
				vHolder.content = (TextView) convertView.findViewById(R.id.tv_content);
				vHolder.delete = (TextView) convertView.findViewById(R.id.tv_delete);
				
				convertView.setTag(vHolder);
				
			}else{
				vHolder = (ViewHolder) convertView.getTag();
			}
			
			
			String msg = getItem(position);
			vHolder.content.setText(msg);
			
			final ViewHolder finalViewHolder = vHolder;
			
			vHolder.delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDatas.remove(position);
					finalViewHolder.sl_test.noShowDeleteView();
					myAdapter.notifyDataSetChanged();
				}
			});
			
			
			return convertView;
		}
		
		

		@Override
		public String getItem(int position) {
			return mDatas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		
		
	}

}
