package com.tang.sppconner.manager;

import com.tang.sppconner.bean.BtConnBean;
import com.tang.sppconner.bean.CmdBean;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public final class BtConnBeanManager {
    public static void addBtConnBean(Realm realm, BtConnBean btConnBean) {
        if (null == realm
                || null == btConnBean)
            return;
        boolean hasBean = realm.where(BtConnBean.class)
                .equalTo("uuid", btConnBean.getUuid())
                .findAll().size() > 0;
        if (!hasBean) {
            realm.beginTransaction();
            realm.copyToRealm(btConnBean);
            realm.commitTransaction();
        }
    }

    public static RealmResults<BtConnBean> getAllBtConnBean(Realm realm) {
        if (null == realm)
            return null;
        return realm
                .where(BtConnBean.class)
                .findAll()
                .sort("connTime",
                        Sort.DESCENDING);
    }
}
