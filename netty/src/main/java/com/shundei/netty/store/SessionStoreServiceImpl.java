package com.shundei.netty.store;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储网关信息的实现类
 *
 * @author huoyouri
 */
@Service
public class SessionStoreServiceImpl implements SessionStoreService {

    private Map<String, SessionStore> cache = new ConcurrentHashMap<>();

    @Override
    public void put(String id, SessionStore sessionStore) {
        if ("".equals(id) || id == null || sessionStore == null) {
            return;
        }
        cache.put(id, sessionStore);
    }

    @Override
    public SessionStore get(String id) {
        if ("".equals(id) || id == null) {
            return null;
        }
        return cache.get(id);
    }

    @Override
    public boolean containsKey(String id) {
        if ("".equals(id) || id == null) {
            return false;
        }
        return cache.containsKey(id);
    }

    @Override
    public void remove(String id) {
        if ("".equals(id) || id == null) {
            return;
        }
        cache.remove(id);
    }

    @Override
    public List<SessionStore> getAllSession() {
        return (List<SessionStore>) cache.values();
    }
}
