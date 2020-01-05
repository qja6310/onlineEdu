package cn.com.newloading.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import com.alibaba.fastjson.JSONObject;

import cn.com.newloading.bean.Admin;
import cn.com.newloading.bean.CurriculumLog;
import cn.com.newloading.bean.FileBean;
import cn.com.newloading.bean.Student;
import cn.com.newloading.bean.StudentAttend;
import cn.com.newloading.bean.StudentCurriculumLog;
import cn.com.newloading.bean.Teacher;
import cn.com.newloading.service.CurriculumLogService;
import cn.com.newloading.service.FileService;
import cn.com.newloading.service.TeacherService;
import cn.com.newloading.utils.TimeUtil;

@Controller
@RequestMapping("/CurriculumLog")
public class CurriculumLogController {

	@Autowired
	private CurriculumLogService service;

	@RequestMapping("/queryCurriculumLog")
	@ResponseBody
	public JSONObject queryCurriculumLog(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String cId = request.getParameter("courseId");
		Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
		List<CurriculumLog> list = service.queryCurriculumLog(cId, teacher.getId());
		json.put("list", list);
		return json;
	}
	
	@RequestMapping("/addCurriculumLog")
	@ResponseBody
	public JSONObject addCurriculumLog(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String cId = request.getParameter("courseId");
		Teacher teacher = (Teacher) request.getSession().getAttribute("teacher");
		
		String notice = request.getParameter("CurriculumLogNotice");
		String state = request.getParameter("CurriculumLogState");
		String time = TimeUtil.dateToString(new Date());
		CurriculumLog c=new CurriculumLog();
		c.setcId(cId);
		c.settId(teacher.getId());
		c.setNotice(notice);
		c.setState(state);
		c.setTime(time);
		int result = service.addCurriculumLog(c);
		if (result < 1) {
			json.put("code", "0");
			return json;
		}
		json.put("code", "1");
		return json;
	}


	@RequestMapping("/editHomeworkTime")
	@ResponseBody
	public JSONObject editHomeworkTime(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String id = request.getParameter("courseLogId");
		String time = request.getParameter("hwCommitTime");
		int result = service.editHomeworkTime(time, id);
		if (result < 1) {
			json.put("code", "0");
			return json;
		}
		json.put("code", "1");
		return json;
	}
	
	@RequestMapping("/addStudentCurriculumLog")
	@ResponseBody
	public JSONObject addStudentCurriculumLog(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		Student student = (Student) request.getSession().getAttribute("student");
		String clId = request.getParameter("courseLogId");
		String state = request.getParameter("stuCourceState");//出席 迟到 旷课
		String time = TimeUtil.dateToString(new Date());
		int result = service.addStudentCurriculumLog(student.getId(), clId, state, time);
		if (result < 1) {
			json.put("code", "0");
			return json;
		}
		json.put("code", "1");
		return json;
	}

	@RequestMapping("/queryStudentOnCourse")
	@ResponseBody
	public JSONObject queryStudentOnCourse(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String clId = request.getParameter("courseLogId");
		List<StudentCurriculumLog> list=service.queryStudentOnCourse(clId);
		json.put("list", list);
		return json;
	}

	@RequestMapping("/stuCommitHomework")
	@ResponseBody
	public JSONObject stuCommitHomework(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		Student student = (Student) request.getSession().getAttribute("student");
		String clId = request.getParameter("courseLogId");
		String state = request.getParameter("stuCommitState");//
		String time = TimeUtil.dateToString(new Date());
		int result = service.stuCommitHomework(state, time, student.getId(), clId);
		if (result < 1) {
			json.put("code", "0");
			return json;
		}
		json.put("code", "1");
		return json;
	}
	
	@RequestMapping("/studentAbsent")
	@ResponseBody
	public JSONObject studentAbsent(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		Student student = (Student) request.getSession().getAttribute("student");
		String sclId = request.getParameter("stuCourseLogId");
		String state = request.getParameter("stuAbsentState");//出席 迟到 缺席
		String time = TimeUtil.dateToString(new Date());
		int result = service.studentAbsent(state, sclId);
		if (result < 1) {
			json.put("code", "0");
			return json;
		}
		json.put("code", "1");
		return json;
	}

	@RequestMapping("/studentScore")
	@ResponseBody
	public JSONObject studentScore(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		Student student = (Student) request.getSession().getAttribute("student");
		String sclId = request.getParameter("stuCourseLogId");
		String stuScore = request.getParameter("stuScore");//分数
		String time = TimeUtil.dateToString(new Date());
		int result = service.studentScore(stuScore, sclId);
		if (result < 1) {
			json.put("code", "0");
			return json;
		}
		json.put("code", "1");
		return json;
	}

	
	@RequestMapping("/studentAttendCount")
	@ResponseBody
	public JSONObject studentAttendCount(HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String cId = request.getParameter("courseId");
		List<StudentAttend> list=service.studentAttendCount(cId);
		json.put("list", list);
		return json;
	}
}
