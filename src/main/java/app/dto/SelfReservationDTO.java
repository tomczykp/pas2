package app.dto;

import app.model.DateAdapter;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.time.LocalDate;

@XmlRootElement
public class SelfReservationDTO {
    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    public LocalDate startDate;

    @XmlElement
    @XmlJavaTypeAdapter(DateAdapter.class)
    public LocalDate endDate;

    @XmlElement
    public Integer product;

    public SelfReservationDTO(LocalDate startDate, LocalDate endDate, Integer product) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.product = product;
    }

    public SelfReservationDTO() {}

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getProduct() {
        return product;
    }

    public void setProduct(Integer product) {
        this.product = product;
    }
}
