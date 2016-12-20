package com.group.android.finalproject.player.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.group.android.finalproject.R;
import com.group.android.finalproject.common.DBbase;
import com.group.android.finalproject.common.EditTextWithDate;
import com.group.android.finalproject.player.adapter.RecordAdapter;
import com.group.android.finalproject.player.util.RecordItem;
import com.group.android.finalproject.recoder.Recorder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PlayerMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SwipeMenuListView swipeMenuListView;
    private DBbase dBbase;
    private List<RecordItem> recordItemList;
    private RecordAdapter recordAdapter;
    private Recorder mRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.player_toolbar);
        toolbar.setTitle("Recorder List");
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.player_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.player_navigation);
        navigationView.setNavigationItemSelectedListener(this);

        swipeMenuListView = (SwipeMenuListView)findViewById(R.id.player_recorder_list);
        dBbase = new DBbase(this);
        mRecorder = new Recorder();

        recordItemList = dBbase.queryAll();
        recordAdapter = new RecordAdapter(this, recordItemList);
        swipeMenuListView.setAdapter(recordAdapter);
        setSwipeList();
        setListClick();
    }

    private void setSwipeList() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem detailItem = new SwipeMenuItem(
                        getApplicationContext());
                detailItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                detailItem.setWidth(dp2px(90));
                detailItem.setTitle("详情");
                detailItem.setTitleSize(18);
                detailItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(detailItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.mipmap.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        swipeMenuListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch(index) {
                    case 0:     // detail
                        break;
                    case 1:     // delete
                        showConfirmDeleteDialog(position);
                        break;
                }
                return true;
            }
        });
    }

    private void setListClick() {
        swipeMenuListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                String fileUrl = recordItemList.get(i).getStoreUrl();
                mRecorder.playRecord(fileUrl);
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_all_list:
                recordItemList.clear();
                recordItemList.addAll(dBbase.queryAll());
                recordAdapter.notifyDataSetChanged();
                break;
            case R.id.menu_search_by_time:
                showQueryDialog();
                break;
            default:
                recordItemList.remove(0);
                recordAdapter.notifyDataSetChanged();
                break;
        }

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.player_drawer);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showConfirmDeleteDialog(final int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerMainActivity.this);

        builder.setTitle("是否删除");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dBbase.delete(recordItemList.get(i).getStoreUrl());
                recordItemList.remove(i);
                recordAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();
    }

    private void showQueryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerMainActivity.this);
        LayoutInflater inflater = PlayerMainActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.player_query_by_time_dialog, null);
        builder.setView(dialogView);

        final EditTextWithDate start_date = (EditTextWithDate) dialogView.findViewById(R.id.search_start);
        final EditTextWithDate end_date = (EditTextWithDate) dialogView.findViewById(R.id.search_end);
        final EditText loc = (EditText) dialogView.findViewById(R.id.search_location);

        builder.setTitle("查询");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recordItemList.clear();
                recordItemList.addAll(dBbase.queryAll());
                String start = start_date.getText().toString() + " 00:00:00";
                String end = end_date.getText().toString() + " 23:59:59";
                String location = loc.getText().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date start_time = new Date(System.currentTimeMillis());
                Date end_time = new Date(System.currentTimeMillis());

                try {
                    start_time = formatter.parse(start);
                    end_time = formatter.parse(end);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < recordItemList.size();) {
                    Date d = new Date();
                    try {
                        d = formatter.parse(recordItemList.get(i).getDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (d.after(start_time) && d.before(end_time) && recordItemList.get(i).getPlace().contains(location)) {
                        i++;
                    } else {
                        recordItemList.remove(i);
                    }
                }
                recordAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
