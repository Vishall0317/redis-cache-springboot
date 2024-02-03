package com.redis.sprinboot.service;

import com.redis.sprinboot.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private static final String HASH_KEY = "book";

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public List<Book> getAllBooks() {
        return redisTemplate.opsForHash().values(HASH_KEY);
    }

    @Override
    public Book getBookById(Long id) {
        System.out.println("getBookById() called !!");
        if(redisTemplate.opsForHash().hasKey(HASH_KEY, id)) {
            return (Book) redisTemplate.opsForHash().get(HASH_KEY, id);
        }
        return null;
    }

    @Override
    public Book saveBook(Book book) {
        redisTemplate.opsForHash().put(HASH_KEY, book.getId(), book);
        return book;
    }

    @Override
    public Book updateBook(Long id, Book book) {
        System.out.println("updateBook() called !!");
        if (redisTemplate.opsForHash().hasKey(HASH_KEY, id)) {
            book.setId(id);
            redisTemplate.opsForHash().put(HASH_KEY, id, book);
            return book;
        }
        return null;
    }

    @Override
    public String deleteBook(Long id) {
        System.out.println("deleteBook() called !!");
        if (redisTemplate.opsForHash().hasKey(HASH_KEY, id)) {
            redisTemplate.opsForHash().delete(HASH_KEY, id);
            return "product removed!!";
        }
        return "product already removed!!";
    }
}
