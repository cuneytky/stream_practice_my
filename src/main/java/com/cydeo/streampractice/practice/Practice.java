package com.cydeo.streampractice.practice;

import com.cydeo.streampractice.model.*;
import com.cydeo.streampractice.service.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Practice {

    public static CountryService countryService;
    public static DepartmentService departmentService;
    public static EmployeeService employeeService;
    public static JobHistoryService jobHistoryService;
    public static JobService jobService;
    public static LocationService locationService;
    public static RegionService regionService;

    public Practice(CountryService countryService, DepartmentService departmentService,
                    EmployeeService employeeService, JobHistoryService jobHistoryService,
                    JobService jobService, LocationService locationService,
                    RegionService regionService) {

        Practice.countryService = countryService;
        Practice.departmentService = departmentService;
        Practice.employeeService = employeeService;
        Practice.jobHistoryService = jobHistoryService;
        Practice.jobService = jobService;
        Practice.locationService = locationService;
        Practice.regionService = regionService;

    }

    // You can use the services above for all the CRUD (create, read, update, delete) operations.
    // Above services have all the required methods.
    // Also, you can check all the methods in the ServiceImpl classes inside the service.impl package, they all have explanations.

    // 1- Display all the employees
    public static List<Employee> getAllEmployees() {
        return employeeService.readAll();
    }

    // 2- Display all the countries
    public static List<Country> getAllCountries() {
        return countryService.readAll();
    }

    // 3-Display all the departments
    public static List<Department> getAllDepartments() {
        return departmentService.readAll();
    }

    // 4- Display all the jobs
    public static List<Job> getAllJobs() {
        return jobService.readAll();
    }

    // 5-Display all the locations
    public static List<Location> getAllLocations() {
        return locationService.readAll();
    }

    // 6-Display all the regions
    public static List<Region> getAllRegions() {
        return regionService.readAll();
    }

    // 7-Display all the job histories
    public static List<JobHistory> getAllJobHistories() {
        return jobHistoryService.readAll();
    }

    //******** buraya kadar sadece return yazdık. bundan sonra stream var


    // 8- Display all the employees' first names
    public static List<String> getAllEmployeesFirstName() {
/* Solution-1:
        return employeeService.readAll().stream()
              .map(employee -> employee.getFirstName()
             .collect(Collectors.toList());
 */
        // Solution-2:
        return employeeService.readAll().stream()
                .map(Employee::getFirstName)
                .collect(Collectors.toList());
    }

    // 9- Display all the countries' names
    public static List<String> getAllCountryNames() {
        return countryService.readAll().stream()
                .map(Country::getCountryName)
                .collect(Collectors.toList());
    }

    // 10- Display all the departments' managers' first names
    public static List<String> getAllDepartmentManagerFirstNames() {

        // Solution-1:
      /*  return departmentService.readAll().stream()
                .map(department -> department.getManager())
                .map(manager -> manager.getFirstName())
                .collect(Collectors.toList());*/

        // Solution-2:
        /* return departmentService.readAll().stream()
                .map(Department::getManager)
                .map(Employee::getFirstName)
                .collect(Collectors.toList());*/

        // Solution-3:
        return departmentService.readAll().stream()
                .map(department -> department.getManager().getFirstName())
                .collect(Collectors.toList());
    }

    // 11- Display all the departments where manager name of the department is 'Steven'
    public static List<Department> getAllDepartmentsWhichManagerFirstNameIsSteven() {
        return departmentService.readAll().stream()
                .filter(department -> department.getManager().getFirstName().equals("Steven"))
                .collect(Collectors.toList());
    }

    // 12- Display all the departments where postal code of the location of the department is '98199'
    public static List<Department> getAllDepartmentsWhereLocationPostalCodeIs98199() {
        return departmentService.readAll().stream()
                .filter(department -> department.getLocation().getPostalCode().equals("98199"))
                .collect(Collectors.toList());
    }

    // 13- Display the region of the IT department
    public static Region getRegionOfITDepartment() throws Exception {
        // Solution-1:
       /* return departmentService.readAll().stream()
                .filter(department -> department.getDepartmentName().equals("IT"))
                .findFirst().get().getLocation().getCountry().getRegion();*/
        /* findFirst()=> bunun findAny den farkı interview sorusu? açıklayabilir ol. BU Optional dır null pointer ı tutar
           get() => bu kontrol etmeden getirir. null pointer verebilir

         */

        // Solution-2:
        return departmentService.readAll().stream()
                .filter(department -> department.getDepartmentName().equals("IT"))
                .findFirst().orElseThrow(() -> new Exception("No department found"))
                .getLocation().getCountry().getRegion();
        /*
        orElseThrow() => burada ()-> throw Exception("...") kullanamayız. cunku throw return etmez.stop eder.
         */
    }

    // 14- Display all the departments where the region of department is 'Europe'
    public static List<Department> getAllDepartmentsWhereRegionOfCountryIsEurope() {
       return departmentService.readAll().stream()
               .filter(department -> department.getLocation().getCountry().getRegion().getRegionName().equals("Europe"))
               .collect(Collectors.toList());
    }

    //*** 15- Display if there is any employee with salary less than 1000. If there is none, the method should return true
    public static boolean checkIfThereIsNoSalaryLessThan1000() {
        // solution-1: all match : tüm olasılıkların 1000 den buyuk olması durumunu kontrol ettık. esleşme VARSA true doner
       /*      return employeeService.readAll().stream()
                   .allMatch(employee -> employee.getSalary()>1000);*/

       /*  solution-2: non-match : tüm olasılıkların 1000 den küçük olması durumunu kontrol ettık. esleşme YOKSA true doner
          return employeeService.readAll().stream()
                   .noneMatch(employee -> employee.getSalary() < 1000);*/
        //solution-2.1:  .noneMatch(employee -> !(employee.getSalary() > 1000))

        // solution-3: any-match:
        return !employeeService.readAll().stream()
                .anyMatch(employee -> employee.getSalary() < 1000);
    }


    // 16 - Check if the salaries of all the employees in IT department are greater than 2000 (departmentName: IT)
    public static boolean checkIfThereIsAnySalaryGreaterThan2000InITDepartment() {
        // solution-1:
        return employeeService.readAll().stream()
                .filter(employee -> employee.getDepartment().getDepartmentName().equals("IT"))
                .noneMatch(employee -> employee.getSalary() < 2000);

        /*solution-2:
        return employeeService.readAll().stream()
                .filter(employee -> employee.getDepartment().getDepartmentName().equals("IT"))
                .allMatch(employee -> employee.getSalary() > 2000);*/

        /*solution-3:
        return !employeeService.readAll().stream()
                .filter(employee -> employee.getDepartment().getDepartmentName().equals("IT"))
                .anyMatch(employee -> employee.getSalary() < 2000);*/
    }

    // 17- Display all the employees whose salary is less than 5000
    public static List<Employee> getAllEmployeesWithLessSalaryThan5000() {
       return employeeService.readAll().stream()
               .filter(employee -> employee.getSalary()<5000)
               .collect(Collectors.toList());
    }

    // 18- Display all the employees whose salary is between 6000 and 7000
    public static List<Employee> getAllEmployeesSalaryBetween() {

        /*solution-1: faster
        return employeeService.readAll().stream()
              .filter(employee -> employee.getSalary()> 6000 && employee.getSalary()<7000)
              .collect(Collectors.toList());*/

       // solution-2:
        return employeeService.readAll().stream()
                .filter(employee -> employee.getSalary() > 6000 )
                .filter(employee -> employee.getSalary() < 7000)
                .collect(Collectors.toList());
    }

    // 19- Display the salary of the employee Grant Douglas (lastName: Grant, firstName: Douglas)
    public static Long getGrantDouglasSalary() throws Exception {
        // Solution-1:
        /* return employeeService.readAll().stream()
                .filter(employee -> employee.getFirstName().equals("Douglas"))
                .filter(employee -> employee.getLastName().equals("Grant"))
                .findFirst().orElseThrow(()-> new Exception("Douglas Grant not found")).getSalary();
          - ilk uygun ısımde kişileri buluyor ve salary getiriyor ama bulamazsa orElseThrow ile bulunmadı notu getirip prg hata vermeden devam ediyor.
                */

        // Solution-2:
        return employeeService.readAll().stream()
                .filter(employee -> employee.getFirstName().equals("Douglas") && employee.getLastName().equals("Grant"))
                .findFirst().orElseThrow(()-> new Exception("Douglas Grant not found")).getSalary();
    }

    // 20- Display the maximum salary an employee gets
    public static Long getMaxSalary()  {
        // solution.1:
       /* return employeeService.readAll().stream()
                 .sorted(Comparator.comparing(Employee::getSalary).reversed())
                 .findFirst().get().getSalary();*/

        // solution.2
       /* return employeeService.readAll().stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .limit(1).collect(Collectors.toList()).get(0).getSalary();*/

        //solution.3
//        return employeeService.readAll().stream()
//                .max(Comparator.comparing(Employee::getSalary))
//                .get().getSalary();

        //solution.4
      /*  return employeeService.readAll().stream()
                .map(Employee::getSalary)
                .reduce((salary1,salary2) -> salary1>salary2 ? salary1:salary2)
                .get();*/

        //solution.5
       /* return employeeService.readAll().stream()
                .map(Employee::getSalary)
                .reduce(Long::max)
                .get();*/

        //solution.6
      /*  return employeeService.readAll().stream()
                .map(Employee::getSalary)
                .collect(Collectors.maxBy(Comparator.comparing(Long::longValue)))
                .get();*/

        //solution.7
      /*  return employeeService.readAll().stream()
                .collect(Collectors.maxBy(Comparator.comparing(Employee::getSalary)))
                .get().getSalary();*/

        //solution.8
        return employeeService.readAll().stream()
                .mapToLong(Employee::getSalary)
                .max().getAsLong();
    }


    // 21- Display the employee(s) who gets the maximum salary
    public static List<Employee> getMaxSalaryEmployee() {

        return employeeService.readAll().stream() //throw except olduğundan maxSalray kullanamdık ya burada n sil y ada buraya try ctch ekle
                .filter(employee -> employee.getSalary().equals(getMaxSalary()))
                .collect(Collectors.toList());
    }

    // 22- Display the max salary employee's job
    public static Job getMaxSalaryEmployeeJob() {
        return getMaxSalaryEmployee().get(0).getJob();
    }

    // 23- Display the max salary in Americas Region
    public static Long getMaxSalaryInAmericasRegion() throws Exception {

        return employeeService.readAll().stream()
                .filter(employee -> employee.getDepartment()
                        .getLocation().getCountry().getRegion()
                        .getRegionName().equals("Americas"))
                .max(Comparator.comparing(Employee::getSalary))
                .get().getSalary();
    }

    // 24-  Display the second maximum salary an employee gets
    public static Long getSecondMaxSalary() {
        //solution.1
        return employeeService.readAll().stream()
                .sorted(Comparator.comparing(Employee::getSalary).reversed())
                .map(Employee::getSalary)
                .distinct()
                .skip(1) //direk skıp  kullanamadık kaç tane max var bilmiyoruz
                .findFirst().get();

        //solution.2
//        return employeeService.readAll().stream()
//                .filter(employee -> employee.getSalary().compareTo(getMaxSalary()) <0)
//                .sorted(Comparator.comparing(Employee::getSalary).reversed())
//                .findFirst().get().getSalary();

    }

    // 25- Display the employee(s) who gets the second maximum salary
    public static List<Employee> getSecondMaxSalaryEmployee() {
       return employeeService.readAll().stream()
               .filter(employee -> employee.getSalary().equals(getSecondMaxSalary()))
               .collect(Collectors.toList());
    }

    // 26- Display the minimum salary an employee gets
    public static Long getMinSalary() {
        //solution.1
//        return employeeService.readAll().stream()
//                .sorted(Comparator.comparing(Employee::getSalary))
//                .findFirst().get().getSalary();

        //solution.2
//        return employeeService.readAll().stream()
//                .sorted(Comparator.comparing(Employee::getSalary))
//                .limit(1).collect(Collectors.toList()).get(0).getSalary();

        //solution.3
//        return employeeService.readAll().stream()
//                .map(Employee::getSalary)
//                .reduce((salary1,salary2)-> salary1<salary2 ? salary1 : salary2)
//                .get();

        //solution.4
//        return employeeService.readAll().stream()
//                .min(Comparator.comparing(Employee::getSalary))
//                .get().getSalary();

        //solution.5
//        return employeeService.readAll().stream()
//                .map(Employee::getSalary)
//                .reduce(Long::min)
//                .get();

        //solution.6
//        return employeeService.readAll().stream()
//                .map(Employee::getSalary)
//                .collect(Collectors.minBy(Comparator.comparing(Long::longValue)))
//                .get();

        //solution.7
//        return employeeService.readAll().stream()
//                .collect(Collectors.minBy(Comparator.comparing(Employee::getSalary)))
//                .get().getSalary();

        //solution.8
        return employeeService.readAll().stream()
                .mapToLong(Employee::getSalary)
                .max().getAsLong();
    }

    // 27- Display the employee(s) who gets the minimum salary
    public static List<Employee> getMinSalaryEmployee() {
        return employeeService.readAll().stream() //throw except olduğundan maxSalray kullanamdık ya burada n sil y ada buraya try ctch ekle
                .filter(employee -> employee.getSalary().equals(getMinSalary()))
                .collect(Collectors.toList());
    }

    // 28- Display the second minimum salary an employee gets
    public static Long getSecondMinSalary()  {
                return employeeService.readAll().stream()
                .filter(employee -> employee.getSalary().compareTo(getMaxSalary()) > 0)
                .sorted(Comparator.comparing(Employee::getSalary))
                .findFirst().get().getSalary();
    }


    // 29- Display the employee(s) who gets the second minimum salary
    public static List<Employee> getSecondMinSalaryEmployee() {
        return employeeService.readAll().stream()
                .filter(employee -> employee.getSalary().equals(getSecondMinSalary()))
                .collect(Collectors.toList());

    }

    // 30- Display the average salary of the employees
    public static Double getAverageSalary() {

        //solution.1
//        return employeeService.readAll().stream()
//                .collect(Collectors.averagingDouble(Employee::getSalary));

        //solution.2
        return employeeService.readAll().stream()
                .mapToDouble(Employee::getSalary)
                .average().orElse(Double.NaN);   // extra information.... I don't know

    }

    // 31- Display all the employees who are making more than average salary
    public static List<Employee> getAllEmployeesAboveAverage() {
        return employeeService.readAll().stream()
                .filter(employee -> employee.getSalary()>getAverageSalary())
                .collect(Collectors.toList());
    }

    // 32- Display all the employees who are making less than average salary
    public static List<Employee> getAllEmployeesBelowAverage() {
        return employeeService.readAll().stream()
                .filter(employee -> employee.getSalary()< getAverageSalary())
                .collect(Collectors.toList());

    }

    // 33- Display all the employees separated based on their department id number
    public static Map<Long, List<Employee>> getAllEmployeesForEachDepartment() {
         return employeeService.readAll().stream()
                 .collect(Collectors.groupingBy(employee -> employee.getDepartment().getId()));

    }

    // 34- Display the total number of the departments
    public static Long getTotalDepartmentsNumber() {
        //solution.1
//        return departmentService.readAll().stream()
//                .sorted(Comparator.comparing(Department::getDepartmentName))
//                .count();
        //I solved it, it didn't give an error. Is it possible ??  ------> ask it?

        //solution.2
//        return departmentService.readAll().stream()
//                .collect(Collectors.counting());

        ////solution.3
 //       return departmentService.readAll().stream().count();

        //solution.4
        return(long) departmentService.readAll().size();
    }

    // 35- Display the employee whose first name is 'Alyssa' and manager's first name is 'Eleni' and department name is 'Sales'
    public static Employee getEmployeeWhoseFirstNameIsAlyssaAndManagersFirstNameIsEleniAndDepartmentNameIsSales() throws Exception {
        return employeeService.readAll().stream()
                .filter(employee -> employee.getFirstName().equals("Alyssa")) // && diyerek 3 unu bağlayabılırsın
                .filter(employee -> employee.getManager().getFirstName().equals("Eleni"))
                .filter(employee -> employee.getDepartment().getDepartmentName().equals("Sales"))
                .findAny().get(); // bir isim gerek o yuzden findany()

    }

    // 36- Display all the job histories in ascending order by start date
    public static List<JobHistory> getAllJobHistoriesInAscendingOrder() {

        return jobHistoryService.readAll().stream()
                .sorted(Comparator.comparing(JobHistory::getStartDate))
                .collect(Collectors.toList());

    }

    // 37- Display all the job histories in descending order by start date
    public static List<JobHistory> getAllJobHistoriesInDescendingOrder() {
          return jobHistoryService.readAll().stream()
                  .sorted(Comparator.comparing(JobHistory::getStartDate).reversed())
                  .collect(Collectors.toList());
    }

    // 38- Display all the job histories where the start date is after 01.01.2005
    public static List<JobHistory> getAllJobHistoriesStartDateAfterFirstDayOfJanuary2005() {

        return jobHistoryService.readAll().stream()             //LocalDateTime diğer date methodları var...
                .filter(jobHistory -> jobHistory.getStartDate().isAfter(LocalDate.of(2005,1,1))) //LocalDate içinde date metodlar
                .collect(Collectors.toList());

    }

    // 39- Display all the job histories where the end date is 31.12.2007 and the job title of job is 'Programmer'
    public static List<JobHistory> getAllJobHistoriesEndDateIsLastDayOfDecember2007AndJobTitleIsProgrammer() {
        return jobHistoryService.readAll().stream()
                .filter(jobHistory -> jobHistory.getEndDate().equals(LocalDate.of(2007,12,31)))
                .filter(jobHistory -> jobHistory.getJob().getJobTitle().equals("Programmer"))
                .collect(Collectors.toList());

        //solution-2:
//        return jobHistoryService.readAll().stream()
//                .filter(jobHistory -> jobHistory.getEndDate().equals(LocalDate.of(2007, 12, 31)) && jobHistory.getJob().getJobTitle().equals("Programmer"))
//                .collect(Collectors.toList());

        //solution-3:
 //        LocalDate endDate = LocalDate.of(2007, 12, 31);
//
//        return jobHistoryService.readAll().stream()
//                .filter(p->p.getEndDate().equals(endDate)
//                        &&
//                        p.getJob().getJobTitle().equalsIgnoreCase("Programmer")).collect(Collectors.toList());

    }

    // 40- Display the employee whose job history start date is 01.01.2007 and job history end date is 31.12.2007 and department's name is 'Shipping'
    public static Employee getEmployeeOfJobHistoryWhoseStartDateIsFirstDayOfJanuary2007AndEndDateIsLastDayOfDecember2007AndDepartmentNameIsShipping() throws Exception {
        return jobHistoryService.readAll().stream()
                .filter(jobHistory -> jobHistory.getStartDate().equals(LocalDate.of(2007,1,1)))
                .filter(jobHistory -> jobHistory.getEndDate().isEqual(LocalDate.of(2007,12,31)))
                .filter(jobHistory -> jobHistory.getDepartment().getDepartmentName().equals("Shipping"))
                .findFirst().get().getEmployee();

        // findFirst() -> Optional<JobHistory>  -> get() -> T (JobHistory)  ->  getEmployee()

    }

    // 41- Display all the employees whose first name starts with 'A'
    public static List<Employee> getAllEmployeesFirstNameStartsWithA() {
        return employeeService.readAll().stream()
                .filter(employee -> employee.getFirstName().startsWith("A")) //string method startwith
                .collect(Collectors.toList());

    }

    // 42- Display all the employees whose job id contains 'IT'
    public static List<Employee> getAllEmployeesJobIdContainsIT() {
        return employeeService.readAll().stream()
                .filter(employee -> employee.getJob().getId().contains("IT"))
                .collect(Collectors.toList());

    }

    // 43- Display the number of employees whose job title is programmer and department name is 'IT'
    public static Long getNumberOfEmployeesWhoseJobTitleIsProgrammerAndDepartmentNameIsIT() {
        return employeeService.readAll().stream()
                .filter(employee -> employee.getJob().getJobTitle().equals("Programmer"))
                .filter(employee -> employee.getDepartment().getDepartmentName().equals("IT"))
                .count();

    }

    // 44- Display all the employees whose department id is 50, 80, or 100
    public static List<Employee> getAllEmployeesDepartmentIdIs50or80or100() {
        return employeeService.readAll().stream()
                .filter(employee -> employee.getDepartment().getId().equals(50)
                || employee.getDepartment().getId().equals(80L)
                || employee.getDepartment().getId().equals(100L))
                .collect(Collectors.toList());
    }

    // 45- Display the initials of all the employees
    // Note: You can assume that there is no middle name
    public static List<String> getAllEmployeesInitials() {
        return employeeService.readAll().stream()
                .map(employee -> {
                    String firstInitial = employee.getFirstName().substring(0,1);
                    String secondInitial = employee.getLastName().substring(0,1);
                    return firstInitial+secondInitial;
                })
                .collect(Collectors.toList());
        // diğer yaklaşım....
//        public static List<String> getAllEmployeesInitials() {
//            List<String> initialsList = employeeService.readAll().stream()
//                    .map(em->em.getFirstName().charAt(0)+""+em.getLastName().charAt(0))
//                    .collect(Collectors.toList());
//            return initialsList;
//        }
    }

    // 46- Display the full names of all the employees
    public static List<String> getAllEmployeesFullNames() {
        return employeeService.readAll().stream()
                .map(employee -> employee.getFirstName()+employee.getLastName())
                .collect(Collectors.toList());
    }

    // 47- Display the length of the longest full name(s)
    public static Integer getLongestNameLength()  {
        return getAllEmployeesFullNames().stream()
                .max(Comparator.comparing(String::length))
                .get().length();
    }

    // 48- Display the employee(s) with the longest full name(s)
    public static List<Employee> getLongestNamedEmployee() {
        return employeeService.readAll().stream()
                .filter(employee ->
                        employee.getFirstName().length() +
                                employee.getLastName().length() + 1
                                == getLongestNameLength())
                .collect(Collectors.toList());
    }

    // 49- Display all the employees whose department id is 90, 60, 100, 120, or 130
    public static List<Employee> getAllEmployeesDepartmentIdIs90or60or100or120or130() {
        return employeeService.readAll().stream()
                .filter(employee -> employee.getDepartment().getId().equals(90)
                        || employee.getDepartment().getId().equals(60L)
                        || employee.getDepartment().getId().equals(100L)
                        || employee.getDepartment().getId().equals(120L)
                        || employee.getDepartment().getId().equals(130L))
                .collect(Collectors.toList());
    }

    // 50- Display all the employees whose department id is NOT 90, 60, 100, 120, or 130
    public static List<Employee> getAllEmployeesDepartmentIdIsNot90or60or100or120or130() {

//        return employeeService.readAll().stream()
//                .filter((p)->p.getDepartment().getId() !=90
//                        && p.getDepartment().getId()!=60
//                        && p.getDepartment().getId()!=100
//                        && p.getDepartment().getId()!=120
//                        && p.getDepartment().getId()!=130 )
//                .collect(Collectors.toList());

        return employeeService.readAll().stream()
                .filter(employee -> !getAllEmployeesDepartmentIdIsNot90or60or100or120or130().contains(employee))
                .collect(Collectors.toList());


    }


}
