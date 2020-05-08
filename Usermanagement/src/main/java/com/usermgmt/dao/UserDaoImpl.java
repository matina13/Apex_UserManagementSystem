package com.usermgmt.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.usermgmt.form.RegistrationForm;
import com.usermgmt.model.User;

@Repository
public class UserDaoImpl {

	public final String URL = "jdbc:mysql://localhost:3306/usermanagement?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";

	public List<User> getAllUsers() {
		PreparedStatement stmt = null;
		Connection connection = null;
		List<User> userList = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String query = "select * from user order by id desc";
			connection = DriverManager.getConnection(URL, "root", null);
			stmt = connection.prepareStatement(query);
			ResultSet result = stmt.executeQuery();
			userList = new ArrayList<>();
			while (result.next()) {
				User user = new User();
				user.setId(result.getInt("id"));
				user.setFirstName(result.getString("first_name"));
				user.setLastName(result.getString("last_name"));
				user.setEmail(result.getString("email"));
				user.setMiddleName(result.getString("middle_name"));
				user.setPassword(result.getString("password"));
				user.setRole(result.getString("role"));
				userList.add(user);
			}

		} catch (Exception e) {
			System.out.println("exception");
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return userList;
	}

	public void saveUser(RegistrationForm form, User loggedInUser) {
		Connection connection = null;
		PreparedStatement stmt = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(URL, "root", null);
			String query = "insert into user (first_name, last_name, middle_name, email, password, security_ans1, security_ans2, role) VALUES (?,?,?,?,?,?,?,?)";
			stmt = connection.prepareStatement(query);
			stmt.setString(1, form.getFname());
			stmt.setString(2, form.getLname());
			stmt.setString(3, form.getMname());
			stmt.setString(4, form.getEmail());
			stmt.setString(5, form.getPass1());
			stmt.setString(6, form.getAns1());
			stmt.setString(7, form.getAns2());
			if (loggedInUser != null && null != loggedInUser.getRole()
					&& loggedInUser.getRole().equalsIgnoreCase("ADMIN")) {
				stmt.setString(8, form.getRole());
			} else {
				stmt.setString(8, "CLIENT");
			}
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public User findByEmailAndPassword(String email, String password) {
		User user = new User();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection(URL, "root", "");
			PreparedStatement stmt = myConn.prepareStatement("select * from user where email = ? and password = ?");
			stmt.setString(1, email);
			stmt.setString(2, password);

			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				user.setId(result.getInt("id"));
				user.setFirstName(result.getString("first_name"));
				user.setLastName(result.getString("last_name"));
				user.setEmail(result.getString("email"));
				user.setMiddleName(result.getString("middle_name"));
				user.setPassword(result.getString("password"));
				user.setRole(result.getString("role"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public User findByEmail(String email) {
		User user = new User();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection myConn = DriverManager.getConnection(URL, "root", "");
			PreparedStatement stmt = myConn.prepareStatement("select * from user where email = ? ");
			stmt.setString(1, email);

			ResultSet result = stmt.executeQuery();
			while (result.next()) {
				user.setId(result.getInt("id"));
				user.setFirstName(result.getString("first_name"));
				user.setLastName(result.getString("last_name"));
				user.setEmail(result.getString("email"));
				user.setMiddleName(result.getString("middle_name"));
				user.setPassword(result.getString("password"));
				user.setRole(result.getString("role"));
				user.setAns1(result.getString("security_ans1"));
				user.setAns2(result.getString("security_ans2"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}

	public void updatePassword(String newPassword, String email) throws SQLException {
		PreparedStatement stmt = null;
		Connection myConn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection(URL, "root", "");
			stmt = myConn.prepareStatement("update user set password = ? where email = ? ");
			stmt.setString(1, newPassword);
			stmt.setString(2, email);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			myConn.close();
		}
	}

	public void deleteUser(Integer deleteUserId) throws SQLException {
		PreparedStatement stmt = null;
		Connection myConn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection(URL, "root", "");
			stmt = myConn.prepareStatement("delete from user where id= ? ");
			stmt.setInt(1, deleteUserId);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			stmt.close();
			myConn.close();
		}
	}
}