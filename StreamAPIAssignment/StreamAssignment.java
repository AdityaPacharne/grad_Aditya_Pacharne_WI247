import java.io.*;
import java.util.*;
import java.util.stream.*;

enum Desg {
    CLERK,
    PROGRAMMER,
    MANAGER
}

enum Gender {
    MALE,
    FEMALE
}

enum Dep {
    TECH,
    FINANCE,
    HR
}

class Employee {
    String name;
    int age;
    Gender gender;
    int salary;
    Desg designation;
    Dep department;

    Employee(String n, int a, Gender g, int s, Desg d, Dep de) {
        name = n;
        age = a;
        gender = g;
        salary = s;
        designation = d;
        department = de;
    }
}

class Helper {
    static void highest_paid(List<Employee> e) {
        e.stream()
         .max(Comparator.comparingInt(emp -> emp.salary))
         .ifPresent(emp ->
             System.out.println(emp.name + " : " + emp.salary)
         );
    }

    static void male_female(List<Employee> e) {
        Map<Boolean, Long> temp = e.stream().collect(Collectors.partitioningBy(emp -> emp.gender == Gender.MALE, Collectors.counting()));
        System.out.println("Males: " + temp.get(true) + ", Females: " + temp.get(false));
    }

    static void total_expense_department(List<Employee> e) {
        System.out.println(e.stream().collect(Collectors.groupingBy(emp -> emp.department, Collectors.summingInt(emp -> emp.salary))));
    }
    
    static void top_five_senior(List<Employee> e) {
        e.stream().sorted(Comparator.comparingInt((Employee emp) -> emp.age).reversed()).limit(5).forEach(emp -> System.out.println(emp.name + " : " + emp.age));
    }

    static void manager_names(List<Employee> e) {
        e.stream().filter(emp -> emp.designation == Desg.MANAGER).map(emp -> emp.name).forEach(System.out::println);
    }

    static void hike_salary(List<Employee> e) {
        e.stream().filter(emp -> emp.designation != Desg.MANAGER).forEach(emp -> emp.salary += 5000);
        System.out.println("Salary Hiked for everyone except Managers");
    }

    static void total_count(List<Employee> e) {
        System.out.println(e.size());
    }
}

public class temp {
    public static void main (String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        List<Employee> e = new ArrayList<Employee>();
        e.add(new Employee("A"  , 23, Gender.MALE   , 15000, Desg.CLERK     , Dep.FINANCE));
        e.add(new Employee("B"  , 34, Gender.FEMALE , 25000, Desg.PROGRAMMER, Dep.TECH));
        e.add(new Employee("C"  , 45, Gender.MALE   , 35000, Desg.MANAGER   , Dep.HR));
        e.add(new Employee("D"  , 56, Gender.FEMALE , 45000, Desg.CLERK     , Dep.FINANCE));
        e.add(new Employee("E"  , 67, Gender.MALE   , 55000, Desg.PROGRAMMER, Dep.HR));
        e.add(new Employee("F"  , 78, Gender.FEMALE , 65000, Desg.MANAGER   , Dep.TECH));
        e.add(new Employee("G"  , 89, Gender.MALE   , 75000, Desg.CLERK     , Dep.FINANCE));
        e.add(new Employee("H"  , 90, Gender.FEMALE , 85000, Desg.PROGRAMMER, Dep.HR));
        e.add(new Employee("I"  , 24, Gender.MALE   , 95000, Desg.MANAGER   , Dep.TECH));
        e.add(new Employee("J" , 35, Gender.FEMALE , 21000, Desg.CLERK     , Dep.FINANCE));
        e.add(new Employee("K" , 46, Gender.MALE   , 22000, Desg.PROGRAMMER, Dep.HR));
        e.add(new Employee("L" , 57, Gender.FEMALE , 23000, Desg.MANAGER   , Dep.TECH));
        e.add(new Employee("M" , 68, Gender.MALE   , 24000, Desg.CLERK     , Dep.FINANCE));
        e.add(new Employee("N" , 79, Gender.FEMALE , 25000, Desg.PROGRAMMER, Dep.HR));
        e.add(new Employee("O" , 80, Gender.MALE   , 26000, Desg.MANAGER   , Dep.TECH));
        e.add(new Employee("P" , 25, Gender.FEMALE , 27000, Desg.CLERK     , Dep.FINANCE));
        e.add(new Employee("Q" , 36, Gender.MALE   , 28000, Desg.PROGRAMMER, Dep.TECH));
        e.add(new Employee("R" , 47, Gender.FEMALE , 29000, Desg.MANAGER   , Dep.HR));
        e.add(new Employee("S" , 58, Gender.MALE   , 25100, Desg.CLERK     , Dep.FINANCE));
        e.add(new Employee("T" , 60, Gender.FEMALE , 25200, Desg.PROGRAMMER, Dep.TECH));

        while(true) {
            String ask =    """
                            1. Highest Paid Employee
                            2. Male & Female Employees
                            3. Total Expense Department Wise
                            4. Top 5 Senior Employees
                            5. Names of Managers
                            6. Hike Salary for every Employee (except Managers)
                            7. Total Employees
                            8. Quit
                            """;

            System.out.println(ask);

            int choice = Integer.parseInt(br.readLine());

            System.out.println("---------------------------");
            if(choice == 1)      Helper.highest_paid(e);
            else if(choice == 2) Helper.male_female(e);
            else if(choice == 3) Helper.total_expense_department(e);
            else if(choice == 4) Helper.top_five_senior(e);
            else if(choice == 5) Helper.manager_names(e);
            else if(choice == 6) Helper.hike_salary(e);
            else if(choice == 7) Helper.total_count(e);
            else break;
            System.out.println("---------------------------");
        }
    }
}

// Employee
// name
// age
// gender
// salary
// designation
// department
//
// Find the highest paid employee
// Find how many male and female employees
// Total Expense of company department wise
// Who is the top5 senior most employees
// Find only the names who all are managers
// Hike the salary for everyone except manager
// Find total number of employees
