package com.example.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "device_tokens")
@Getter
@Setter
public class DeviceTokens {
    @Id
    @Column(name = "token")
    private String token;

    @Column(name = "subscribe")
    private Integer subscribe;
}
