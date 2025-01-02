package fps.cmn.db.mapper.member;

import fps.cmn.db.entity.member.Member;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface MemberMapper {
    List<Member> selectAllMembers();
}