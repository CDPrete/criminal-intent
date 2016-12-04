package com.bignerdranch.android.criminalintent.model.entity;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Unique;

import java.util.Date;

/**
 * @author Cosimo Damiano Prete
 * @since 02/12/2016
 */
@Entity(active = true, nameInDb = "CRIME")
public class CrimeEntity {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    @Unique
    private String uuid;

    private String title;

    @NotNull private Date date;

    @NotNull private boolean solved;

    @ToOne
    private SuspectEntity suspect;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 290012313)
    private transient CrimeEntityDao myDao;

    @Generated(hash = 1130558032)
    public CrimeEntity(Long id, @NotNull String uuid, String title,
            @NotNull Date date, boolean solved) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.date = date;
        this.solved = solved;
    }

    @Generated(hash = 2004715950)
    public CrimeEntity() {
    }

    @Generated(hash = 1117390805)
    private transient boolean suspect__refreshed;

    @Override
    public String toString() {
        return "CrimeEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", solved=" + solved +
                ", suspect=" + suspect +
                '}';
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean getSolved() {
        return this.solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    /** To-one relationship, resolved on first access. */
    //TODO generate it again when the optional relationships are allowed
    @Keep
    public SuspectEntity getSuspect() {
        if (suspect != null || !suspect__refreshed) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SuspectEntityDao targetDao = daoSession.getSuspectEntityDao();
            if(suspect != null) {
                targetDao.refresh(suspect);
                suspect__refreshed = true;
            }
        }
        return suspect;
    }

    /** To-one relationship, returned entity is not refreshed and may carry only the PK property. */
    @Generated(hash = 985266143)
    public SuspectEntity peakSuspect() {
        return suspect;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 611840788)
    public void setSuspect(SuspectEntity suspect) {
        synchronized (this) {
            this.suspect = suspect;
            suspect__refreshed = true;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 999032834)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getCrimeEntityDao() : null;
    }
}
