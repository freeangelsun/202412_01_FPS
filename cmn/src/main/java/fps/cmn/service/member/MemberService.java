package fps.cmn.service.member;

import fps.cmn.db.entity.member.Member;
import fps.cmn.db.mapper.member.MemberMapper;
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
