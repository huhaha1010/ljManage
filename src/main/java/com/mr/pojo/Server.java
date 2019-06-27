package com.mr.pojo;

public class Server {
    private Integer serverId;

    private String serverName;

    private String serverIp;

    private Integer serverCompanyId;

    private Integer serverState;

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName == null ? null : serverName.trim();
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp == null ? null : serverIp.trim();
    }

    public Integer getServerCompanyId() {
        return serverCompanyId;
    }

    public void setServerCompanyId(Integer serverCompanyId) {
        this.serverCompanyId = serverCompanyId;
    }

    public Integer getServerState() {
        return serverState;
    }

    public void setServerState(Integer serverState) {
        this.serverState = serverState;
    }
}