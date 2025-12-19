import dao.ServiceDao;
import model.Service;

import java.util.List;

public class ServiceConsoleTest {

    public static void main(String[] args) {
        System.out.println("=== SERVICE CONSOLE TEST ===");

        ServiceDao dao = new ServiceDao();

        try {
            List<Service> services = dao.findAllApproved();

            System.out.println("Services loaded: " + services.size());
            System.out.println("--------------------------------");

            for (Service s : services) {
                System.out.println(
                        "ID: " + s.getServiceId() +
                                " | Title: " + s.getTitle() +
                                " | Category: " + s.getCategory() +
                                " | Price: " + s.getOfferPrice() +
                                " | From: " + s.getPickupAddress() +
                                " | To: " + s.getDropoffAddress() +
                                " | Posted: " + s.getCreatedAt()
                );
            }

        } catch (Exception e) {
            System.err.println("❌ ERROR while loading services");
            e.printStackTrace();
        }
    }
}
