package com.balancefriends.todos.interceptor

import com.balancefriends.todos.annotaion.NoAuth
import org.springframework.http.HttpStatus
import org.springframework.web.method.HandlerMethod
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthInterceptor : HandlerInterceptor {
    companion object {
        private const val API_KEY = "apiKey"
        private const val API_AUTH_KEY = "123"
        private const val UNAUTHORIZED_MESSAGE = "Not Authorized"
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (isNoAuthMethod(handler)) {
            return true;
        }

        val apiKeyParam = request.getParameter(API_KEY)
        if (apiKeyParam == null || apiKeyParam != API_AUTH_KEY) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_MESSAGE)
        }
        return true
    }

    private fun isNoAuthMethod(handler: Any): Boolean {
        if (handler is HandlerMethod) {
            return handler.method.getAnnotation(NoAuth::class.java) != null
        }
        return false
    }
}