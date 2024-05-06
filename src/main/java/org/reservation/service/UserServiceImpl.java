package org.reservation.service;

import io.grpc.stub.StreamObserver;
import org.user.*;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {
        super.createUser(request, responseObserver);
    }

    @Override
    public void loginUser(LoginUserRequest request, StreamObserver<LoginUserResponse> responseObserver) {
        super.loginUser(request, responseObserver);
    }
}
