package lk.ijse.gdse.pawsandclawscaremvc.model;

import lk.ijse.gdse.pawsandclawscaremvc.dto.ServiceDetailsDto;
import lk.ijse.gdse.pawsandclawscaremvc.dto.ServiceDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceModel {

    public ArrayList<String> getAllItemIds() throws SQLException {
        ResultSet rst = CrudUtil.execute("select serviceId from Service");
        ArrayList<String> serviceId = new ArrayList<>();

        while (rst.next()) {
            serviceId.add(rst.getString(1));
        }
        return serviceId;
    }

    public ServiceDto findServiceByEmployeeId(String empId) throws SQLException {
        // Using CrudUtil.execute to execute the query and handle parameters
        ResultSet rst = CrudUtil.execute(
                "SELECT s.serviceId FROM Services s " +
                        "JOIN EmployeeServices es ON s.serviceId = es.serviceId " +
                        "WHERE es.empId = ?", empId);

        // Processing the result
        if (rst.next()) {
            return new ServiceDto(
                    rst.getString("serviceId"),
                    null,
                    null,
                    null,
                    null
            );
        }
        return null;
    }

    public ArrayList<ServiceDto> getAllServices() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Service");
        ArrayList<ServiceDto> serviceDtos = new ArrayList<>();
        while (rst.next()) {
            ServiceDto serviceDto = new ServiceDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getTime(3),
                    rst.getDouble(4),
                    rst.getString(5)
            );
            serviceDtos.add(serviceDto);
        }
        return serviceDtos;
    }


    public String getNextCustomerId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select serviceId from Service order by serviceId desc limit 1");
        if (rst.next()) {
            String lastId = rst.getString(1);
            String subString = lastId.substring(1);
            int i = Integer.parseInt(subString);
            int newIndex = i + 1;
            return String.format("S%03d", newIndex);
        }
        return "S001";
    }

    public boolean saveService(ServiceDto serviceDto) throws SQLException {
        return CrudUtil.execute("insert into Service values(?,?,?,?,?)",
                serviceDto.getServiceId(),
                serviceDto.getDescription(),
                serviceDto.getDuration(),
                serviceDto.getPrice(),
                serviceDto.getAvailability()
        );
    }

    public boolean updateService(ServiceDto serviceDto) throws SQLException {
        return CrudUtil.execute(
                "update Service set availibility=?, duration=?, description=?, price=? where serviceId =?",
                serviceDto.getAvailability(),    // Corrected parameter order
                serviceDto.getDuration(),
                serviceDto.getDescription(),
                serviceDto.getPrice(),
                serviceDto.getServiceId()
        );
    }


    public boolean deleteService(String serviceIdText) throws SQLException {
        return CrudUtil.execute("delete from Service where serviceId = ?", serviceIdText);
    }

    public ArrayList<String> getAllServiceIds() throws SQLException {
        ArrayList<String> serviceIds = new ArrayList<>();
        ResultSet rst = CrudUtil.execute("SELECT serviceId FROM Service WHERE availibility = 'Available'");

        while (rst.next()) {
            serviceIds.add(rst.getString("serviceId"));
        }
        return serviceIds;
    }

    public double getPricePerHour(String selectedService) throws SQLException {
        ResultSet rst = CrudUtil.execute("select price from Service where serviceId = ?", selectedService);
        if (rst.next()) {
            return rst.getDouble(1);
        }
        return 0;
    }

    public String getDuration(String selectedService) throws SQLException {
        ResultSet rst = CrudUtil.execute("select duration from Service where serviceId = ?", selectedService);
        if (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public boolean bookService(ServiceDetailsDto serviceDetailsDto) throws SQLException {
        String serviceId = serviceDetailsDto.getServiceId();
        String description = serviceDetailsDto.getDescription();
        String resId = serviceDetailsDto.getResId();
        boolean isServiceAvailable = checkServiceAvailability(serviceId, description);

        if (!isServiceAvailable) {
            return false;
        }

        boolean isServiceUpdated = updateServiceAvailability(serviceId);
        System.out.println("isServiceUpdated" + isServiceUpdated);

        if (!isServiceUpdated) {
            return false;
        }

        boolean isServiceDetailsSaved = saveServiceDetails(serviceDetailsDto);
        System.out.println("isServiceDetailsSaved" + isServiceDetailsSaved);

        if (!isServiceDetailsSaved) {
            return false;
        }

        return true;
    }

    private boolean saveServiceDetails(ServiceDetailsDto serviceDetailsDto) {
        String insertQuery = "INSERT INTO ServiceDetails (serviceId, description, resId) VALUES (?, ?, ?)";
        try {
            return CrudUtil.execute(insertQuery, serviceDetailsDto.getServiceId(), serviceDetailsDto.getDescription(), serviceDetailsDto.getResId()); // Return true if service details are saved
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean updateServiceAvailability(String serviceId) throws SQLException {
        String updateQuery = "UPDATE Service SET availibility = 'Not AVAILABLE' WHERE serviceId = ?";
        try {
            return CrudUtil.execute(updateQuery, serviceId); // If rows are updated, the service is successfully booked
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean checkServiceAvailability(String serviceId, String description) throws SQLException {
        String query = "SELECT availibility FROM Service WHERE serviceId = ?";
        ResultSet resultSet = CrudUtil.execute(query, serviceId);

        if (resultSet.next()) {
            String status = resultSet.getString(1);
            return status != null && status.equals("Available");
        }

        return false; // If no service found or an error occurs, return false
    }

    public String getServiceDescriptionById(String selectedServiceId) throws SQLException {
        ResultSet rst = CrudUtil.execute("SELECT description FROM Service WHERE serviceId = ?",selectedServiceId);
        while (rst.next()) {
            return rst.getString(1);
        }
        return null;
    }

    public ArrayList<String> getAllServiceIdDesc() throws SQLException {
        ResultSet rst = CrudUtil.execute("select serviceId ,description from Service");
        ArrayList<String> serviceIds = new ArrayList<>();
        while (rst.next()) {
            String serviceId = rst.getString(1);
            String description = rst.getString(2);
            serviceIds.add(serviceId + "-" + description);
        }
        return serviceIds;
    }
}
