package kr.inhatc.spring.chart.controller;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.inhatc.spring.excel.entity.ExcelData;
import kr.inhatc.spring.excel.service.ExcelDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = "/chart")
@RequiredArgsConstructor
@Slf4j
public class ChartController
{

	private final ExcelDataService excelDataService;

	@GetMapping("/basic")
	public String basicChart()
	{
		return "/chart/basicChart";
	}

	@GetMapping("/google")
	public String basicGoogle(Model model)
	{

		List<ExcelData> lists = excelDataService.getAllExcelDatas();

		JSONObject jsonData = makeJsonObject(lists);
		log.info(">>> jsonData : " + jsonData);
		
		model.addAttribute("jsonData", jsonData);
		model.addAttribute("aa", "test");
		return "/chart/googleChart";
	}

	private JSONObject makeJsonObject(List<ExcelData> lists)
	{
		// 0. 최종적으로 리턴할 json 객체
		JSONObject data = new JSONObject();
		
		// 1. cols 배열에 넣기
		JSONObject col1 = new JSONObject();
		JSONObject col2 = new JSONObject();
		JSONObject col3 = new JSONObject();
		JSONObject col4 = new JSONObject();
		JSONObject col5 = new JSONObject();
		JSONObject col6 = new JSONObject();

		JSONArray title = new JSONArray();
		
		col1.put("label", "번호");
		col1.put("type", "number");
//		col2.put("label", "나이");
//		col2.put("type", "number");
		col3.put("label", "이름");
		col3.put("type", "string");
//		col4.put("label", "지역");
//		col4.put("type", "string");
//		col5.put("label", "직위");
//		col5.put("type", "string");
		col6.put("label", "월급");
		col6.put("type", "number");

//		title.add(col1);
//		title.add(col2);
		title.add(col3);
//		title.add(col4);
//		title.add(col5);
		title.add(col6);

		data.put("cols", title);

		// 2. rows 배열에 넣기
		JSONArray body = new JSONArray(); // rows
		for (ExcelData excel : lists)
		{

			// {"c":[{"v":"삼성 칼라 TV 29인치"}, {"v":14280000}]},;
//			JSONObject id = new JSONObject();
//			id.put("v", excel.getId());
			JSONObject name = new JSONObject();
            name.put("v", excel.getName());
			JSONObject salary = new JSONObject();
			salary.put("v", excel.getSaraly()); // 금액

			JSONArray row = new JSONArray();
//			row.add(id);
			row.add(name);
			row.add(salary);

			JSONObject cell = new JSONObject();
			cell.put("c", row);

			body.add(cell); // 레코드 1행 추가
		} // end for
//
		data.put("rows", body);
		return data;
	}
}
