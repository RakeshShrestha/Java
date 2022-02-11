/**
# Copyright Rakesh Shrestha (rakesh.shrestha@gmail.com)
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
# Redistributions must retain the above copyright notice.
 */

package app.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

class Db() {
	private val dataSource: DataSource

	init {
		dataSource = HikariConfig().let { config ->
			config.jdbcUrl = "jdbc:mysql://localhost:3306/mydb"
			config.driverClassName = "org.mariadb.jdbc.Driver"
			config.username = "root"
			config.password = ""
			HikariDataSource(config)
		}
	}

	fun getDataSource(): DataSource {
		return dataSource
	}
	
}
