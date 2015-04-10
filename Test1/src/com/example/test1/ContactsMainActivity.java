package com.example.test1;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.DrawerLayout;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.AlphabetIndexer;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnSwipeListener;

import com.ikimuhendis.ldrawer.ActionBarDrawerToggle;
import com.ikimuhendis.ldrawer.DrawerArrowDrawable;

public class ContactsMainActivity extends Activity implements ActionBar.TabListener,
ActionBar.OnNavigationListener{

	/** 
     * 分组的布局 
     */  
    private LinearLayout titleLayout;  
    
    
    /** 
     * 弹出式分组的布局 
     */  
    private RelativeLayout sectionToastLayout;  
  
    /** 
     * 右侧可滑动字母表 
     */  
    private Button alphabetButton;  
  
    
  
    /** 
     * 弹出式分组上的文字 
     */  
    private TextView sectionToastText;  
    
  
    /** 
     * 分组上显示的字母 
     */  
    private TextView title;  
  
    /** 
     * 联系人ListView 
     */  
    //private ListView contactsListView;  
    private SwipeMenuListView contactsListView;
    /** 
     * 联系人列表适配器 
     */  
    private ContactAdapter adapter;  
  
    /** 
     * 用于进行字母表分组 
     */  
    private AlphabetIndexer indexer;  
  
    /** 
     * 存储所有手机中的联系人 
     */  
    private List<Contact> contacts = new ArrayList<Contact>();  
  
    /** 
     * 定义字母表的排序规则 
     */  
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";  
  
    /** 
     * 上次第一个可见元素，用于滚动时记录标识。 
     */  
    private int lastFirstVisibleItem = -1;  
    
    /*
     * 异步加载对象
     * 
     */
    private AsyncQueryHandler asyncQuery; 
    
    

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerArrowDrawable drawerArrow;
    
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.fragment_main);
        
        initUI();
        
        initActionBar();
        
        setListener();
        
        adapter = new ContactAdapter(this, R.layout.item_list_contactors, contacts); 
   
        asyncQuery = new MyAsyncQueryHandler(getContentResolver());  
        
    } 
    
    @Override  
    protected void onResume() {  
        super.onResume();  
        Uri uri = Uri.parse("content://com.android.contacts/data/phones"); // 联系人的Uri  
        String[] projection = { "display_name", "sort_key" }; // 查询的列   
        asyncQuery.startQuery(0, null, uri, projection, null, null,  
                "sort_key COLLATE LOCALIZED asc"); // 按照sort_key升序查询   
    }
    


	private void initUI(){
    	mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.navdrawer);
        
        titleLayout = (LinearLayout) findViewById(R.id.title_layout);  
        title = (TextView) findViewById(R.id.title);  
        
        contactsListView = (SwipeMenuListView) findViewById(R.id.listView);  
        //contactsListView = (ListView) findViewById(R.id.listView);
        
        sectionToastLayout = (RelativeLayout) findViewById(R.id.section_toast_layout);  
        sectionToastText = (TextView) findViewById(R.id.section_toast_text);  
        alphabetButton = (Button) findViewById(R.id.alphabetButton);
    }
	
	private void initActionBar(){
		ActionBar ab = getActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeButtonEnabled(true);
		
		drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return false;
            }
        };
        Drawable colorDrawable = new ColorDrawable(Color.parseColor("#FF0099CC".toString()));
        ab.setBackgroundDrawable(colorDrawable);
       
      //Ldrawer侧边栏

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
            drawerArrow, R.string.drawer_open,
            R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        String[] values = new String[]{
            "1",
            "2",
            "3",
            "4",
            "5",
            "6",
            "7"
        };
        
        ArrayAdapter<String> LDadapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, android.R.id.text1, values);
        mDrawerList.setAdapter(LDadapter);
        
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;
                    case 5:

                        break;
                    case 6:

                        break;
                }

            }
        });
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		// step 2. listener item click event
				contactsListView
						.setOnMenuItemClickListener(new OnMenuItemClickListener() {
							@Override
							public boolean onMenuItemClick(int position,
									SwipeMenu menu, int index) {
								Contact item = contacts.get(position);
								switch (index) {
								case 0:
									// open
									// open(item);
									break;
								case 1:
									// delete
									// delete(item);
									break;
								}
								return false;
							}
						});

				// set SwipeListener
				contactsListView.setOnSwipeListener(new OnSwipeListener() {

					@Override
					public void onSwipeStart(int position) {
						// swipe start
					}

					@Override
					public void onSwipeEnd(int position) {
						// swipe end
					}
				});

				// other setting
				// listView.setCloseInterpolator(new BounceInterpolator());

				// test item long click
				contactsListView
						.setOnItemLongClickListener(new OnItemLongClickListener() {

							@Override
							public boolean onItemLongClick(AdapterView<?> parent,
									View view, int position, long id) {
								Toast.makeText(getApplicationContext(),
										position + " long click", 0).show();
								return false;
							}
						});
				
				 
		
	}
  
	/** 
     * 数据库异步查询类AsyncQueryHandler 
     *  
     * @author administrator 
     *  
     */  
    private class MyAsyncQueryHandler extends AsyncQueryHandler {  
  
        public MyAsyncQueryHandler(ContentResolver cr) {  
            super(cr);  
  
        }  
  
        /** 
         * 查询结束的回调函数 
         */  
        @Override  
        protected void onQueryComplete(int token, Object cookie, Cursor cursor) {  
            if (cursor != null && cursor.getCount() > 0) {  
            	 if (cursor.moveToFirst()) {  
                     do {  
                         String name = cursor.getString(0);  
                         String sortKey = getSortKey(cursor.getString(1));  
                         Contact contact = new Contact();  
                         contact.setName(name);  
                         contact.setSortKey(sortKey);  
                         contacts.add(contact);  
                     } while (cursor.moveToNext());  
                 }  
                 startManagingCursor(cursor);  
                 indexer = new AlphabetIndexer(cursor, 1, alphabet);  
                 adapter.setIndexer(indexer);  
                 if (contacts.size() > 0) {  
                     setupContactsListView();  
                     setAlpabetListener();  
                 }  
            }  
        }  
  
    }  

    
    private void setupContactsListView() {  
    
        contactsListView.setAdapter(adapter); 
        

        SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "open" item
				SwipeMenuItem editItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				editItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
						0xCE)));
				// set item width
				editItem.setWidth(dp2px(90));
				// set item title
				editItem.setTitle("编辑");
				// set item title fontsize
				editItem.setTitleSize(18);
				// set item title font color
				editItem.setTitleColor(Color.WHITE);
				// add to menu
				menu.addMenuItem(editItem);

				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				deleteItem.setIcon(R.drawable.ic_delete);
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		contactsListView.setMenuCreator(creator);
		
		/** 
	     * 为联系人ListView设置监听事件，根据当前的滑动状态来改变分组的显示位置，从而实现挤压动画的效果。 
	     */
		//onScrollListener
		contactsListView.setOnScrollListener(new OnScrollListener() {  
            @Override  
            public void onScrollStateChanged(AbsListView view, int scrollState) {  
            }  
  
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,  
                    int totalItemCount) {  
                int section = indexer.getSectionForPosition(firstVisibleItem);  
                int nextSecPosition = indexer.getPositionForSection(section + 1);  
                if (firstVisibleItem != lastFirstVisibleItem) {  
                    MarginLayoutParams params = (MarginLayoutParams) titleLayout.getLayoutParams();  
                    params.topMargin = 0;  
                    titleLayout.setLayoutParams(params);  
                    title.setText(String.valueOf(alphabet.charAt(section)));  
                }  
                if (nextSecPosition == firstVisibleItem + 1) {  
                    View childView = view.getChildAt(0);  
                    if (childView != null) {  
                        int titleHeight = titleLayout.getHeight();  
                        int bottom = childView.getBottom();  
                        MarginLayoutParams params = (MarginLayoutParams) titleLayout  
                                .getLayoutParams();  
                        if (bottom < titleHeight) {  
                            float pushedDistance = bottom - titleHeight;  
                            params.topMargin = (int) pushedDistance;  
                            titleLayout.setLayoutParams(params);  
                        } else {  
                            if (params.topMargin != 0) {  
                                params.topMargin = 0;  
                                titleLayout.setLayoutParams(params);  
                            }  
                        }  
                    }  
                }  
                lastFirstVisibleItem = firstVisibleItem;  
            }  
        });

    } 
    
    /** 
     * 设置字母表上的触摸事件，根据当前触摸的位置结合字母表的高度，计算出当前触摸在哪个字母上。 
     * 当手指按在字母表上时，展示弹出式分组。手指离开字母表时，将弹出式分组隐藏。 
     */  
    private void setAlpabetListener() {  
        alphabetButton.setOnTouchListener(new OnTouchListener() {  
            @Override  
            public boolean onTouch(View v, MotionEvent event) {  
                float alphabetHeight = alphabetButton.getHeight();  
                float y = event.getY();  
                int sectionPosition = (int) ((y / alphabetHeight) / (1f / 27f));  
                if (sectionPosition < 0) {  
                    sectionPosition = 0;  
                } else if (sectionPosition > 26) {  
                    sectionPosition = 26;  
                }  
                String sectionLetter = String.valueOf(alphabet.charAt(sectionPosition));  
                int position = indexer.getPositionForSection(sectionPosition);  
                switch (event.getAction()) {  
                case MotionEvent.ACTION_DOWN:  
                    alphabetButton.setBackgroundResource(R.drawable.a_z_click);  
                    sectionToastLayout.setVisibility(View.VISIBLE);  
                    sectionToastText.setText(sectionLetter);  
                    contactsListView.setSelection(position);  
                    break;  
                case MotionEvent.ACTION_MOVE:  
                    sectionToastText.setText(sectionLetter);  
                    contactsListView.setSelection(position);  
                    break;  
                default:  
                    alphabetButton.setBackgroundResource(R.drawable.a_z);  
                    sectionToastLayout.setVisibility(View.GONE);  
                }  
                return true;  
            }  
        });  
    }
    
  
    /** 
     * 获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。 
     *  
     * @param sortKeyString 
     *            数据库中读取出的sort key 
     * @return 英文字母或者# 
     */  
    private String getSortKey(String sortKeyString) {  
        String key = sortKeyString.substring(0, 1).toUpperCase();  
        if (key.matches("[A-Z]")) {  
            return key;  
        }  
        return "#";  
    }  
  
    
    //实现侧边栏接口
    private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
    	switch (item.getItemId()) {

		case R.id.search_icon:

			//To be done
			
			 break;
		case android.R.id.home :
			 if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
	                mDrawerLayout.closeDrawer(mDrawerList);
	            } else {
	                mDrawerLayout.openDrawer(mDrawerList);
	            }
			 break;

		}
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		return false;
	}
    
}  
