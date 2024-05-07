package org.reservation.service;

import io.grpc.stub.StreamObserver;

import java.util.Map;

import org.reservation.CreateReservationRequest;
import org.reservation.CreateReservationResponse;
import org.reservation.ReservationServiceGrpc;

public class ReservationServiceImpl extends ReservationServiceGrpc.ReservationServiceImplBase {
    private Map<Integer, CreateReservationRequest> reservations;

    @Override
    public void createReservation(CreateReservationRequest request, StreamObserver<CreateReservationResponse> responseObserver) {
        System.out.println("Create reservation request received" + request.getItemId());
        CreateReservationRequest reservation = CreateReservationRequest.newBuilder()
            .setItemId(request.getItemId())
            .setReservationDate(request.getReservationDate())
            .setPaymentDetails(request.getPaymentDetails())
            .build();

        reservations.put(request.getItemId(), reservation);

        CreateReservationResponse response = CreateReservationResponse.newBuilder()
                .setMessage(reservation.getItemId() + " reserved")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
