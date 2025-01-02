package fps.acc.db.mapper.member;

import fps.acc.db.entity.member.AccMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccMemberMapper {
    List<AccMember> selectAllMembers();
}
