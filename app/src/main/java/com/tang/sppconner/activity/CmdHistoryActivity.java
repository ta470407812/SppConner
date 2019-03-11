package com.tang.sppconner.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.tang.sppconner.R;
import com.tang.sppconner.base.BaseBtServiceActivity;
import com.tang.sppconner.base.BaseListAdapter;
import com.tang.sppconner.bean.CmdBean;
import com.tang.sppconner.config.BtConfig;
import com.tang.sppconner.manager.CmdBeanManager;
import com.tang.sppconner.utils.BytesUtils;
import com.tang.sppconner.utils.SimpleDateUtils;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class CmdHistoryActivity extends BaseBtServiceActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.btn_title)
    TextView textTitle;

    private Realm realm;
    private RealmResults<CmdBean> realmResults;
    private CmdAdapter cmdAdapter;

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
    protected void initView() {
        listView.setOnItemClickListener(this);
        textTitle.setText("命令历史");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CmdBean cmdBean = realmResults.get(position);
        if (null != cmdBean
                && cmdBean.getCmdType() == BtConfig.CmdType.Send)
            btService.sendCmd(cmdBean.getCmdData());
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
    protected void initData() {
        realmResults = CmdBeanManager.getAllCmdBeans(realm);
        realmResults.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<CmdBean>>() {
            @Override
            public void onChange(RealmResults<CmdBean> cmdBeans, OrderedCollectionChangeSet changeSet) {
                realmResults = cmdBeans;
                cmdAdapter.notifyDataSetChanged();
            }
        });
        cmdAdapter = new CmdAdapter(this, R.layout.item_cmd_history, realmResults);
        listView.setAdapter(cmdAdapter);
    }

    private class CmdAdapter extends BaseListAdapter<CmdBean> {
        public CmdAdapter(Context context, int layoutId, List<CmdBean> data) {
            super(context, layoutId, data);
        }

        @Override
        protected void setViewData(View convertView, int position, CmdBean cmdBean) {
            ((TextView) convertView.findViewById(R.id.item_info))
                    .setText(BytesUtils.bytes2Hex(cmdBean.getCmdData()));
            ((TextView) convertView.findViewById(R.id.item_msg))
                    .setText(cmdBean.getCmdType() == BtConfig.CmdType.Send ? "发送" : "接收");
            ((TextView) convertView.findViewById(R.id.item_time))
                    .setText(SimpleDateUtils.getDateTimeString2(new Date(cmdBean.getCmdTime())));
        }
    }
}
