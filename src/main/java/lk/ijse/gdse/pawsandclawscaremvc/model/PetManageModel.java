package lk.ijse.gdse.pawsandclawscaremvc.model;

import lk.ijse.gdse.pawsandclawscaremvc.dto.PetDto;
import lk.ijse.gdse.pawsandclawscaremvc.util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PetManageModel {
    public String getNextPetId() throws SQLException {
        ResultSet rst = CrudUtil.execute("select petId from Pet order by petId desc limit 1");

        if (rst.next()) {
            String lastId = rst.getString(1);
            String substring = lastId.substring(1);
            int i = Integer.parseInt(substring);
            int newIdIndex = i + 1;
            return String.format("P%03d", newIdIndex);
        }
        return "P001";
    }

    public ArrayList<PetDto> getAllPets() throws SQLException {
        ResultSet rst = CrudUtil.execute("select * from Pet");
        ArrayList<PetDto> petDtos = new ArrayList<>();

        while (rst.next()) {
            PetDto petDto = new PetDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3)
            );
            petDtos.add(petDto);
        }
        return petDtos;
    }

    public boolean updatePet(PetDto petDto) throws SQLException {
        return CrudUtil.execute("UPDATE Pet SET name = ?, breed = ?  WHERE petId = ?",
                petDto.getName(),
                petDto.getBreed(),
                petDto.getPetId());
    }

    public boolean savePet(PetDto petDto) throws SQLException {
        return CrudUtil.execute("INSERT INTO Pet VALUES (?, ?, ?)",
                petDto.getPetId(),
                petDto.getName(),
                petDto.getBreed());
    }

    public boolean deletePet(String petId) throws SQLException {
        return CrudUtil.execute("DELETE FROM Pet WHERE petId = ?", petId);
    }

    public ArrayList<PetDto> searchPetsByNameOrId(String searchText) throws SQLException {
        ArrayList<PetDto> pets = new ArrayList<>();
        ResultSet rs = CrudUtil.execute("SELECT * FROM Pet WHERE petId LIKE ? OR name LIKE ?",
                "%" + searchText + "%", "%" + searchText + "%");

        while (rs.next()) {
            pets.add(new PetDto(
                    rs.getString("petId"),
                    rs.getString("name"),
                    rs.getString("breed")
            ));
        }
        return pets;
    }
}
