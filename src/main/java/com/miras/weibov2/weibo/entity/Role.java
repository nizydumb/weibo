package com.miras.weibov2.weibo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="roles")
@Data
public class Role extends BaseEntity {

    @Column(name="name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "roles")
    private List<User> users;



}
