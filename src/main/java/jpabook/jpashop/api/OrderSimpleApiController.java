package jpabook.jpashop.api;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderQuerySimpleDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * X-To-One(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    //엔티티 노출이 됨
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //LAZY 강제 초기화
            order.getDelivery().getAddress(); //LAZY 강제 초기화
        }
        return all;

    }

    //DTO를 통한 엔티티 노출을 없앴지만 N+1문제가 나옴
    @GetMapping("/api/v2/simple-orders")
    public ResultOrder ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return new ResultOrder(collect);
    }

    //fatch join을 통한 N+1문제 해결, select문에 엄청 찍히는 단점 존재
    @GetMapping("/api/v3/simple-orders")
    public ResultOrder<SimpleOrderDto> ordersV3(){
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> collect = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());
        return new ResultOrder(collect);

    }

    //v3보다 쿼리가 짧다(성능 최적화). 단,너무 fit해서 다른 곳에서 활용 못하고 오직 이 기능만 실시
    @GetMapping("/api/v4/simple-orders")
    public ResultOrder<OrderQuerySimpleDto> ordersV4() {

        List<OrderQuerySimpleDto> orders = orderRepository.findOrderDtos();
        List<OrderQuerySimpleDto> collect = orders.stream()
                .map(o -> new OrderQuerySimpleDto(o.getOrderId(), o.getName(), o.getOrderDate(), o.getOrderStatus(), o.getAddress()))
                .collect(Collectors.toList());
        return new ResultOrder(collect);

        //return orderRepository.findOrderDtos();
    }
    @Data
    @AllArgsConstructor
    static class ResultOrder<T> {
        private T data;
    }

    @Data
    public class SimpleOrderDto{
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
