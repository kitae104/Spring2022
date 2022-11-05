package kr.inhatc.spring.chart.controller;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.inhatc.spring.excel.entity.ExcelData;
import kr.inhatc.spring.excel.service.ExcelDataService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/chart")
@RequiredArgsConstructor
public class ChartController
{

	private final ExcelDataService excelDataService;

	@GetMapping("/basic")
	public String basicChart()
	{
		return "/chart/basicChart";
	}

	@GetMapping("/google")
	public String basicGoogle()
	{

		List<ExcelData> lists = excelDataService.getAllExcelDatas();

		JSONObject data = makeJsonObject(lists);

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
		col1.put("type", "string");
		col2.put("label", "나이");
		col2.put("type", "number");
		col3.put("label", "이름");
		col3.put("type", "");
		col4.put("label", "지역");
		col4.put("type", "number");
		col5.put("label", "직위");
		col5.put("type", "number");
		col6.put("label", "월급");
		col6.put("type", "number");

		title.add(col1);
		title.add(col2);

		data.put("cols", title);

		// 2. rows 배열에 넣기
//		JSONArray body = new JSONArray(); // rows
//		for (Map<String, Object> map : lists)
//		{
//
//			// {"c":[{"v":"삼성 칼라 TV 29인치"}, {"v":14280000}]},;
//			JSONObject prodName = new JSONObject();
//			prodName.put("v", map.get("PROD_NAME"));
//			JSONObject money = new JSONObject();
//			money.put("v", map.get("MONEY")); // 금액
//
//			JSONArray row = new JSONArray();
//			row.add(prodName);
//			row.add(money);
//
//			JSONObject cell = new JSONObject();
//			cell.put("c", row);
//
//			body.add(cell); // 레코드 1행 추가
//		} // end for
//
//		data.put("rows", body);
		return null;
	}
}
