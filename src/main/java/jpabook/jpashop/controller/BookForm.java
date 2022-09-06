package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    //item 공통 속성들
    private Long id;

    private String name;
    private int price;
    private int stockQuatity;

    //book의 고유 속성들
    private String author;
    private String isbn;
}
