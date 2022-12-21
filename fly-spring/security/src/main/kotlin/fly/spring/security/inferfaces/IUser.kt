package fly.spring.security.inferfaces

import fly.spring.common.`interface`.core.IId
import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.userdetails.UserDetails


interface IUser : IId, UserDetails, CredentialsContainer{
    override fun eraseCredentials() {
        password = null
    }

    var password: String?

    override fun getPassword(): String? {
        return password
    }

}
