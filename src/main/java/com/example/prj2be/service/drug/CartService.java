package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Cart;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.drug.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartMapper mapper;

    public Map<String, Object> update(Cart cart, Member login){

        Cart dbcart = mapper.selectByDrugIdAndMemberId(cart.getDrugId(), login.getId());

        if (dbcart == null) {
            Cart newCart = new Cart();
            newCart.setDrugId(cart.getDrugId());
            newCart.setMemberId(login.getId());
            newCart.setQuantity(cart.getQuantity());
            mapper.insert(newCart);
        } else {
            dbcart.setQuantity(cart.getQuantity());
            mapper.updateIncreaseQuantity(dbcart);
        }
        return Map.of();
    }

    public Map<String, Object> get(Integer drugId, Member login) {
        int countCart = mapper.countByDrugId(drugId);

        Cart cart = null;
        if (login != null){
            cart = mapper.selectByDrugIdAndMemberId(drugId,login.getId());
        }

        return Map.of("cart", cart != null, "countCart", countCart);
    }

    public List<Cart> cartList() {
        return mapper.selectCartList();
    }

    public boolean remove(Integer id) {
        return mapper.deleteById(id) == 1;
    }
}







