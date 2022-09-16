package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private  final OrderRepository orderRepository;

    //엔티티 직접노출, 사용x
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1(){
        List<Order> findOrder = orderRepository.findAllByString(new OrderSearch());
        for (Order order : findOrder) {
            order.getDelivery().getAddress();
            order.getMember().getName();
            List<OrderItem> orderItems = order.getOrderItems();
            /*for (OrderItem orderItem : orderItems) {
                orderItem.getItem().getName();
            }*/
            orderItems.stream().forEach(o -> o.getItem().getName());
        }
        return findOrder;
    }


    @GetMapping("/api/v2/orders")
    public List<OrdersDto> ordersV2(){
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrdersDto> collect = orders.stream()
                .map(o -> new OrdersDto(o))
                .collect(Collectors.toList());
        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrdersDto> ordersV3() {
        List<Order> all = orderRepository.findAllWithItem();
        List<OrdersDto> result = all.stream()
                .map(o -> new OrdersDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrdersDto> ordersV3_page() {
        List<Order> all = orderRepository.findAllWithMemberDelivery(); //NToOne만 DTO로
        List<OrdersDto> result = all.stream()
                .map(o -> new OrdersDto(o))
                .collect(Collectors.toList());
        return result;
    }
    @Data
    static class OrdersDto{

        private  Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        private List<OrderItemDto> orderItems; //엔티티 반환 없애야함. 또 내부에 Dto 써야함

        public OrdersDto(Order order){
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto{

        private String itemName;//상품면
        private int orderPrice;//주문 가격
        private int count;// 주문 수량

        public OrderItemDto(OrderItem orderItem){
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();

        }
    }
}