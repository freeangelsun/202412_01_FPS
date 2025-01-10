package fps.acc.service.member;

import fps.acc.entity.member.AccMember;
import fps.acc.mapper.member.AccMemberMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccMemberService {

    private final AccMemberMapper accMemberMapper;

    public AccMemberService(AccMemberMapper accMemberMapper) {
        this.accMemberMapper = accMemberMapper;
    }

    public List<AccMember> getAllAccMembers() {
        return accMemberMapper.selectAllMembers();
    }
}
