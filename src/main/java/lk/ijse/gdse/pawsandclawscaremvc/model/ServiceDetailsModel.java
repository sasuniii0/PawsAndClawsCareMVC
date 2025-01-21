package lk.ijse.gdse.pawsandclawscaremvc.model;

import lk.ijse.gdse.pawsandclawscaremvc.dto.ServiceDetailsDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServiceDetailsModel {

    ServiceModel serviceModel = new ServiceModel();

    public boolean saveReservationDetailsList(ArrayList<ServiceDetailsDto> serviceDetailsDtos) throws SQLException {

        for (ServiceDetailsDto serviceDetailsDto : serviceDetailsDtos) {
            boolean isReservationSaved = saveReservationDetails(serviceDetailsDto);
            System.out.println("isReservationSaved " + isReservationSaved);

            if (!isReservationSaved) {
                return false;
            }
            boolean isServiceUpdated = serviceModel.bookService(serviceDetailsDto);
            System.out.println("isServiceUpdated " + isServiceUpdated);

            if (!isServiceUpdated) {
                return false;
            }
        }
        return true;
    }

    private boolean saveReservationDetails(ServiceDetailsDto serviceDetailsDto) throws SQLException {
        return  CrudUtil.execute(
                "insert into ServiceDetails values (?,?,?)",
                serviceDetailsDto.getServiceId(),
                serviceDetailsDto.getResId(),
                serviceDetailsDto.getDescription()

        );
    }
}


