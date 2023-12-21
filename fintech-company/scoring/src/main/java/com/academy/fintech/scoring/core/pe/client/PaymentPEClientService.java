package com.academy.fintech.scoring.core.pe.client;

import com.academy.fintech.overdue_payment.Date;
import com.academy.fintech.overdue_payment.OverduePaymentRequest;
import com.academy.fintech.scoring.core.pe.client.grpc.PEGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentPEClientService {

    private final PEGrpcClient peGrpcClient;

    /**
     * Получение первых дат просрочки каждого договора у клиента, если просрочки есть  и возвращает
     * массив с датами просрочек.
     */
    public List<LocalDate> getOverduePayments(String clientId) {
        OverduePaymentRequest request = OverduePaymentRequest.newBuilder()
                .setClientId(clientId)
                .build();

        List<LocalDate> dates = new ArrayList<>();
        for (Date date : peGrpcClient.getOverduePayments(request).getOverdueDatesList()) {
            dates.add(mapDateToLocalDate(date));
        }

        return dates;
    }

    private static LocalDate mapDateToLocalDate(Date date) {
        return LocalDate.of(date.getYear(), date.getMonth(), date.getDay());
    }
}
