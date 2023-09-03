package aswemake.project.domain.member.entity;

import aswemake.project.base.baseEntity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role_type")
    @Enumerated(EnumType.STRING)
    private MemberRoleType roleType;

    public Map<String, Object> toClaims() {
        return Map.of(
                "id", getId(),
                "email", getEmail()
        );
    }
}
