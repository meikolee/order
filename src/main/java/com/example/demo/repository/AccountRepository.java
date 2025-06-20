package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

@Mapper
public interface AccountRepository {
    @Update("UPDATE account SET balance = balance - #{amount} WHERE user_id = #{userId}")
    void debit(@Param("userId") String userId, @Param("amount") BigDecimal amount);

    @Update("UPDATE account SET balance = balance + #{amount} WHERE user_id = #{userId}")
    void credit(@Param("userId") String userId, @Param("amount") BigDecimal amount);

    @Select("SELECT SUM(balance) FROM account")
    BigDecimal sumAllBalance();
}
