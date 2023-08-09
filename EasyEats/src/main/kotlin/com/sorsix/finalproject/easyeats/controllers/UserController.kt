package com.sorsix.finalproject.easyeats.controllers

import com.sorsix.finalproject.easyeats.models.User
import com.sorsix.finalproject.easyeats.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = ["http://localhost:4200"], allowCredentials = "true")
class UserController(private val userService: UserService) {

    @PutMapping("/{id}")
    fun updatedUser (@PathVariable id: Long, @RequestBody updatedUser: User) : ResponseEntity<User>{
        val user = userService.updateUser(updatedUser)
        return ResponseEntity.ok(user)
    }

    @GetMapping("/token")
    fun getUserFromToken(@RequestParam token: String): User?{
        return userService.getUserFromToken(token)
    }

    @PutMapping("/profilepicture/{user_id}")
    fun updateImageField(@PathVariable user_id: Long, @RequestParam image: MultipartFile): ResponseEntity<User>{
        val user = userService.updateImageField(user_id, image)
        return ResponseEntity.ok(user)
    }

}
