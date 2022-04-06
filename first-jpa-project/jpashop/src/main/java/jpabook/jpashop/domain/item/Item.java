package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private Collection<OrderItem> orderItem;

    public Collection<OrderItem> getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(Collection<OrderItem> orderItem) {
        this.orderItem = orderItem;
    }

    //==비즈니스 로직==//

    /*
    * 재고 수량 증가
    * */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /*
     * 재고 수량 감소
     * */
    public void removeStock(int quantity) {
        int result = this.stockQuantity - quantity;
        if (result < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = result;
    }
}
