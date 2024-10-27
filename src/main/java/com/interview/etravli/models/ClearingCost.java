package com.interview.etravli.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="clearing_cost")
@Getter
@Setter
@Audited
public class ClearingCost extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name="card_issuing_country", length=2, nullable=false, unique=true)
    private String cardIssuingCountry;

    @Column(name = "clearing_cost_value", precision = 15, scale = 3, nullable = false)
    private BigDecimal clearingCost;

}
