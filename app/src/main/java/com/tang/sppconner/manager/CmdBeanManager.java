package com.tang.sppconner.manager;

import com.tang.sppconner.bean.CmdBean;
import com.tang.sppconner.utils.SimpleLog;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class CmdBeanManager {
    public static void addCmdBean(Realm realm, CmdBean cmdBean) {
        if (null == realm
                || null == cmdBean)
            return;
        boolean hasBean = realm.where(CmdBean.class)
                .equalTo("cmdData", cmdBean.getCmdData())
                .equalTo("cmdType", cmdBean.getCmdType())
                .findAll().size() > 0;
        if (!hasBean) {
            realm.beginTransaction();
            realm.copyToRealm(cmdBean);
            realm.commitTransaction();
        }
    }

    public static RealmResults<CmdBean> getAllCmdBeans(Realm realm) {
        if (null == realm)
            return null;
        return realm.where(CmdBean.class).findAll().sort("cmdTime",
                Sort.DESCENDING);
    }
}
