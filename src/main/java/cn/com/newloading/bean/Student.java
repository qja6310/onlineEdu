package cn.com.newloading.bean;

public class Student extends Role{

	private String id;
	private String stuPhone;
	private String stuEmail;
	private String stuPassword;
	private String stuStudyNumber;
	private String role;
	public Student() {
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStuPhone() {
		return stuPhone;
	}
	public void setStuPhone(String stuPhone) {
		this.stuPhone = stuPhone;
	}
	public String getStuEmail() {
		return stuEmail;
	}
	public void setStuEmail(String stuEmail) {
		this.stuEmail = stuEmail;
	}
	public String getStuPassword() {
		return stuPassword;
	}
	public void setStuPassword(String stuPassword) {
		this.stuPassword = stuPassword;
	}
	public String getStuStudyNumber() {
		return stuStudyNumber;
	}
	public void setStuStudyNumber(String stuStudyNumber) {
		this.stuStudyNumber = stuStudyNumber;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
