package com.jj.blog.blogsearchapi.mapper

interface GenericMapper<D, E> {
    fun toDto(e: E): D
    fun toEntity(d: D): E
}
