package com.messagerie.dto;

import java.util.List;

public class ChannelDTO {
    private Long id;
    private String name;
    private Boolean isPrivate;
    private List<Long> memberIds;

    public ChannelDTO() {}

    public ChannelDTO(Long id, String name, Boolean isPrivate, List<Long> memberIds) {
        this.id = id;
        this.name = name;
        this.isPrivate = isPrivate;
        this.memberIds = memberIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public List<Long> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Long> memberIds) {
        this.memberIds = memberIds;
    }
}
