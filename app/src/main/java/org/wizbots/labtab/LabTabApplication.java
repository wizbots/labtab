package org.wizbots.labtab;

import android.app.Application;
import android.content.res.TypedArray;
import android.util.Log;

import com.craterzone.logginglib.manager.LoggerManager;

import org.wizbots.labtab.interfaces.BaseManagerInterface;
import org.wizbots.labtab.interfaces.OnLoadListener;
import org.wizbots.labtab.retrofit.WebService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LabTabApplication extends Application {

    private static String TAG = LabTabApplication.class.getName();
    private static LabTabApplication _instance;
    public WebService webService;
    private boolean closed;
    private ArrayList registeredManagers;
    private Map<Class<? extends BaseManagerInterface>, Collection<? extends BaseManagerInterface>> managerInterfaces;

    public LabTabApplication() {
        _instance = this;
        closed = false;
        registeredManagers = new ArrayList<>();
        managerInterfaces = new HashMap<>();
    }

    public static LabTabApplication getInstance() {
        if (_instance == null) {
            _instance = new LabTabApplication();
        }
        return _instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application onCreate()");
        //initManagers();
        //initDB();
        //loadManagers();
        //initRetrofit();
        LoggerManager.getInstance(getApplicationContext()).init();
    }

    private void initManagers() {
        TypedArray clienttables = getResources().obtainTypedArray(
                R.array.labtab_managers);
        for (int index = 0; index < clienttables.length(); index++) {
            try {
                Class.forName(clienttables.getString(index));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        clienttables.recycle();
    }

    private void initDB() {
        TypedArray clienttables = getResources().obtainTypedArray(
                R.array.labtabtables);
        for (int index = 0; index < clienttables.length(); index++) {
            try {
                Class.forName(clienttables.getString(index));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        clienttables.recycle();
    }

    public void addManager(Object manager) {
        registeredManagers.add(manager);
    }

    private void loadManagers() {
        for (OnLoadListener listener : getManagers(OnLoadListener.class)) {
            listener.onLoad();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseManagerInterface> Collection<T> getManagers(
            Class<T> cls) {
        if (closed)
            return Collections.emptyList();
        Collection<T> collection = (Collection<T>) managerInterfaces.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            for (Object manager : registeredManagers)
                if (cls.isInstance(manager))
                    collection.add((T) manager);
            collection = Collections.unmodifiableCollection(collection);
            managerInterfaces.put(cls, collection);
        }
        return collection;
    }

    public void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        webService = retrofit.create(WebService.class);
    }

    public WebService getWebService() {
        return webService;
    }
}
