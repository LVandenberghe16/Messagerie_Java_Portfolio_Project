package com.messagerie.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;


@Entity
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Boolean isPrivate;

    @ManyToMany
    @JoinTable(
        name = "channel_members",
        joinColumns = @JoinColumn(name = "channels_id"),
        inverseJoinColumns = @JoinColumn(name = "members_id")
    )
    @JsonManagedReference 
    private Set<User> members = new HashSet<>();

    public Channel() {
        // Default constructor for JPA
    }

    public Channel(String name, Boolean isPrivate) {
        this.name = name;
        this.isPrivate = isPrivate;
    }

    public Long getId() {
        return id;
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

    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public void addMember(User user) {
        this.members.add(user);
    }
}
