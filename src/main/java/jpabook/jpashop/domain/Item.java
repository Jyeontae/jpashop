package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    //== 비즈니스 로직 ==//
    // 엔티티 내 data를 사용하는 메소드를작성할 경우에는 엔티티 내부에서 만드는것이 좋음.
    // 특히 변경이 있는 경우는 setter를 쓰지 않고 비즈니스 로직을 만드는것이 나음.
    /**
     *
     * 상품 재고(stock)증가
     */
    public void addStock(int quantity){
        this.stockQuantity += quantity;
    }

    /**
     * 상품 재고(stock) 감소
     */
    public void removeStock(int quatity){
        int restStock = this.stockQuantity -= quatity;
        if(restStock < 0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

}
