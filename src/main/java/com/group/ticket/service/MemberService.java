package com.group.ticket.service;

import com.group.ticket.DAO.ChangePasswordRequest;
import com.group.ticket.DAO.JoinRequest;
import com.group.ticket.DAO.LoginRequest;
import com.group.ticket.domain.entity.MemberEntity;
import com.group.ticket.domain.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Autowired
    public MemberRepository memberRepository;



    /**
     * 회원가입
     */
//    public String save (MemberDTO memberDTO) {
//        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
//        Optional<MemberEntity> existingMember = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
//        if (existingMember.isPresent()) {
//            return "이미 존재하는 회원입니다.";
//        }
//        memberRepository.save(memberEntity);
//        return null;
//    }
    /**
     * Email 중복 체크
     * 회원가입 기능 구현 시 사용
     * 중복되면 true return
     */
    public boolean checkMemberEmailDuplicate(String MemberEmail) {
        return memberRepository.existsByMemberEmail(MemberEmail);
    }

    /**
     * 회원가입 기능
     * 화면에서 JoinRequest(loginId, password, nickname)을 입력받아 User로 변환 후 저장
     * 비밀번호를 암호화해서 저장 req.toMemberEntity(encoder.encode(req.getMemberPassword()));
     * loginId, nickname 중복 체크는 Controller에서 진행 => 에러 메세지 출력을 위해
     */
    public void join(JoinRequest req) {
        MemberEntity memberEntity = req.toMemberEntity(req.getMemberPassword());
        memberRepository.save(memberEntity);
    }
    /**
     *  로그인 기능`
     *  화면에서 LoginRequest(loginId, password)을 입력받아 loginId와 password가 일치하면 memberEntity return
     *  loginId가 존재하지 않거나 password가 일치하지 않으면 null return
     */
    public MemberEntity login(LoginRequest req){
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(req.getMemberEmail());
        // Email과 일치하는 User가 없으면 null return

        if(optionalMemberEntity.isEmpty()){
            return null;
        }
        MemberEntity memberEntity = optionalMemberEntity.get();
        // 찾아온 Member의 password와 입력된 password가 다르면 null return
        if(!memberEntity.getMemberPassword().equals(req.getMemberPassword())){
            return null;
        };
        return memberEntity;
    }
    /**
     * userId(Long)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * userId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * userId로 찾아온 User가 존재하면 User return
     */
    public MemberEntity getLoginUserByMno(Long Mno) {
        Optional<MemberEntity> optionalUser = memberRepository.findByMno(Mno);
        if (optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
    public String getEmailByMno(Long Mno) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMno(Mno);
        if (optionalMemberEntity.isEmpty()) return null;

        return optionalMemberEntity.get().getMemberEmail();
    }
    /**
     * loginId(String)를 입력받아 User을 return 해주는 기능
     * 인증, 인가 시 사용
     * loginId가 null이거나(로그인 X) userId로 찾아온 User가 없으면 null return
     * loginId로 찾아온 User가 존재하면 User return
     */
    public MemberEntity getLoginUserByLoginId(String MemberEmail) {
        if(MemberEmail == null) return null;

        Optional<MemberEntity> optionalUser = memberRepository.findByMemberEmail(MemberEmail);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
    /**
     * 비밀번호 변경 기능
     * 현재 이메일에 맞는 비밀번호인지 확인
     * 비밀번호와 비밀번호 확인이 같은지 확인
     */
    public boolean changePassword(ChangePasswordRequest req,MemberEntity memberEntity){

        // 찾아온 Member의 password와 입력된 현재 password가 일치하는지 확인
        if(memberEntity.getMemberPassword().equals(req.getCurrentPassword())){
            // 새 비밀번호와 비밀번호 확인이 같은지 확인
            if(req.getMemberPassword().equals(req.getMemberPasswordCheck())) {
                // 새 비밀번호로 업데이트
                memberEntity.setMemberPassword(req.getMemberPassword());
                // 변경된 정보를 데이터베이스에 저장
                memberRepository.save(memberEntity);
                return true; // 변경된 MemberEntity 반환
            }
        }
        return false; // 조건에 부합하지 않을 경우 null 반환
    }
}