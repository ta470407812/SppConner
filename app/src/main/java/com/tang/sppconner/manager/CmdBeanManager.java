package com.tang.sppconner.manager;

import com.tang.sppconner.bean.BtConnBean;
import com.tang.sppconner.bean.CmdBean;
import com.tang.sppconner.utils.SimpleLog;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public final class CmdBeanManager {
    public static void addCmdBean(Realm realm, CmdBean cmdBean) {
        if (null == realm
                || null == cmdBean)
            return;
        RealmResults<CmdBean> realmResults = realm.where(CmdBean.class)
                .equalTo("cmdData", cmdBean.getCmdData())
                .equalTo("cmdType", cmdBean.getCmdType())
                .findAll();
        boolean hasBean = (null != realmResults && realmResults.size() > 0);
        if (hasBean) {
            updateCmdBean(realm, realmResults.get(0));
        } else {
            realm.beginTransaction();
            realm.copyToRealm(cmdBean);
            realm.commitTransaction();
        }
    }

    public static RealmResults<CmdBean> getAllCmdBeans(Realm realm) {
        if (null == realm)
            return null;
        return realm
                .where(CmdBean.class)
                .findAll()
                .sort("cmdTime",
                        Sort.DESCENDING);
    }

    private static void updateCmdBean(Realm realm, CmdBean cmdBean) {
        if (null == realm
                || null == cmdBean)
            return;
        realm.beginTransaction();
        cmdBean.setCmdTime(System.currentTimeMillis());
        realm.commitTransaction();
    }
}
