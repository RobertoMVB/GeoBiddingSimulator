import com.fasterxml.jackson.annotation.JsonProperty;
import com.hypr.geobidding.dto.bid.BidRequestInventory;
import com.hypr.geobidding.dto.bid.BidRequestUser;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class BidRequest {

    @NotNull
    @JsonProperty("request_id")
    private String requestId;

    @NotNull
    @Valid
    @JsonProperty("user")
    private BidRequestUser bidRequestUser;

    @NotNull
    @Valid
    @JsonProperty("inventory")
    private BidRequestInventory bidRequestInventory;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public BidRequestUser getBidRequestUser() {
        return bidRequestUser;
    }

    public void setBidRequestUser(BidRequestUser bidRequestUser) {
        this.bidRequestUser = bidRequestUser;
    }

    public BidRequestInventory getBidRequestInventory() {
        return bidRequestInventory;
    }

    public void setBidRequestInventory(BidRequestInventory bidRequestInventory) {
        this.bidRequestInventory = bidRequestInventory;
    }
}
