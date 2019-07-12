package net.borolis.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер для главной страницы
 *
 * @author bliskov
 * @since July 5, 2019
 */
@Controller
public class IndexPageController
{
    /**
     * Главная страница
     *
     * @return page template for thymeleaf
     */
    @GetMapping("/")
    public String renderPage()
    {
        return "index";
    }
}