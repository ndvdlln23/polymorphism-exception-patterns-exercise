import java.util.ArrayList;
import java.util.List;

public class ParcelManager {
    private static ParcelManager instance;
    private List<Parcel> parcels;

    private ParcelManager() {
        parcels = new ArrayList<>();
    }

    public static ParcelManager getInstance() {
        if (instance == null) {
            instance = new ParcelManager();
        }
        return instance;
    }

    public void addParcel(Parcel parcel) {
        parcels.add(parcel);
    }

    public Parcel getParcel(String trackingNumber) {
        for (Parcel p : parcels) {
            if (p.getTrackingNumber().equals(trackingNumber)) {
                return p;
            }
        }
        return null;
    }

    public boolean trackingNumberExists(String trackingNumber) {
        return getParcel(trackingNumber) != null;
    }

    public List<Parcel> getAllParcels() {
        return parcels;
    }
}