package com.station.activity;



import java.util.ArrayList;
import java.util.List;

import android.view.View.OnClickListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;

public class EditErrorList extends Activity {
    public final static int MENU_ADD = Menu.FIRST;
    public final static int MENU_DELETE = Menu.FIRST+1;
	
    Activity activity;
    
	List<String> group;
    List<List<String>> child;
    
    ExpandableListView expandList;
    InfoDetailsAdapter mAdapter;
    
    Dialog dialogAdd;
    Dialog dialogDelete;
    
    View viewAdd;
    View viewDelete;
    
    OnClickListener clickListener;
    
    EditText add_name;
    EditText add_phone;
    EditText add_sex;
    EditText add_home;
    Button add_ok,add_no;
    
    Button add_new_error;
    
    EditText delete_id;
    Button delete_ok,delete_no;
    
	
    
    
    
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_error_list);
        activity = this;
        final String session_id = getIntent().getStringExtra("session_id");
        
        
        createDialogAdd();
        createDialogDelete();
        
        expandList = (ExpandableListView)findViewById(R.id.expandList);
        
        add_new_error=(Button)findViewById(R.id.add_new_error);
        
        initialData();
        
        mAdapter = new InfoDetailsAdapter(this);
        
        expandList.setAdapter(mAdapter);
        
        add_new_error.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialogAdd.show();
			}
		});
        
        
        
        
        
        
        
        
        
        
        
        expandList.setOnGroupClickListener(new OnGroupClickListener(){

			public boolean onGroupClick(ExpandableListView arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
//				Toast.makeText(activity,"[Group Click]:"+arg2,Toast.LENGTH_LONG).show();
				return false;
			}
        	
        });
        
        //expandablelist设置监听器
        expandList.setOnChildClickListener(new OnChildClickListener(){

			public boolean onChildClick(ExpandableListView arg0, View arg1,
					int arg2, int arg3, long arg4) {
				// TODO Auto-generated method stub
				//可以显示具体的坐标，arg2是group，arg3是child
				if((arg2==7)&&(arg3==0))
				{
//					Toast.makeText(activity,"[Child Click]:"+arg2+arg3,Toast.LENGTH_LONG).show();

				}
				else
				{
					Intent intent=new Intent();
			    	intent.setClass(EditErrorList.this, AddError.class);
					intent.putExtra("session_id", session_id);
					intent.putExtra("errortype", child.get(arg2).get(arg3));
			    	startActivity(intent);
				}
				return false;
			}
        	
        });
        
        
    }
    
    
    
    
    
    
    
    
    public void createDialogAdd(){
    	viewAdd = this.getLayoutInflater().inflate(R.layout.add, null);
        
        dialogAdd = new Dialog(this);
        dialogAdd.setContentView(viewAdd);
        dialogAdd.setTitle("输入新故障");
        
        add_name = (EditText)viewAdd.findViewById(R.id.add_name);
//        add_phone = (EditText)viewAdd.findViewById(R.id.add_phone);
//        add_sex = (EditText)viewAdd.findViewById(R.id.add_sex);
//        add_home = (EditText)viewAdd.findViewById(R.id.add_home);
        
        add_ok = (Button)viewAdd.findViewById(R.id.add_ok);
        add_no = (Button)viewAdd.findViewById(R.id.add_no);
    
        
        
        
        
        
        
        
        add_ok.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String[] data = {
//						add_phone.getText().toString(),
//						add_sex.getText().toString(),
//						add_home.getText().toString()
//						尊重原创，同时当初出现了一个空指针异常，就是因为
//						上述三个没有赋值，可以打印出他们的值
//						或者断点调试，只不过eclipse的断点调试感觉有点
//						太细致了，对于我来说比较烦
						add_name.getText().toString()
				};
				
//				addInfo(add_name.getText().toString(),data);
				updateInfo(data);
				dialogAdd.dismiss();
				
				mAdapter.notifyDataSetChanged();
			}
        });
        
        
        
        
        
        
        
        add_no.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogAdd.dismiss();
			}
        });
    }
    
    
    
    
    
    
    
    
    public void createDialogDelete(){
    	viewDelete = this.getLayoutInflater().inflate(R.layout.delete, null);
        
        dialogDelete = new Dialog(this);
        dialogDelete.setContentView(viewDelete);
        dialogDelete.setTitle("删除指定成员");
        
        delete_id = (EditText)viewDelete.findViewById(R.id.delete_id);
        delete_ok = (Button)viewDelete.findViewById(R.id.delete_ok);
        delete_no = (Button)viewDelete.findViewById(R.id.delete_no);
        
        delete_ok.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String id = delete_id.getText().toString();
				
				if(! id.equals("")){
					int i = Integer.parseInt(id);
					group.remove(i);
					child.remove(i);
					
					dialogDelete.dismiss();
					
					mAdapter.notifyDataSetChanged();
				}
					
					
			}
        });
        
        delete_no.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogDelete.dismiss();
			}
        });
    }
    
    
    
    
    
    
    
    
    
    public void initialData(){
    	group = new ArrayList<String>();
    	
    	child = new ArrayList<List<String>>();
    	
    	addInfo("   角钢塔部分", new String[]{"天线支架检查","垂直度测量","塔体构件检查","塔基塔脚检查","塔体防腐、防锈处理","走线架与爬梯检查","航标灯检查","房顶塔、拉线塔屋面防漏修补","环境检查"});
    	addInfo("   单管塔部分",new String[]{"油漆及防锈处理","塔体构件检查","天线支架检查","走线架与爬梯检查","单管塔垂直度","塔内照明及航标灯检查","环境检查"});
    	addInfo("   三管塔部分",new String[]{"塔体防腐、防锈处理","塔体构件检查","法兰贴合率的检查","天线支架检查","走线架与爬梯检查","三管塔垂直度","航标灯检查","环境检查"});
    	addInfo("   拉线塔部分",new String[]{"拉线塔拉线调整及拉线部件检查","天线支架检查","垂直度测量","塔体构件检查","塔基塔脚检查","塔体防腐、防锈处理","走线架与爬梯检查","航标灯检查","房顶塔、拉线塔屋面防漏修补","环境检查"});
    	addInfo("   抱杆部分",new String[]{"杆基、支杆检查","抱杆构件检查","抱杆支架防腐、防锈处理","天线支架检查","走线架与爬梯检查","环境检查"});
    	addInfo("   馈线部分",new String[]{"跳线及馈管接头是否完好？有无进水及处理","馈线外形检查处理","扎带检查处理","小跳线的检查处理"});
    	addInfo("   接地部分",new String[]{"馈线接地","地网、接地引入线、接地线和接地汇集线（接地牌）的检查处理","避雷针","避雷引下线","避雷器的检查处理","接地电阻（山区小于10欧姆，平原小于1欧姆）"});
    	addInfo("   其他故障",new String[]{"您所增加的新故障如下："});
    }
    
    
    
    
    
    public void addInfo(String p,String[] c){
    	group.add(p);
    	
    	List<String> item = new ArrayList<String>();
    	
    	for(int i=0;i<c.length;i++){
    		item.add(c[i]);
    	}
    	
    	child.add(item);
    }
    
	
    
    public void updateInfo(String[] c){
    	
    	
    	
    	for(int i=0;i<c.length;i++){
    		child.get(7).add(c[i]);
    	}
    	
    	
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, MENU_ADD, 0, "新增");
//		这里可以增加删除
//		menu.add(0, MENU_DELETE, 0, "删除");
		
		return true;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) { 
    	case MENU_ADD:
    		dialogAdd.show();
    		break;
    	
    	case MENU_DELETE:
    		dialogDelete.show();
    		break;
    	}
    	return false; 
    }
	
    
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public class InfoDetailsAdapter extends BaseExpandableListAdapter {
    	Activity activity;
    	
    	public InfoDetailsAdapter(Activity a){
    		activity = a;
    	}
    	
    	//child method stub
    	
		public Object getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return child.get(groupPosition).get(childPosition);
		}

		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return child.get(groupPosition).size();
		}
		
		
		
		
		
		//这里的VIEW很重要，获取child的view
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String string = child.get(groupPosition).get(childPosition);
			return getGenericView(string);
		}


		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//group method stub
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return group.get(groupPosition);
		}

		public int getGroupCount() {
			// TODO Auto-generated method stub
			return group.size();
		}

		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}
		
//		获取group的view
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			String string = group.get(groupPosition);
			return getGenericView(string);
		}

		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		//View stub to create Group/Children 's View
		public TextView getGenericView(String s) {
            // Layout parameters for the ExpandableListView
            AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT, 90);

            TextView text = new TextView(activity);
            text.setLayoutParams(lp);
            // Center the text vertically
            text.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
            // Set the text starting position
            text.setPadding(36, 0, 0, 0);
            
            text.setTextSize(25);
            text.setText(s);
            return text;
        }
		
		
		
		
		
		
		
		
		
		
		
		
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}
    	
    	
    }
    
}
     