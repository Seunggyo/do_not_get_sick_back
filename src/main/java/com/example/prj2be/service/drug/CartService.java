package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Cart;
import com.example.prj2be.mapper.drug.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper mapper;

    public void update(Cart cart){
        // 처음 좋아요 누를 때 : insert
        // 다시 누르면 delete

        int count = 0;
        if (mapper.delete(cart) == 0) {
            count = mapper.insert(cart);
        }
    }
}







