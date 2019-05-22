package com.yaa.trading.server.starter;

import com.yaa.trading.comm.ServerConfig;
import com.yaa.trading.server.handler.ServerHandler;
import org.springframework.stereotype.Component;
import org.tio.server.ServerGroupContext;
import org.tio.websocket.server.WsServerAioListener;
import org.tio.websocket.server.WsServerStarter;

import javax.annotation.PostConstruct;

@Component
public class SocketServer {

    private ServerGroupContext serverGroupContext;

    private WsServerStarter wsServerStarter;


    @PostConstruct
    public void starts() throws Exception {
        wsServerStarter = new WsServerStarter(ServerConfig.SERVER_PORT, ServerHandler.me);

        serverGroupContext = wsServerStarter.getServerGroupContext();
        serverGroupContext.setServerAioListener(new WsServerAioListener());

        //设置ip统计时间段
//        serverGroupContext.ipStats.addDurations(ServerConfig.IpStatDuration.IP_STAT_DURATIONS);
        //设置ip监控
//        serverGroupContext.setIpStatListener(ServerIpStatListener.me);
        //设置心跳超时时间
        serverGroupContext.setHeartbeatTimeout(ServerConfig.HEARTBEAT_TIMEOUT);

        ServerConfig.groupContext = getServerGroupContext();
        wsServerStarter.start();
    }



    public ServerGroupContext getServerGroupContext() {
        return serverGroupContext;
    }



}
