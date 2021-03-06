package cn.com.newloading.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cn.com.newloading.bean.Admin;
import cn.com.newloading.bean.Dict;
import cn.com.newloading.bean.StudentReg;
import cn.com.newloading.enums.AuditStatu;
import cn.com.newloading.service.DictService;
import cn.com.newloading.service.StudentRegService;
import cn.com.newloading.utils.StringUtil;
import cn.com.newloading.utils.TimeUtil;

@Controller
@RequestMapping("/studentReg")
public class StudentRegController {

	@Autowired
	private StudentRegService studentRegService;
	@Autowired
	private DictService dictService;

	/**
	 * 学生注册
	 * @param request
	 * @return
	 */
	@RequestMapping("/registerStu")
	@ResponseBody
	public JSONObject registerStu(HttpServletRequest request,@RequestBody Map<String, Object> params) {
		String code = (String) params.get("verificationCode");//验证码
		if(StringUtil.isEmpty(code)) {
			return responseMsg("CODE0002","CODE");
		}
		String verificationCode = (String) request.getSession().getAttribute("verificationCode");//session中的验证码
		if(StringUtil.isEmpty(verificationCode)) {
			return responseMsg("CODE0003","CODE");
		}
		if(!code.equalsIgnoreCase(verificationCode)) {
			return responseMsg("CODE0001","CODE");
		}
		String stuPhone = (String) params.get("stuPhone");// 手机号
		String stuEmail = (String) params.get("stuEmail");// 邮箱
		if (StringUtil.isBlank(stuEmail) && StringUtil.isBlank(stuPhone)) {
			return responseMsg("REGSTU0005","REGSTU");
		}
		String stuPassword = (String) params.get("stuPassword");// 密码
		if (StringUtil.isBlank(stuPassword)) {
			return responseMsg("REGSTU0006","REGSTU");
		}
		String stuStudyNumber = (String) params.get("stuStudyNumber");// 学号
		if (StringUtil.isBlank(stuStudyNumber)) {
			return responseMsg("REGSTU0007","REGSTU");
		}
		StudentReg studentReg = new StudentReg();
		studentReg.setStuPhone(stuPhone);
		studentReg.setStuEmail(stuEmail);
		studentReg.setStuPassword(stuPassword);
		studentReg.setStuStudyNumber(stuStudyNumber);
		String regTime = TimeUtil.dateToString(new Date());
		studentReg.setRegTime(regTime);
		String res = studentRegService.registerStu(studentReg);
		return responseMsg(res,"REGSTU");
	}

	/**
	 * 查询学生注册列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/queryStuReg")
	@ResponseBody
	public JSONObject queryStuReg(@RequestBody Map<String, Object> params) {
		JSONObject json = new JSONObject();
		/* 查询条件 */
		String stuPhone = (String) params.get("stuPhone");// 手机号
		String stuEmail = (String) params.get("stuEmail");// 邮箱
		String stuStudyNumber = (String) params.get("stuStudyNumber");// 学号
		String status = (String) params.get("status");//状态
		StudentReg studentReg = new StudentReg();

		// 非空判断,不包括空格
		if (StringUtil.isNotBlank(stuPhone)) {
			studentReg.setStuPhone(stuPhone);
		}
		if (StringUtil.isNotBlank(stuEmail)) {
			studentReg.setStuEmail(stuEmail);
		}
		if (StringUtil.isNotBlank(stuStudyNumber)) {
			studentReg.setStuStudyNumber(stuStudyNumber);
		}
		if(StringUtil.isNotBlank(status)) {
			if(!AuditStatu.PASS.getP().equals(status) && !AuditStatu.NOPASS.getP().equals(status) &&!AuditStatu.PENDING.getP().equals(status)) {
				return responseMsg("AUDIT0015","AUDIT");
			}
			studentReg.setStatus(status);
		}

		List<StudentReg> stuRegList = studentRegService.queryStuReg(studentReg);
		json.put("stuRegList", stuRegList);
		return json;
	}

	@RequestMapping("/auditStudentReg")
	@ResponseBody
	public JSONObject auditStudentReg(HttpServletRequest request,@RequestBody Map<String, Object> params) {
		/* 条件判断 */
		// session里面取adminId
		Admin admin = (Admin) request.getSession().getAttribute("admin");
		String adminId = admin.getId();
		if (StringUtil.isBlank(adminId)) {
			// session失效
			return responseMsg("AUDIT0012","AUDIT");
		}
		String stuRegId = (String) params.get("stuRegId");
		if (StringUtil.isBlank(stuRegId)) {
			return responseMsg("AUDIT0013","AUDIT");
		}
		String auditResult = (String) params.get("auditResult");
//		if (StringUtil.isBlank(auditResult)) {
//			return responseMsg("AUDIT0013","AUDIT");
//		}
		String dealExplain = (String) params.get("dealExplain");
//		if (StringUtil.isBlank(dealExplain)) {
//			return responseMsg("AUDIT0013","AUDIT");
//		}
		String status = (String) params.get("status");
		if (StringUtil.isBlank(status)) {
			return responseMsg("AUDIT0013","AUDIT");
		}
		//判断处理类型
		if(!AuditStatu.PASS.getP().equals(status) && !AuditStatu.NOPASS.getP().equals(status)) {
			return responseMsg("AUDIT0014","AUDIT");
		}
		String retCode = studentRegService.auditStudentReg(adminId, stuRegId, auditResult, dealExplain, status);
		return responseMsg(retCode,"AUDIT");
	}
	
	/*错误码返回*/
	private JSONObject responseMsg(String code,String type) {
		JSONObject json = new JSONObject();
		Dict dict = new Dict();
		dict.setCode(code);
		dict.setType(type);
		List<Dict> dictList = dictService.queryDict(dict);
		dict = dictList.get(0);
		json.put("retCode", dict.getCode());
		json.put("retMsg", dict.getValue());
		return json;
	}
}
