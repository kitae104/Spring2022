package kr.inhatc.spring.chart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/chart")
public class ChartController {

  @GetMapping("/basic")
  public String basicChart() {    
    return "/chart/basicChart";
  }
  
  @GetMapping("/basic2")
  public String basicChart2() {    
    return "/chart/basicChart2";
  }
}
