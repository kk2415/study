package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("userA", "서울", "1", "11111");

            Book book1 = createBook("JPA1 BOOK", 10000, 100);
            Book book2 = createBook("JPA2 BOOK", 20000, 100);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);

            em.persist(member);
            em.persist(book1);
            em.persist(book2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("userB", "부산", "2", "22222");

            Book book1 = createBook("SPRING1 BOOK", 10000, 100);
            Book book2 = createBook("SPRING2 BOOK", 20000, 100);
            Book book3 = createBook("SPRING3 BOOK", 20000, 100);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
            OrderItem orderItem3 = OrderItem.createOrderItem(book3, 20000, 3);

            Delivery delivery = createDelivery(member);

            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            Order order2 = Order.createOrder(member, delivery, orderItem3);

            em.persist(member);
            em.persist(book1);
            em.persist(book2);
            em.persist(book3);
            em.persist(order);
            em.persist(order2);
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }
    }
}
