import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Admin {

    private static String url = "jdbc:mysql://localhost:3306/employeeData";
    private static String user = "root";
    private static String password = "Yessica6446!";
    private HashMap<Integer, Employee> employees;
    private HashMap<Integer, Job> jobs;
    private HashMap<Integer, Integer> employee_job_titles;
    private HashMap<Integer, Division> divisions;
    private HashMap<Integer, Integer> employee_division;
    private HashMap<Integer, List<Payroll>> payrolls;
    private HashMap<Integer, Address> address;
    private Scanner scanner;
    
    public Admin() {
        init();
    }

    private void init() {
        // Map empID to employee object
        employees = new HashMap<>();
        jobs = new HashMap<>();
        employee_job_titles = new HashMap<>();
        divisions = new HashMap<>();
        employee_division = new HashMap<>();
        payrolls = new HashMap<>();
        scanner = new Scanner(System.in);

        // Initialize jobs locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT * FROM job_titles";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                Job job = new Job();
                job.setJobID(rs.getInt("job_title_id"));
                job.setTitle(rs.getString("job_title"));

                jobs.put(job.getJobID(), job);
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }

        // Initialize employee_job_titles locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT * FROM employee_job_titles";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                employee_job_titles.put(rs.getInt("empid"), rs.getInt("job_title_id"));
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }

        // Initialize divisions locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT * FROM division";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                Division division = new Division();
                division.setDivisionID(rs.getInt("ID"));
                division.setName(rs.getString("Name"));
                division.setCity(rs.getString("city"));
                division.setAddressLine1(rs.getString("addressLine1"));
                division.setAddressLine2(rs.getString("addressLine2"));
                division.setState(rs.getString("state"));
                division.setCountry(rs.getString("country"));
                division.setPostalCode(rs.getString("postalCode"));

                divisions.put(division.getDivisionID(), division);
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }

        // Initialize employee_division locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT * FROM employee_division";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                employee_division.put(rs.getInt("empid"), rs.getInt("div_ID"));
            }
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
                
        // Initialize employees locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) 
        {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT * FROM employees";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                Employee employee = new Employee();
                employee.setEmpID(rs.getInt("empid"));
                employee.setFirstName(rs.getString("Fname"));
                employee.setLastName(rs.getString("Lname"));
                employee.setEmail(rs.getString("email"));
                employee.setHireDate(rs.getDate("HireDate"));
                employee.setSalary(rs.getDouble("Salary"));
                employee.setSsn(rs.getString("ssn"));
                employees.put(employee.getEmpID(), employee);
            }
            myConn.close();
        }  catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }

        // Initialize address locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) 
        {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT * FROM address";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                Address a = new Address();
                a.setEmpID(rs.getInt("empid"));
                a.setGender(rs.getString("gender"));
                a.setPronouns(rs.getString("pronouns"));
                a.setIdentifiedRace(rs.getString("identified_race"));
                a.setDob(rs.getDate("dob"));
                a.setPhone(rs.getString("mobile_phone"));
                a.setCityID(rs.getInt("city_id"));
                a.setStateID(rs.getInt("state_id"));
                address.put(a.getEmpID(), a);
            }
            myConn.close();
        }  catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }

        // Initialize payroll locally
        try (Connection myConn = DriverManager.getConnection(url, user, password)) 
        {
            Statement myStmt = myConn.createStatement();
            String query = "SELECT * FROM payroll";
            ResultSet rs = myStmt.executeQuery(query);

            while (rs.next()) {
                Payroll payroll = new Payroll();
                payroll.setPayID(rs.getInt("payID"));
                payroll.setPayDate(rs.getDate("pay_date"));
                payroll.setEarnings(rs.getDouble("earnings"));
                payroll.setFedTax(rs.getDouble("fed_tax"));
                payroll.setFedMed(rs.getDouble("fed_med"));
                payroll.setFedSS(rs.getDouble("fed_SS"));
                payroll.setStateTax(rs.getDouble("state_tax"));
                payroll.setRetire401k(rs.getDouble("retire_401k"));
                payroll.setHealthCare(rs.getDouble("health_care"));
                payroll.setEmpID(rs.getInt("empid"));

                if (!payrolls.containsKey(payroll.getEmpID())) {
                    List<Payroll> l = new ArrayList<>();
                    l.add(payroll);
                    payrolls.put(payroll.getEmpID(), l);
                } else {
                    payrolls.get(payroll.getEmpID()).add(payroll);
                }
            }
            myConn.close();
        }  catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }

        // for (List<Payroll> l : payrolls.values()) {
        //     System.out.println(l.size());
        // }
    }

    public boolean displayMenu() {
        System.out.println("What would you like to do?");
        System.out.println("0: Quit");
        System.out.println("1: Get Employee Information with Pay Statement History");
        System.out.println("2: Get Total Pay for Month by Job Title");
        System.out.println("3: Get Total Pay for Month by Division");
        System.out.println("4: Search for an employee");
        System.out.println("5: Update an employee's data");
        System.out.println("6: Add a new employee");
        System.out.println("7: Delete an employee");
        System.out.println("8: Increase employee salary by % if in range");
        System.out.println("9: Update all employee's salary less than amount");
        int option = validateIntegerInput();

        switch (option) {
            case 0:
                return false;
            case 1:
                System.out.println("You selected to get employee information with pay statement history");
                break;
            case 2:
                getTotalPayForMonthByJobTitle();
                break;
            case 3:
                getTotalPayForMonthByDivision();
                break;
            case 4:
                searchEmployee();
                break;
            case 5:
                updateEmployeeData();
                break;
            case 6:
                addEmployee();
                break;
            case 7:
                deleteEmployee();
                break;
            case 8:
                System.out.println("You selected to update employee's salary by % if in range");
                break;
            case 9:
                System.out.println("You selected to update all employee's salary less than amount");
                break;
        }
        return true;
    }

    public HashMap<Integer, Employee> getEmployees() {
        return employees;
    }

    public HashMap<Integer, Division> getDivisions() {
        return divisions;
    }

    public HashMap<Integer, Job> getJobs() {
        return jobs;
    }

    public void addEmployee() {
        Employee newEmployee = createEmployee();
        int empID;

        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "INSERT INTO employees (Fname, Lname, email, HireDate, Salary, ssn) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, newEmployee.getFirstName());
            ps.setString(2, newEmployee.getLastName());
            ps.setString(3, newEmployee.getEmail());
            ps.setDate(4, newEmployee.getHireDate());
            ps.setDouble(5, newEmployee.getSalary());
            ps.setString(6, newEmployee.getSsn());
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            // System.out.println("Getting ResultSet");
            empID = rs.getInt(1);

            sqlCommand = "INSERT INTO employee_job_titles (empid, job_title_id) VALUES (?, ?)";
            ps = myConn.prepareStatement(sqlCommand);
            ps.setInt(1, empID);
            ps.setInt(2, newEmployee.getJobID());
            ps.executeUpdate();

            sqlCommand = "INSERT INTO employee_division (empid, div_ID) VALUES (?, ?)";
            ps = myConn.prepareStatement(sqlCommand);
            ps.setInt(1, empID);
            ps.setInt(2, newEmployee.getDivID());
            ps.executeUpdate();

            employees.put(empID, newEmployee);
            myConn.close();
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }

    private void deleteEmployee() {
        Employee e = searchEmployee();
        int empID = e.getEmpID();
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "DELETE FROM employees WHERE empid = ?";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand);
            ps.setInt(1, empID);
            ps.executeUpdate();

            employees.remove(empID);
            myConn.close();
        } catch (Exception error) {
            System.out.println("ERROR " + error.getLocalizedMessage());
        }
    }

    private Employee createEmployee() {
        Employee newEmployee = new Employee();
        Calendar calendar = Calendar.getInstance();
        displayJobSelection();
        System.out.println();
        System.out.print("What is the Job Title? (Enter ID) ");
        newEmployee.setJobID(validateIntegerInput());
        System.out.println();
        scanner.nextLine();
        displayDivisionSelection();
        System.out.println();
        System.out.print("What is the Division Name? (Enter ID) ");
        newEmployee.setDivID(validateIntegerInput());
        System.out.println();
        scanner.nextLine();
        System.out.print("What is the First Name? ");
        newEmployee.setFirstName(validateStringInput());
        System.out.print("What is the Last Name? ");
        newEmployee.setLastName(validateStringInput());
        System.out.print("What is the Email? ");
        newEmployee.setEmail(validateStringInput());
        System.out.print("What is the Hire Date? Enter Month: ");
        // Calendar.MONTH starts at index 0 = January ~ 11 = December
        calendar.set(Calendar.MONTH, validateIntegerInput() - 1);
        System.out.print("What is the Hire Date? Enter Day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.print("What is the Hire Date? Enter Year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date date = new Date(calendar.getTimeInMillis());
        newEmployee.setHireDate(date);
        System.out.print("What is the Salary? ");
        newEmployee.setSalary(validateDoubleInput());
        System.out.print("What is the SSN (no dashes)? ");
        // first validate SSN is numerical, then convert to String
        newEmployee.setSsn(String.valueOf(validateLongInput()));
        
        // for testing purposes, need to read nextLine() to wait for input before print
        // scanner.nextLine();
        // System.out.println(date);
        return newEmployee;
    }

    private void displayJobSelection() {
        for (Job job : jobs.values()) {
            System.out.println("Job ID: " + job.getJobID() + ": " + job.getTitle());
        }
    }

    private void displayDivisionSelection() {
        for (Division div : divisions.values()) {
            System.out.println("Division ID: " + div.getDivisionID() + ": " + div.getName());
        }
    }

    public void addPayroll() {
        Payroll payroll = createPayroll();

        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "INSERT INTO payroll (payID, pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care, empid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, payroll.getPayID());
            ps.setDate(2, payroll.getPayDate());
            ps.setDouble(3, payroll.getEarnings());
            ps.setDouble(4, payroll.getFedTax());
            ps.setDouble(5, payroll.getFedMed());
            ps.setDouble(6, payroll.getFedSS());
            ps.setDouble(7, payroll.getStateTax());
            ps.setDouble(8, payroll.getRetire401k());
            ps.setDouble(9, payroll.getHealthCare());
            ps.setInt(10, payroll.getEmpID());
            ps.executeUpdate();
            System.out.println("Executed adding payroll");

            if (!payrolls.containsKey(payroll.getEmpID())) {
                List<Payroll> l = new ArrayList<>();
                l.add(payroll);
                payrolls.put(payroll.getEmpID(), l);
            } else {
                payrolls.get(payroll.getEmpID()).add(payroll);
            }
            
            myConn.close();
        } catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }

    private Payroll createPayroll() {
        Payroll payroll = new Payroll();
        Calendar calendar = Calendar.getInstance();
        // payID is auto incremented based on current value tied with empID
        System.out.print("What is the Pay ID? ");
        payroll.setPayID(validateIntegerInput());
        System.out.print("What is the Pay Date? Enter Month: ");
        // Calendar.MONTH starts at index 0 = January ~ 11 = December
        calendar.set(Calendar.MONTH, validateIntegerInput() - 1);
        System.out.print("What is the Pay Date? Enter Day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.print("What is the Pay Date? Enter Year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date date = new Date(calendar.getTimeInMillis());
        
        payroll.setPayDate(date);
        System.out.print("What is the Earnings? ");
        payroll.setEarnings(validateDoubleInput());
        System.out.print("What is the Federal Tax? ");
        payroll.setFedTax(validateDoubleInput());
        System.out.print("What is the Federal Med? ");
        payroll.setFedMed(validateDoubleInput());
        System.out.print("What is the Federal SS? ");
        payroll.setFedSS(validateDoubleInput());
        System.out.print("What is the State Tax? ");
        payroll.setStateTax(validateDoubleInput());
        System.out.print("What is the Retire 401k? ");
        payroll.setRetire401k(validateDoubleInput());
        System.out.print("What is the Health Care? ");
        payroll.setHealthCare(validateDoubleInput());
        System.out.print("What is the Employee ID? ");
        payroll.setEmpID(validateIntegerInput());
        // set payroll.setEmpID
        return payroll;
    }

    private void getTotalPayForMonthByJobTitle() {
        displayJobSelection();
        System.out.println();
        System.out.print("Select Job Title (Enter ID): ");
        int jobID = validateIntegerInput();

        Calendar calendar = Calendar.getInstance();
        System.out.println("Select a date range");
        System.out.print("Enter starting month: ");
        calendar.set(Calendar.MONTH, validateMonthInput() - 1);
        System.out.print("Enter starting day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.print("Enter starting year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date startDate = new Date(calendar.getTimeInMillis());

        System.out.print("Enter ending month: ");
        calendar.set(Calendar.MONTH, validateMonthInput() - 1);
        System.out.print("Enter ending day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.print("Enter ending year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date endDate = new Date(calendar.getTimeInMillis());
        double totalPay = 0;
        String job_title = "";
        for (int empID : employee_job_titles.keySet()) {
            int job_title_id = employee_job_titles.getOrDefault(empID, 0);
            if (jobID == job_title_id) {
                job_title = jobs.get(jobID).getTitle();
                for (int eID : payrolls.keySet()) {
                    if (empID == eID) {
                        List<Payroll> p = payrolls.get(eID);
                        for (Payroll pr : p) {
                            if (pr.getPayDate().after(startDate) && pr.getPayDate().before(endDate)) {
                                totalPay += pr.getEarnings();

                            }
                        }
                    }
                }
            }
        }

        System.out.println("Total Pay between " + startDate + " and " + endDate + " for " + job_title + ": $" + totalPay);
    }

    private void getTotalPayForMonthByDivision() {
        displayDivisionSelection();
        System.out.println();
        System.out.print("Select Division (Enter ID): ");
        int divID = validateIntegerInput();

        Calendar calendar = Calendar.getInstance();
        System.out.println("Select a date range");
        System.out.print("Enter starting month: ");
        calendar.set(Calendar.MONTH, validateMonthInput() - 1);
        System.out.print("Enter starting day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.print("Enter starting year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date startDate = new Date(calendar.getTimeInMillis());

        System.out.print("Enter ending month: ");
        calendar.set(Calendar.MONTH, validateMonthInput() - 1);
        System.out.print("Enter ending day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.print("Enter ending year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date endDate = new Date(calendar.getTimeInMillis());
        double totalPay = 0;
        String divName = "";

        for (int empID : employee_division.keySet()) {
            int div_id = employee_division.getOrDefault(empID, 0);

            if (divID == div_id) {
                divName = divisions.get(divID).getName();
                for (int eID : payrolls.keySet()) {
                    if (empID == eID) {
                        List<Payroll> p = payrolls.get(eID);
                        for (Payroll pr : p) {
                            if (pr.getPayDate().after(startDate) && pr.getPayDate().before(endDate)) {
                                totalPay += pr.getEarnings();
                            }
                        }
                    }
                }
            }
        }

        System.out.println("Total Pay between " + startDate + " and " + endDate + " for " + divName + ": $" + totalPay);
    }

    private Address createAddress() {
        Address address = new Address();
        Calendar calendar = Calendar.getInstance();
        
        System.out.println("What is the Gender (M or F)? ");
        scanner.nextLine();
        address.setGender(validateStringInput());
        System.out.println("What are the pronouns? ");
        address.setPronouns(validateStringInput());
        System.out.println("What is the Identified Race? ");
        address.setIdentifiedRace(validateStringInput());
        System.out.println("What is the Date of Birth? Enter Month: ");
        // Calendar.MONTH starts at index 0 = January ~ 11 = December
        calendar.set(Calendar.MONTH, validateIntegerInput() - 1);
        System.out.println("What is the Date of Birth? Enter Day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.println("What is the Date of Birth? Enter Year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date date = new Date(calendar.getTimeInMillis());
        
        address.setDob(date);
        System.out.println("What is the Mobile Phone Number (no dashes)? ");
        address.setPhone(String.valueOf(validateLongInput()));
        // figure out what city and state ID are...
        System.out.println("What is the City ID? ");
        address.setCityID(validateIntegerInput());
        System.out.println("What is the State ID? ");
        address.setStateID(validateIntegerInput());
        return address;
    }

    private int validateMonthInput() {
        int input = 0;
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextInt();
                if (input >= 1 && input <= 12) {
                    valid = false;
                }
            } catch (Exception e) {
                System.out.println(e);
                // ignore what user typed to avoid infinite loop
                scanner.nextLine();
            }
        }
        return input;
    }

    private String validateStringInput() {
        String input = "";
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextLine();
                valid = false;
            } catch (Exception e) {
                System.out.println(e);
                // ignore what user typed to avoid infinite loop
                scanner.nextLine();
            }
        }
        return input;
    }

    private int validateIntegerInput() {
        int input = 0;
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextInt();
                valid = false;
            } catch (Exception e) {
                System.out.println(e);
                // ignore what user typed to avoid infinite loop
                scanner.nextLine();
            }
        }
        return input;
    }

    private long validateLongInput() {
        long input = 0;
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextLong();
                valid = false;
            } catch (Exception e) {
                System.out.println(e);
                // ignore what user typed to avoid infinite loop
                scanner.nextLine();
            }
        }
        return input;
    }

    private double validateDoubleInput() {
        double input = 0.0;
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextDouble();
                valid = false;
            } catch (Exception e) {
                System.out.println(e);
                // ignore what user typed to avoid infinite loop
                scanner.nextLine();
            }
        }
        return input;
    }

    private int validateSearchInput() {
        int input = 0;
        boolean valid = true;
        while (valid) {
            try {
                input = scanner.nextInt();
                if (input >= 1 && input <= 3) {
                    valid = false;
                }
            } catch (Exception e) {
                System.out.println(e);
                // ignore what user typed to avoid infinite loop
                scanner.nextLine();
            }
        }
        return input;
    }

    private Employee searchEmployee() {
        System.out.println("Select an option");
        System.out.println("1: Search by employee ID");
        System.out.println("2: Search by employee SSN");
        System.out.println("3: Search by employee NAME");
        int option = validateSearchInput();
        Employee e = null;

        switch (option) {
            case 1:
                e = getEmployeeByID();
                break;
            case 2:
                e = getEmployeeBySSN();
                break;
            case 3:
                e = getEmployeeByName();
                break;
        }

        if (e != null) {
            System.out.println("Employee ID: " + e.getEmpID());
            System.out.println("Full Name: " + e.getFirstName() + " " + e.getLastName());
            System.out.println("Email: " + e.getEmail());
            System.out.println("Hire Date: " + e.getHireDate());
            System.out.println("Salary: $" + e.getSalary());
            System.out.println("SSN: " + e.getSsn());
        } else {
            System.out.println(String.format("Employee not found"));
        }
        return e;
    }

    private Employee getEmployeeByID() {
        System.out.print("Enter employee's ID: ");
        int empID = validateIntegerInput();
        return employees.getOrDefault(empID, null);
    }
    
    private Employee getEmployeeBySSN() {
        System.out.print("Enter employee's SSN: ");
        scanner.nextLine();
        String ssn = validateStringInput();
        Employee e = null;
        for (Employee employee : employees.values()) {
            if (ssn.equals(employee.getSsn())) {
                return employee;
            }
        }
        return null;
    }

    private Employee getEmployeeByName() {
        System.out.println("Enter employee's Name: ");
        scanner.nextLine();
        String name = validateStringInput();
        for (Employee e : employees.values()) {
            String empName = e.getFirstName() + " " + e.getLastName();
            if (empName.toLowerCase().contains(name.toLowerCase())) {
                return e;
            }
        }
        return null;
    }

    private void updateEmployeeData() {
        System.out.println("What would you like to update?");
        System.out.println("1: First Name");
        System.out.println("2: Last Name");
        System.out.println("3: Email");
        System.out.println("4: Hire Date");
        System.out.println("5: Salary");
        System.out.println("6: SSN");
        int option = validateIntegerInput();

        switch (option) {
            case 1:
                updateEmployeeFirstName();
                break;
            case 2:
                updateEmployeeLastName();
                break;
            case 3:
                updateEmployeeEmail();
                break;
            case 4:
                updateEmployeeHireDate();
                break;
            case 5:
                updateEmployeeSalary();
                break;
            case 6:
                updateEmployeeSSN();
                break;
        }
    }

    private void updateEmployeeFirstName() {
        Employee e = searchEmployee();
        int empID = e.getEmpID();
        System.out.print("What is the new First Name? ");
        scanner.nextLine();
        String newValue = validateStringInput();
        
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "UPDATE employees SET Fname = ? WHERE empid = ?";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand);
            ps.setString(1, newValue);
            ps.setInt(2, empID);
            ps.executeUpdate();

            e.setFirstName(newValue);
            myConn.close();
        } catch (Exception error) {
            System.out.println("ERROR " + error.getLocalizedMessage());
        }
    }

    private void updateEmployeeLastName() {
        Employee e = searchEmployee();
        int empID = e.getEmpID();
        System.out.print("What is the new Last Name? ");
        scanner.nextLine();
        String newValue = validateStringInput();
        
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "UPDATE employees SET Lname = ? WHERE empid = ?";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand);
            ps.setString(1, newValue);
            ps.setInt(2, empID);
            ps.executeUpdate();

            e.setLastName(newValue);
            myConn.close();
        } catch (Exception error) {
            System.out.println("ERROR " + error.getLocalizedMessage());
        }
    }

    private void updateEmployeeEmail() {
        Employee e = searchEmployee();
        int empID = e.getEmpID();
        System.out.print("What is the new Email? ");
        scanner.nextLine();
        String newValue = validateStringInput();
        
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "UPDATE employees SET email = ? WHERE empid = ?";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand);
            ps.setString(1, newValue);
            ps.setInt(2, empID);
            ps.executeUpdate();

            e.setEmail(newValue);
            myConn.close();
        } catch (Exception error) {
            System.out.println("ERROR " + error.getLocalizedMessage());
        }
    }

    private void updateEmployeeHireDate() {
        Employee e = searchEmployee();
        int empID = e.getEmpID();
        Calendar calendar = Calendar.getInstance();
        System.out.println("What is the new Hire Date? ");
        System.out.print("Enter Month: ");
        // Calendar.MONTH starts at index 0 = January ~ 11 = December
        calendar.set(Calendar.MONTH, validateIntegerInput() - 1);
        System.out.print("Enter Day: ");
        calendar.set(Calendar.DATE, validateIntegerInput());
        System.out.print("Enter Year: ");
        calendar.set(Calendar.YEAR, validateIntegerInput());
        Date date = new Date(calendar.getTimeInMillis());
        
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "UPDATE employees SET HireDate = ? WHERE empid = ?";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand);
            ps.setDate(1, date);
            ps.setInt(2, empID);
            ps.executeUpdate();

            e.setHireDate(date);
            myConn.close();
        } catch (Exception error) {
            System.out.println("ERROR " + error.getLocalizedMessage());
        }
    }

    private void updateEmployeeSalary() {
        Employee e = searchEmployee();
        int empID = e.getEmpID();
        System.out.print("What is the new Salary? ");
        scanner.nextLine();
        double newValue = validateDoubleInput();
        
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "UPDATE employees SET Salary = ? WHERE empid = ?";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand);
            ps.setDouble(1, newValue);
            ps.setInt(2, empID);
            ps.executeUpdate();

            e.setSalary(newValue);
            myConn.close();
        } catch (Exception error) {
            System.out.println("ERROR " + error.getLocalizedMessage());
        }
    }

    private void updateEmployeeSSN() {
        Employee e = searchEmployee();
        int empID = e.getEmpID();
        System.out.print("What is the new SSN? ");
        scanner.nextLine();
        String newValue = validateStringInput();
        
        try (Connection myConn = DriverManager.getConnection( url, user, password )) 
        {
            String sqlCommand = "UPDATE employees SET ssn = ? WHERE empid = ?";
            PreparedStatement ps = myConn.prepareStatement(sqlCommand);
            ps.setString(1, newValue);
            ps.setInt(2, empID);
            ps.executeUpdate();

            e.setSsn(newValue);
            myConn.close();
        } catch (Exception error) {
            System.out.println("ERROR " + error.getLocalizedMessage());
        }
    }

    private void queryTester() {
        try (Connection myConn = DriverManager.getConnection(url, user, password)) 
        {
            Statement myStmt = myConn.createStatement();
            // String query = "SELECT e.*, ejt.job_title_id FROM employees e LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid";
            String query = "SELECT e.*, ejt.job_title_id, ed.div_ID, p.* FROM employees e LEFT JOIN employee_job_titles ejt ON e.empid = ejt.empid LEFT JOIN employee_division ed ON e.empid = ed.empid LEFT JOIN payroll p ON e.empid = p.empid";
            // String query = "SELECT * FROM division";
            ResultSet rs = myStmt.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colNum = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= colNum; i++) {
                    if (i > 1) System.out.print(", ");
                    String colVal = rs.getString(i);
                    System.out.print(rsmd.getColumnName(i) + ": " + colVal);
                }
                System.out.println();
            }
            myConn.close();
        }  catch (Exception e) {
            System.out.println("ERROR " + e.getLocalizedMessage());
        }
    }
}
