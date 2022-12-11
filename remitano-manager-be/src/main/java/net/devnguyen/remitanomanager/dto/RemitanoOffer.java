package net.devnguyen.remitanomanager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.models.auth.In;

import java.math.BigDecimal;
import java.math.BigInteger;

public class RemitanoOffer {
    @JsonProperty("id")
    public Long id;
    @JsonProperty("canonical_name")
    public String canonicalName;
    @JsonProperty("offer_type")
    public String offerType;
    @JsonProperty("min_amount")
    public BigInteger minAmount;
    @JsonProperty("max_amount")
    public BigInteger maxAmount;
    @JsonProperty("total_amount")
    public BigInteger totalAmount;
    @JsonProperty("currency")
    public String currency;
    @JsonProperty("payment_time")
    public Integer paymentTime;
    @JsonProperty("payment_descriptions")
    public Object paymentDescriptions;
    @JsonProperty("price")
    public BigInteger price;
    @JsonProperty("country_code")
    public String countryCode;
    @JsonProperty("min_coin_price")
    public Object minCoinPrice;
    @JsonProperty("max_coin_price")
    public Object maxCoinPrice;
    @JsonProperty("reference_exchange")
    public String referenceExchange;
    @JsonProperty("aml_adjustment")
    public Object amlAdjustment;
    @JsonProperty("fixed_coin_price")
    public BigInteger fixedCoinPrice;
    @JsonProperty("payment_method")
    public String paymentMethod;
    @JsonProperty("disabled")
    public Boolean disabled;
    @JsonProperty("payment_details")
    public PaymentDetails paymentDetails;
    @JsonProperty("terms_of_trade")
    public Object termsOfTrade;
    @JsonProperty("sell_all_fiat_token")
    public Boolean sellAllFiatToken;
    @JsonProperty("allow_user_level_from")
    public Integer allowUserLevelFrom;

    public class PaymentDetails {
        @JsonProperty("ban_id")
        public Integer bankId;
        @JsonProperty("bank_name")
        public String bankName;
        @JsonProperty("bank_account_name")
        public String bankAccountName;
        @JsonProperty("bank_account_number")
        public String bankAccountNumber;
    }

}
