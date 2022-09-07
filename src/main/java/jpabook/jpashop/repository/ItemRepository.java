package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.item.Book;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Getter
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){//새 상품 등록
            em.persist(item);
        }
        else{//업데이트
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List findAll(){
        return em.createQuery("select i from Item i", Item.class)
                    .getResultList();
    }
}
