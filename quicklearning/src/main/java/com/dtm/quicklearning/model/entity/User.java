package com.dtm.quicklearning.model.entity;

import com.dtm.quicklearning.model.token.RefreshToken;
import com.dtm.quicklearning.validation.NullOrNotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Integer userId;

    @NaturalId
    @Email
    @Column(name = "email")
    @NullOrNotBlank(message = "Username can not be blank")
    private String email;

    @NotBlank
    @Column(name = "password")
    private String passWord;

    @NotBlank
    @Column(name = "full_name")
    private String fullName;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_authority", joinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "role_id", referencedColumnName = "role_id")})
    private Set<Role> roles = new HashSet<>();

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @Column(name = "is_email_verified", nullable = false)
    private Boolean isEmailVerified;

    @Column(name = "notification_token")
    private String notificationToken;

    @OneToOne
    private RefreshToken refreshToken;

    @Column(name = "is_refresh_active")
    private Boolean isRefreshActive;

    public User(@NotBlank String name, @NotBlank @Email String email, @NotBlank String password) {
        this.fullName = name;
        this.email = email;
        this.passWord = password;
    }

    public void markVerificationConfirmed() {
        setIsEmailVerified(true);
    }

    public User(User user) {
        this.email = user.getEmail();
        this.passWord = user.getPassWord();
        this.fullName = user.getFullName();
        this.roles = user.getRoles();
        this.active = user.getActive();
        this.isEmailVerified = user.isEmailVerified;
    }

    public void addRole(Role role) {
        roles.add(role);
        role.getUserList().add(this);
    }

    public void addRoles(Set<Role> roles) {
        roles.forEach(this::addRole);
    }
}
