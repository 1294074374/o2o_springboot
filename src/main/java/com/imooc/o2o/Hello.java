package com.imooc.o2o;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Hello {
	@RequestMapping(value = "/hello", method = RequestMethod.GET)
	//返回hello
	public String HelloWord() {
		return "HelloSpringBoot";
	}
}
