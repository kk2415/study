package study.datajpa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.repository.MemberRepository;

import javax.annotation.PostConstruct;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members")
    public Page<MemberDto> getMembers(Pageable pageable) {
        //http://localhost:8080/members?page=1&size=5&sort=id,desc

        Page<Member> page = memberRepository.findAll(pageable);

        return page.map(MemberDto::new);
        /*
        * {
            "content": [
                {
                    "id": 95,
                    "age": 94,
                    "username": "member94"
                },
                {
                    "id": 94,
                    "age": 93,
                    "username": "member93"
                },
                {
                    "id": 93,
                    "age": 92,
                    "username": "member92"
                },
                {
                    "id": 92,
                    "age": 91,
                    "username": "member91"
                },
                {
                    "id": 91,
                    "age": 90,
                    "username": "member90"
                }
            ],
            "pageable": {
                "sort": {
                    "empty": false,
                    "sorted": true,
                    "unsorted": false
                },
                "offset": 5,
                "pageSize": 5,
                "pageNumber": 1,
                "paged": true,
                "unpaged": false
            },
            "last": false,
            "totalElements": 100,
            "totalPages": 20,
            "size": 5,
            "number": 1,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "first": false,
            "numberOfElements": 5,
            "empty": false
        * */
    }

    @PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("member" + i, (long) i));
        }
    }

}
