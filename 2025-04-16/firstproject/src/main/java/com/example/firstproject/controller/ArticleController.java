package com.example.firstproject.controller;

import com.example.firstproject.dto.ArticleForm;
import com.example.firstproject.entity.Article;
import com.example.firstproject.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ArticleController {
  @Autowired  // 스프링부트가 미리 생성해 놓은 리파지터리 객체 주입(DI)
  private ArticleRepository articleRepository;

  @GetMapping("/articles/new")
  public String newArticleForm() {
    return "articles/new";
  }

  @PostMapping("/articles/create")
  public String createArticle(ArticleForm form) {
    //System.out.println("form.toString() :: " + form.toString());
    log.info("form.toString() :: " + form.toString());
    // 1. DTO 를 엔티티로 변환
    Article article = form.toEntity();
    //System.out.println("article.toString() ::: " + article.toString());
    log.info("article.toString() ::: " + article.toString());

    // 2. 리파지터리로 엔티티를 DB에 저장
    Article saved = articleRepository.save(article);
    System.out.println("saved.toString() ::: " + saved.toString());
    log.info("saved.toString() ::: " + saved.toString());

    return "redirect:/articles/" + saved.getId();
  }
  
  @GetMapping("/articles/{id}")
  public String show(@PathVariable Long id, Model model){  // 매개변수 id 받아 오기
    log.info("id = "+ id);
    // 1. id를 조회해 데이터 가져오기
    Optional<Article> articleEntity = Optional.ofNullable(articleRepository.findById(id).orElse(null));
    //Optional<Article> articleEntity = articleRepository.findById(id);
    log.info("articleEntity.toString() : "+ articleEntity.toString());
    // 2. 모델에 데이터를 등록하기
    model.addAttribute("article", articleEntity.get());

    // 3. 뷰 페이지에 반환하기
    return "articles/show";
  }

  @GetMapping("/articles")  // 목록 조회
  public String index(Model model){
    // 1. DB에서 모든 Article 데이터 가져오기
    ArrayList<Article> articleEntityList = articleRepository.findAll();

    // 2. 가져온 Article 묶음을 모델에 등록하기
    model.addAttribute("articleList", articleEntityList);

    // 3. 사용자에게 보여 줄 뷰페이지 설정하기
    return "articles/index";
  }

  @GetMapping("/articles/{id}/edit")
  public String edit(@PathVariable Long id, Model model){
    // 수정할 데이터 가져오기
    Optional<Article> articleEntity = Optional.ofNullable(articleRepository.findById(id).orElse(null));
    // 가져온 Article 모델에 등록하기
    model.addAttribute("article", articleEntity.get());
    // 뷰페이지 설정하기
    return "articles/edit";
  }

  @PostMapping("/articles/update")
  public String update(ArticleForm form){
    log.info("form.toString() :: "+ form.toString());
    // 1. DTO를 엔티티로 변환 하기
    Article articleEntity = form.toEntity();
    // 2-1 DB에서 기존 데이터 가져오기
    Article target = articleRepository.findById(articleEntity.getId()).orElse(null);
    // 2-2 기존 데이터 값을 갱신하기
    if(target != null){
     articleRepository.save(articleEntity); // 엔티티를 DB에 저장(갱신)
    }
    // 3. 수정결과 페이지로 리다이렉트 하기
    return "redirect:/articles/"+ articleEntity.getId();
  }

  @GetMapping("/articles/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes rttr){
      log.info("삭제 요청이 들어왔습니다!!");
      // 1. 삭제할 대상 가져오기
      Article target = articleRepository.findById(id).orElse(null);
      log.info("target.toString() : "+ target.toString());
      if(target != null){
        articleRepository.delete(target);
        rttr.addAttribute("msg", "삭제됐습니다.");
      }
    return "redirect:/articles";
  }

}
