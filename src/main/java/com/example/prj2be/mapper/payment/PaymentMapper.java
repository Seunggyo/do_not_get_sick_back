package com.example.prj2be.mapper.payment;

import com.example.prj2be.domain.Payment.Payment;
import com.example.prj2be.dto.PaymentDto;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface PaymentMapper {

    @Insert("""
        insert into payment (amount, paymentName, paymentUid, paySuccessYN, status)
        values (#{amount}, #{paymentName}, #{paymentUid}, false, #{status})
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

    @Select("""
        select * from payment
        where paymentUid = #{orderId}
""")
    Payment findByUid(String orderId);

    @Delete("""
        delete from payment
        where memberId = #{memberId}
""")
    void deleteByMemberId(String memberId);
}
