import dao.ContractDao;
import model.Contract;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        try {
            var dao = new ContractDao();

            // list
            dao.findAll(5).forEach(System.out::println);

            // insert
            Contract ct = new Contract();
            ct.setVehicleId(203); // must exist in your vehicle table
            ct.setCreatedAt(LocalDateTime.now()); // or null to use DB default
            ct.setTransportFee(new BigDecimal("120.50"));
            ct.setPickupDate(LocalDateTime.now().plusDays(1));
            ct.setDeliveryDate(LocalDateTime.now().plusDays(4));
            ct.setSignedDate(null);
            ct.setTermsText("Handle with care. No liquids.");
            ct.setPaymentMethod(Contract.PaymentMethod.Cash);
            ct.setPayment(Contract.Payment.Pending);
            ct.setStatus(Contract.Status.Active);

            int id = dao.insert(ct);
            System.out.println("Inserted contract_id=" + id);

            // update
            Contract fetched = dao.findById(id);
            fetched.setPayment(Contract.Payment.Paid);
            fetched.setStatus(Contract.Status.Completed);
            dao.update(fetched);
            System.out.println("Updated: " + dao.findById(id));

            // delete
            dao.delete(id);
            System.out.println("Deleted? " + (dao.findById(id) == null));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
