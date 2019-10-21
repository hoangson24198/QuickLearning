package com.dtm.quicklearning.model.token;

import com.dtm.quicklearning.model.eNum.TokenStatus;
import com.dtm.quicklearning.model.entity.User;
import com.dtm.quicklearning.utils.DateAudit;
import lombok.Data;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@Table(name = "email_verification_token")
public class EmailVerificationToken extends DateAudit {

    @Id
    @Column(name = "token_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_token_seq")
    @SequenceGenerator(name = "email_token_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(name = "token_status")
    @Enumerated(EnumType.STRING)
    private TokenStatus tokenStatus;

    @Column(name = "expiry_date", nullable = false)
    private Instant expiryDate;

    public void setConfirmedStatus() {
        setTokenStatus(TokenStatus.STATUS_CONFIRMED);
    }
}
