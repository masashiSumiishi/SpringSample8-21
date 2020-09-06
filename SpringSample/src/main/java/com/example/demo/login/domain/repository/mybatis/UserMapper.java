package com.example.demo.login.domain.repository.mybatis;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.example.demo.login.domain.model.User;

@Mapper
public interface UserMapper {

	//登録メソッド
	@Insert("INSERT INTO m_user ("
			+ " user_id," + " password," + " user_name,"
			+ " birthday," + " age," + " marriage," + " role)"
			+ " VALUES(" + " #{userId}," + " #{password},"
			+ " #{userName}," + " #{birthday}," + " #{age},"
			+ " #{marriage}," + " #{role}")
	public boolean insert(User user);
}
