package be.mystore.model;

import lombok.Getter;

@Getter
public enum ProductStatus {
    AVAILABLE("Còn Hàng"),
    SOLD_OUT("Hết Hàng"),
    ON_SALE("Đang Giảm Giá"),
    STOPPED_SELLING("Ngừng Kinh Doanh");


    private final String displayValue;

    ProductStatus(String displayValue) {
        this.displayValue = displayValue;
    }
}
