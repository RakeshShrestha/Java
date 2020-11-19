package app.domain

import java.util.*
import org.joda.time.DateTime

data class ProfileDTO(val profile: Profile?)

data class Profile(
	val username: String? = null,
	val bio: String? = null,
	val image: String? = null,
	
	val firstname: String? = null,
	val lastname: String? = null,
	val contactmobile: String? = null,

	val country: String? = null,
	val perms: String? = null,
	val status: String? = null,
	
	val createdAt: Date? = null,
	
	val following: Boolean = false
)