package br.edu.ifsp.addthenewsoul;

import br.edu.ifsp.addthenewsoul.application.repository.inMemory.InMemoryAssetDAO;
import br.edu.ifsp.addthenewsoul.application.repository.inMemory.InMemoryEmployeeDAO;
import br.edu.ifsp.addthenewsoul.application.repository.inMemory.InMemoryInventoryDAO;
import br.edu.ifsp.addthenewsoul.application.repository.inMemory.InMemoryLocationDAO;
import br.edu.ifsp.addthenewsoul.domain.entities.asset.Asset;
import br.edu.ifsp.addthenewsoul.domain.entities.asset.Location;
import br.edu.ifsp.addthenewsoul.domain.entities.employee.Employee;
import br.edu.ifsp.addthenewsoul.domain.entities.employee.Role;
import br.edu.ifsp.addthenewsoul.domain.entities.inventory.Inventory;
import br.edu.ifsp.addthenewsoul.domain.usecases.asset.AssetDAO;
import br.edu.ifsp.addthenewsoul.domain.usecases.employee.EmployeeDAO;
import br.edu.ifsp.addthenewsoul.domain.usecases.employee.LoginEmployeeUseCase;
import br.edu.ifsp.addthenewsoul.domain.usecases.employee.LogoutEmployeeUseCase;
import br.edu.ifsp.addthenewsoul.domain.usecases.inventory.InventoryDAO;
import br.edu.ifsp.addthenewsoul.domain.usecases.location.LocationDAO;
import br.edu.ifsp.addthenewsoul.domain.usecases.utils.Session;

import java.time.LocalDate;
import java.util.*;

public class Main {
    private final Scanner scanner = new Scanner(System.in);

    private final AssetDAO inMemoryAssetDAO = new InMemoryAssetDAO();
    private final EmployeeDAO inMemoryEmployeeDAO = new InMemoryEmployeeDAO();
    private final LocationDAO inMemoryLocationDAO = new InMemoryLocationDAO();
    private final InventoryDAO inMemoryInventoryDAO = new InMemoryInventoryDAO();

    public static void main(String[] args) {
        Main main = new Main();
        main.manage();
    }

    public void manage() {
        int opcao = -1;
        while (opcao != 0) {
            opcao = menu();
            switch (opcao) {
                case 1 -> login();
                case 2 -> logout();
                case 3 -> printLoggedEmployee();
                case 4 -> addAsset();
                case 5 -> updateAsset();
                case 6 -> removeAsset();
                case 7 -> filterAssetsByLocation();
                case 8 -> filterAssetsByEmployeeInCharge();
                case 9 -> filterAssetsByLocationAndEmployeeInCharge();
                case 11 -> addEmployee();
                case 12 -> updateEmployee();
                case 13 -> removeEmployee();
                case 15 -> listLocations();
                case 16 -> addLocation();
                case 17 -> updateLocation();
                case 18 -> removeLocation();
                case 22 -> issueInventoryReport();
                case 23 -> issueEmployeesReport();
                case 24 -> issueLocationsReport();
            }
        }
    }

    private int menu() {

        System.out.println("1. Login employee");
        System.out.println("2. Logout employee");
        System.out.println("3. Print logged employee");
        System.out.println("4. List assets");
        System.out.println("5. Add asset");
        System.out.println("6. Update asset");
        System.out.println("7. Remove asset");
        System.out.println("8. Filter assets by location");
        System.out.println("9. Filter assets by employee in charge");
        System.out.println("10. Filter assets by location and employee in charge");
        System.out.println("11. List employees");
        System.out.println("12. Add employee");
        System.out.println("13. Update employee");
        System.out.println("14. Remove employee");
        System.out.println("15. List locations");
        System.out.println("16. Add location");
        System.out.println("17. Update location");
        System.out.println("18. Remove location");
        System.out.println("19. List inventories");
        System.out.println("20. Start inventories");
        System.out.println("21. Finish inventories");
        System.out.println("22. Rate asset in inventory");
        System.out.println("23. Issue inventory report");
        System.out.println("24. Issue employees report");
        System.out.println("25. Issue locations report");
        System.out.println("26. Export employees CSV");
        System.out.println("27. Import employees CSV");
        System.out.println("28. Export assets CSV");
        System.out.println("29. Import assets CSV");
        System.out.println("30. Import locations CSV");
        System.out.println("31. Import locations CSV");
        System.out.println("0. Sair");
        System.out.print(">> ");
        return Integer.parseInt(scanner.nextLine());
    }

    private void login() {
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Description: ");
        String password = scanner.nextLine();

        try {
            LoginEmployeeUseCase loginEmployeeUseCase = new LoginEmployeeUseCase(inMemoryEmployeeDAO);
            loginEmployeeUseCase.login(email, password);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void logout() {
        try {
            LogoutEmployeeUseCase logoutEmployeeUseCase = new LogoutEmployeeUseCase();
            logoutEmployeeUseCase.logout();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printLoggedEmployee() {
        try {
            Session session = Session.getInstance();
            Employee loggedEmployee = session.getLoggedUser();
            if (loggedEmployee == null) {
                System.out.println("There is no logged employee.");
                return;
            }

            System.out.println("Logged employee: " + loggedEmployee.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAsset() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        System.out.print("Description: ");
        String description = scanner.nextLine();
        System.out.print("Registration number frm employee in charge: ");
        String regNumberEmployee = scanner.nextLine();
        Optional<Employee> employeeInCharge = inMemoryEmployeeDAO.findByRegistrationNumber(regNumberEmployee);
        Employee employee = employeeInCharge.get();
        System.out.print("Value: ");
        double value = scanner.nextDouble();
        System.out.print("Damage: ");
        String damage = scanner.nextLine();
        System.out.print("Location: ");
        int locationId = scanner.nextInt();
        Optional<Location> locationObj = inMemoryLocationDAO.findById(locationId);
        Location location = locationObj.get();

        Asset asset = new Asset(id, description, employee, value, damage, location);

        inMemoryAssetDAO.add(asset);
    }

    private void updateAsset() {

        if (inMemoryAssetDAO.findAll().isEmpty()) {
            System.out.println("No assets registered.");
            return;
        }
        System.out.print("Id: ");
        int id = scanner.nextInt();
        inMemoryAssetDAO.findById(id).ifPresent(asset -> inMemoryAssetDAO.update(updateFromConsole(asset)));
    }

    private Asset updateFromConsole(Asset asset) {
        System.out.print("Description: ");
        String description = scanner.nextLine();
        asset.setDescription(description);
        System.out.print("Registration number frm employee in charge: ");
        String regNumberEmployee = scanner.nextLine();
        Employee employee = inMemoryEmployeeDAO.findByRegistrationNumber(regNumberEmployee).orElseThrow();
        asset.setEmployeeInCharge(employee);
        System.out.print("Value: ");
        double value = scanner.nextDouble();
        asset.setValue(value);
        System.out.print("Damage: ");
        String damage = scanner.nextLine();
        asset.setDamage(damage);
        System.out.print("Location: ");
        int locationId = scanner.nextInt();
        Location location = inMemoryLocationDAO.findById(locationId).get();
        asset.setLocation(location);
        return asset;
    }

    private void removeAsset() {

        if (inMemoryAssetDAO.findAll().isEmpty()) {
            System.out.println("No assets to remove.");
            return;
        }
        System.out.println("Id: ");
        int id = scanner.nextInt();
        inMemoryAssetDAO.delete(id);
    }

    private void findAllAssets() {
        inMemoryAssetDAO.findAll().forEach(System.out::println);
    }

    private void findAssetById(){
        System.out.println("Digite um id: ");
        int id = scanner.nextInt();
        System.out.println(inMemoryAssetDAO.findById(id));
    }

    private void addEmployee() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Registration number: ");
        String regNumberEmployee = scanner.nextLine();
        System.out.print("Hash password: ");
        String hashPassword = scanner.nextLine();
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        System.out.print("Role: ");
        String role = scanner.nextLine();

        Employee employee = new Employee(name, regNumberEmployee, hashPassword, email, phone, Role.valueOf(role));

        inMemoryEmployeeDAO.add(employee);
    }

    private void updateEmployee() {

        if (inMemoryEmployeeDAO.findAll().isEmpty()) {
            System.out.println("No employees registered.");
            return;
        }
        System.out.print("Id: ");
        String regNumber = scanner.nextLine();
        inMemoryEmployeeDAO.findByRegistrationNumber(regNumber).ifPresent(employee -> inMemoryEmployeeDAO.update(updateFromConsole(employee)));
    }

    private Employee updateFromConsole(Employee employee) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        employee.setName(name);
        System.out.print("Hash password: ");
        String hashPassword = scanner.nextLine();
        employee.setName(hashPassword);
        System.out.print("E-mail: ");
        String email = scanner.nextLine();
        employee.setName(email);
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        employee.setName(phone);
        System.out.print("Role: ");
        String role = scanner.nextLine();
        employee.setName(role);
        return employee;
    }

    private void removeEmployee() {

        if (inMemoryEmployeeDAO.findAll().isEmpty()) {
            System.out.println("No employees to remove.");
            return;
        }
        System.out.println("Registration Number: ");
        String regNumber = scanner.nextLine();
        inMemoryEmployeeDAO.delete(regNumber);
    }

    private void findAllEmployees() {
        inMemoryEmployeeDAO.findAll().forEach(System.out::println);
    }

    private void findEmployeeByRegistrationNumber(){
        System.out.println("Registration Number: ");
        String regNumber = scanner.nextLine();
        System.out.println(inMemoryEmployeeDAO.findByRegistrationNumber(regNumber));
    }

    private void listLocations() {
        inMemoryLocationDAO.findAll().forEach(System.out::println);
    }

    private void addLocation() {
        System.out.print("Id: ");
        int id = scanner.nextInt();
        System.out.print("Number: ");
        int number = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Section: ");
        String section = scanner.nextLine();

        Location location = new Location(id, number, section);

        inMemoryLocationDAO.add(location);
    }

    private void updateLocation() {

        if (inMemoryLocationDAO.findAll().isEmpty()) {
            System.out.println("No locations registered.");
            return;
        }
        System.out.print("Id: ");
        int id = scanner.nextInt();
        inMemoryLocationDAO.findById(id).ifPresent(location -> inMemoryLocationDAO.update(updateFromConsole(location)));
    }

    private Location updateFromConsole(Location location) {
        System.out.print("Number: ");
        int number = scanner.nextInt();
        location.setNumber(number);
        System.out.print("Section: ");
        String section = scanner.nextLine();
        location.setSection(section);
        return location;
    }

    private void removeLocation() {

        if (inMemoryLocationDAO.findAll().isEmpty()) {
            System.out.println("No locations to remove.");
            return;
        }
        System.out.println("Id: ");
        int id = scanner.nextInt();
        inMemoryLocationDAO.delete(id);
    }

    private void findLocationById(){
        System.out.println("Id: ");
        int id = scanner.nextInt();
        System.out.println(inMemoryLocationDAO.findById(id));
    }

    private void filterAssetsByLocation(){
        System.out.print("Location id: ");
        int id = scanner.nextInt();
        Location location = inMemoryLocationDAO.findById(id).orElseThrow();
        System.out.println(inMemoryAssetDAO.filterByLocation(location));
    }

    private void filterAssetsByEmployeeInCharge(){
        System.out.print("Employee in charge registration number: ");
        String regNumber = scanner.nextLine();
        Employee employee = inMemoryEmployeeDAO.findByRegistrationNumber(regNumber).orElseThrow();
        System.out.println(inMemoryAssetDAO.filterByEmployee(employee));
    }

    private void filterAssetsByLocationAndEmployeeInCharge(){
        System.out.print("Location id: ");
        int id = scanner.nextInt();
        System.out.print("Employee in charge registration number: ");
        String regNumber = scanner.nextLine();
        Location location = inMemoryLocationDAO.findById(id).orElseThrow();
        Employee employee = inMemoryEmployeeDAO.findByRegistrationNumber(regNumber).orElseThrow();
        System.out.println(inMemoryAssetDAO.filterByLocationAndEmployee(location, employee));
    }

    private void issueInventoryReport() {
        System.out.print("Initial date: ");
        LocalDate initialDate = LocalDate.parse(scanner.nextLine());
        System.out.print("End date: ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        List<Inventory> inventoriesFiltered = null;
        for (Inventory inventory : inMemoryInventoryDAO.findAll()) {
            if(inventory.getEndDate() != null){
                inMemoryInventoryDAO.filterByPeriod(inMemoryInventoryDAO.findAll(), initialDate, endDate);
            }
        }
    }


    private void issueEmployeesReport(){
        System.out.print("Employee in charge registration number: ");
        String regNumber = scanner.nextLine();
        for (Asset asset : inMemoryAssetDAO.findAll()) {
            if(asset.getEmployeeInCharge().getRegistrationNumber().equals(regNumber)){
                System.out.println(asset);
            }
        }

    }
    private void issueLocationsReport(){
        System.out.print("Location: ");
        int locationId = scanner.nextInt();
        for (Asset asset : inMemoryAssetDAO.findAll()) {
            if(asset.getLocation().getId() == locationId){
                System.out.println(asset);
            }
        }
    }
}


