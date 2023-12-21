package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.create.CreateRequest;
import com.academy.fintech.create.CreateResponse;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgreementCreationService {

    private final AgreementService agreementService;
    private final ProductService productService;

    /**
     * @param request Запрос на создание договора.
     * @return Ответ с идентификатором созданного договора или сообщением об ошибке.
     */
    public CreateResponse createAgreement(CreateRequest request) {
        Optional<Product> productOptional = productService.get(request.getProductCode());

        // Проверяем наличие продукта
        if (productOptional.isEmpty()) {
            return buildErrorResponse("Product CL1.1 does not exist");
        }

        Product product = productOptional.get();
        CreateResponse response;

        response = isCorrectOriginationAmount(product.getMinOriginationAmount(),
                product.getMaxOriginationAmount(),
                request.getOriginationAmount());
        if (response != null) return response;

        response = isCorrectLoanTerm(new BigDecimal(product.getMinLoanTerm()),
                new BigDecimal(product.getMaxLoanTerm()),
                request.getLoanTerm());
        if (response != null) return response;

        response = isCorrectPrincipalAmount(product.getMinPrincipalAmount(),
                product.getMaxPrincipalAmount(),
                new BigDecimal(request.getDisbursementAmount()).add(new BigDecimal(request.getOriginationAmount())));
        if (response != null) return response;

        response = isCorrectInterest(product.getMinInterest(),
                product.getMaxInterest(),
                request.getInterest());
        if (response != null) return response;

        return CreateResponse.newBuilder()
                .setAgreementId(agreementService.add(mapRequestToAgreement(request)))
                .build();
    }

    private <T> CreateResponse isCorrectLoanTerm(BigDecimal minValue, BigDecimal maxValue, T value) {
        ErrorInfo errorInfo;
        errorInfo = isCorrectValue(minValue, maxValue, value, "loan term");
        if (errorInfo.hasError())
            return buildErrorResponse(errorInfo.errorMessage());
        return null;
    }

    private <T> CreateResponse isCorrectPrincipalAmount(BigDecimal minValue, BigDecimal maxValue, T value) {
        ErrorInfo errorInfo;
        errorInfo = isCorrectValue(minValue, maxValue, value, "principal amount");
        if (errorInfo.hasError())
            return buildErrorResponse(errorInfo.errorMessage());
        return null;
    }

    private <T> CreateResponse isCorrectInterest(BigDecimal minValue, BigDecimal maxValue, T value) {
        ErrorInfo errorInfo;
        errorInfo = isCorrectValue(minValue, maxValue, value, "interest");
        if (errorInfo.hasError())
            return buildErrorResponse(errorInfo.errorMessage());
        return null;
    }

    private <T> CreateResponse isCorrectOriginationAmount(BigDecimal minValue, BigDecimal maxValue, T value) {
        ErrorInfo errorInfo;
        errorInfo = isCorrectValue(minValue, maxValue, value, "origination amount");
        if (errorInfo.hasError())
            return buildErrorResponse(errorInfo.errorMessage());
        return null;
    }

    /**
     * Проверяет, принадлежит ли значение к заданному диапазону.
     *
     * @param minValue Минимальное значение диапазона.
     * @param maxValue Максимальное значение диапазона.
     * @param value    Значение для проверки.
     * @return true, если значение принадлежит диапазону, иначе false.
     */
    private boolean isBelongToRange(BigDecimal minValue, BigDecimal maxValue, BigDecimal value) {
        int comparisonMin = minValue.compareTo(value);
        int comparisonMax = maxValue.compareTo(value);

        return comparisonMin <= 0 && comparisonMax >= 0;
    }

    /**
     * Проверяет, принадлежит ли значение к заданному диапазону.
     *
     * @param minValue  Минимальное значение диапазона.
     * @param maxValue  Максимальное значение диапазона.
     * @param value     Значение для проверки.
     * @param typeValue Тип значения (например, "loan term", "interest").
     * @return Информация об ошибке, если значение не принадлежит диапазону, иначе true.
     */
    private <T> ErrorInfo isCorrectValue(BigDecimal minValue, BigDecimal maxValue, T value, String typeValue) {

        if (isBelongToRange(minValue, maxValue, new BigDecimal(value.toString()))) {
            return new ErrorInfo(false, null);
        }

        return new ErrorInfo(true,
                "Invalid " + typeValue
                        + ", acceptable values are from " + minValue + " to " + maxValue);
    }

    /**
     * Создает ответ с сообщением об ошибке.
     *
     * @param errorMessage Сообщение об ошибке.
     * @return Ответ с сообщением об ошибке.
     */
    private CreateResponse buildErrorResponse(String errorMessage) {
        return CreateResponse.newBuilder()
                .setErrorMessage(errorMessage)
                .build();
    }

    /**
     * Генерирует объект договора на основе запроса.
     *
     * @param request Запрос на создание договора.
     * @return Объект договора.
     */
    private Agreement mapRequestToAgreement(CreateRequest request) {
        return Agreement.builder()
                .productCode(request.getProductCode())
                .clientId(request.getClientId())
                .interest(new BigDecimal(request.getInterest()))
                .loanTerm(request.getLoanTerm())
                .principalAmount(new BigDecimal(request.getDisbursementAmount())
                        .add(new BigDecimal(request.getOriginationAmount())))
                .originationAmount(new BigDecimal(request.getOriginationAmount()))
                .status(AgreementStatus.NEW)
                .build();
    }
}
