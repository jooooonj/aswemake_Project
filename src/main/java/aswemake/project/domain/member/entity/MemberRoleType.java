package aswemake.project.domain.member.entity;

import lombok.Getter;

@Getter
public enum MemberRoleType {
    MART_ADMIN("MART"), MEMBER("MEMBER");

    private final String value;

    MemberRoleType(String value) {
        this.value = value;
    }
}
