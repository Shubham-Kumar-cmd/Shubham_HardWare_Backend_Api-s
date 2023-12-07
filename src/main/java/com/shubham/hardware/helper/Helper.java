package com.shubham.hardware.helper;

import com.shubham.hardware.dtos.PageableResponse;
import com.shubham.hardware.dtos.UserDto;
import com.shubham.hardware.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;

public class Helper {

//    <U,V> are <Entity,Dto>,<User,UserDto>
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page,Class<V> type){
        List<U> entity = page.getContent();
//        for User only
//        List<UserDto> dtoList = userEntity.stream().map(user -> new ModelMapper().map(user, UserDto.class)).collect(Collectors.toList());

//        Generalization
        List<V> dtoList = entity.stream().map(object->new ModelMapper().map(object, type)).toList();

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(dtoList);
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLastPage(page.isLast());
        return response;
    }
}
