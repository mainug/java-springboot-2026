package com.pknu26.springlog;

import org.slf4j.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    // 로그 클래스 객체 생성
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @GetMapping("/home")
    @ResponseBody
    public String home() {
        System.out.println("일반 콘솔 출력");
        logger.info("Home 컨트롤러 시작");
        // 디버깅 모드에서
        logger.debug("디버그 시 필요한 로그1");
        logger.trace("디버그 시 필요한 로그2");

        // 문제 발생 시
        logger.warn("경고 표시 (에러가 날 가능성이 있는 로직)");
        // 완전 문제. 해결해야 하는 것
        logger.error("오류 표시 (무조건 해결해야 하는 것");

        return "Home Page";
    }

}
