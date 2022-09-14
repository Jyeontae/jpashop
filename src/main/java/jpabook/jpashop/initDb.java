package jpabook.jpashop;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문 2개
 * * userA
 * ** JPA1 BOOK
 * ** JPA2 BOOK
 * * userB
 * ** SPRING1 BOOK
 * ** SPRING2 BOOK
 */
@Component
@RequiredArgsConstructor
public class initDb {

    private final  InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbinit2();
    }
    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;
        public void dbInit1(){
            Member member = Member.createMember("userA", "서울", "1", "1111");
            em.persist(member);

            Book book1 = Book.createBook("JPA1 BOOK", 10000, 100, "김영한", "123");
            Book book2 = Book.createBook("JPA2 BOOK", 20000, 100, "김영한", "1234");
            em.persist(book1);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        public void dbinit2(){
            Member member = Member.createMember("userB", "진주", "2", "2222");
            em.persist(member);

            Book book1 = Book.createBook("SPRING1 BOOK", 30000, 100, "김영한", "321");
            Book book2 = Book.createBook("SPRING2 BOOK", 40000, 100, "김영한", "4321");
            em.persist(book1);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 30000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }
    }
}
