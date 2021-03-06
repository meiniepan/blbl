package com.gs.buluo.app.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import com.gs.buluo.app.Constant;
import com.gs.buluo.app.R;
import com.gs.buluo.app.TribeApplication;
import com.gs.buluo.app.adapter.VisitorListAdapter;
import com.gs.buluo.app.bean.LockKey;
import com.gs.buluo.app.bean.ResponseBody.VisitorListResponse;
import com.gs.buluo.app.bean.VisitorActiveBean;
import com.gs.buluo.app.network.DoorApis;
import com.gs.buluo.app.network.TribeRetrofit;
import com.gs.buluo.app.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hjn on 2017/3/9.
 */
public class VisitorListActivity extends BaseActivity implements Callback<VisitorListResponse> {
    @Bind(R.id.visitor_list)
    ExpandableListView listView;
    private ArrayList<VisitorActiveBean> visitorList;
    private VisitorListAdapter adapter;

    @Override
    protected void bindView(Bundle savedInstanceState) {
        visitorList = new ArrayList<>();
        adapter = new VisitorListAdapter(this, visitorList);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent=new Intent(getCtx(),OpenDoorActivity.class);
                intent.putExtra(Constant.DOOR, visitorList.get(groupPosition).keys.get(childPosition));
                startActivity(intent);
                finish();
                return false;
            }
        });
        findViewById(R.id.add_visitor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getCtx(),AddVisitorActivity.class);
                startActivity(intent);
            }
        });

        TribeRetrofit.getInstance().createApi(DoorApis.class).getVisitorList(TribeApplication.getInstance().getUserInfo().getId()).enqueue(this);
        showLoadingDialog();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_visitor_list;
    }


    @Override
    public void onResponse(Call<VisitorListResponse> call, Response<VisitorListResponse> response) {
        dismissDialog();
        if (response!=null &&response.code()==200 &&response.body()!=null){
            List<VisitorActiveBean> data = response.body().data;
            if (data==null||data.size()==0){
                findViewById(R.id.visitor_empty_view).setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }else {
                findViewById(R.id.visitor_empty_view).setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                visitorList.addAll(data);
                adapter.notifyDataSetChanged();
            }
        }else {
            ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
        }
    }

    @Override
    public void onFailure(Call<VisitorListResponse> call, Throwable t) {
        dismissDialog();
        ToastUtils.ToastMessage(getCtx(),R.string.connect_fail);
    }

    public void showEmpty() {
        findViewById(R.id.visitor_empty_view).setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }
}
