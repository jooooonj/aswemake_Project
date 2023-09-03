package aswemake.project.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberRoleType {
    MART_ADMIN("mart_admin"), MEMBER("member");

    private final String value;

    MemberRoleType(String value) {
        this.value = value;
    }
}
