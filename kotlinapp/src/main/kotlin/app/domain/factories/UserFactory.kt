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

package app.domain.factories

import io.javalin.http.NotFoundResponse

import app.domain.User

import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

import org.joda.time.DateTime
import javax.sql.DataSource

internal object Users : LongIdTable() {
	val email: Column<String> = varchar("email", 200).uniqueIndex()
	val username: Column<String> = varchar("username", 100).index()
	val password: Column<String> = varchar("password", 150)

	val firstname: Column<String?> = varchar("firstname", 255).nullable()
	val lastname: Column<String?> = varchar("lastname", 255).nullable()
	val contactmobile: Column<String?> = varchar("contactmobile", 255).nullable()

	val country: Column<String?> = varchar("country", 255).nullable()
	val perms: Column<String?> = varchar("perms", 255).nullable()
	val status: Column<String?> = varchar("status", 255).nullable()
	val remarks: Column<String?> = varchar("remarks", 1000).nullable()
	val ipregistered: Column<String?> = varchar("ipregistered", 50).nullable()
	
	val bio: Column<String?> = varchar("bio", 1000).nullable()
	val image: Column<String?> = varchar("image", 255).nullable()
	
    val createdAt: Column<DateTime> = date("created_at")
    val updatedAt: Column<DateTime> = date("updated_at")

	fun toDomain(row: ResultRow): User {
		return User(
			id = row[Users.id].value,
			email = row[Users.email],
			username = row[Users.username],
			password = row[Users.password],

			firstname = row[Users.firstname],
			lastname = row[Users.lastname],
			contactmobile = row[Users.contactmobile],
				
			country = row[Users.country],
			perms = row[Users.perms],
			status = row[Users.status],
			remarks = row[Users.remarks],
			ipregistered = row[Users.ipregistered],
				
			bio = row[Users.bio],
			image = row[Users.image],
				
			createdAt = row[createdAt].toDate(),
			updatedAt = row[updatedAt].toDate()				
		)
	}
}

internal object Follows : Table() {
	val user: Column<Long> = long("user").primaryKey()
	val follower: Column<Long> = long("user_follower").primaryKey()
}

class UserFactory(private val dataSource: DataSource) {
	
	init {
		transaction(Database.connect(dataSource)) {
			SchemaUtils.create(Users)
			SchemaUtils.create(Follows)
		}
	}

	fun findByEmail(email: String): User? {
		return transaction(Database.connect(dataSource)) {
			Users.select { Users.email eq email }
			.map { Users.toDomain(it) }
			.firstOrNull()
		}
	}

	fun findByUsername(username: String): User? {
		return transaction(Database.connect(dataSource)) {
			Users.select { Users.username eq username }
			.map { Users.toDomain(it) }
			.firstOrNull()
		}
	}

	fun create(user: User): Long? {
		return transaction(Database.connect(dataSource)) {
			Users.insertAndGetId { row ->
				row[Users.email] = user.email
				row[Users.username] = user.username!!
				row[Users.password] = user.password!!

				row[Users.firstname] = user.firstname
				row[Users.lastname] = user.lastname
				row[Users.contactmobile] = user.contactmobile
								
				row[Users.country] = user.country
				row[Users.perms] = user.perms
				row[Users.status] = "Y"
				row[Users.remarks] = user.remarks
				row[Users.ipregistered] = user.ipregistered
				
				row[Users.bio] = user.bio
				row[Users.image] = user.image
				
                row[createdAt] = DateTime()
                row[updatedAt] = DateTime()				
			}.value
		}
	}

	fun update(email: String, user: User): User? {
		transaction(Database.connect(dataSource)) {
			Users.update({ Users.email eq email }) { row ->
				row[Users.email] = user.email
				if (user.username != null)
					row[Users.username] = user.username
				if (user.password != null)
					row[Users.password] = user.password
				
				if (user.firstname != null)
					row[Users.firstname] = user.firstname
				if (user.lastname != null)
					row[Users.lastname] = user.lastname
				if (user.contactmobile != null)
					row[Users.contactmobile] = user.contactmobile
				
				if (user.country != null)
					row[Users.country] = user.country
				if (user.perms != null)
					row[Users.perms] = user.perms
				if (user.status != null)
					row[Users.status] = user.status
				if (user.remarks != null)
					row[Users.remarks] = user.remarks
				if (user.ipregistered != null)
					row[Users.ipregistered] = user.ipregistered
				
				if (user.bio != null)
					row[Users.bio] = user.bio
				if (user.image != null)
					row[Users.image] = user.image
				if (user.createdAt != null)
					row[Users.createdAt] = DateTime(user.createdAt)
				
                row[updatedAt] = DateTime()
				
			}
		}
		return findByEmail(user.email)
	}

	fun findIsFollowUser(email: String, userIdToFollow: Long): Boolean {
		return transaction(Database.connect(dataSource)) {
			Users.join(
				Follows, JoinType.INNER,
				additionalConstraint = {
					Follows.user eq Users.id and (Follows.follower eq userIdToFollow)
				}
			)
			.select {
				Users.email eq email
			}
			.count() > 0
		}
	}

	fun follow(email: String, usernameToFollow: String): User {
		val user = findByEmail(email) ?: throw NotFoundResponse()
		val userToFollow = findByUsername(usernameToFollow) ?: throw NotFoundResponse()
		transaction(Database.connect(dataSource)) {
			Follows.insert { row ->
				row[Follows.user] = userToFollow.id!!
				row[Follows.follower] = user.id!!
			}
		}
		return userToFollow
	}

	fun unfollow(email: String, usernameToUnFollow: String): User {
		val user = findByEmail(email) ?: throw NotFoundResponse()
		val userToUnfollow = findByUsername(usernameToUnFollow) ?: throw NotFoundResponse()
		transaction(Database.connect(dataSource)) {
			Follows.deleteWhere {
				Follows.user eq user.id!! and (Follows.follower eq userToUnfollow.id!!)
			}
		}
		return userToUnfollow
	}
	
}