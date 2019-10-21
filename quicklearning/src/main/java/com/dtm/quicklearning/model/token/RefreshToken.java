package com.dtm.quicklearning.model.token;

import com.dtm.quicklearning.utils.DateAudit;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.NegativeOrZero;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@Table(name = "refresh_token")
public class RefreshToken extends DateAudit {
    @Id
    @Column(name = "TOKEN_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_seq")
    @SequenceGenerator(name = "refresh_token_seq", allocationSize = 1)
    private Integer id;

    @Column(name = "TOKEN", nullable = false, unique = true)
    @NaturalId(mutable = true)
    private String token;

    @Column(name = "REFRESH_COUNT")
    private Long refreshCount;

    @Column(name = "EXPIRY_DT", nullable = false)
    private Instant expiryDate;
}
