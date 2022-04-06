package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;

    /**
     * Query: 루트 1번, 컬렉션 N 번 실행
     * ToOne(N:1, 1:1) 관계들을 먼저 조회하고, ToMany(1:N) 관계는 각각 별도로 처리한다.
     * 이런 방식을 선택한 이유는 다음과 같다.
     * ToOne 관계는 조인해도 데이터 row 수가 증가하지 않는다.
     * ToMany(1:N) 관계는 조인하면 row 수가 증가한다.
     * row 수가 증가하지 않는 ToOne 관계는 조인으로 최적화 하기 쉬우므로 한번에 조회하고, ToMany
     * 관계는 최적화 하기 어려우므로 findOrderItems() 같은 별도의 메서드로 조회한다.
     * */

    //V4: JPA에서 DTO 직접 조회
    public List<OrderQueryDto> findOrderQueryDto() {
        List<OrderQueryDto> result = findOrders();

        result.forEach(orderDto -> {
                    List<OrderItemQueryDto> orderItems = findOrderItems(orderDto.getOrderId());
                    orderDto.setOrderItems(orderItems);
                });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                        "from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select new jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                "from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderQueryDto.class).getResultList();
    }


    //V5: JPA에서 DTO 직접 조회 - 컬렉션 조회 최적화
    public List<OrderQueryDto> findAllByDto_optimization() {
        //일단 orderItem를 뺀 채 order DTO를 가져옴
        List<OrderQueryDto> result = findOrders();

        //orderId를 모아서 리스트로 만듬
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

        //SQL IN절에 orderIds를 넣어서 한꺼번에 조회
        List<OrderItemQueryDto> orderItems = em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count) " +
                                "from OrderItem oi" +
                                " join oi.item i" +
                                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        //Collectors.groupingBy()을 이용해서 orderId를 key로, List<OrderItemQueryDto> value로 Map 컬렉션을 만듬
        Map<Long, List<OrderItemQueryDto>> orderItemMap =
                orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto -> OrderItemQueryDto.getOrderId()));

        //forEach()문 돌면서 setOrderItems()로 각 오더 DTO에 OrderItems 넣어줌줌
        result.forEach(orderDto -> orderDto.setOrderItems(orderItemMap.get(orderDto.getOrderId())));
        return result;
    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
            "select new" +
                    " jpabook.jpashop.repository.order.query.OrderFlatDto" +
                    "(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                    "from Order o" +
                    " join o.member m" +
                    " join o.delivery d" +
                    " join o.orderItems oi" +
                    " join oi.item i", OrderFlatDto.class
                ).getResultList();
    }
}
