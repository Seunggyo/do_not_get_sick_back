package com.example.prj2be.service.drug;

import com.example.prj2be.domain.drug.Buy;
import com.example.prj2be.domain.drug.Cart;
import com.example.prj2be.domain.member.Member;
import com.example.prj2be.mapper.drug.CartMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class CartService {

    private final CartMapper mapper;
    private final S3Client s3;
    @Value("${image.file.prefix}")
    private String urlPrefix;
    @Value("${aws.s3.bucket.name}")
    private String bucket;

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

    public Map<String, Object> cartList(Member login) {
        int amount = 0;
        Map<String, Object> map = new HashMap<>();
        if (login != null) {

            List<Cart> cartList = mapper.selectCartList(login.getId());
            for (Cart cart : cartList) {
                String url = urlPrefix + "prj2/drug/" + cart.getDrugId() + "/" + cart.getFileName();
                cart.setUrl(url);
            }
            map.put("cartList", cartList);

            for (Cart cart : cartList) {
                amount += cart.getTotal();
            }
            map.put("amount", amount);
        }

        return map;

    }

    public boolean remove(Integer id) {
        return mapper.deleteById(id) == 1;
    }


}







