package com.sample.application.models.cache;

public class CacheInvalidationEvent {
    private Long ticketId;
    private String instanceVersion;

    public CacheInvalidationEvent(Long ticketId, String instanceVersion) {
        this.ticketId = ticketId;
        this.instanceVersion = instanceVersion;
    }

    // Getter và Setter
    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public String getInstanceVersion() {
        return instanceVersion;
    }

    public void setInstanceVersion(String instanceVersion) {
        this.instanceVersion = instanceVersion;
    }

    @Override
    public String toString() {
        return "CacheInvalidationEvent{" +
                "ticketId=" + ticketId +
                ", instanceVersion='" + instanceVersion + '\'' +
                '}';
    }
}