package fps.acc.mapper.member;

import fps.acc.entity.member.AccMember;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccMemberMapper {
    List<AccMember> selectAllMembers();
}
