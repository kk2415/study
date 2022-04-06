package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 쿼리 방식 선택 권장 순서
 * 1. 우선 엔티티를 DTO로 변환하는 방법을 선택한다.
 * 2. 필요하면 페치 조인으로 성능을 최적화 한다. 대부분의 성능 이슈가 해결된다.
 * 3. 그래도 안되면 DTO로 직접 조회하는 방법을 사용한다.
 * 4. 최후의 방법은 JPA가 제공하는 네이티브 SQL이나 스프링 JDBC Template을 사용해서 SQL을 직접
 * 사용한다.
 * */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    //V1: 엔티티를 직접 노출
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            /**
             * order.getMember()
             * 여기까지 호출하면 LAZY 로딩이기 때문에 DB에 접근하지 않고 ProxyMember에게 접근함
             * 하지만 order.getMember().getName();
             * 멤버 엔티티의 getName()까지 호출하면 실제 name을 끌고 와야되기 때문에 DB에 접근해서 가져옴
             * */
            order.getMember().getName(); //LAZY 강제 초기화
            order.getDelivery().getAddress(); //LAZY 강제 초기화
        }
        return all;
    }

    //V2: 엔티티를 DTO로 변환
    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleQueryDto> ordersV2() {
        //ORDER 2개
        //N + 1 -> 1 + 회원 N  1 + 배송 N
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        List<OrderSimpleQueryDto> collect = all.stream().map(o -> new OrderSimpleQueryDto(o)).collect(Collectors.toList());
        return collect;
    }

    //V3: 엔티티를 DTO로 변환 - 페치 조인 최적화
    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleQueryDto> ordersV3() {
        /**
         * 모든 연관관계는 LAZY로 설정하고
         * 필요한건만 fetch 조인으로 객체 그래프로 딱 묶어서 DB에서 한 방에 가져오면
         * 대부분의 성능 문제가 해결된다.
         * */
        List<Order> orderList = orderRepository.findAllWithMemberDelivery(); //fetch 조인
        List<OrderSimpleQueryDto> collect = orderList.stream().map(o -> new OrderSimpleQueryDto(o)).collect(Collectors.toList());

        return collect;
    }

    //V4: JPA에서 DTO로 바로 조회
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        /**
         * 일반적인 SQL을 사용할 때 처럼 원하는 값을 선택해서 조회
         * new 명령어를 사용해서 JPQL의 결과를 DTO로 즉시 변환
         * SELECT 절에서 원하는 데이터를 직접 선택하므로 DB 애플리케이션 네트웍 용량 최적화(생각보다 미비)
         * 리포지토리 재사용성 떨어짐, API 스펙에 맞춘 코드가 리포지토리에 들어가는 단점
         * */
        return orderSimpleQueryRepository.findOrderDtos();
    }
}
