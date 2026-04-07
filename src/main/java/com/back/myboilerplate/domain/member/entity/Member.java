package com.back.myboilerplate.domain.member.entity;


import com.back.myboilerplate.global.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SuperBuilder
@Table(name = "members")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public void updateMember(String nickName) {
        this.nickName = nickName;
    }
}

