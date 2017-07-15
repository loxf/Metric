package org.loxf.metric.service.session;

import org.loxf.metric.dal.po.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by luohj on 2017/7/15.
 */
public class UserSessionManager {
    private static Logger logger = LoggerFactory.getLogger(UserSessionManager.class);
    private static UserSessionManager sessionManager;
    private static long effectTime = 30*60*1000;// 默认30分钟失效
    private Map<String, User> session = new HashMap();
    private Map<String, Long> effectSessionMap = new HashMap();
    public static UserSessionManager getSession(){
        if(sessionManager==null){
            synchronized (UserSessionManager.class){
                if(sessionManager==null){
                    sessionManager = new UserSessionManager();
                }
            }
        }
        return sessionManager;
    }
    private UserSessionManager(){
        // 开启一个守护进程
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    Iterator ite = effectSessionMap.keySet().iterator();
                    long now = new Date().getTime();
                    Map<String, User> newSession = new HashMap();
                    Map<String, Long> newEffMap = new HashMap();
                    List<String> rmUserList = new ArrayList<>();
                    while (ite.hasNext()){
                        String userName = ite.next().toString();
                        long effect = effectSessionMap.get(userName);
                        if(effect<=now){
                            // 复制一份
                            newSession.put(userName, session.get(userName));
                            newEffMap.put(userName, effectSessionMap.get(userName));
                        }
                    }
                    synchronized (session){
                        session = newSession;
                        effectSessionMap = newEffMap;
                        newSession = null;
                        newEffMap = null;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        logger.error("用户SESSION守护线程被打断", e);
                    }
                }
            }
        });
        t.start();
    }

    public User getUserSession(String userName) {
        return (User)session.get(userName);
    }

    public User rmUserSession(String userName) {
        return (User)session.remove(userName);
    }

    /**
     * @param user
     */
    public void setUserSession(User user) {
        session.put(user.getUserName(), user);
        updateUserSessionEffectTime(user.getUserName());
    }

    public void updateUserSessionEffectTime(String username){
        long effect = new Date().getTime() + effectTime;
        effectSessionMap.put(username, effect);
    }
}
