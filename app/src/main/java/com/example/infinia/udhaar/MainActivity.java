package com.example.infinia.udhaar;

import java.util.ArrayList;
import java.util.Random;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.baoyz.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.github.clans.fab.FloatingActionButton;

import pl.pawelkleczkowski.customgauge.CustomGauge;


public class MainActivity extends ActionBarActivity {
    LinearLayout ll;
    final Context context = this;
    DatabaseHandler db;
    private SwipeMenuListView mListView;
    private CustomAdapter adapter;
    private ArrayList<String> arrayList;
    private ArrayList<Integer> balance;
    TextView text;
    FloatingActionButton mFab;
    static int window_width;
    static int window_height;
    int x;
    CustomGauge gauge1;
    CustomGauge gauge2;
    TextView text1,text2;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        mFab = (FloatingActionButton) findViewById(R.id.fabbutton);


        //Code to get height and width
        Display display = getWindowManager().getDefaultDisplay();
        window_width = display.getWidth();
        window_height = display.getHeight();


        ll = (LinearLayout) findViewById(R.id.top);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) ll.getLayoutParams();
        params1.height = (int) (0.30f * window_height);
        params1.width = (int) (window_width);
        ll.setLayoutParams(params1);

        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) mListView.getLayoutParams();
        params2.height = (int) (0.70f * window_height);
        params2.width = (int) (window_width);
        mListView.setLayoutParams(params2);

        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        arrayList = new ArrayList<String>();
        balance = new ArrayList<Integer>();
        adapter = new CustomAdapter(this, arrayList, balance);
        mListView.setAdapter(adapter);


        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem editItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                editItem.setBackground(new ColorDrawable(Color.rgb(0x4C, 0xAF,
                        0x50)));
                // set item width
                editItem.setWidth(dp2px(60));
                // set a icon
                editItem.setIcon(R.mipmap.ic_create_white_24dp);
                // add to menu
                menu.addMenuItem(editItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF4,
                        0x43, 0x36)));
                // set item width
                deleteItem.setWidth(dp2px(60));
                // set a icon
                deleteItem.setIcon(R.mipmap.ic_delete_white_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);


                // create "delete" item
                SwipeMenuItem remindItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                remindItem.setBackground(new ColorDrawable(Color.rgb(0x21,
                        0x96, 0xF3)));
                // set item width
                remindItem.setWidth(dp2px(60));
                // set a icon
                remindItem.setIcon(R.mipmap.ic_notifications_on_white_24dp);
                // add to menu
                menu.addMenuItem(remindItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                final int pos = position;
                switch (index) {
                    case 0:
                        // edit
                        edit(position);
                        break;
                    case 1:
                        // delete
                        new MaterialDialog.Builder(MainActivity.this)
                                .title("Alert")
                                .content("Do you want to delete this transaction?")
                                .positiveText("Yes")
                                .negativeText("Cancel")
                                .positiveColorRes(R.color.primary)
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog dialog) {
                                        delete(pos);
                                    }
                                }).show();

                        break;
                    case 2:
                        remind(position);
                        break;

                }
                return false;
            }
        });


        // other setting
        mListView.setCloseInterpolator(new BounceInterpolator());

        //Click on list item
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str[] = showHistory(arrayList.get(position));
                final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(MainActivity.this);
                adapter.clear();
                //for(int i=0;i<str.length;i++)
                    adapter.add(new MaterialSimpleListItem.Builder(MainActivity.this)
                            .content(str[str.length-1]).build());

                new MaterialDialog.Builder(MainActivity.this)
                        .title("History")
                        .adapter(adapter, new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                MaterialSimpleListItem item = adapter.getItem(which);
                            }
                        })
                        .positiveText(android.R.string.cancel)
                        .show();
            }

        });

    }


    public String[] showHistory(String name) {
        int j = 0;
        String[] his = new String[100];
        DatabaseHandler db;
        String str = "";
        db = new DatabaseHandler(this);
        Cursor c = null;
        c = db.getDetails(name);
        //label.setText("hii");
        if (c.moveToFirst())
            if (c.getInt(5) == 0) {
                str = "Date:" + c.getString(2) + ",Location:" + c.getString(3) + ",Debit:" + c.getInt(4) + "\n";
                //his[0] = str;
            } else {
                str = "Date:" + c.getString(2) + ",Location:" + c.getString(3) + ",Credit:" + c.getInt(5) + "\n";
                //his[0] = str;
            }

        while (c.moveToNext()) {
            if (c.getInt(5) == 0) {
                str = str + "Date:" + c.getString(2) + ",Location:" + c.getString(3) + ",Debit:" + c.getInt(4) + "\n";
                his[j++] = str;
            } else {
                str = str + "Date:" + c.getString(2) + ",Location:" + c.getString(3) + ",Credit:" + c.getInt(5) + "\n";
                his[j++] = str;
            }
        }
        return his;

    }

    public void fabClicked(View view) {
        Intent i = new Intent(this, Add.class);
        startActivity(i);
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    protected void onResume() {

        gauge1 = (CustomGauge) findViewById(R.id.myProgress1);
        gauge2 = (CustomGauge) findViewById(R.id.myProgress2);
        text1  = (TextView) findViewById(R.id.tv1);
        text2  = (TextView) findViewById(R.id.tv2);
        text1.setText(Integer.toString(gauge1.getValue()));
        text2.setText(Integer.toString(gauge1.getValue()));

        db = new DatabaseHandler(this);
        Cursor c = null;
        c = db.getAllDetails();
        arrayList.clear();
        balance.clear();

        if (c.moveToFirst()) {
            do {
                int flag = 0;
                int deb = c.getInt(4);
                int cred = c.getInt(5);
                int x = cred - deb;

                for (int i = 0; i < arrayList.size(); i++) {
                    if (c.getString(1).equals(arrayList.get(i))) {
                        flag = 1;
                        balance.set(i, x + balance.get(i));
                        break;
                    }
                }
                if (flag == 0) {
                    arrayList.add(c.getString(1));
                    balance.add(x);
                }

            }
            while (c.moveToNext());

            int debit = 0, credit = 0;

            for (int j = 0; j < balance.size(); j++) {
                if (balance.get(j) < 0)
                    debit = debit + balance.get(j);

                else if (balance.get(j) > 0)
                    credit = credit + balance.get(j);

                else continue;
            }


            if (debit != 0 || credit != 0) {
                float amount1 = (-debit * 100) / (-debit + credit);
                float amount2 = 100 - amount1;
                final int amount11 = (int) amount1;
                int amount22 = (int) amount2;


                new Thread() {
                    public void run() {
                        for (x=0;x<amount11+1;x++) {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        gauge1.setValue(x);
                                        gauge2.setValue(100-x);
                                        text1.setText(Integer.toString(gauge1.getValue())+ "%");
                                        text2.setText(Integer.toString(gauge2.getValue())+ "%");
                                    }
                                });
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }.start();

               /* gauge1.setValue(amount11);
                text = ((TextView) findViewById(R.id.tv1));
                text.setText(amount11 + "%");
                gauge2.setValue(amount22);
                text = ((TextView) findViewById(R.id.tv2));
                text.setText(amount22 + "%");*/
            } else {
                gauge1.setValue(0);
                text = ((TextView) findViewById(R.id.tv1));
                text.setText("0%");
                gauge2.setValue(0);
                text = ((TextView) findViewById(R.id.tv2));
                text.setText("0%");
            }

        } else {
            gauge1.setValue(0);
            text = ((TextView) findViewById(R.id.tv1));
            text.setText("0%");
            gauge2.setValue(0);
            text = ((TextView) findViewById(R.id.tv2));
            text.setText("0%");
        }

        adapter.notifyDataSetChanged();
        adapter = new CustomAdapter(this, arrayList, balance);
        mListView.setAdapter(adapter);
        c.close();
        super.onResume();
    }

    public void edit(int position) {
        Intent i = new Intent(this, Edit.class);
        i.putExtra("name", arrayList.get(position));
        startActivity(i);
        adapter.notifyDataSetChanged();
    }

    public void delete(int position) {
        db = new DatabaseHandler(this);
        boolean b = db.deleteDetails(arrayList.get(position));
        arrayList.remove(position);
        balance.remove(position);
        adapter.notifyDataSetChanged();
        onResume();
    }

    public void remind(int position) {
        Intent i = new Intent(this, Reminderme.class);
        i.putExtra("name", arrayList.get(position));
        i.putExtra("balance", balance.get(position).toString());
        startActivity(i);
    }


}

class CustomAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<String> mItems;
    private ArrayList<Integer> mBalance;

    private class ViewHolder {
        TextView textView1;
        TextView textView2;
        TextView textView3;
    }

    public CustomAdapter(Context context, ArrayList<String> items, ArrayList<Integer> balance) {
        mContext = context;
        mItems = items;
        mBalance = balance;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            inflater = ((Activity) mContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.lv, null);
            holder.textView1 = (TextView) convertView.findViewById(R.id.name);
            holder.textView2 = (TextView) convertView.findViewById(R.id.balance);
            holder.textView3 = (TextView) convertView.findViewById(R.id.initials);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        String s = Character.toString(mItems.get(position).charAt(0));
        holder.textView1.setText(mItems.get(position));
        holder.textView2.setText(mBalance.get(position).toString());
        holder.textView3.setText(s.toUpperCase());
        holder.textView3.setBackgroundResource(R.drawable.circle);
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        int randomColor = Color.rgb(r, g, b);
        GradientDrawable sd = (GradientDrawable) holder.textView3.getBackground();
        sd.setColor(randomColor);
        sd.invalidateSelf();

        return convertView;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= mItems.size())
            return mBalance.get(position);
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

}