package org.reservation.service;

import io.grpc.stub.StreamObserver;
import org.reservation.CreateReservationRequest;
import org.reservation.CreateReservationResponse;
import org.reservation.ReservationServiceGrpc;

public class ReservationServiceImpl extends ReservationServiceGrpc.ReservationServiceImplBase {
    @Override
    public void createReservation(CreateReservationRequest request, StreamObserver<CreateReservationResponse> responseObserver) {
        super.createReservation(request, responseObserver);
    }
}
