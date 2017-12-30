/**
 * 
 */
package com.nj.nfhy.controller;

import com.nj.nfhy.controller.base.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author 88386726 2017年12月28日
 *
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController {

	@RequestMapping("/demo.htm")
	public String test(){

		return "test";
	}
}
