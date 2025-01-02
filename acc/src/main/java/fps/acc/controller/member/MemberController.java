package fps.acc.controller.member;

import fps.acc.db.entity.member.AccMember;
import fps.acc.service.member.AccMemberService;
import fps.acc.service.member.CmnMemberProxyService;
import fps.cmn.db.entity.member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final AccMemberService accMemberService;
    private final CmnMemberProxyService cmnMemberProxyService;

    @Autowired
    public MemberController(AccMemberService accMemberService, CmnMemberProxyService cmnMemberProxyService) {
        this.accMemberService = accMemberService;
        this.cmnMemberProxyService = cmnMemberProxyService;
    }

    /**
     * ACC 멤버 조회
     */
    @GetMapping("/acc")
    public ResponseEntity<Map<String, Object>> getAllAccMembers() {
        List<AccMember> accMembers = accMemberService.getAllAccMembers();

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "ACC members fetched successfully");
        response.put("data", accMembers);

        return ResponseEntity.ok(response);
    }

    /**
     * CMN 멤버 조회
     */
    @GetMapping("/cmn")
    public ResponseEntity<Map<String, Object>> getAllCmnMembers() {
        List<Member> cmnMembers = cmnMemberProxyService.getAllMembersFromCMN();

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "CMN members fetched successfully");
        response.put("data", cmnMembers);

        return ResponseEntity.ok(response);
    }

    /**
     * ACC 및 CMN 멤버 통합 조회
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllMembers() {
        List<AccMember> accMembers = accMemberService.getAllAccMembers();
        List<Member> cmnMembers = cmnMemberProxyService.getAllMembersFromCMN();

        Map<String, Object> data = new HashMap<>();
        data.put("accMembers", accMembers);
        data.put("cmnMembers", cmnMembers);

        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("message", "ACC and CMN members fetched successfully");
        response.put("data", data);

        return ResponseEntity.ok(response);
    }
}
