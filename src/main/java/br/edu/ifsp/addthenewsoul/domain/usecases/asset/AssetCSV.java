package br.edu.ifsp.addthenewsoul.domain.usecases.asset;

import br.edu.ifsp.addthenewsoul.application.io.CSV;
import br.edu.ifsp.addthenewsoul.application.io.CSVNode;
import br.edu.ifsp.addthenewsoul.domain.entities.asset.Asset;
import br.edu.ifsp.addthenewsoul.domain.entities.asset.Location;
import br.edu.ifsp.addthenewsoul.domain.entities.employee.Employee;
import br.edu.ifsp.addthenewsoul.domain.usecases.utils.AssetDependencyNotFoundException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AssetCSV extends CSV<Asset> {
    @Override
    public List<Asset> read(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner reader = new Scanner(file);
        List<Asset> assets = new ArrayList<>();

        while (reader.hasNextLine()) {
            String[] parts = reader.nextLine().split(",");
            assets.add(new Asset(
                Integer.parseInt(parts[0]),
                parts[1],
                Double.parseDouble(parts[2]),
                parts[3]
            ));
        }

        reader.close();

        return assets;
    }

    /*
    public List<Asset> readWithDependencies(String fileName, boolean withInvalidDependencies, Map<String, Employee> employees, Map<Integer, Location> locations) throws AssetDependencyNotFoundException, FileNotFoundException {
        List<Asset> assets = read(fileName);

        for (Asset asset : assets) {
            Employee employee = employees.get(asset.getRegistrationNumber());
            Location location = locations.get(asset.getLocalId());

            if (location == null || employee == null) {
                if (withInvalidDependencies) throw new AssetDependencyNotFoundException("Local and/or employee not found");
            }
            else {
                asset.setLocation(location);
                asset.setEmployeeInCharge(employee);
            }
        }

        return assets;
    }
     */
}
