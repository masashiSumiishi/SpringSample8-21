INSERT INTO employee (employee_id,
employee_name, age) values(1, '山田太朗', 30);

/* アドミン */
INSERT INTO m_user (user_id, password, user_name, birthday, age,
marriage, role) VALUES('yamada@xxx.co.jp',
' $2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
'山田太朗', '1990-01-01', 28, false, 'ROLE_ADMIN');

/* 一般権限 */
INSERT INTO m_user (user_id, password, user_name, birthday, age,
marriage, role) VALUES('tamura@xxx.co.jp',
' $2a$10$xRTXvpMWly0oGiu65WZlm.3YL95LGVV2ASFjDhe6WF4.Qji1huIPa',
'田村達也', '1986-11-05', 31, false, 'ROLE_GENERAL');
