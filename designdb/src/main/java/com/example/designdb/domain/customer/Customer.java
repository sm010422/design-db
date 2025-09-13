package com.example.designdb.domain.customer;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "customer", indexes = {
        @Index(name = "ix_customer_email", columnList = "email")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Customer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 주민번호(실습용) – 실무는 암호화/마스킹 권장 */
    @Column(nullable = false, unique = true, length = 20)
    private String rrn;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String address;

    private LocalDate birthDate;

    @Column(length = 120, unique = true)
    private String email;

    @Column(length = 25)
    private String phone;

    @Column(length = 100)
    private String job;
}
