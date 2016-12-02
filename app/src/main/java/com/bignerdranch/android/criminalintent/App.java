package com.bignerdranch.android.criminalintent;

import android.app.Application;

import com.bignerdranch.android.criminalintent.model.entity.DaoMaster;
import com.bignerdranch.android.criminalintent.model.entity.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * @author Cosimo Damiano Prete
 * @since 02/12/2016
 */

public class App extends Application {
    private static final boolean ENCRYPTED_DB = true;
    private static final String DATABASE_NAME = "securitySuite";
    private static final String PASSWORD_FOR_ENCRYPTION = "password";

    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, DATABASE_NAME);
        Database db = ENCRYPTED_DB ? helper.getEncryptedWritableDb(PASSWORD_FOR_ENCRYPTION) : helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }
}
