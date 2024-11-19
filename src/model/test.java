package model;

import java.sql.Connection;

import Connection.JDBC;

public class test {
	public static void main(String[] args) {
		Connection c = JDBC.getConnection();
		System.out.println(c);
	}
}

