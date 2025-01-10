package fps.cmn.service.member;

import fps.cmn.entity.member.Member;
import fps.cmn.mapper.member.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;

    public List<Member> getAllMembers() {
        return memberMapper.selectAllMembers();
    }
}
