package com.sorsix.finalproject.easyeats.service.implementations

import com.sorsix.finalproject.easyeats.configurations.JwtService
import com.sorsix.finalproject.easyeats.models.User
import com.sorsix.finalproject.easyeats.models.enumerations.Role
import com.sorsix.finalproject.easyeats.models.exception.InvalidArgumentsException
import com.sorsix.finalproject.easyeats.models.exception.InvalidPasswordException
import com.sorsix.finalproject.easyeats.models.exception.*
import com.sorsix.finalproject.easyeats.repository.UserRepository
import com.sorsix.finalproject.easyeats.service.UserService
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.security.Principal
import kotlin.io.path.exists


@Service
class UserServiceImpl(val repository: UserRepository,
                      val jwtService: JwtService, val passwordEncoder: PasswordEncoder) : UserService {

    //    override fun login(username: String?, password: String?, request: HttpServletRequest): User? {
//        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
//            throw InvalidArgumentsException()
//        }
//
//        val user = repository.findByUsername(username).orElseThrow { UsernameNotFoundException() };
//
//        if (user != null) {
//            if (passwordEncoder.matches(password, user.password)) {
//                request.session.setAttribute("user", user)
//                return user
//            } else {
//                throw InvalidPasswordException()
//            }
//        }else{
//            throw UsernameNotFoundException()
//        }



    override fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$")
        return email.matches(emailRegex)
    }

    override fun isValidUsername(username: String): Boolean {
        val usernameRegex = Regex("^[a-zA-Z0-9.,'\\-?]+\$")
        return username.matches(usernameRegex)
    }

    override fun doesUsernameExist(username: String): Boolean {
        return repository.findByUserName(username) != null
    }

    override fun doesEmailExist(email: String): Boolean {
        return repository.findByEmail(email) != null
    }

    override fun getLoggedInUser(request: HttpServletRequest): User? {
        return request.session.getAttribute("user") as? User
    }


    override fun updateUser(userId: Long, updatedUser: User, principal: Principal): User {
        val existingUser = repository.findById(userId)
            .orElseThrow { EmailNotFoundException() }

        existingUser.apply {
            first_name = updatedUser.first_name
            last_name = updatedUser.last_name
            userName=updatedUser.userName
            email = updatedUser.email
            passw = passwordEncoder.encode(updatedUser.passw)
            role=updatedUser.role
            image = updatedUser.image
        }

        return repository.save(existingUser)
    }

    override fun getUserFromToken(token: String): User? {
        return repository.findByEmail(jwtService.extractUsername(token))
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = repository.findByUserName(username)
        if (user == null) {
            throw EmailNotFoundException()
        }
        val authorities = mutableListOf<SimpleGrantedAuthority>()

        if (user != null) {
            authorities.add(SimpleGrantedAuthority("ROLE_${user.role.name}"))
        }

        return org.springframework.security.core.userdetails.User(
            user?.username,
            user?.password,
            authorities
        )
    }

}



    private fun generateRandomImageName(): String {
        val sb = StringBuilder()
        for (i in 0..5) {
            val rand = listOf(('a'..'z'), ('A'..'Z')).flatten().random()
            sb.append(rand)
        }
        return sb.toString()
    }

//    override fun register(
//        username: String?,
//        email: String?,
//        password: String?,
//        repeatPassword: String?,
//        name: String?,
//        surname: String?,
//        role: Role?,
//        file: MultipartFile,
//        request: HttpServletRequest
//    ): User? {
//        if (username.isNullOrEmpty() || password.isNullOrEmpty() || email.isNullOrEmpty() || name.isNullOrEmpty() || surname.isNullOrEmpty() || role == null) {
//            val errorMessage = "Invalid or missing values for the following fields: " +
//                    "username=$username, email=$email, password=$password, repeatPassword=$repeatPassword, " +
//                    "name=$name, surname=$surname, role=$role"
//            println(errorMessage)
//            throw InvalidUsernameOrPasswordException(errorMessage)
//        }
//
//        if (password != repeatPassword) {
//            throw PasswordDoNotMatch()
//        }
//
//        if (repository.findByUsername(username!!).isPresent) {
//            throw UsernameAlreadyExist(username)
//        }
//
//        var imageName = generateRandomImageName() + ".jpg"
//        var imageFilePath = Paths.get("src\\main\\Frontend\\EasyEats\\src\\assets\\user_images\\", imageName)
//        while (imageFilePath.exists()) {
//            imageName = generateRandomImageName()
//            imageFilePath = Paths.get("src\\main\\Frontend\\EasyEats\\src\\assets\\user_images\\", imageName)
//        }
//        Files.copy(file.inputStream, Paths.get(imageFilePath.toString()))
//        val image = "../../assets/user_images/$imageName"
//
//        val user = User(0, name, surname, email, username, passwordEncoder.encode(password), role, image)
//        request.session.setAttribute("user", user)
//        return repository.save(user)
//    }


