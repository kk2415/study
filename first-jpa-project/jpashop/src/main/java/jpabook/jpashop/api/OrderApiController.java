package jpabook.jpashop.api;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    //V1: 엔티티를 직접 노출
    @GetMapping("/api/v1/orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            /**
             * order.getMember()
             * 여기까지 호출하면 LAZY 로딩이기 때문에 DB에 접근하지 않고 ProxyMember에게 접근함
             * ProxyMember를 접근해서 그걸 JASON으로 바꾸면 에러가 남(LazyInitializationException).
             * 그래서 프록시로 접근하면 안됨! 하지만 order.getMember().getName();
             * 멤버 엔티티의 getName()까지 호출하면 실제 name을 끌고 와야되기 때문에 DB에 접근해서 가져옴
             * */
            order.getMember().getName(); //LAZY 강제 초기화
            order.getDelivery().getAddress(); //LAZY 강제 초기화

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName()); //LAZY 강제 초기화
        }
        return all;
    }

    //V2: 엔티티를 DTO로 변환
    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return collect;
    }

    //V3: 엔티티를 DTO로 변환 - 페치 조인 최적화
    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return collect;
    }

    //V3.1: 엔티티를 DTO로 변환 - 페이징과 한계 돌파
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> orderV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {
        /**
         * 1. 먼저 ToOne(OneToOne, ManyToOne) 관계를 모두 페치조인 한다. ToOne 관계는 row수를
         *    증가시키지 않으므로 페이징 쿼리에 영향을 주지 않는다.
         * 2. 컬렉션은 지연 로딩으로 조회한다.
         * 3. 지연 로딩 성능 최적화를 위해 hibernate.default_batch_fetch_size , @BatchSize 를 적용한다.
         *     - hibernate.default_batch_fetch_size: 글로벌 설정
         *     - @BatchSize: 개별 최적화
         *     - 이 옵션을 사용하면 컬렉션이나, 프록시 객체를 한꺼번에 설정한 size 만큼 IN 쿼리로 조회한다.
         * */

        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());

        return collect;
    }

    //V4: JPA에서 DTO 직접 조회
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDto();
    }

    //V5: JPA에서 DTO 직접 조회 - 컬렉션 조회 최적화
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        /**
         * Query: 루트 1번, 컬렉션 1번
         * ToOne 관계들을 먼저 조회하고, 여기서 얻은 식별자 orderId로 ToMany 관계인 OrderItem 을
         * 한꺼번에 조회
         * MAP을 사용해서 매칭 성능 향상(O(1))
         * */
        return orderQueryRepository.findAllByDto_optimization();
    }

    //V6: JPA에서 DTO로 직접 조회, 플랫 데이터 최적화(쿼리 1번으로 다 조회)
    @GetMapping("/api/v6/orders")
    public List<OrderQueryDto> ordersV6() {
        /**
         * Query: 1번
         * 단점
         *  쿼리는 한번이지만 조인으로 인해 DB에서 애플리케이션에 전달하는 데이터에 중복 데이터가
         *  추가되므로 상황에 따라 V5 보다 더 느릴 수 도 있다.
         *  애플리케이션에서 추가 작업이 크다.페이징 불가능
         * */
        List<OrderFlatDto> flats = orderQueryRepository.findAllByDto_flat();

        return flats.stream()
                .collect(groupingBy(o -> new OrderQueryDto(o.getOrderId(),
                o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()),
                        mapping(o -> new OrderItemQueryDto(
                        o.getOrderId(), o.getItemName(), o.getOrderPrice(), o.getCount()), toList())))
                .entrySet().stream()
                .map(e -> new OrderQueryDto(e.getKey().getOrderId(), e.getKey().getName(), e.getKey().getOrderDate(), e.getKey().getOrderStatus(),e.getKey().getAddress(), e.getValue())).collect(toList());
    }

    @Data
    static class OrderDto {
        private Long id;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            this.id = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
            this.orderItems = order.getOrderItems().stream()
                    .map(o -> new OrderItemDto(o))
                    .collect(toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;

        public OrderItemDto(OrderItem orderItem) {
            this.itemName = orderItem.getItem().getName();
            this.orderPrice = orderItem.getOrderPrice();
            this.count = orderItem.getCount();
        }
    }
}
