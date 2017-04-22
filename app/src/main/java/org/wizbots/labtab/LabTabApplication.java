package org.wizbots.labtab;

import android.app.Application;
import android.content.Context;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import com.craterzone.logginglib.manager.LoggerManager;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.interfaces.BaseManagerInterface;
import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.interfaces.OnLoadListener;
import org.wizbots.labtab.model.metadata.MetaData;
import org.wizbots.labtab.model.program.Student;
import org.wizbots.labtab.pushnotification.NotiManager;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LabTabApplication extends Application {

    private static final String TAG = LabTabApplication.class.getName();
    private static LabTabApplication _instance;
    private LabTabApiInterface labTabApiInterface;
    private boolean closed;
    private ArrayList registeredManagers;
    private Map<Class<? extends BaseManagerInterface>, Collection<? extends BaseManagerInterface>> managerInterfaces;
    private Map<Class<? extends BaseUIListener>, Collection<? extends BaseUIListener>> uiListeners;
    private MetaData[] metaDatas;
    private final Handler handler;


    public LabTabApplication() {
        _instance = this;
        closed = false;
        registeredManagers = new ArrayList<>();
        managerInterfaces = new HashMap<>();
        uiListeners = new HashMap<>();
        handler = new Handler();
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
        initManagers();
        initDB();
        loadManagers();
        initRetrofit();
        LoggerManager.getInstance(getApplicationContext()).init();
        metaDatas =  LabTabPreferences.getInstance(LabTabApplication.getInstance()).getProjectsMetaData();
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
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(7, TimeUnit.MINUTES)
                .writeTimeout(7, TimeUnit.MINUTES)
                .retryOnConnectionFailure(true)

                .readTimeout(7, TimeUnit.MINUTES);
        OkHttpClient client = builder.build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(LabTabApiInterface.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        labTabApiInterface = retrofit.create(LabTabApiInterface.class);
    }

    public LabTabApiInterface getLabTabApiInterface() {
        return labTabApiInterface;
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseUIListener> Collection<T> getOrCreateUIListeners(
            Class<T> cls) {
        Collection<T> collection = (Collection<T>) uiListeners.get(cls);
        if (collection == null) {
            collection = new ArrayList<T>();
            uiListeners.put(cls, collection);
        }
        return collection;
    }

    public <T extends BaseUIListener> Collection<T> getUIListeners(Class<T> cls) {
        if (closed)
            return Collections.emptyList();
        return Collections.unmodifiableCollection(getOrCreateUIListeners(cls));
    }

    public <T extends BaseUIListener> void addUIListener(Class<T> cls,
                                                         T listener) {
        getOrCreateUIListeners(cls).add(listener);
    }

    public <T extends BaseUIListener> void removeUIListener(Class<T> cls,
                                                            T listener) {
        getOrCreateUIListeners(cls).remove(listener);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void setMetaDatas(MetaData[] metaDatas) {
        this.metaDatas = metaDatas;
    }

    public MetaData[] getMetaDatas() {
        return metaDatas;
    }

    public String[] getKnowledgeNuggets(String labLevel) {
/*        if (labLevel.toUpperCase().equals(LabTabConstants.LabLevels.NOVICE)) {
            labLevel = LabTabConstants.LabLevels.LAB_CERTIFIED;
        }*/
        String[] knowledgeNuggets = null;
        ArrayList<String> kn = new ArrayList<>();
        if (metaDatas == null) {
            return null;
        } else {
            for (int i = 0; i < metaDatas.length; i++) {
//                if (labLevel.toUpperCase().equals(metaDatas[i].getName().toUpperCase())) {
                kn.addAll(Arrays.asList(metaDatas[i].getNuggets()));
/*                    break;
                }*/
            }
            if(!kn.isEmpty()){
                knowledgeNuggets = kn.toArray(new String[kn.size()]);
            }
        }
        return knowledgeNuggets;
    }

    public String[] getKnowledgeNuggetsByStudent(ArrayList<Student> studentList) {
        String[] knowledgeNuggets = null;
        Set<String> kn = new HashSet<>();
        ArrayList<String> knStudent = new ArrayList<>();
        if (metaDatas == null || studentList == null || studentList.isEmpty()) {
            return null;
        } else {
            for (int i = 0; i < metaDatas.length; i++) {
                for (int j = 0; j < studentList.size(); j++) {
//                    if (metaDatas[i].getName().toUpperCase().equalsIgnoreCase(studentList.get(j).getLevel())){
                        kn.addAll(Arrays.asList(metaDatas[i].getNuggets()));
//                    }
                }
            }
            if(!kn.isEmpty()){
                knowledgeNuggets = kn.toArray(new String[kn.size()]);
            }
        }
        return knowledgeNuggets;
    }

    public void runOnUiThread(final Runnable runnable) {
        handler.post(runnable);
    }
}
