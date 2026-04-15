package com.platform.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.platform.model.enums.DriverStatus;
import com.platform.model.enums.ServiceType;
import com.platform.model.enums.VehicleCategory;
import com.platform.model.ride.Vehicle;
import com.platform.model.user.Administrator;
import com.platform.model.user.Customer;
import com.platform.model.user.Driver;
import com.platform.repository.AdministratorRepository;
import com.platform.repository.CustomerRepository;
import com.platform.repository.DriverRepository;
import com.platform.repository.VehicleRepository;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Initialize sample data for demo purposes
 */
@Component
public class DataInitService implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final DriverRepository driverRepository;
    private final AdministratorRepository administratorRepository;
    private final VehicleRepository vehicleRepository;
    private final JdbcTemplate jdbcTemplate;

    public DataInitService(CustomerRepository customerRepository,
                           DriverRepository driverRepository,
                           AdministratorRepository administratorRepository,
                           VehicleRepository vehicleRepository,
                           JdbcTemplate jdbcTemplate) {
        this.customerRepository = customerRepository;
        this.driverRepository = driverRepository;
        this.administratorRepository = administratorRepository;
        this.vehicleRepository = vehicleRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Keep schema evolution resilient for existing local H2 data.
        ensureOtpColumnExists();
        // Seed demo users only if missing (do NOT delete data).
        seedDemoUsersIfMissing();
    }

    private void ensureOtpColumnExists() {
        jdbcTemplate.execute(
                "ALTER TABLE trips ADD COLUMN IF NOT EXISTS ride_start_otp_verified BOOLEAN DEFAULT FALSE"
        );
        jdbcTemplate.execute(
                "UPDATE trips SET ride_start_otp_verified = FALSE WHERE ride_start_otp_verified IS NULL"
        );
    }

    private void seedDemoUsersIfMissing() {
        // --- Customer ---
        customerRepository.findByEmail("customer@example.com").orElseGet(() -> {
            Customer customer = new Customer();
            customer.setFirstName("Demo");
            customer.setLastName("Customer");
            customer.setEmail("customer@example.com");
            customer.setPhone("+1111111111");
            customer.setPasswordHash("password123"); // demo only
            customer.setEmergencyContact("+911234567890"); // keep booking rules happy
            return customerRepository.save(customer);
        });

        // --- Driver + Vehicle ---
        Driver driver = driverRepository.findByEmail("driver@example.com").orElseGet(() -> {
            Driver d = new Driver();
            d.setFirstName("Demo");
            d.setLastName("Driver");
            d.setEmail("driver@example.com");
            d.setPhone("+2222222222");
            d.setPasswordHash("driver123"); // demo only
            d.setDriverStatus(DriverStatus.AVAILABLE);
            d.setCurrentLocation("12.9716,77.5946");
            d.setPreferredType(ServiceType.SEDAN);
            d.setPreferredCategory(VehicleCategory.SEDAN);
            return driverRepository.save(d);
        });

        // Ensure driver has an active vehicle
        Vehicle v = vehicleRepository.findByRegistrationNumber("KA01DEMO1234").orElseGet(() -> {
            Vehicle nv = new Vehicle();
            nv.setDriver(driver);
            nv.setRegistrationNumber("KA01DEMO1234");
            nv.setModel("Demo Sedan");
            nv.setColor("White");
            nv.setCapacity(4);
            nv.setServiceType(ServiceType.SEDAN);
            nv.setVehicleCategory(VehicleCategory.SEDAN);
            return vehicleRepository.save(nv);
        });

        if (driver.getActiveVehicle() == null) {
            driver.setActiveVehicle(v);
            driverRepository.save(driver);
        }

        // --- Admin ---
        administratorRepository.findByEmail("admin@example.com").orElseGet(() -> {
            Administrator admin = new Administrator();
            admin.setFirstName("Demo");
            admin.setLastName("Admin");
            admin.setEmail("admin@example.com");
            admin.setPhone("+3333333333");
            admin.setPasswordHash("admin123"); // demo only
            admin.setAdminId("ADMIN-DEMO");
            return administratorRepository.save(admin);
        });
    }
}
