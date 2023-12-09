package com.example.prj2be.mapper.payment;

import com.example.prj2be.domain.Payment.Payment;
import com.example.prj2be.dto.PaymentDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface PaymentMapper {

    @Insert("""
        insert into payment (amount, paymentName, paymentUid, paySuccessYN)
        values (#{amount}, #{paymentName}, #{paymentUid}, false)
""")
    int insertByDTO(PaymentDto paymentDto);

    @Select("""
        select * from payment
        where paymentUid=#{paymentUid}
""")
    Payment selectByDTO(PaymentDto paymentDto);

    @Update("""
        update payment
        set memberId = #{memberId}
        where paymentUid=#{paymentUid}
""")
    void updateMemberIdByPayment(Payment payment);
}
