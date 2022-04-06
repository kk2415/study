package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;


    /**
    * 준영속 엔티티를 업데이트하는 방법 2가지
     * 1. 변경 감지 이용
     *   - 준영속 엔티티의 id를 참조헤서 영속성 엔티티를 불러와서
     *     그 영속성 엔티티에서 변경 감지를 이용한다.
     * 2. em.merge() 이용
     *   - merge()를 이용하면 JPA가 자동으로 아래 updateItem(Long itemId, Book bookForm)
     *     메서드처럼 setter를 이용해서 변경 감지를 다 알아서 해준다
    * */
    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item);
        }
    }

    /*public Item updateItem(Long itemId, Book bookForm) {
        Item findItem = findOne(itemId);
        findItem.setName(bookForm.getName());
        findItem.setPrice(bookForm.getPrice());
        findItem.setStockQuantity(bookForm.getStockQuantity());

        return findItem;
    }*/


    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

}
