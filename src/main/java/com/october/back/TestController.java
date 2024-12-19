package com.october.back;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

	@GetMapping("/test")
	public String test() {
		return "무중단 배포 테스트 컨트롤러";
	}

	@GetMapping("/main")
	public String main() {
		return "Main!";
	}
}
