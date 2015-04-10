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
     * ����Ĳ��� 
     */  
    private LinearLayout titleLayout;  
    
    
    /** 
     * ����ʽ����Ĳ��� 
     */  
    private RelativeLayout sectionToastLayout;  
  
    /** 
     * �Ҳ�ɻ�����ĸ�� 
     */  
    private Button alphabetButton;  
  
    
  
    /** 
     * ����ʽ�����ϵ����� 
     */  
    private TextView sectionToastText;  
    
  
    /** 
     * ��������ʾ����ĸ 
     */  
    private TextView title;  
  
    /** 
     * ��ϵ��ListView 
     */  
    //private ListView contactsListView;  
    private SwipeMenuListView contactsListView;
    /** 
     * ��ϵ���б������� 
     */  
    private ContactAdapter adapter;  
  
    /** 
     * ���ڽ�����ĸ����� 
     */  
    private AlphabetIndexer indexer;  
  
    /** 
     * �洢�����ֻ��е���ϵ�� 
     */  
    private List<Contact> contacts = new ArrayList<Contact>();  
  
    /** 
     * ������ĸ���������� 
     */  
    private String alphabet = "#ABCDEFGHIJKLMNOPQRSTUVWXYZ";  
  
    /** 
     * �ϴε�һ���ɼ�Ԫ�أ����ڹ���ʱ��¼��ʶ�� 
     */  
    private int lastFirstVisibleItem = -1;  
    
    /*
     * �첽���ض���
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
        Uri uri = Uri.parse("content://com.android.contacts/data/phones"); // ��ϵ�˵�Uri  
        String[] projection = { "display_name", "sort_key" }; // ��ѯ����   
        asyncQuery.startQuery(0, null, uri, projection, null, null,  
                "sort_key COLLATE LOCALIZED asc"); // ����sort_key�����ѯ   
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
       
      //Ldrawer�����

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
     * ���ݿ��첽��ѯ��AsyncQueryHandler 
     *  
     * @author administrator 
     *  
     */  
    private class MyAsyncQueryHandler extends AsyncQueryHandler {  
  
        public MyAsyncQueryHandler(ContentResolver cr) {  
            super(cr);  
  
        }  
  
        /** 
         * ��ѯ�����Ļص����� 
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
				editItem.setTitle("�༭");
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
	     * Ϊ��ϵ��ListView���ü����¼������ݵ�ǰ�Ļ���״̬���ı�������ʾλ�ã��Ӷ�ʵ�ּ�ѹ������Ч���� 
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
     * ������ĸ���ϵĴ����¼������ݵ�ǰ������λ�ý����ĸ��ĸ߶ȣ��������ǰ�������ĸ���ĸ�ϡ� 
     * ����ָ������ĸ����ʱ��չʾ����ʽ���顣��ָ�뿪��ĸ��ʱ��������ʽ�������ء� 
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
     * ��ȡsort key���׸��ַ��������Ӣ����ĸ��ֱ�ӷ��أ����򷵻�#�� 
     *  
     * @param sortKeyString 
     *            ���ݿ��ж�ȡ����sort key 
     * @return Ӣ����ĸ����# 
     */  
    private String getSortKey(String sortKeyString) {  
        String key = sortKeyString.substring(0, 1).toUpperCase();  
        if (key.matches("[A-Z]")) {  
            return key;  
        }  
        return "#";  
    }  
  
    
    //ʵ�ֲ�����ӿ�
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
