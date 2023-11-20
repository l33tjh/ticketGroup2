package com.group.ticket.DAO;

import com.group.ticket.domain.entity.MemberEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {
    private Long Mno;
    @NotBlank(message = "비밀번호가 비어있습니다.")
    @Size(min = 1, max = 30, message = "비밀번호는 1 ~ 30자 이여야 합니다!")
    private String MemberPassword;
    private String MemberPasswordCheck;
    @NotBlank(message = "이름이 비어있습니다.")
    @Size(min = 1, max = 10, message = "이름은 1 ~ 10자 이여야 합니다!")
    private String MemberName;
    @NotBlank(message = "이메일이 비어있습니다.")
    @Size(min = 1, max = 50, message = "이메일은 1 ~ 50자 이여야 합니다!")
    @Email
    private String MemberEmail;

    public MemberEntity toMemberEntity(String encodedPassword) {
        return MemberEntity.builder()
                .memberEmail(this.MemberEmail)
                .memberPassword(encodedPassword)
                .memberName(this.MemberName)
                .role(MemberEntity.UserRole.USER)
                .build();
    }

}