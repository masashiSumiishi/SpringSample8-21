package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

@Repository("UserDaoNamedJdbcImpl")
public class UserDaoNamedJdbcImpl implements UserDao {

	@Autowired
	private NamedParameterJdbcTemplate jdbc;

	//Userテーブルの件数
	@Override
	public int count() {
		String sql = "SELECT COUNT(*) FROM m_user";
		//パラメーター生成
		SqlParameterSource params = new MapSqlParameterSource();

		return jdbc.queryForObject(sql, params, Integer.class);
	}

	//データを１件insert
	@Override
	public int insertOne(User user) {
		String sql = "INSERT INTO m_user(user_id,"
				+ " password," + " user_name," + "birthday,"
				+ " age," + " marriage," + " role)"
				+ " VALUES(:userId," + " :password,"
				+ " :userName," + " :birthday," + " :age,"
				+ " :marriage," + " :role)";
		SqlParameterSource params = new MapSqlParameterSource().addValue("userId", user.getUserId())
				.addValue("password", user.getPassword())
				.addValue("userName", user.getUserName())
				.addValue("birthday", user.getBirthday())
				.addValue("age", user.getAge())
				.addValue("marriage", user.isMarriage())
				.addValue("role", user.getRole());

		return jdbc.update(sql, params);
	}

	//データを１件取得
	@Override
	public User selectOne(String userId) {
		String sql = "SELECT * FROM m_user WHERE user_id = :userId";
		SqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);

		//SQL実行
		Map<String, Object> map = jdbc.queryForMap(sql, params);

		User user = new User();
		user.setUserId((String)map.get("user_id"));
		user.setPassword((String)map.get("password"));
		user.setUserName((String)map.get("user_name"));
		user.setBirthday((Date)map.get("birthday"));
		user.setAge((Integer)map.get("age"));
		user.setMarriage((Boolean)map.get("marriage"));
		user.setRole((String)map.get("role"));

		return user;
	}

	//全データを取得
	@Override
	public List<User> selectMany() throws DataAccessException {
		String sql = "SELECT * FROM m_user";
		SqlParameterSource params = new MapSqlParameterSource();

		//SQL実行
		List<Map<String, Object>> getList = jdbc.queryForList(sql, params);
		//結果返却用のList
		List<User> userList = new ArrayList<>();
		//取得データをloop
		for(Map<String, Object> map:getList) {
			User user = new User();
			user.setUserId((String)map.get("user_id"));
			user.setPassword((String)map.get("password"));
			user.setUserName((String)map.get("user_name"));
			user.setBirthday((Date)map.get("birthday"));
			user.setAge((Integer)map.get("age"));
			user.setMarriage((Boolean)map.get("marriage"));
			user.setRole((String)map.get("role"));
			userList.add(user);
		}
		return userList;
	}

	//1件更新
	@Override
	public int updateOne(User user) {
		String sql = "UPDATE m_user" + " SET"
				+ " password = :password," + " user_name = :userName,"
				+ " birthday = :birthday," + " age = :age,"
				+ " marriage = :marriage" + " WHERE user_id = :userId";

		SqlParameterSource params = new MapSqlParameterSource().addValue("userId", user.getUserId())
				.addValue("password", user.getPassword())
				.addValue("userName", user.getPassword())
				.addValue("birthday", user.getBirthday())
				.addValue("age", user.getAge())
				.addValue("marriage", user.isMarriage());

		return jdbc.update(sql, params);
	}

	//1件削除
	@Override
	public int deleteOne(String userId) {
		String sql = "DELETE FROM m_user WHERE user_id = :userId";
		SqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);

		//SQL実行
		int rowNumber = jdbc.update(sql, params);
		return rowNumber;
	}

	//SQL取得結果をサーバーにCSVで保存する
	@Override
	public void userCsvOut() {
		String sql = "SELECT * FROM m_user";
		UserRowCallbackHandler handler = new UserRowCallbackHandler();

		//クエリー実行＆CSV出力
		jdbc.query(sql, handler);

	}
}
