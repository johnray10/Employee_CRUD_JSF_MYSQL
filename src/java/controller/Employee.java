/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author JayJomJohn
 */
@ManagedBean
@RequestScoped
public class Employee {

    private int id;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String address;

    private ArrayList employeeList;

    private Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    Connection conn;
    ResultSet rs;
    PreparedStatement pst;
    Statement stmt;

    // Used to establish connection
    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees", "root", "");
            System.out.println("You Are Connected To Database");
        } catch (Exception e) {
            System.out.println(e);
        }
        return conn;
    }

    // Used to fetch all records
    public ArrayList employeesList() {
        try {
            employeeList = new ArrayList();
            conn = getConnection();
            stmt = getConnection().createStatement();
            rs = stmt.executeQuery("SELECT * FROM tbl_employee");
            while (rs.next()) {
                Employee employee = new Employee();
                employee.setId(rs.getInt("id"));
                employee.setName(rs.getString("name"));
                employee.setEmail(rs.getString("email"));
                employee.setPassword(rs.getString("password"));
                employee.setGender(rs.getString("gender"));
                employee.setAddress(rs.getString("address"));
                employeeList.add(employee);
            }
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return employeeList;
    }

    // Used to save user record
    public String save() {
        int result = 0;
        try {
            conn = getConnection();
            pst = conn.prepareStatement("insert into tbl_employee(name,email,password,gender,address) values(?,?,?,?,?)");
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);
            pst.setString(4, gender);
            pst.setString(5, address);
            result = pst.executeUpdate();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        if (result != 0) {
            return "index.xhtml?faces-redirect=true";
        } else {
            return "create.xhtml?faces-redirect=true";
        }
    }

    // Used to fetch record to update
    public String edit(int id) {

        System.out.println(id);
        try {
            conn = getConnection();
            stmt = getConnection().createStatement();
            rs = stmt.executeQuery("select * from tbl_employee where id = " + (id));
            rs.next();
            Employee employee = new Employee();
            employee.setId(rs.getInt("id"));
            employee.setName(rs.getString("name"));
            employee.setEmail(rs.getString("email"));
            employee.setGender(rs.getString("gender"));
            employee.setAddress(rs.getString("address"));
            employee.setPassword(rs.getString("password"));
            System.out.println(rs.getString("password"));
            sessionMap.put("editEmployee", employee);
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "/edit.xhtml?faces-redirect=true";
    }

    // Used to update user record
    public String update(Employee e) {
        //int result = 0;
        try {
            conn = getConnection();
            pst = conn.prepareStatement("update tbl_employee set name=?, email=?, password=?, gender=?, address=? where id=?");
            pst.setString(1, e.getName());
            pst.setString(2, e.getEmail());
            pst.setString(3, e.getPassword());
            pst.setString(4, e.getGender());
            pst.setString(5, e.getAddress());
            pst.setInt(6, e.getId());
            pst.executeUpdate();
            conn.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return "/index.xhtml?faces-redirect=true";
    }

    // Used to delete user record
    public void delete(int id) {
        try {
            conn = getConnection();
            pst = conn.prepareStatement("delete from tbl_employee where id = " + id);
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Used to set user gender
    public String getGenderName(char gender) {
        if (gender == 'M') {
            return "Male";
        } else {
            return "Female";
        }
    }
}
