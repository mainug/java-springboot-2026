package com.pknu26.httpmethod.controller;

import com.pknu26.httpmethod.entity.Student;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 임시로 데이터를 저장할 리스트 (데이터베이스 대신 사용)
    private static final List<Student> studentList = new ArrayList<>();

    @GetMapping("/list")
    public String getAllStudent(Model model) {
        model.addAttribute("students", studentList);
        return "list";  // list.html 과 연결
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        // student라는 이름의 모델을 Student 클래스 객체 생성 후 create.html 페이지로 전달
        return "create";  // create.html 과 연결
    }

    @PostMapping("/create")
    public String createStudent(@ModelAttribute Student student, Model model) {
        studentList.add(student);
        logger.info(student.getName());
        logger.info(String.valueOf(student.getAge()));
        // return "redirect:/students/list"; // 등록 후 리스트 페이지로 이동
        return "result"; // result.html 화면에 띄움
    }

    @GetMapping("/search")
    public String search(@RequestParam String name, @RequestParam int age, Model model) {
        logger.info("검색 이름 : {}", name);
        logger.warn("검색 나이 : {}", age);

        model.addAttribute("result", "검색 완료!");
        return "list";
    }

    @GetMapping("/student/{id}")
    public String getStudent(@PathVariable int id, Model model) {
        logger.info("조회 ID {}", id);

        // 실전에서는 DB 조회
        Student student = new Student();
        student.setName("홍길동");
        student.setAge(20);

        model.addAttribute("student", student);

        return "create";
    }

}