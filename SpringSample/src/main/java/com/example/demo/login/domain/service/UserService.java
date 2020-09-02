package com.example.demo.login.domain.service;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

@Transactional
@Service
public class UserService {

	@Autowired
	@Qualifier("UserDaoJdbcImpl")
	UserDao dao;

	//insert メソッド
	public boolean insert(User user) {
		int rowNumber = dao.insertOne(user);
		boolean result = false;
		if (rowNumber > 0) {
			result = true;
		}
		return result;
	}

	//カウント用
	public int count() {
		return dao.count();
	}

	//全件取得用メソッド
	public List<User> selectMany() {
		return dao.selectMany();
	}

	//1件取得
	public User selectOne(String userId) {
		return dao.selectOne(userId);
	}

	//1件更新
	public boolean updateOne(User user) {
		int rowNumber = dao.updateOne(user);
		boolean result = false;
		if (rowNumber >0) {
			result = true;
		}
		return result;
	}

	//1件削除メソッド
	public boolean deleteOne(String userId) {
		int rowNumber = dao.deleteOne(userId);
		boolean result = false;
		if (rowNumber > 0) {
			result = true;
		}
		return result;
	}

	//ユーザー一覧をCSV出力
	public void userCsvOut() throws DataAccessException {
		//CSV出力
		dao.userCsvOut();
	}

	//サーバーに保存されているファイルを取得して、byte配列に変換
	public byte[] getFile(String fileName) throws IOException {
		//ファイルシステムの取得
		FileSystem fs = FileSystems.getDefault();
		//ファイル取得
		Path p = fs.getPath(fileName);
		//ファイルをbyte配列に変換
		byte[] bytes = Files.readAllBytes(p);
		return bytes;
	}
}
