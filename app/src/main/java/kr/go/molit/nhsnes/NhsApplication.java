package kr.go.molit.nhsnes;

import android.app.Application;

import com.modim.lan.lanandroid.INativeImple;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import kr.go.molit.nhsnes.activity.NhsIntroActivity;
import kr.go.molit.nhsnes.common.StorageUtil;

/**
 * Created by jongrakmoon on 2017. 5. 25..
 */

public class NhsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        realmSetUp();
        INativeImple.getInstance(getApplicationContext());
    }

    public void realmSetUp() {
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .directory(StorageUtil.getSaveStorage(this))
                .build();

        Realm.setDefaultConfiguration(configuration);

    }

}
