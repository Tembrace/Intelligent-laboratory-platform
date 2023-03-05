package com.shundei.netty.store;


import java.util.List;

/**
 * 存储网关信息的接口
 *
 * @author huoyouri
 */
public interface SessionStoreService {
    /**
     * 存储会话信息
     *
     * @param id           网关id
     * @param sessionStore 存储的信息
     */
    void put(String id, SessionStore sessionStore);

    /**
     * 获得会话信息
     *
     * @param id 网关id
     * @return 会话信息
     */
    SessionStore get(String id);

    /**
     * 查看该会话信息是否存在
     *
     * @param id 网关id
     * @return true/false
     */
    boolean containsKey(String id);

    /**
     * 删除会话信息
     *
     * @param id 网关id
     */
    void remove(String id);

    /**
     * 获得所有的会话信息
     *
     * @return 所有的会话信息
     */
    List<SessionStore> getAllSession();
}
