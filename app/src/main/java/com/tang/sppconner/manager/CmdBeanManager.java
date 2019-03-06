package com.tang.sppconner.manager;

import com.tang.sppconner.bean.CmdBean;

import java.util.List;

import io.realm.Realm;

public class CmdBeanManager {
    public static void addCmdBean(Realm realm, CmdBean cmdBean) {
        if (null == realm
                || null == cmdBean)
            return;
        realm.beginTransaction();
        CmdBean tempCmd = realm.copyToRealm(cmdBean);
        realm.commitTransaction();
    }

    public static List<CmdBean> getAllCmdBeans(Realm realm) {
        if (null == realm)
            return null;
        return realm.where(CmdBean.class).findAll();
    }
}
