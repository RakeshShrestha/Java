package app.config

import io.javalin.core.security.Role

internal enum class Roles : Role {
    ANYONE, AUTHENTICATED
}
