package data;

public class ManagerData {
    public final String name;
    public final String drugLicenseNumber;
    public final String pharmacyRegNumber;
    public final String contact; // Helpline contact number

    public ManagerData(String name, String drugLicenseNumber, String pharmacyRegNumber, String contact) {
        this.name = name;
        this.drugLicenseNumber = drugLicenseNumber;
        this.pharmacyRegNumber = pharmacyRegNumber;
        this.contact = contact;
    }
}