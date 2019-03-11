package com.tang.sppconner.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tang.sppconner.R;
import com.tang.sppconner.base.BaseBtServiceActivity;
import com.tang.sppconner.base.BaseListAdapter;
import com.tang.sppconner.bean.BtConnBean;
import com.tang.sppconner.manager.BtConnBeanManager;
import com.tang.sppconner.utils.SimpleDateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class BtConnHistoryActivity extends BaseBtServiceActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.btn_title)
    TextView textTitle;

    private ConnAdapter connAdapter;

    private Realm realm;
    private RealmResults<BtConnBean> realmResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        realm.close();
        super.onDestroy();
    }

    @Override
    protected void serviceConnected(BtService service) {

    }

    @Override
    protected void serviceWillDisconnect(BtService service) {

    }

    @Override
    protected void serviceDisconnected() {

    }

    @Override
    protected int getLayoutResID() {
        return R.layout.acitivty_history;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BtConnBean btConnBean = realmResults.get(position);
        btService.connDevice(btConnBean.getUuid());
        finish();
    }

    @OnClick({R.id.btn_back,
            R.id.btn_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_save:
                finish();
                break;
        }
    }

    @Override
    protected void initView() {
        listView.setOnItemClickListener(this);
        textTitle.setText("连接历史");
    }

    @Override
    protected void initData() {
        realmResults = BtConnBeanManager.getAllBtConnBean(realm);
        realmResults.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<BtConnBean>>() {
            @Override
            public void onChange(RealmResults<BtConnBean> connBeans, OrderedCollectionChangeSet changeSet) {
                realmResults = connBeans;
                connAdapter.notifyDataSetChanged();
            }
        });
        connAdapter = new ConnAdapter(this, R.layout.item_conn_history, realmResults);
        listView.setAdapter(connAdapter);
    }

    private class ConnAdapter extends BaseListAdapter<BtConnBean> {
        public ConnAdapter(Context context, int layoutId, List<BtConnBean> data) {
            super(context, layoutId, data);
        }

        @Override
        protected void setViewData(View convertView, int position, BtConnBean btConnBean) {
            if (!TextUtils.isEmpty(btConnBean.getName())) {
                ((TextView) convertView
                        .findViewById(R.id.item_name))
                        .setText(btConnBean.getName());
            }

            ((TextView) convertView
                    .findViewById(R.id.item_address))
                    .setText(btConnBean.getUuid());

            ((TextView) convertView
                    .findViewById(R.id.item_time))
                    .setText(SimpleDateUtils.getDateTimeString2(new Date(btConnBean.getConnTime())));
        }
    }
}
