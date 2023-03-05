package com.shundei.netty.store;


import io.netty.channel.Channel;

/**
 * 存储网关信息的类,后续存其他信息可扩展
 *
 * @author huoyouri
 */
public class SessionStore {
    private String id;
    private Channel channel;

    public SessionStore() {
    }

    public SessionStore(String id, Channel channel) {
        this.id = id;
        this.channel = channel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "SessionStore{" +
                "id='" + id + '\'' +
                ", channel=" + channel +
                '}';
    }
}
