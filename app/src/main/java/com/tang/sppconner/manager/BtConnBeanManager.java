package com.tang.sppconner.manager;

import com.tang.sppconner.bean.BtConnBean;
import com.tang.sppconner.bean.CmdBean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.Sort;

public final class BtConnBeanManager {
    public static void addBtConnBean(Realm realm, BtConnBean btConnBean) {
        if (null == realm
                || null == btConnBean)
            return;
        RealmResults<BtConnBean> realmResults = realm.where(BtConnBean.class)
                .equalTo("uuid", btConnBean.getUuid())
                .findAll();
        boolean hasBean = (null != realmResults && realmResults.size() > 0);
        if (hasBean) {
            updateBtConnTime(realm, realmResults.get(0));
        } else {
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

    private static void updateBtConnTime(Realm realm, BtConnBean btConnBean) {
        if (null == realm
                || null == btConnBean)
            return;
        realm.beginTransaction();
        btConnBean.setConnTime(System.currentTimeMillis());
        realm.commitTransaction();
    }
}
