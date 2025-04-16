package com.example.firstproject.repository;

import com.example.firstproject.entity.Article;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

// CrudRepository는 JPA에서 제공하는 인터페이스
// 이를 상속해 엔티티를 관리(CRUD).
// Article: 관리대상 엔티티의 클래스 타입
// Long: 관리대상 엔티티의 대표값 타입(id 의 타입)
public interface ArticleRepository extends CrudRepository<Article, Long> {
  @Override
  ArrayList<Article> findAll();   // Iterable -> ArrayList 수정
}
