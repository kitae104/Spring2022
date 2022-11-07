package kr.inhatc.spring.xterm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class XtermController {
  
  @GetMapping("/xterm/basic")
  public String xtermBaisc() {
    return "/xterm/basic";
  }
}
