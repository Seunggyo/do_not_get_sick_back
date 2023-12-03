package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Cart;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.drug.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper mapper;

    public Map<String, Object> update(Cart cart, Member login){

        cart.setMemberId(login.getId());
        // 처음 좋아요 누를 때 : insert
        // 다시 누르면 delete

        int count = 0;
        if (mapper.delete(cart) == 0) {
            count = mapper.insert(cart);
        }

        int countCart = mapper.countByDrugId(cart.getDrugId());

        return Map.of("cart", count == 1,
                "countCart", countCart);
    }

    public Map<String, Object> get(Integer drugId, Member login) {
        int countCart = mapper.countByDrugId(drugId);

        Cart cart = null;
        if (login != null){
            cart = mapper.selectByDrugIdAndMemberId(drugId,login.getId());
        }

        return Map.of("cart", cart != null, "countCart", countCart);
    }
}







