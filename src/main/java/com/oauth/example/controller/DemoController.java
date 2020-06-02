package com.oauth.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.oauth.example.model.EmployeeModel;
import com.oauth.example.service.TestDataService;

@RestController
@RequestMapping("/demo")
public class DemoController {
	
	@Autowired
	private TestDataService testDataService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/getAll", headers = "Accept= application/json", produces = "application/json")
	public List<EmployeeModel> getAll() throws Exception {
		List<EmployeeModel> bean = null;
		try {
			bean = testDataService.getAllInfo();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}

}
