package com.example.demo.login.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.login.domain.model.SignupForm;
import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.service.UserService;

@Controller
public class HomeController {

	@Autowired
	UserService userService;
	//結婚ステータス
	private Map<String, String> radioMarriage;
	//結婚ステータスの初期化
	private Map<String, String> initRadioMarriage() {
		Map<String, String> radio = new LinkedHashMap<>();
		radio.put("既婚", "true");
		radio.put("未婚", "false");

		return radio;
	}

	@GetMapping("/home")
	public String getHome(Model model) {
		model.addAttribute("contents", "login/home::home_contents");
		return "login/homeLayout";
	}

	@GetMapping("/userList")
	public String getUserList(Model model) {
		//コンテンツ部分にユーザー一覧を表示するための文字列
		model.addAttribute("contents", "login/userList::userList_contents");
		//ユーザー一覧
		List<User> userList = userService.selectMany();
		model.addAttribute("userList", userList);
		//データ件数取得
		int count = userService.count();
		model.addAttribute("userListCount", count);

		return "login/homeLayout";
	}

	//ユーザー詳細画面
	@GetMapping("/userDetail/{id:.+}")
	public String getUserDetail(@ModelAttribute SignupForm form, Model model, @PathVariable("id") String userId) {
		//ユーザーID確認
		System.out.println("userId = " + userId);
		//コンテンツ部分にユーザー詳細を表示するための文字列を登録
		model.addAttribute("contents", "login/userDetail::userDetail_contents");

		radioMarriage = initRadioMarriage();
		model.addAttribute("radioMarriage", radioMarriage);

		if (userId != null && userId.length() > 0) {
			User user = userService.selectOne(userId);
			//Userクラスをフォームクラスに変換
			form.setUserId(user.getUserId());
			form.setUserName(user.getUserName());
			form.setBirthday(user.getBirthday());
			form.setAge(user.getAge());
			form.setMarriage(user.isMarriage());

			model.addAttribute("signupForm", form);
		}
		return "login/homeLayout";
	}

	//ユーザー更新用処理
	@PostMapping(value="/userDetail", params="update")
	public String postUserDetailUpdate(@ModelAttribute SignupForm form, Model model) {
		System.out.println("更新ボタンの処理");
		User user = new User();
		user.setUserId(form.getUserId());
		user.setPassword(form.getPassword());
		user.setUserName(form.getUserName());
		user.setBirthday(form.getBirthday());
		user.setAge(form.getAge());
		user.setMarriage(form.isMarriage());

		boolean result = userService.updateOne(user);

		if(result == true) {
			model.addAttribute("result", "更新成功");
		} else {
			model.addAttribute("result", "更新失敗");
		}

		//ユーザー一覧画面を表示
		return getUserList(model);
	}

	//ユーザー削除
	@PostMapping(value="/userDetail", params="delete")
	public String postUserDetailDelete(@ModelAttribute SignupForm form, Model model) {
		System.out.println("削除ボタンの処理");
		boolean result = userService.deleteOne(form.getUserId());
		if (result == true) {
			model.addAttribute("result", "削除成功");
		} else {
			model.addAttribute("result", "削除失敗");
		}

		return getUserList(model);
	}

	//ユーザー一覧のCSV出力用処理
	@GetMapping("/userList/csv")
	public ResponseEntity<byte[]> getUserListCsv(Model model) {
		//ユーザーを全件取得して、CSVをサーバーに保存する
		userService.userCsvOut();
		byte[] bytes = null;

		try {
			//サーバーに保存されているsample.csvファイルをbyteで取得
			bytes = userService.getFile("sample.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}

		//Httpヘッダーの設定
		HttpHeaders header = new HttpHeaders();
		header.add("Content-Type", "text/csv; charset=UTF-8");
		header.setContentDispositionFormData("filename", "sample.csv");

		//sample.csvを戻す
		return new ResponseEntity<>(bytes, header, HttpStatus.OK);
	}

	@GetMapping("/logout")
	public String postLogout() {
		return "redirect:/login";
	}
}
